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
        if(gp.gameState == gp.statusScreenState){
            drawStatusScreen();
        }
            
    }

    public void drawStatusScreen(){
        BufferedImage formImage = null;
        final int frameX =  gp.tileSize *2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize *5;
        final int frameHeight = gp.tileSize *10;
        //draw frame
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));
        int textX = frameX+20;
        int textY = frameY + gp.tileSize;
        final int lineHeight =35;
        String form = "";
        //check if no equiped form
        if(gp.player.currentForm == null){
            formImage = gp.player.down1;
        }
        else{
            formImage = gp.player.currentForm.image;
        }
        //text
       
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("EXP", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("STR", textX, textY);
        textY += lineHeight;
        g2.drawString("AGI", textX, textY);
        textY += lineHeight;
        g2.drawString("INT", textX, textY);
        textY += lineHeight;
        g2.drawString("END", textX, textY);
        textY += lineHeight;
        g2.drawString("ATK", textX, textY);
        textY += lineHeight;
        g2.drawString("DEF", textX, textY);
        textY += lineHeight;
        g2.drawString("Form", textX, textY);
        textY += lineHeight;
        
        // values
        int tailX = (frameX + frameWidth ) -30;
        textY  = frameY+ gp.tileSize;
        String value;
        value = String.valueOf(gp.player.level);
        textX = getAlignmentForStatus(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.exp);
        textX = getAlignmentForStatus(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.life +"/"+gp.player.maxLife);
        textX = getAlignmentForStatus(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.strength);
        textX = getAlignmentForStatus(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.agility);
        textX = getAlignmentForStatus(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.intelligence);
        textX = getAlignmentForStatus(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.endurance);
        textX = getAlignmentForStatus(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.attackDamage);
        textX = getAlignmentForStatus(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.defense);
        textX = getAlignmentForStatus(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        g2.drawImage(formImage, tailX- gp.tileSize, textY - 28, null);
    
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
    public int getAlignmentForStatus(String value, int tailX){
        int length = (int)g2.getFontMetrics().getStringBounds(value, g2).getWidth();
        int x = tailX - length;
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
