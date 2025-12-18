package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.SpriteManager;

public abstract class Entity {

    //core
    protected final GamePanel gp;
    protected final SpriteManager sm;

    //position
    public int worldX, worldY;
    public String direction = "down";
    public int speed;
    public boolean collisionOn;

    //hitbox
    public Rectangle hitBox = new Rectangle(0, 0, 32, 32);

    //state
    public boolean alive = true;
    public int entityType;

    //id
    public String name;

    //sprites
    public BufferedImage up1, up2, down1, down2;
    public BufferedImage left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2;
    public BufferedImage attackDown1, attackDown2;
    public BufferedImage attackLeft1, attackLeft2;
    public BufferedImage attackRight1, attackRight2;
    public BufferedImage image, image2, image3;
    
    public int life;
    public int maxLife;
    public int strength, defense, agility, intelligence, endurance;
    public int attackDamage;
    public int level, exp, nextLevelExp;
    public Entity currentForm;  
    public java.util.List<Item> inventory;

    protected int spriteCounter;
    protected int spriteNum = 1;

    public static final int typeNPC = 1;
    public static final int typeForm = 2;

    public boolean knockBack = false;
    public String knockBackDirection = "down";
    public boolean collision = false;

    public int motion1Duration;
    public int motion2Duration;

    protected Entity(GamePanel gp) {
        this.gp = gp;
        this.sm = gp.spriteManager;
    }

    
    public abstract void update();

   
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        BufferedImage image = getCurrentSprite();

        int drawWidth = (image != null) ? image.getWidth() : gp.tileSize;
        int drawHeight = (image != null) ? image.getHeight() : gp.tileSize;

        if (playerScreen()) {
            g2.drawImage(image, screenX, screenY, drawWidth, drawHeight, null);
        }
    }

    protected BufferedImage getCurrentSprite() {
        return switch (direction) {
            case "up" ->
                (spriteNum == 1) ? up1 : up2;
            case "down" ->
                (spriteNum == 1) ? down1 : down2;
            case "left" ->
                (spriteNum == 1) ? left1 : left2;
            case "right" ->
                (spriteNum == 1) ? right1 : right2;
            default ->
                down1;
        };
    }

   
    protected BufferedImage setup(String path, int w, int h) {
        return sm.load(path, w, h);
    }

    protected void loadMovementSprites(String basePath, int w, int h) {
        up1 = setup(basePath + "_up1", w, h);
        up2 = setup(basePath + "_up2", w, h);
        down1 = setup(basePath + "_down1", w, h);
        down2 = setup(basePath + "_down2", w, h);
        left1 = setup(basePath + "_left1", w, h);
        left2 = setup(basePath + "_left2", w, h);
        right1 = setup(basePath + "_right1", w, h);
        right2 = setup(basePath + "_right2", w, h);
    }

    protected void loadObjectSprites(String basePath, int w, int h) {
        down1 = setup(basePath + "1", w, h);
        down2 = setup(basePath + "2", w, h);
    }

    private boolean playerScreen() {
        boolean onScreen = false;
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            onScreen = true;
        }
        return onScreen;
    }
}
