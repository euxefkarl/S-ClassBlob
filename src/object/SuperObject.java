package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;


    public void drawObject(Graphics2D g2, GamePanel gp){
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY =  worldY - gp.player.worldY + gp.player.screenY;
            

            //improve rendering performance by only rendering screen pixels, not entire map
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.worldY &&
               worldY - gp.tileSize< gp.player.worldY + gp.player.worldY){
            g2.drawImage(image,screenX,screenY, gp.tileSize,gp.tileSize,null );
    }
}
}