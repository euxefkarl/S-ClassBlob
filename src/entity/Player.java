package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int hasItem = 0;
    public boolean  hasFlame = false;
    public boolean  hasWind = false;
    public boolean hasWater = false;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
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
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 25;
        speed = 4;
        direction = "down";
    }

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
            
            /* 
            up1 = ImageIO.read(new File("C:\\Users\\acer\\OneDrive\\Desktop\\CODE\\S-ClassBlob\\src\\res\\player\\player_up1.png"));
            up2 = ImageIO.read(new File("C:\\Users\\acer\\OneDrive\\Desktop\\CODE\\S-ClassBlob\\src\\res\\player\\player_up2.png"));
            down1 = ImageIO.read(new File("C:\\Users\\acer\\OneDrive\\Desktop\\CODE\\S-ClassBlob\\src\\res\\player\\player_down1.png"));
            down2 = ImageIO.read(new File("C:\\Users\\acer\\OneDrive\\Desktop\\CODE\\S-ClassBlob\\src\\res\\player\\player_down2.png"));
            left1 = ImageIO.read(new File("C:\\Users\\acer\\OneDrive\\Desktop\\CODE\\S-ClassBlob\\src\\res\\player\\player_left1.png"));
            left2 = ImageIO.read(new File("C:\\Users\\acer\\OneDrive\\Desktop\\CODE\\S-ClassBlob\\src\\res\\player\\player_left2.png"));
            right1 = ImageIO.read(new File("C:\\Users\\acer\\OneDrive\\Desktop\\CODE\\S-ClassBlob\\src\\res\\player\\player_right1.png"));
            right2 = ImageIO.read(new File("C:\\Users\\acer\\OneDrive\\Desktop\\CODE\\S-ClassBlob\\src\\res\\player\\player_right2.png"));
            */
            


        }catch(IOException e){
            //e.printStackTrace();
            System.out.println(getClass().getResource("/player/player_up1.png"));
        }

    }

    public void update(){
        if(keyH.upPressed == true || keyH.downPressed == true 
            || keyH.leftPressed == true || keyH.rightPressed == true){

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

        if(collisionOn == false){
            switch(direction){
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;

            }
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

    public void pickUpObject(int i){
        if(i != 999){
            String objectName = gp.obj[i].name;

            switch(objectName){
                case "flame":
                    hasFlame = true;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Acquired power of flame");
                    break;
                case "wind":
                    hasWind = true;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Acquired power of wind");
                    break;
                case "water":
                    hasWater= true;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Acquired power of water");
                    break;
            }
        }
    }

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
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

}
