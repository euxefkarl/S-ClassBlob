package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import object.OBJ_flame;
import object.OBJ_wind;
import object.OBJ_water;




public class UI {
    GamePanel gp;
    Font arial_40;
    BufferedImage flameImage;
    BufferedImage windImage;
    BufferedImage waterImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

    String flame = "Inactive";
    String wind = "Inactive";
    String water = "Inactive";
    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 15);
        OBJ_flame fImage = new OBJ_flame();
        OBJ_water wImage =  new OBJ_water();
        OBJ_wind winImage = new OBJ_wind();
        flameImage = fImage.image;
        windImage = winImage.image;
        waterImage = wImage.image;
        
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }



    public void draw(Graphics2D g2, GamePanel gp){

        if(gp.player.hasFlame == true){
            flame = "Active";
        }
        if(gp.player.hasWater == true){
            water = "Active";
        }
        if(gp.player.hasWind == true){
            wind = "Active";
        }

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawImage(flameImage ,gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize,null);
        g2.drawImage(windImage ,gp.tileSize/2 , gp.tileSize/2 + 30, gp.tileSize, gp.tileSize,null);
        g2.drawImage(waterImage ,gp.tileSize/2, gp.tileSize/2 + 60, gp.tileSize, gp.tileSize,null);
        g2.drawString(" "+ flame,  60,50);
        g2.drawString(" "+ wind,60,80);
        g2.drawString(" "+ water,60,110);
        
        
        
        if(messageOn == true){
            g2.setFont(g2.getFont().deriveFont(30F));
            g2.drawString(message,gp.tileSize/2 * 9 + 30,gp.tileSize/2 *5);
            messageCounter++;
              
            if(messageCounter > 120){
                messageCounter = 0;
                messageOn = false;
                
            }

        }
        

    }
}
