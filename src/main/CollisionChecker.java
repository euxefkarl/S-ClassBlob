package main;

import entity.Entity;

public class CollisionChecker {
    GamePanel gp;
    public CollisionChecker(GamePanel gp){
        this.gp = gp;

    }
    //checks location of world objects with collision on
    public void checkTile(Entity entity){
        int entityLeftWorldX = entity.worldX + entity.hitBox.x;
        int entityRightWorldX = entity.worldX + entity.hitBox.x + entity.hitBox.width;
        int entityTopWorldY = entity.worldY + entity.hitBox.y;
        int entityBotWorldY = entity.worldY + entity.hitBox.y+ entity.hitBox.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBotRow = entityBotWorldY/gp.tileSize;

        int tileNum1, tileNum2;

        //temporary direction for knockback implementation
        String direction = entity.direction;
        if(entity.knockBack == true){
            direction = entity.knockBackDirection;
        }

        switch(direction){
            case "up" -> {
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
            }
            case "down" -> {
                entityBotRow = (entityBotWorldY + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBotRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBotRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
            }
            case "left" -> {
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBotRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
            }
            case "right" -> {
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBotRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
            }
        }

    }
    //checks collision between objects and entities
    public int checkObject (Entity entity, boolean player){
        int index = 999;
        for(int i = 0; i < gp.obj.length; i++){
            if(gp.obj[i] != null){
                entity.hitBox.x = entity.worldX + entity.hitBox.x;
                entity.hitBox.y = entity.worldY + entity.hitBox.y;

                gp.obj[i].hitBox.x = gp.obj[i].worldX + gp.obj[i].hitBox.x;
                gp.obj[i].hitBox.y = gp.obj[i].worldY + gp.obj[i].hitBox.y;

                switch(entity.direction){
                    case "up":entity.hitBox.y -= entity.speed; break;
                    case "down":entity.hitBox.y += entity.speed;break;
                    case "left":entity.hitBox.x -= entity.speed;break;
                    case "right":entity.hitBox.x += entity.speed; break;   
                }
                if(entity.hitBox.intersects(gp.obj[i].hitBox)){
                            if(gp.obj[i].collision == true){
                                entity.collisionOn = true;
                            }
                            if(player == true){
                                index = i;
                            }
                        }
                gp.obj[i].hitBox.x = gp.obj[i].defaultHitBoxX;
                gp.obj[i].hitBox.y = gp.obj[i].defaultHitBoxY;
                entity.hitBox.x = entity.defaultHitBoxX;
                entity.hitBox.y = entity.defaultHitBoxY;

            }
        }
        return index;
    }
    //NPC or monster colilsion check
    public int checkEntity(Entity entity, Entity[] target){
        int index = 999;

        //temporary direction for knockback implementation
        String direction = entity.direction;
        if(entity.knockBack == true){
            direction = entity.knockBackDirection;
        }

        for(int i = 0; i < target.length; i++){
            if(target[i] != null){
                entity.hitBox.x = entity.worldX + entity.hitBox.x;
                entity.hitBox.y = entity.worldY + entity.hitBox.y;

                target[i].hitBox.x = target[i].worldX + target[i].hitBox.x;
                target[i].hitBox.y = target[i].worldY + target[i].hitBox.y;

                switch(direction){
                    case "up": entity.hitBox.y -= entity.speed;break;
                    case "down":entity.hitBox.y += entity.speed;break;   
                    case "left":entity.hitBox.x -= entity.speed;break;
                    case "right":entity.hitBox.x += entity.speed;break;   
                }
                if(entity.hitBox.intersects(target[i].hitBox)){
                    if(target[i] != entity){
                            entity.collisionOn = true;
                            index = i;
                    }
                }
                target[i].hitBox.x = target[i].defaultHitBoxX;
                target[i].hitBox.y = target[i].defaultHitBoxY;
                entity.hitBox.x = entity.defaultHitBoxX;
                entity.hitBox.y = entity.defaultHitBoxY;
           
            }
        }        return index;
    }

    public boolean checkPlayer(Entity entity){
        boolean contactPlayer = false;
        entity.hitBox.x = entity.worldX + entity.hitBox.x;
                entity.hitBox.y = entity.worldY + entity.hitBox.y;

                gp.player.hitBox.x = gp.player.worldX + gp.player.hitBox.x;
                gp.player.hitBox.y = gp.player.worldY + gp.player.hitBox.y;

                switch(entity.direction){
                    case "up":entity.hitBox.y -= entity.speed;break;
                    case "down":entity.hitBox.y += entity.speed;break;
                    case "left":entity.hitBox.x -= entity.speed;break;
                    case "right":entity.hitBox.x += entity.speed;break;
                    }
                if(entity.hitBox.intersects(gp.player.hitBox)){
                    entity.collisionOn = true;
                    contactPlayer = true;
                }
                gp.player.hitBox.x = gp.player.defaultHitBoxX;
                gp.player.hitBox.y = gp.player.defaultHitBoxY;
                entity.hitBox.x = entity.defaultHitBoxX;
                entity.hitBox.y = entity.defaultHitBoxY;
           return contactPlayer;
    }
}
