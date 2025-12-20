package combat;

import combat.CombatComponent.CombatState;
import entity.Entity;
import entity.Item;
import entity.LivingEntity;
import entity.Player;
import entity.Projectile;
import java.awt.Rectangle;
import main.GamePanel;


//handles all combat related manners with regards to damage 
public class CombatComponent {

    private final GamePanel gp;
    private final Entity owner;
    private final DamageCalculator calculator;

    public enum CombatState {
        READY, WINDUP, ACTIVE, COOLDOWN
    }

    private CombatState currentState = CombatState.READY;

    private int stateTimer = 0;
    private int windupDuration = 5;
    private int activeDuration = 25;
    private int cooldownDuration = 5;

    private final Rectangle attackHitbox;
    private Projectile currentAbility;

    public CombatComponent(GamePanel gp, Entity owner, DamageCalculator calculator) {
        this.gp = gp;
        this.owner = owner;
        this.calculator = calculator;
        this.attackHitbox = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
    }

    public void update() {
        if (currentState == CombatState.READY) {
            return;
        }

        stateTimer++;

        // handle state transitions based on timers
        switch (currentState) {
            case WINDUP -> {
                if (stateTimer > windupDuration) {
                    currentState = CombatState.ACTIVE;
                    stateTimer = 0;
                    performAttackLogic();
                }
            }
            case ACTIVE -> {
                if (stateTimer > activeDuration) {
                    currentState = CombatState.COOLDOWN;
                    stateTimer = 0;
                }
            }
            case COOLDOWN -> {
                if (stateTimer > cooldownDuration) {
                    currentState = CombatState.READY;
                    stateTimer = 0;
                }
            }
        }
    }

    public boolean tryAttack() {
        if (currentState == CombatState.READY) {
            currentState = CombatState.WINDUP;
            stateTimer = 0;
            return true;
        }
        return false;
    }

    private void performAttackLogic() {
        updateHitboxPosition();

        // branch logic based on alignment to find targets
        if (owner instanceof Player) {
            if (gp.monster != null) {
                for (Entity monster : gp.monster) {
                    if (monster != null && gp.cChecker.checkHit(owner, monster)) {
                        attackTarget(monster);
                    }
                }
            }
        } else {
            if (gp.cChecker.checkHit(owner, gp.player)) {
                attackTarget(gp.player);
            }
        }
    }

    public void updateHitboxPosition() {
        int width = attackHitbox.width;
        int height = attackHitbox.height;
        int worldX = owner.worldX;
        int worldY = owner.worldY;
        int tileSize = gp.tileSize;

        // project hitbox offset based on facing direction
        switch (owner.direction) {
            case "up" ->
                attackHitbox.setBounds(
                        worldX + (tileSize - width) / 2,
                        worldY - height + 10,
                        width, height);
            case "down" ->
                attackHitbox.setBounds(
                        worldX + (tileSize - width) / 2,
                        worldY + tileSize,
                        width, height);
            case "left" ->
                attackHitbox.setBounds(
                        worldX - width + 10,
                        worldY + (tileSize - height) / 2,
                        width, height);
            case "right" ->
                attackHitbox.setBounds(
                        worldX + tileSize,
                        worldY + (tileSize - height) / 2,
                        width, height);
        }

        if (owner instanceof LivingEntity living) {
            living.attackHitbox = this.attackHitbox;
        }
    }

    public void attackTarget(Entity target) {
        if (!(target instanceof Damagable damageable)) {
            return;
        }

        // use abstract calculator to decouple damage math
        int damage = calculator.calculate((Attacker) owner, (Defender) target);
        damageable.takeDamage(damage, owner);
    }

    public Rectangle getAttackHitbox() {
        return attackHitbox;
    }

    public void setForm(Item formItem) {
        if (formItem == null) {
            return;
        }
        String prefix = formItem.getFormSpritePrefix();
        Projectile ability = formItem.getFormAbility();
        if (prefix != null) {
            loadFormSprites(prefix);
        }
        if (ability != null) {
            this.currentAbility = ability;
        }
    }

    public void performAbility() {
        // instantiate projectile from ability reference
        if (currentAbility != null && !currentAbility.alive) {
            currentAbility.set(owner.worldX, owner.worldY, owner.direction, true, owner);
            gp.projectileList.add(currentAbility);
        }
    }

    private void loadFormSprites(String prefix) {
        int size = gp.tileSize;
        String path = "/res/player/" + prefix;
        owner.up1 = gp.spriteManager.load(path + "_up1", size, size);
        owner.up2 = gp.spriteManager.load(path + "_up2", size, size);
        owner.down1 = gp.spriteManager.load(path + "_down1", size, size);
        owner.down2 = gp.spriteManager.load(path + "_down2", size, size);
        owner.left1 = gp.spriteManager.load(path + "_left1", size, size);
        owner.left2 = gp.spriteManager.load(path + "_left2", size, size);
        owner.right1 = gp.spriteManager.load(path + "_right1", size, size);
        owner.right2 = gp.spriteManager.load(path + "_right2", size, size);

        owner.attackUp1 = gp.spriteManager.load(path + "_up_atk1", size, size * 2);
        owner.attackUp2 = gp.spriteManager.load(path + "_up_atk2", size, size * 2);
        owner.attackDown1 = gp.spriteManager.load(path + "_down_atk1", size, size * 2);
        owner.attackDown2 = gp.spriteManager.load(path + "_down_atk2", size, size * 2);
        owner.attackLeft1 = gp.spriteManager.load(path + "_left_atk1", size * 2, size);
        owner.attackLeft2 = gp.spriteManager.load(path + "_left_atk2", size * 2, size);
        owner.attackRight1 = gp.spriteManager.load(path + "_right_atk1", size * 2, size);
        owner.attackRight2 = gp.spriteManager.load(path + "_right_atk2", size * 2, size);
    }

    public void setAttackSpeed(int windup, int active, int cooldown) {
        this.windupDuration = windup;
        this.activeDuration = active;
        this.cooldownDuration = cooldown;
    }

    public CombatState getState() {
        return currentState;
    }

    public boolean isAttacking() {
        return currentState != CombatState.READY;
    }
}