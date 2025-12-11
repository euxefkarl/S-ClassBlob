package entity;

import main.GamePanel;

public class Villager extends  Entity{
    
    public Villager(GamePanel gp){
        super(gp);

        direction = "down";
        speed = 2;
      
        getImage();
        setDialogue();


    }
    public void getImage(){
      
            up1 = setup("/res/npc/villager_up1", gp.tileSize, gp.tileSize);
            up2 = setup("/res/npc/villager_up2", gp.tileSize, gp.tileSize);
            down1 = setup("/res/npc/villager_down1", gp.tileSize, gp.tileSize);
            down2 = setup("/res/npc/villager_down2", gp.tileSize, gp.tileSize);
            left1 = setup("/res/npc/villager_left1", gp.tileSize, gp.tileSize);
            left2 = setup("/res/npc/villager_left2", gp.tileSize, gp.tileSize);
            right1 = setup("/res/npc/villager_right1", gp.tileSize, gp.tileSize);
            right2 = setup("/res/npc/villager_right2", gp.tileSize, gp.tileSize);
    }
    @Override
    public void setAction(){
        actionLockCounter++;
        if(actionLockCounter == 120){ //change direction every 2 seconds
            chooseDirection();
            actionLockCounter = 0;
        }     
    }
    public void chooseDirection(){
        int i = (int)(Math.random()*100)+1; //pick random number from 1 to 100
        // restrict movement area
        int minCol = 37;
        int maxCol = 44;
        int minRow = 34;
        int maxRow = 48;

        //scale to world coordinates
        int tileSize = gp.tileSize;
        int minX = minCol * tileSize;
        int maxX = (maxCol + 1) * tileSize; 
        int minY = minRow * tileSize;
        int maxY = (maxRow + 1) * tileSize;

        if(i <= 25){
            // try move up, otherwise pick down to stay inside area
            if(worldY - speed >= minY) direction = "up";
            else direction = "down";
        }
        else if(i > 25 && i <= 50){
            // try move down, otherwise pick up
            if(worldY + speed <= maxY) direction = "down";
            else direction = "up";
        }
        else if(i > 50 && i <= 75){
            // try move left, otherwise pick right
            if(worldX - speed >= minX) direction = "left";
            else direction = "right";
        }
        else { // 76-100
            // try move right, otherwise pick left
            if(worldX + speed <= maxX) direction = "right";
            else direction = "left";
        }
    }

    public void setDialogue(){
        dialogues[0] = "Hello there!";
        dialogues[1] = "Are you new around here?";
        dialogues[2] = "You are going to defeat the evil blob, right?";
        dialogues[3] = "Theres are elemental gems somewhere here.";
        dialogues[4] = "It's no good for me, but you might need it.\nSince you're a blob and all.";
    }
    @Override
    public void speak(){
        super.speak();
    }
        
    

}
