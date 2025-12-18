package entity;

import ai.EntityAI;
import combat.Attacker;
import combat.CombatComponent;
import combat.Damagable;
import combat.Defender;
import combat.HealthComponent;
import java.awt.Rectangle;
import main.GamePanel;

public abstract class LivingEntity extends Entity implements Damagable, Attacker, Defender {

    // --- State ---
    protected boolean attacking;
    protected boolean moving;
    protected boolean onPath;
    protected EntityAI ai;
    public Rectangle attackHitbox = new Rectangle(0, 0, 0, 0);
    protected HealthComponent health;
    protected CombatComponent combat;

    protected boolean invincible;
    protected int invincibleCounter;
    protected boolean knockBack;

    protected int defaultSpeed;
    protected String knockBackDirection;

    public LivingEntity(GamePanel gp) {
        super(gp);
    }

    // === Grid helpers ===
    public int getCol() {
        return (worldX + hitBox.x) / gp.tileSize;
    }

    public int getRow() {
        return (worldY + hitBox.y) / gp.tileSize;
    }

    public void setDirection(String dir) {
        direction = dir;
    }

    public boolean isAttacking() {
        return attacking;
    }

    // === Core Loop ===
    @Override
    public void update() {
        setAction();          // AI / input decides direction
        collisionOn = false;

        if (direction != null) {
            gp.cChecker.checkTile(this);
            gp.cChecker.checkEntity(this, gp.npc, true);
            gp.cChecker.checkEntity(this, gp.monster, true);

            if (!collisionOn) {
                tryMove(direction);
            }
        }

        updateInvincibility();
        updateAnimation();
    }

    
    protected abstract void setAction();

    
    private void checkCollisions() {
        collisionOn = false;                      // reset collision
        gp.cChecker.checkTile(this);              // tile collision
        gp.cChecker.checkEntity(this, gp.npc, true);      // NPC collision
        gp.cChecker.checkEntity(this, gp.monster, true);  // Monster collision
        if (!(this instanceof Player)) {
            gp.cChecker.checkPlayer(this);
            if (!collisionOn) {
                tryMove(direction);
            }
        }
    }

    public void tryMove(String direction) {
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

    
    @Override
    public void takeDamage(int amount, Entity attacker) {
        if (invincible || !alive) {
            return;
        }

        health.damage(amount);
        invincible = true;
        invincibleCounter = 0;

        damageReaction(attacker);

        if (health.isDead()) {
            alive = false;
            dying();
        }
    }

    @Override
    public int getAttackPower() {
        return (combat != null) ? combat.owner.attackDamage : 0;
    }

    @Override
    public int getDefense() {
        return (health != null) ? 0 : 0; //placeholder
    }

    protected void loadAttackSprites(String basePath, int upDownW, int upDownH, int leftRightW, int leftRightH) {
        attackUp1 = setup(basePath + "_up_atk1", upDownW, upDownH);
        attackUp2 = setup(basePath + "_up_atk2", upDownW, upDownH);
        attackDown1 = setup(basePath + "_down_atk1", upDownW, upDownH);
        attackDown2 = setup(basePath + "_down_atk2", upDownW, upDownH);
        attackLeft1 = setup(basePath + "_left_atk1", leftRightW, leftRightH);
        attackLeft2 = setup(basePath + "_left_atk2", leftRightW, leftRightH);
        attackRight1 = setup(basePath + "_right_atk1", leftRightW, leftRightH);
        attackRight2 = setup(basePath + "_right_atk2", leftRightW, leftRightH);
    }

    protected void dying() {
    }

    protected void updateInvincibility() {
    }

    protected void damageReaction(Entity attacker) {
    }

    protected void updateAnimation() {
    }
}
