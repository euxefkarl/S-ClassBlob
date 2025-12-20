package monster;

import ai.EntityAI;
import combat.CombatComponent;
import combat.HealthComponent;
import combat.PhysicalDamageCalculator;
import main.GamePanel;

public class Orc extends Monster {

    public Orc(GamePanel gp) {
        super(gp);

        ai = new EntityAI(gp, this);
        speed = 1;
        defaultSpeed = speed;

        // stats
        maxLife = 10;
        life = maxLife;
        attackDamage = 4;
        defense = 1;
        exp = 5;

        // components
        health = new HealthComponent(maxLife);
        combat = new CombatComponent(gp, this, new PhysicalDamageCalculator());

       //long telegraphed windup
        combat.setAttackSpeed(30, 10, 60);

        this.entityType = typeMonster;

        loadMovementSprites("/res/monster/orc", gp.tileSize, gp.tileSize);
        loadAttackSprites("/res/monster/orc", gp.tileSize, gp.tileSize * 2, gp.tileSize * 2, gp.tileSize);
    }

    @Override
    public void setAction() {

        if (isAttacking()) {
            return;
        }

        if (onPath) {
            // check attack range

            if (ai.shouldAttack(gp.player, gp.tileSize, 60)) {
                startAttack();
            } else {
                actionLockCounter = 0;
                ai.moveToward(gp.player);
                moving = true;
            }

            if (ai.shouldEndChase(gp.player, 15)) {
                onPath = false;
            }
        } else {
            if (ai.shouldStartChase(gp.player, 5)) {
                onPath = true;
            } else {
                actionLockCounter++;
                if (actionLockCounter > 120) {
                    ai.moveRandomly();
                    moving = true;
                    actionLockCounter = 0;
                }
            }
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