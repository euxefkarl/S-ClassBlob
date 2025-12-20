package entity;

import ai.EntityAI;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import main.GamePanel;
import main.SpriteManager;

public abstract class Entity {

    // --- core systems ---
    protected final GamePanel gp;
    protected final SpriteManager sm;
    protected EntityAI ai;

    // --- position & movement ---
    public int worldX, worldY;
    public int speed;
    public String direction = "down";
    public Rectangle hitBox = new Rectangle(0, 0, 32, 32);

    // --- state ---
    public int entityType;
    public boolean collisionOn = false;
    public boolean collision = false;
    public boolean alive = true;
    public String name;
    public boolean generatesKnockback = false;

    // --- visual state ---
    public boolean hpBarOn = false;
    public int hpBarCounter = 0;

    public boolean invincible = false;
    public boolean dying = false;
    public int dyingCounter = 0;

    protected int spriteCounter;
    protected int spriteNum = 1;

    // --- stats ---
    public int life;
    public int maxLife;
    public int strength, defense, agility, intelligence, endurance;
    public int attackDamage;
    public int level, exp, nextLevelExp;
    public int knockBackPower;

    // --- inventory ---
    public Entity currentForm;
    public List<Item> inventory;

    // --- sprites ---
    public BufferedImage up1, up2, down1, down2;
    public BufferedImage left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2;
    public BufferedImage attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage image, image2, image3;

    // --- constants ---
    public final int typePlayer = 0;
    public final int typeNPC = 1;
    public final int typeMonster = 2;
    public final int typeConsumable = 3;
    public final int typeForm = 4;

    public Entity(GamePanel gp) {
        this.gp = gp;
        this.sm = gp.spriteManager;
    }

    public void update() {
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = getCurrentSprite();

        // transform world coords to relative screen coords
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (onScreen(screenX, screenY)) {

            drawMonsterHealthBar(g2, screenX, screenY);

            applyStatusEffects(g2);

            int tempScreenX = screenX;
            int tempScreenY = screenY;
            int drawWidth = gp.tileSize;
            int drawHeight = gp.tileSize;

            // shift drawing position for larger attack frames
            if (this instanceof LivingEntity living && living.isAttacking()) {
                switch (direction) {
                    case "up" -> tempScreenY -= gp.tileSize;
                    case "left" -> tempScreenX -= gp.tileSize;
                }
            }

            if (image != null) {
                drawWidth = image.getWidth();
                drawHeight = image.getHeight();
            }

            g2.drawImage(image, tempScreenX, tempScreenY, drawWidth, drawHeight, null);

            changeAlpha(g2, 1f);

            if (gp.keyH.showDebug) {
                drawHitboxDebug(g2, screenX, screenY);
            }
        }
    }

    // --- visual helpers ---

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        // apply transparency layer for effects
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    private boolean onScreen(int screenX, int screenY) {
        // check if coordinates are within the player camera view
        return worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY;
    }

    private void applyStatusEffects(Graphics2D g2) {
        if (dying) {
            dyingAnimation(g2);
        } else if (invincible) {
            hpBarOn = true;
            hpBarCounter = 0;
            changeAlpha(g2, 0.4f);
        }
    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;
        int blinkInterval = 5; 

        // manage the visibility cycle to produce a flicker
        if (dyingCounter > blinkInterval * 8) {
            alive = false;
        } else {
            if ((dyingCounter / blinkInterval) % 2 == 0) {
                changeAlpha(g2, 0f);
            } else {
                changeAlpha(g2, 1f);
            }
        }
    }

    private void drawMonsterHealthBar(Graphics2D g2, int screenX, int screenY) {
        if (entityType == typeMonster && hpBarOn) {
            g2.setColor(new Color(35, 35, 35));
            g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

            // scale the foreground bar to match current health percentage
            double oneScale = (double) gp.tileSize / maxLife;
            double hpBarValue = oneScale * life;
            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

            hpBarCounter++;
            if (hpBarCounter > 600) {
                hpBarCounter = 0;
                hpBarOn = false;
            }
        }
    }

    protected void drawHitboxDebug(Graphics2D g2, int screenX, int screenY) {
        // calculate collision bounds relative to screen space
        int hbX = screenX + hitBox.x;
        int hbY = screenY + hitBox.y;

        g2.setColor(new Color(0, 255, 255, 100));
        g2.fillRect(hbX, hbY, hitBox.width, hitBox.height);
        g2.setColor(Color.white);
        g2.drawRect(hbX, hbY, hitBox.width, hitBox.height);

        // draw active weapon hitboxes for living entities
        if (this instanceof LivingEntity living && living.isAttacking()) {
            int ahX = living.attackHitbox.x - gp.player.worldX + gp.player.screenX;
            int ahY = living.attackHitbox.y - gp.player.worldY + gp.player.screenY;

            g2.setColor(new Color(255, 0, 0, 100));
            g2.fillRect(ahX, ahY, living.attackHitbox.width, living.attackHitbox.height);
            g2.setColor(Color.red);
            g2.drawRect(ahX, ahY, living.attackHitbox.width, living.attackHitbox.height);
        }
    }

    // --- sprite helpers ---

    protected BufferedImage getCurrentSprite() {
        // logic switch for standard four-way movement frames
        return switch (direction) {
            case "up" -> (spriteNum == 1) ? up1 : up2;
            case "down" -> (spriteNum == 1) ? down1 : down2;
            case "left" -> (spriteNum == 1) ? left1 : left2;
            case "right" -> (spriteNum == 1) ? right1 : right2;
            default -> down1;
        };
    }

    protected BufferedImage setup(String path, int w, int h) {
        return sm.load(path, w, h);
    }

    protected void loadMovementSprites(String basePath, int w, int h) {
        // batch load walk animation frames from filesystem
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

    // --- hooks ---
    public int getAttackPower() { return 0; }
    public int getDefense() { return 0; }
    public boolean use(Entity user) { return false; }
    public void onPickup(Entity picker) {}
    public void takeDamage(int damage, Entity source) {}
}