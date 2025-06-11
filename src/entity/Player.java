package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();

    }

    public void setDefaultValues(){
        x = 100;
        y = 100;
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
        if(keyH.upPressed){
            y -= speed;
            direction = "up";
        }
        else if(keyH.downPressed){
            y += speed;
            direction = "down";
        }
        else if(keyH.leftPressed){
            x -= speed;
            direction = "left";
        }
        else if(keyH.rightPressed){
            x += speed;
            direction = "right";
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

    public void draw(Graphics2D g2){
        //g2.setColor(Color.white);
        //g2.fillRect(x, y, gp.tileSize, gp.tileSize);
        
        //draw player image based on direction
        BufferedImage image = null;
        switch(direction){
            case "up" :
                if(spriteNum == 1){
                    image = up1;
                }
                if(spriteNum == 2){
                    image = up2;
                }
                break;
            case "down" :
                if(spriteNum == 1){
                    image = down1;
                }
                if(spriteNum == 2){
                    image = down2;
                }
                break;
            case "left" : 
                if(spriteNum == 1){
                    image = left1;
                }
                if(spriteNum == 2){
                    image = left2;
                }
                break;
            case "right" :
                if(spriteNum == 1){
                    image = right1;
                }
                if(spriteNum == 2){
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }

}
