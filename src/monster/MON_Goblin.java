package monster;

import entity.Entity;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class MON_Goblin extends Entity{
    public MON_Goblin(GamePanel gp){
        
        super(gp);
        name = "Goblin";
        entityType = 2;
        maxLife = 4;
        life = maxLife;
        speed = 1;

       
        getImage();
    }

    public void getImage(){
        try{
             
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/goblin_up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/monster/goblin_up2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/goblin_down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/monster/goblin_down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/goblin_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/monster/goblin_left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/goblin_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/monster/goblin_right2.png"));
    


        }catch(IOException e){
            //e.printStackTrace();
            System.out.println(getClass().getResource("/res/monster/goblin_up1.png"));
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

    

}
