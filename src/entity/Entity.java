package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public class Entity {

    public GamePanel gp;

    //set commmon constants and variables
    public BufferedImage image, image2, image3;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public Rectangle hitBox = new Rectangle(0, 0, 32, 32);
    public Rectangle attackHitBox = new Rectangle(0, 0, 0, 0);
    public int defaultHitBoxX;
    public int defaultHitBoxY;
    String[] dialogues = new String[20];
    String text;

    //counters
    public int actionLockCounter = 0;
    public int invinceCounter = 0;
    public int spriteCounter = 0;
    public int dialogueIndex = 0;
    public int dyingCounter = 0;
    int hpBarCounter = 0;
    int knockBackCounter = 0;

    //state
    public boolean collision = false;
    public int worldX, worldY;
    public boolean collisionOn = false;
    public String direction = "down";
    public boolean invincible = false;
    public int spriteNum = 1;
    boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;
    public boolean knockBack = false;
    public boolean onPath = false;

    //character status
    public int maxLife;
    public int life;
    public int entityType; // 0 = player, 1 = npc, 2 = monster, 3 = form change item 4 = consumable
    public String name;
    public int speed;
    public int defaultSpeed;
    public int level;
    public int strength;
    public int agility;
    public int intelligence;
    public int endurance;
    public int defense;
    public int attackDamage;
    public int exp;
    public int nextLevelExp;
    public Entity currentForm;
    public int damageAmp;
    public Projectile fireball;
    public Projectile wave;
    public Projectile tornado;

    //item attributes
    public int itemAttackDamage;
    public int itemDefense;
    public String description = "";

    //entity types
    public final int typeMonster = 2;
    public final int typeForm = 3;
    public final int typeConsumable = 4;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void update() {
        if (knockBack == true) {
            checkCollision();
            if (collisionOn == true) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            } else if (collisionOn == false) {
                switch (gp.player.direction) {
                    case "up" ->
                        worldY -= speed;
                    case "down" ->
                        worldY += speed;
                    case "left" ->
                        worldX -= speed;
                    case "right" ->
                        worldX += speed;
                }
            }
            knockBackCounter++;
            if (knockBackCounter == 10) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }

        } else {
            setAction();
            checkCollision();
            if (collisionOn == false) {
                switch (direction) {
                    case "up" ->
                        worldY -= speed;
                    case "down" ->
                        worldY += speed;
                    case "left" ->
                        worldX -= speed;
                    case "right" ->
                        worldX += speed;

                }
            }
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        if (invincible == true) {
            invinceCounter++;
            if (invinceCounter > 40) {
                invincible = false;
                invinceCounter = 0;
            }
        }

    }

    public void checkCollision() {
        //collison check
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);

        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.entityType == 2 && contactPlayer == true) {
            // Monster contacts player
            damagePlayer();

        }
    }

    public void damagePlayer() {
        if (gp.player.invincible == false) {
            int damage = attackDamage - gp.player.defense;
            if (damage < 0) {
                damage = 0;
            }
            gp.player.life -= damage;
            gp.player.invincible = true;
            gp.player.invinceCounter = 0;
        }
    }

    public void setAction() {
        //to be overridden
    }

    public void use(Entity entity) {
        //to be overriden
    }

    public void damageReaction() {
        //to be overridden
    }

    public void checkDrop() {
        //to be overriden
    }

    public void dropItem(Entity item) {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) {
                gp.obj[i] = item;
                gp.obj[i].worldX = worldX;
                gp.obj[i].worldY = worldY;
                break;
            }
        }
    }

    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            onPath = true;
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;
        switch (gp.player.direction) {
            case "up" ->
                direction = "down";
            case "down" ->
                direction = "up";
            case "left" ->
                direction = "right";
            case "right" ->
                direction = "left";

        }

    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        BufferedImage image = null;

        //improve rendering performance by only rendering screen pixels, not entire map
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                && worldY + gp.tileSize > gp.player.worldY - gp.player.worldY
                && worldY - gp.tileSize < gp.player.worldY + gp.player.worldY) {

            switch (direction) {
                case "up" -> {
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                }
                case "down" -> {
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                }
                case "left" -> {
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                }
                case "right" -> {
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                }
            }
            //monster hp
            if (entityType == typeMonster && hpBarOn == true) {
                double oneScale = gp.tileSize / maxLife;
                double hpBar = oneScale * life;
                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);
                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBar, 10);
                hpBarCounter++;
                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }
            if (invincible == true) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4f);
            }
            if (dying == true) {
                dyingAnimation(g2);
            }
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            changeAlpha(g2, 1f);
        }
    }

    public void dyingAnimation(Graphics2D g2) {
        int i = 5;
        dyingCounter++;
        if (dyingCounter <= i) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i && dyingCounter <= i * 2) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 2 && dyingCounter <= i * 3) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i * 3 && dyingCounter <= i * 4) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 4 && dyingCounter <= i * 5) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i * 5 && dyingCounter <= i * 6) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 6 && dyingCounter <= i * 7) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i * 7 && dyingCounter <= i * 8) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 8) {
            alive = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    //setup method for loading images
    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + hitBox.x)/gp.tileSize;
        int startRow = (worldY + hitBox.y)/gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if(gp.pFinder.search() == true){
            //next world x and y
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            //entity current solid area position
            int eLeftX = worldX + hitBox.x;
            int eRightX = worldX + hitBox.x + hitBox.width;
            int eTopY = worldY + hitBox.y;
            int eBotY = worldY + hitBox.y + hitBox.height;

            if(eTopY > nextY && eLeftX >= nextX && eRightX < nextX + gp.tileSize){
                direction = "up";
            }
            else if(eTopY < nextY && eLeftX >= nextX && eRightX < nextX + gp.tileSize){
                direction = "down";
            }
            else if(eTopY >= nextY && eBotY < nextY + gp.tileSize){
                //left or right
                if(eLeftX > nextX){
                    direction = "left";
                }
                if(eLeftX < nextX){
                    direction = "right";
                }
            }
            else if(eTopY > nextY && eLeftX > nextX){
                direction = "up";
                checkCollision();
                if(collision == true){
                    direction = "left";
                }
            }
            else if(eTopY > nextY && eLeftX < nextX){
                //up or right
                direction = "up";
                checkCollision();
                if(collision == true){
                    direction = "right";
                }
            }
            else if(eTopY < nextY && eLeftX > nextX){
                //down or left
                direction = "down";
                checkCollision();
                if(collision == true){
                    direction = "left";
                }
            }
             else if(eTopY < nextY && eLeftX < nextX){
                //down or right
                direction = "down";
                checkCollision();
                if(collision == true){
                    direction = "right";
                }
            }
            //once reached stop search
            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;
            if(nextCol == goalCol && nextRow == goalRow){
                onPath = false;
                System.out.println("no longer on path");
            }
        }
    }
}
