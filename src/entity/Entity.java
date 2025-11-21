package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class Entity {

    GamePanel gp;
    //set commmon constants and variables
    public int worldX, worldY;
    public int speed;
    public BufferedImage up1,up2,down1,down2,left1,left2,right1,right2;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle hitBox = new Rectangle(0,0,24,24);
    public int defaultHitBoxX;
    public int defaultHitBoxY;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    String[] dialogues = new String[20];
    public int dialogueIndex = 0;


    public Entity(GamePanel gp){
        this.gp = gp;
    }
    public void update(){
        setAction();
        //collison check
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkPlayer(this);
        
        if(collisionOn == false){
            switch(direction){
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;

            }
        }
        spriteCounter++;
        if(spriteCounter > 12){
            if(spriteNum == 1){
                spriteNum = 2;
            }
            else if(spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
       
    }
    public void setAction(){
        //to be overridden
    }

    public void speak(){
        if(dialogues[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;
        switch (gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
            default:
                break;
            }

    }



    public void draw(java.awt.Graphics2D g2){
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY =  worldY - gp.player.worldY + gp.player.screenY;
        BufferedImage image = null;

            //improve rendering performance by only rendering screen pixels, not entire map
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.worldY &&
            worldY - gp.tileSize< gp.player.worldY + gp.player.worldY){
               
             switch(direction){
                case "up" -> {
                    if(spriteNum == 1){
                        image = up1;
                    }
                    if(spriteNum == 2){
                        image = up2;
                    }
                }
                case "down" -> {
                    if(spriteNum == 1){
                     image = down1;
                    }
                    if(spriteNum == 2){
                        image = down2;
                    }
                }
                case "left" -> { 
                    if(spriteNum == 1){
                        image = left1;
                    }
                    if(spriteNum == 2){
                        image = left2;
                    }
                }
                case "right" -> {
                    if(spriteNum == 1){
                        image = right1;
                    }
                    if(spriteNum == 2){
                        image = right2;
                    }
                }
        }
            g2.drawImage(image,screenX,screenY, gp.tileSize,gp.tileSize,null );
    }

        
}
}
