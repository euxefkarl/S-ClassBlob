package main;

import entity.Entity;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import object.OBJ_Heart;




public class UI {
    GamePanel gp;
    Font arial_40;
    Graphics2D g2;
    public boolean messageOn = false;
    public String message = "";
    public int messageCounter = 0;
    public String currentDialogue = "";
    public int commandNum = 0;
    BufferedImage full_life, half_life, empty_life;
   
    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 15);
        Entity heart = new OBJ_Heart(gp);
        full_life = heart.image;
        half_life = heart.image2;
        empty_life = heart.image3;
        
        
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }



    public void draw(Graphics2D g2, GamePanel gp){
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }
        if(gp.gameState == gp.playState){
            drawPlayerLife();
        }
        if(gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPausedScreen();
        }
        if(gp.gameState == gp.dialogueState){
            drawPlayerLife();
            drawDialogueScreen();
        }
            
    }
    public void drawDialogueScreen(){
        //window parameters
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;

        drawSubWindow(x,y,width,height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,26F));
        x += gp.tileSize;
        y += gp.tileSize;
        for (String line: currentDialogue.split("\n")){
             g2.drawString(line, x, y);
             y+=40;
        }
    }
    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(0,0,0,200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
    public void drawPausedScreen(){
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
        String text = "PAUSED";
        int x = getCenteredX(text);

        int y = gp.screenHeight/2;
        g2.drawString(text, x, y);

    }



    public void drawTitleScreen(){
        //title name
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
        String text = "S-Class Blob";
        int x = getCenteredX(text);
        int y = gp.tileSize*3;

        //shadow
        g2.setColor(Color.gray);
        g2.drawString(text, x+5, y+5);

        //main color
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        //blob image
        x = gp.screenWidth/2 - (gp.tileSize);
        y += gp.tileSize;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

        //menu options
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));

        text = "NEW GAME";
        x = getCenteredX(text);
        y += gp.tileSize*4;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x - gp.tileSize, y);
        }
        
        text = "LOAD GAME";
        x = getCenteredX(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "QUIT";
        x = getCenteredX(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 2){
            g2.drawString(">", x - gp.tileSize, y);
        }
    }
    public int getCenteredX(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }

    public void drawPlayerLife(){
        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;
        //draw max life
        while(i < gp.player.maxLife/2){
            g2.drawImage(empty_life, x, y, null);
            i++;
            x += full_life.getWidth();
        }
        //reset
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;
        //draw current life
        while(i < gp.player.life){
            g2.drawImage(half_life, x, y, null);
            i++;
            if(i < gp.player.life){
                g2.drawImage(full_life, x, y, null);
            }
            i++;
            x += full_life.getWidth();
        }
    }
        
}
