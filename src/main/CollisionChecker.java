package main;

import entity.Entity;
import entity.LivingEntity;
import java.awt.Rectangle;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    // --- TILE COLLISION ---
    public void checkTile(Entity entity) {
        // find world bounds of entity hitbox
        int entityLeftWorldX = entity.worldX + entity.hitBox.x;
        int entityRightWorldX = entity.worldX + entity.hitBox.x + entity.hitBox.width;
        int entityTopWorldY = entity.worldY + entity.hitBox.y;
        int entityBotWorldY = entity.worldY + entity.hitBox.y + entity.hitBox.height;

        // convert world coords to tile grid indices
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBotRow = entityBotWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        // predict next tile position based on direction and speed
        switch (entity.direction) {
            case "up" -> {
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            case "down" -> {
                entityBotRow = (entityBotWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBotRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBotRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            case "left" -> {
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBotRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            case "right" -> {
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBotRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
        }
    }

    // --- OBJECT INTERACTION ---
    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                Entity target = gp.obj[i];

                // map local hitbox coords to absolute world position
                int entityOriginalX = entity.hitBox.x;
                int entityOriginalY = entity.hitBox.y;
                int targetOriginalX = target.hitBox.x;
                int targetOriginalY = target.hitBox.y;

                entity.hitBox.x = entity.worldX + entity.hitBox.x;
                entity.hitBox.y = entity.worldY + entity.hitBox.y;
                target.hitBox.x = target.worldX + target.hitBox.x;
                target.hitBox.y = target.worldY + target.hitBox.y;

                // shift entity hitbox to prospective destination
                switch (entity.direction) {
                    case "up" -> entity.hitBox.y -= entity.speed;
                    case "down" -> entity.hitBox.y += entity.speed;
                    case "left" -> entity.hitBox.x -= entity.speed;
                    case "right" -> entity.hitBox.x += entity.speed;
                }

                // flag collision if rectangles overlap
                if (entity.hitBox.intersects(target.hitBox)) {
                    if (target.collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }

                // restore relative coords to prevent displacement bugs
                entity.hitBox.x = entityOriginalX;
                entity.hitBox.y = entityOriginalY;
                target.hitBox.x = targetOriginalX;
                target.hitBox.y = targetOriginalY;
            }
        }
        return index;
    }

    // --- ENTITY vs ENTITY ---
    public int checkEntity(Entity entity, Entity[] targets) {
        int index = 999;
        for (int i = 0; i < targets.length; i++) {
            if (targets[i] != null && targets[i] != entity) {
                Entity target = targets[i];

                int entityOriginalX = entity.hitBox.x;
                int entityOriginalY = entity.hitBox.y;
                int targetOriginalX = target.hitBox.x;
                int targetOriginalY = target.hitBox.y;

                entity.hitBox.x = entity.worldX + entity.hitBox.x;
                entity.hitBox.y = entity.worldY + entity.hitBox.y;
                target.hitBox.x = target.worldX + target.hitBox.x;
                target.hitBox.y = target.worldY + target.hitBox.y;

                switch (entity.direction) {
                    case "up" -> entity.hitBox.y -= entity.speed;
                    case "down" -> entity.hitBox.y += entity.speed;
                    case "left" -> entity.hitBox.x -= entity.speed;
                    case "right" -> entity.hitBox.x += entity.speed;
                }

                if (entity.hitBox.intersects(target.hitBox)) {
                    entity.collisionOn = true;
                    index = i;
                }

                entity.hitBox.x = entityOriginalX;
                entity.hitBox.y = entityOriginalY;
                target.hitBox.x = targetOriginalX;
                target.hitBox.y = targetOriginalY;
            }
        }
        return index;
    }

    public boolean checkEntity(Entity entity, Entity target) {
        Entity[] targets = {target};
        return checkEntity(entity, targets) != 999;
    }

    // --- COMBAT HITBOX ---
    public boolean checkHit(Entity attacker, Entity defender) {
        if (defender == null || !defender.alive || attacker == defender) {
            return false;
        }

        // evaluate intersection between attack area and target hitbox
        if (attacker instanceof LivingEntity livingAttacker) {
            Rectangle attackArea = new Rectangle(
                    livingAttacker.attackHitbox.x,
                    livingAttacker.attackHitbox.y,
                    livingAttacker.attackHitbox.width,
                    livingAttacker.attackHitbox.height
            );

            Rectangle defenderArea = new Rectangle(
                    defender.worldX + defender.hitBox.x,
                    defender.worldY + defender.hitBox.y,
                    defender.hitBox.width,
                    defender.hitBox.height
            );

            return attackArea.intersects(defenderArea);
        }
        return false;
    }

    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;

        int entityOriginalX = entity.hitBox.x;
        int entityOriginalY = entity.hitBox.y;
        int playerOriginalX = gp.player.hitBox.x;
        int playerOriginalY = gp.player.hitBox.y;

        entity.hitBox.x = entity.worldX + entity.hitBox.x;
        entity.hitBox.y = entity.worldY + entity.hitBox.y;

        gp.player.hitBox.x = gp.player.worldX + gp.player.hitBox.x;
        gp.player.hitBox.y = gp.player.worldY + gp.player.hitBox.y;

        // check intersection using predicted player position
        switch (entity.direction) {
            case "up" -> entity.hitBox.y -= entity.speed;
            case "down" -> entity.hitBox.y += entity.speed;
            case "left" -> entity.hitBox.x -= entity.speed;
            case "right" -> entity.hitBox.x += entity.speed;
        }

        if (entity.hitBox.intersects(gp.player.hitBox)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }

        entity.hitBox.x = entityOriginalX;
        entity.hitBox.y = entityOriginalY;
        gp.player.hitBox.x = playerOriginalX;
        gp.player.hitBox.y = playerOriginalY;

        return contactPlayer;
    }
}