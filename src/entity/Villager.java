package entity;

import java.io.IOException;
import javax.imageio.ImageIO;
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
        try{
             
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/npc/villager_up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/npc/villager_up2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/npc/villager_down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/npc/villager_down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/npc/villager_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/npc/villager_left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/npc/villager_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/npc/villager_right2.png"));
        }catch(IOException e){
            //e.printStackTrace();
            System.out.println(getClass().getResource("/res/npc/villager_up1.png"));
        }

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

        if(i <= 25){
            direction = "up";
        }
        else if(i > 25 && i <= 50){
            direction = "down";
        }
        else if(i > 50 && i <= 75){
            direction = "left";
        }
        else if(i > 75 && i <= 100){
            direction = "right";
        }
    }

    public void setDialogue(){
        dialogues[0] = "Hello there!";
        dialogues[1] = "Are you new around here?";
        dialogues[2] = "You are going to defeat the evil blob, right?";
        dialogues[3] = "Theres an elemental gem somewhere here.";
        dialogues[4] = "It's no good for me, but you might need it.";
    }
    @Override
    public void speak(){
        super.speak();
    }
        
    

}
