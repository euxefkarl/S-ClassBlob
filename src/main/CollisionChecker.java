package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    
    //checks location of world objects with collision on
    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.hitBox.x;
        int entityRightWorldX = entity.worldX + entity.hitBox.x + entity.hitBox.width;
        int entityTopWorldY = entity.worldY + entity.hitBox.y;
        int entityBotWorldY = entity.worldY + entity.hitBox.y + entity.hitBox.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBotRow = entityBotWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        //temporary direction for knockback implementation
        String direction = entity.direction;
        if (entity.knockBack == true) {
            direction = entity.knockBackDirection;
        }

        switch (direction) {
            case "up" -> {
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
            }
            case "down" -> {
                entityBotRow = (entityBotWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBotRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBotRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
            }
            case "left" -> {
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBotRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
            }
            case "right" -> {
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBotRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
            }
        }

    }

    //check entity
    public int checkEntity(Entity entity, Entity[] targets, boolean returnIndexIfPlayer) {
        int index = 999;
        String direction = entity.knockBack ? entity.knockBackDirection : entity.direction;
        if (direction == null) return 999;
        for (int i = 0; i < targets.length; i++) {
            Entity target = targets[i];
            if (target == null) {
                continue;
            }
            if (target == entity) {
                continue;
            }
            if (!target.collision) {
                continue;
            }

            if (intersects(entity, target, direction, entity.speed)) {
                entity.collisionOn = true;
                if (returnIndexIfPlayer) {
                    index = i;
                }
            }
        }
        return index;
    }

    //check player
    public boolean checkPlayer(Entity entity) {
        String direction = entity.knockBack ? entity.knockBackDirection : entity.direction;
        if (intersects(entity, gp.player, direction, entity.speed)) {
            entity.collisionOn = true;
            return true;
        }
        return false;
    }

    //utility hitbox intersection tool
    private boolean intersects(Entity e1, Entity e2, String direction, int speed) {
        int e1Left = e1.worldX + e1.hitBox.x;
        int e1Top = e1.worldY + e1.hitBox.y;
        int e1Right = e1Left + e1.hitBox.width;
        int e1Bottom = e1Top + e1.hitBox.height;

        int e2Left = e2.worldX + e2.hitBox.x;
        int e2Top = e2.worldY + e2.hitBox.y;
        int e2Right = e2Left + e2.hitBox.width;
        int e2Bottom = e2Top + e2.hitBox.height;

        switch (direction) {
            case "up" ->
                e1Top -= speed;
            case "down" ->
                e1Bottom += speed;
            case "left" ->
                e1Left -= speed;
            case "right" ->
                e1Right += speed;
        }

        return e1Right > e2Left && e1Left < e2Right && e1Bottom > e2Top && e1Top < e2Bottom;
    }
}
