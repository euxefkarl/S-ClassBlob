package entity;

import combat.Attacker;
import combat.Damagable;
import combat.Defender;
import main.GamePanel;

public abstract class Projectile extends Entity implements Attacker {

    protected Entity user;
    protected int life;
    protected int maxLife;

    public Projectile(GamePanel gp) {
        super(gp);
    }
    //sets projectile to be shot
    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;

        if (user instanceof combat.Attacker attacker) {
            this.attackDamage = attacker.getAttackPower();
        }
    }

    @Override
    public void update() {
        moveProjectile();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        if (collisionOn) {
            alive = false;
            return;
        }

        // entity collision
        if (user instanceof Player) {
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if (monsterIndex != 999) {
                damageTarget(gp.monster[monsterIndex]);
            }
        } else {
            boolean hitPlayer = gp.cChecker.checkEntity(this, gp.player);
            if (hitPlayer) {
                damageTarget(gp.player);
            }
        }

        life--;
        if (life <= 0)
            alive = false;

        animateSprite();
    }

    protected void damageTarget(Entity target) {
        if (target instanceof Damagable damagable && target instanceof Defender defender) {
            int damage = Math.max(0, this.attackDamage - defender.getDefense());

            damagable.takeDamage(damage, this);

            alive = false;
        }
    }

    protected void moveProjectile() {
        switch (direction) {
            case "up" -> worldY -= speed;
            case "down" -> worldY += speed;
            case "left" -> worldX -= speed;
            case "right" -> worldX += speed;
        }
    }

    @Override
    public int getAttackPower() {
        return attackDamage;
    }

    protected void animateSprite() {
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public abstract void loadProjectileSprites();
}