package monster;

import ai.EntityAI;
import combat.CombatComponent;
import combat.HealthComponent;
import combat.PhysicalDamageCalculator;
import entity.Entity; // Import needed
import main.GamePanel;

public class Goblin extends Monster {

    private boolean provoked = false; // state flag

    public Goblin(GamePanel gp) {
        super(gp);

        ai = new EntityAI(gp, this);
        speed = 2;
        defaultSpeed = speed;

        maxLife = 6;
        life = maxLife;
        attackDamage = 2;
        defense = 0;
        exp = 2;

        health = new HealthComponent(maxLife);
        combat = new CombatComponent(gp, this, new PhysicalDamageCalculator());

        this.entityType = typeMonster;
        loadMovementSprites("/res/monster/goblin", gp.tileSize, gp.tileSize);
    }

    @Override
    protected void setAction() {
        if (isAttacking()) return; // safety lock

        if (provoked) {
            // if hit, chase the player regardless of distance
            ai.moveToward(gp.player);
            moving = true;
        } 
        else {
            // passive wandering
            actionLockCounter++;
            if (actionLockCounter > 120) {
                ai.moveRandomly();
                moving = true;
                actionLockCounter = 0;
            }
        }
    }

    @Override
    protected void damageReaction(Entity attacker) {
        
        if (attacker.entityType == typePlayer) {
            provoked = true;
            actionLockCounter = 0; 
        }
    }

    @Override
    protected void onDeathComplete() {
        gp.player.exp += this.exp;
        gp.ui.showMessage("Killed " + this.name + "! +" + this.exp + " exp");
        gp.player.checkLevelUp();

        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] == this) {
                gp.monster[i] = null;
                break;
            }
        }
    }
}