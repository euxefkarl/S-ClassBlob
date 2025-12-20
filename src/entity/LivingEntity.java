package entity;

import combat.Attacker;
import combat.CombatComponent;
import combat.Damagable;
import combat.Defender;
import combat.HealthComponent;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;


//abstract class that deals with entities that have interaction with one another
//if theres no 2 way influence you are not here
public abstract class LivingEntity extends Entity implements Damagable, Attacker, Defender {

    protected boolean moving;
    protected boolean onPath;

    // --- KNOCKBACK STATE ---
    protected boolean knockBack = false;
    protected int knockBackCounter = 0;
    protected String knockBackDirection;

    public Rectangle attackHitbox = new Rectangle(0, 0, 0, 0);
    public HealthComponent health;
    protected CombatComponent combat;

    protected int invincibleCounter;
    protected int actionLockCounter = 0;
    protected int defaultSpeed;

    public LivingEntity(GamePanel gp) {
        super(gp);
    }

    @Override
    public void update() {
        // stop logic if death process started
        if (dying) {
            if (!alive)
                onDeathComplete();
            return;
        }

        // handle physics for being pushed before checking ai
        if (knockBack) {
            performKnockback();
            updateStatusCounters();
            return; 
        }

        setAction();
        collisionOn = false;

        if (combat != null)
            combat.update();

        // process movement only if not currently swinging weapon
        if (!isAttacking()) {
            if (moving) {
                gp.cChecker.checkTile(this);
                boolean isPlayer = (this instanceof Player);
                int objIndex = gp.cChecker.checkObject(this, isPlayer);
                if (isPlayer && objIndex != 999)
                    pickUpObject(objIndex);

                gp.cChecker.checkEntity(this, gp.npc);
                gp.cChecker.checkEntity(this, gp.monster);

                if (!isPlayer) {
                    boolean contactPlayer = gp.cChecker.checkPlayer(this);
                    onCollisionWithPlayer(contactPlayer);
                }

                if (!collisionOn)
                    tryMove(direction);
            }
        }

        updateAnimation();
        updateStatusCounters();
    }

    private void performKnockback() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);

        // stop movement if entity hits a solid wall
        if (collisionOn) {
            stopKnockback();
        } else {
            switch (knockBackDirection) {
                case "up" -> worldY -= speed;
                case "down" -> worldY += speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        }

        knockBackCounter++;
        // finish push state once frame limit reached
        if (knockBackCounter >= 10) {
            stopKnockback();
        }
    }

    private void stopKnockback() {
        knockBackCounter = 0;
        knockBack = false;
        speed = defaultSpeed;
    }

    @Override
    public void takeDamage(int amount, Entity attacker) {
        if (invincible || !alive || dying)
            return;

        // initialize sliding state if attacker has knockback ability
        if (attacker.generatesKnockback && !knockBack) {
            this.knockBack = true;
            this.knockBackCounter = 0;
            this.knockBackDirection = attacker.direction;
            this.speed = attacker.knockBackPower;
            this.onPath = false;
        }

        if (health != null) {
            health.damage(amount);
            this.life = health.getLife();
            if (health.isDead()) {
                dying = true;
                knockBack = false;
            }
        }

        invincible = true;
        invincibleCounter = 0;
        hpBarOn = true;
        hpBarCounter = 0;
        damageReaction(attacker);
    }

    public int getCol() {
        return (worldX + hitBox.x) / gp.tileSize;
    }

    public int getRow() {
        return (worldY + hitBox.y) / gp.tileSize;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isInvincible() {
        return invincible;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public int getAttackPower() {
        return attackDamage;
    }

    protected void updateStatusCounters() {
        // manage mercy frames after taking damage
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void tryMove(String direction) {
        switch (direction) {
            case "up" -> worldY -= speed;
            case "down" -> worldY += speed;
            case "left" -> worldX -= speed;
            case "right" -> worldX += speed;
        }
    }

    protected abstract void setAction();

    protected void pickUpObject(int index) {
    }

    protected void damageReaction(Entity attacker) {
    }

    protected void onCollisionWithPlayer(boolean contactPlayer) {
    }

    protected void onDeathComplete() {
    }

    public void startAttack() {
        if (combat != null)
            combat.tryAttack();
    }

    public void performAbility() {
        if (combat != null)
            combat.performAbility();
    }

    public boolean isAttacking() {
        return combat != null && combat.isAttacking();
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
    }

    @Override
    protected BufferedImage getCurrentSprite() {
        // select specific attack frame based on combat state
        if (isAttacking()) {
            int tempSpriteNum = (spriteNum > 2) ? 1 : spriteNum;
            return switch (direction) {
                case "up" -> (tempSpriteNum == 1) ? attackUp1 : attackUp2;
                case "down" -> (tempSpriteNum == 1) ? attackDown1 : attackDown2;
                case "left" -> (tempSpriteNum == 1) ? attackLeft1 : attackLeft2;
                case "right" -> (tempSpriteNum == 1) ? attackRight1 : attackRight2;
                default -> attackDown1;
            };
        }
        return super.getCurrentSprite();
    }

    protected void loadAttackSprites(String basePath, int uW, int uH, int lW, int lH) {
        attackUp1 = setup(basePath + "_up_atk1", uW, uH);
        attackUp2 = setup(basePath + "_up_atk2", uW, uH);
        attackDown1 = setup(basePath + "_down_atk1", uW, uH);
        attackDown2 = setup(basePath + "_down_atk2", uW, uH);
        attackLeft1 = setup(basePath + "_left_atk1", lW, lH);
        attackLeft2 = setup(basePath + "_left_atk2", lW, lH);
        attackRight1 = setup(basePath + "_right_atk1", lW, lH);
        attackRight2 = setup(basePath + "_right_atk2", lW, lH);
    }

    protected void updateAnimation() {
        // priority branch for attack frames vs walk frames
        if (isAttacking()) {
            spriteCounter++;
            if (combat.getState() == CombatComponent.CombatState.WINDUP) {
                spriteNum = 1;
            } else if (combat.getState() == CombatComponent.CombatState.ACTIVE) {
                spriteNum = 2;
            }
            return; 
        }

        if (moving) {
            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        } else {
            spriteNum = 1;
        }
    }
}