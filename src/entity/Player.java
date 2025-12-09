package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
   
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    
    

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        hitBox = new Rectangle();
        hitBox.x = 8;
        hitBox.y = 16;
        hitBox.width = 32;
        hitBox.height = 20;

        setDefaultValues();
        getPlayerImage();

    }

    public void setDefaultValues(){
        // desired start tile (tile coordinates)
        int startTileX = 2;
        int startTileY = 47;

        // convert to world pixels
        worldX = gp.tileSize * startTileX;
        worldY = gp.tileSize * startTileY;
        speed = 4;
        direction = "down";

        //player status
        maxLife = 6;
        life = maxLife;

        
    }
    //load player sprites
    public void getPlayerImage(){
        try{
             
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/player/player_up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/player/player_up2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/player/player_down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/player/player_down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/player/player_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/player/player_left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/player/player_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/player/player_right2.png"));
    


        }catch(IOException e){
            //e.printStackTrace();
            System.out.println(getClass().getResource("/player/player_up1.png"));
        }

    }

    //checks key input to change player sprite 
    @Override
    public void update(){
        if(keyH.upPressed == true || keyH.downPressed == true 
            || keyH.leftPressed == true || keyH.rightPressed == true || keyH.interactPressed == true){

            if(keyH.upPressed){
                direction = "up";
            }
            else if(keyH.downPressed){
                direction = "down";
            }
            else if(keyH.leftPressed){
                direction = "left";
            }
            else if(keyH.rightPressed){
                direction = "right";
            }
    
        //collison check
        collisionOn = false;
        gp.cChecker.checkTile(this);
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex);
        //check npc collision
         int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        interactNPC(npcIndex);
        //check monster collision
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        contactMonster(monsterIndex);
        if(collisionOn == false && keyH.interactPressed == false){
            switch(direction){
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;

            }
        }
        gp.keyH.interactPressed = false;

        
      }
      //changes sprite for walking animation
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
        //invincibility timer
        if (invincible == true) {
            invinceCounter++;
            if (invinceCounter > 60) {
                invincible = false;
                invinceCounter = 0;
            }
        }
    }
    //check npc collision
   
    //checks collision between pick upable objects
    public void pickUpObject(int i){
        if(i != 999){
           
        }
    }
    public void interactNPC(int i){
        if(i != 999){
            if(gp.keyH.interactPressed == true){
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }     
        }
        
    }
    public void contactMonster(int i){
        if(i != 999){
            //damage player
            if (invincible == false) {
                life -= 1;
                invincible = true;
                invinceCounter = 0;
            }
    ;
        }
    }
    //draws player current sprite
    @Override
    public void draw(Graphics2D g2){
        //g2.setColor(Color.white);
        //g2.fillRect(x, y, gp.tileSize, gp.tileSize);
        
        //draw player image based on direction
        BufferedImage image = null;
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
        //make player transparent when invincible
        if (invincible == true){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

}
