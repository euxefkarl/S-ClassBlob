package monster;

import ai.EntityAI;
import combat.CombatComponent;
import combat.HealthComponent;
import combat.PhysicalDamageCalculator;
import java.awt.Rectangle;
import main.GamePanel;

public class Boss extends Monster { // Extends Monster (Inherits collision damage)

    private int phase = 1;

    public Boss(GamePanel gp) {
        super(gp);

        ai = new EntityAI(gp, this);
    //stats
        maxLife = 50;
        life = maxLife;
        attackDamage = 8; 
        defense = 2; 
        exp = 100; 

        speed = 1;
        defaultSpeed = speed;

        //combat components
        health = new HealthComponent(maxLife);
        combat = new CombatComponent(gp, this, new PhysicalDamageCalculator());

        //boss 2x player size
        hitBox = new Rectangle(8, 16, (gp.tileSize - 16) * 2,(gp.tileSize - 16) * 2);
        attackHitbox = new Rectangle(0, 0, (gp.tileSize - 16) * 2,(gp.tileSize - 16) * 2);

        //load sprites
        loadMovementSprites("/res/monster/boss", gp.tileSize * 2, gp.tileSize * 2);
        loadAttackSprites("/res/monster/boss", gp.tileSize * 2, gp.tileSize * 4, gp.tileSize * 4, gp.tileSize * 2);
    }

    @Override
    protected void setAction() {
        // PHASE TRANSITION LOGIC
        // Using 'life' variable which is synced with HealthComponent
        if (phase == 1 && life < (maxLife / 2)) {
            phase = 2;
            speed = 2; // Enrage: Speed up
            attackDamage = 12; // Enrage: Hit harder
            System.out.println("Boss Enraged! Entering Phase 2");
        }

        if (phase == 1) {
            patrol();
        } else {
            aggressiveChase();
        }
    }

    private void patrol() {
        // Phase 1: Lazy movement, only chases if very close
        if (ai.shouldStartChase(gp.player, 3)) {
            ai.moveToward(gp.player);
            moving = true;
        } else {
            actionLockCounter++;
            if (actionLockCounter > 120) {
                ai.moveRandomly();
                moving = true;
                actionLockCounter = 0;
            }
        }
    }

    private void aggressiveChase() {
        // Phase 2: Relentless chasing
        actionLockCounter = 0;
        moving = true;

        ai.moveToward(gp.player);

        // Boss specific: Try to attack if close enough
        // (Range 48px, 60% chance per frame if cooldown allows)
        if (ai.shouldAttack(gp.player, gp.tileSize, 60)) {
            startAttack();
        }
    }

    @Override
    protected void onDeathComplete() {
        // 1. Give Rewards
        gp.player.exp += this.exp;
        gp.ui.showMessage("Killed " + this.name + "! +" + this.exp + " exp");
        gp.player.checkLevelUp(); // Ensure Player has this method or remove line
        gp.gameState = gp.gameWinState;
        // 2. Remove from Game Array
        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] == this) {
                gp.monster[i] = null; // POOF! It's gone from the map.
                break;
            }
        }
    }
}
