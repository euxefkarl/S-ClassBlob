package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
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
    public Entity attacker;

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
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;
    public boolean knockBack = false;
    public boolean onPath = false;
    public String knockBackDirection;

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
    public int motion1Duration;
    public int motion2Duration;

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
                switch (knockBackDirection) {
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

        } else if (attacking == true) {
            attack();
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
            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
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

    public void attack() {

        spriteCounter++;

        if (spriteCounter <= motion1Duration) {
            spriteNum = 1;
        }
        if (spriteCounter > motion1Duration && spriteCounter <= motion2Duration) {
            spriteNum = 2;
            // Save current hitbox state
            int currentHitBoxX = hitBox.x;
            int currentHitBoxY = hitBox.y;
            int currentHitBoxWidth = hitBox.width;
            int currentHitBoxHeight = hitBox.height;

            // Determine attack dimensions. For left/right attacks we swap so the attack becomes a tile-wide area scaled to the entity's size.
            int attackW = attackHitBox.width;
            int attackH = attackHitBox.height;
            if (direction.equals("left") || direction.equals("right")) {
                int _tmp = attackW;
                attackW = attackH;
                attackH = _tmp;
            }

            // Place the hitbox in front of the entity according to direction
            switch (direction) {
                case "up" -> {
                    hitBox.width = attackW;
                    hitBox.height = attackH;
                    hitBox.x = defaultHitBoxX + (currentHitBoxWidth - attackW) / 2;
                    hitBox.y = defaultHitBoxY - attackH;
                }
                case "down" -> {
                    hitBox.width = attackW;
                    hitBox.height = attackH;
                    hitBox.x = defaultHitBoxX + (currentHitBoxWidth - attackW) / 2;
                    hitBox.y = defaultHitBoxY + currentHitBoxHeight;
                }
                case "left" -> {
                    hitBox.width = attackW;
                    hitBox.height = attackH;
                    hitBox.x = defaultHitBoxX - attackW;
                    hitBox.y = defaultHitBoxY + (currentHitBoxHeight - attackH) / 2;
                }
                case "right" -> {
                    hitBox.width = attackW;
                    hitBox.height = attackH;
                    hitBox.x = defaultHitBoxX + currentHitBoxWidth;
                    hitBox.y = defaultHitBoxY + (currentHitBoxHeight - attackH) / 2;
                }
            }

            // Check collision for damage
            if (entityType == typeMonster) {
                if (gp.cChecker.checkPlayer(this)) {
                    damagePlayer();
                }
            } else {
                // player
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                gp.player.damageMonster(monsterIndex, this);
            }

            // Restore original hitbox offsets and dimensions
            hitBox.x = currentHitBoxX;
            hitBox.y = currentHitBoxY;
            hitBox.width = currentHitBoxWidth;
            hitBox.height = currentHitBoxHeight;
        }

        if (spriteCounter > motion2Duration) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;

        }

    }

    public void checkAttack(int rate, int straight, int horizontal) {
        boolean targetInRange = false;
        int xDis = getXdistance(gp.player);
        int yDis = getYdistance(gp.player);

        switch (direction) {
            case "up":
                if (gp.player.getCenterY() < getCenterY() && yDis < straight && xDis < horizontal) {
                    targetInRange = true;
                }
                break;
            case "down":
                if (gp.player.getCenterY() > getCenterY() && yDis < straight && xDis < horizontal) {
                    targetInRange = true;
                }
                break;
            case "left":
                if (gp.player.getCenterX() < getCenterX() && xDis < straight && yDis < horizontal) {
                    targetInRange = true;
                }
                break;
            case "right":
                if (gp.player.getCenterY() > getCenterY() && yDis < straight && xDis < horizontal) {
                    targetInRange = true;
                }
                break;
        }
        if (targetInRange == true) {
            int i = new Random().nextInt(rate);
            if (i == 0) {
                attacking = true;
                spriteNum = 1;
                spriteCounter = 0;

            }
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

    public void knockBack(Entity target, Entity attacker) {
        this.attacker = attacker;
        target.knockBackDirection = attacker.direction;
        target.speed += 10;
        target.knockBack = true;
        System.out.println("knocked back");
    }

    public int getXdistance(Entity target) {
        int xDistance = Math.abs(getCenterX() - target.getCenterX());
        return xDistance;
    }

    public int getYdistance(Entity target) {
        int yDistance = Math.abs(getCenterY() - target.getCenterY());
        return yDistance;
    }

    public int getTiledistance(Entity target) {
        int tileDistance = (getXdistance(target) + getYdistance(target)) / gp.tileSize;
        return tileDistance;
    }

    public int getGoalCol(Entity target) {
        int goalCol = (target.worldX + target.hitBox.x) / gp.tileSize;
        return goalCol;
    }

    public int getGoalRow(Entity target) {
        int goalRow = (target.worldY + target.hitBox.y) / gp.tileSize;
        return goalRow;
    }

    public void checkEndChase(Entity target, int distance) {
        if (getTiledistance(target) > distance) {
            onPath = false;
        }
    }

    public void checkStartChase(Entity target, int distance) {
        if (getTiledistance(target) < distance) {
            onPath = true;
        }
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
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        if (dialogueIndex == 5) {
            System.out.println("on path villager");
            onPath = true;
            dialogueIndex = 0;
        }

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

    public void chooseDirection() {
        actionLockCounter++;
        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;
            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "right";
            }
            if (i > 75 && i <= 100) {
                direction = "left";
            }
            actionLockCounter = 0;
        }
    }

    public int getCenterX() {
        int centerX = (worldX + up1.getWidth()) / 2;
        return centerX;
    }

    public int getCenterY() {
        int centerY = (worldY + up1.getHeight()) / 2;
        return centerY;
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        BufferedImage image = null;

        //improve rendering performance by only rendering screen pixels, not entire map
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            int tempScreenX = screenX;
            int tempScreenY = screenY;
            switch (direction) {
                case "up" -> {
                    if (attacking == false) {
                        if (spriteNum == 1) {
                            image = up1;
                        }
                        if (spriteNum == 2) {
                            image = up2;
                        }
                    }
                    if (attacking == true) {
                        tempScreenY = screenY - gp.tileSize;
                        if (spriteNum == 1) {
                            image = attackUp1;
                        }
                        if (spriteNum == 2) {
                            image = attackUp2;
                        }
                    }
                    break;
                }
                case "down" -> {
                    if (attacking == false) {
                        if (spriteNum == 1) {
                            image = down1;
                        }
                        if (spriteNum == 2) {
                            image = down2;
                        }
                    }
                    if (attacking == true) {
                        if (spriteNum == 1) {
                            image = attackDown1;
                        }
                        if (spriteNum == 2) {
                            image = attackDown2;
                        }
                    }
                    break;
                }
                case "left" -> {
                    if (attacking == false) {
                        if (spriteNum == 1) {
                            image = left1;
                        }
                        if (spriteNum == 2) {
                            image = left2;
                        }
                    }
                    if (attacking == true) {
                        tempScreenX = screenX - gp.tileSize;
                        if (spriteNum == 1) {
                            image = attackLeft1;
                        }
                        if (spriteNum == 2) {
                            image = attackLeft2;
                        }
                    }
                    break;
                }
                case "right" -> {
                    if (attacking == false) {
                        if (spriteNum == 1) {
                            image = right1;
                        }
                        if (spriteNum == 2) {
                            image = right2;
                        }
                    }
                    if (attacking == true) {
                        if (spriteNum == 1) {
                            image = attackRight1;
                        }
                        if (spriteNum == 2) {
                            image = attackRight2;
                        }
                    }
                    break;
                }
            }
            // make player/monster transparent when invincible
            if (invincible == true) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
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

            // Use the actual image dimensions (already scaled in setup) so larger sprites render correctly
            int drawWidth = (image != null) ? image.getWidth() : gp.tileSize;
            int drawHeight = (image != null) ? image.getHeight() : gp.tileSize;

            // Adjust screen offsets when attack/visuals extend beyond one tile
            if (attacking) {
                switch (direction) {
                    case "up":
                        // when attacking up the sprite may extend above the base tile
                        tempScreenY = screenY - (drawHeight - gp.tileSize);
                        break;
                    case "left":
                        // when attacking left the sprite may extend to the left
                        tempScreenX = screenX - (drawWidth - gp.tileSize);
                        break;
                    // for down/right the default alignment generally places the sprite correctly
                }
            }

            g2.drawImage(image, tempScreenX, tempScreenY, drawWidth, drawHeight, null);
            changeAlpha(g2, 1f);

            // Debug: draw hitboxes when debug mode is enabled
            if (gp.keyH.showDebug) {
                // Normal hitbox (cyan, semi-transparent)
                int hbX = tempScreenX + hitBox.x;
                int hbY = tempScreenY + hitBox.y;
                java.awt.Color hbFill = new java.awt.Color(0, 255, 255, 80);
                g2.setColor(hbFill);
                g2.fillRect(hbX, hbY, hitBox.width, hitBox.height);
                g2.setColor(java.awt.Color.CYAN);
                g2.drawRect(hbX, hbY, hitBox.width, hitBox.height);

                // Attack hitbox (red, semi-transparent) - drawn in front of entity depending on direction
                if (attackHitBox.width > 0 && attackHitBox.height > 0) {
                    int ahW = attackHitBox.width;
                    int ahH = attackHitBox.height;
                    // Swap for left/right so the overlay matches the actual attack dimensions used in attack()
                    if (direction.equals("left") || direction.equals("right")) {
                        int _tmp = ahW;
                        ahW = ahH;
                        ahH = _tmp;
                    }

                    int ahX = hbX;
                    int ahY = hbY;
                    switch (direction) {
                        case "up" -> {
                            ahY = hbY - ahH;
                            ahX = hbX + (hitBox.width - ahW) / 2;
                        }
                        case "down" -> {
                            ahY = hbY + hitBox.height;
                            ahX = hbX + (hitBox.width - ahW) / 2;
                        }
                        case "left" -> {
                            ahX = hbX - ahW;
                            ahY = hbY + (hitBox.height - ahH) / 2;
                        }
                        case "right" -> {
                            ahX = hbX + hitBox.width;
                            ahY = hbY + (hitBox.height - ahH) / 2;
                        }
                    }
                    java.awt.Color ahFill = new java.awt.Color(255, 0, 0, 80);
                    g2.setColor(ahFill);
                    g2.fillRect(ahX, ahY, ahW, ahH);
                    g2.setColor(java.awt.Color.RED);
                    g2.drawRect(ahX, ahY, ahW, ahH);
                }
            }
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
        int startCol = (worldX + hitBox.x) / gp.tileSize;
        int startRow = (worldY + hitBox.y) / gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if (gp.pFinder.search() == true) {
            //next world x and y
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            //entity current solid area position
            int eLeftX = worldX + hitBox.x;
            int eRightX = worldX + hitBox.x + hitBox.width;
            int eTopY = worldY + hitBox.y;
            int eBotY = worldY + hitBox.y + hitBox.height;

            if (eTopY > nextY && eLeftX >= nextX && eRightX < nextX + gp.tileSize) {
                direction = "up";
            } else if (eTopY < nextY && eLeftX >= nextX && eRightX < nextX + gp.tileSize) {
                direction = "down";
            } else if (eTopY >= nextY && eBotY < nextY + gp.tileSize) {
                //left or right
                if (eLeftX > nextX) {
                    direction = "left";
                }
                if (eLeftX < nextX) {
                    direction = "right";
                }
            } else if (eTopY > nextY && eLeftX > nextX) {
                direction = "up";
                checkCollision();
                if (collision == true) {
                    direction = "left";
                }
            } else if (eTopY > nextY && eLeftX < nextX) {
                //up or right
                direction = "up";
                checkCollision();
                if (collision == true) {
                    direction = "right";
                }
            } else if (eTopY < nextY && eLeftX > nextX) {
                //down or left
                direction = "down";
                checkCollision();
                if (collision == true) {
                    direction = "left";
                }
            } else if (eTopY < nextY && eLeftX < nextX) {
                //down or right
                direction = "down";
                checkCollision();
                if (collision == true) {
                    direction = "right";
                }
            }
            //once reached stop search
            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;
            if (nextCol == goalCol && nextRow == goalRow) {
                onPath = false;
                System.out.println("no longer on path");
            }
        }
    }
}
