package monster;

import ai.EntityAI;
import combat.CombatComponent;
import combat.HealthComponent;
import combat.PhysicalDamageCalculator;
import entity.LivingEntity;
import main.GamePanel;

public class Orc extends LivingEntity {

    public Orc(GamePanel gp) {
        super(gp);

        ai = new EntityAI(gp, this);
        speed = 2;
        defaultSpeed = speed;
        collision = true;
        health = new HealthComponent(10);
        combat = new CombatComponent(this, new PhysicalDamageCalculator());

        loadMovementSprites("/res/monster/orc", gp.tileSize, gp.tileSize);
    }

    @Override
    protected void setAction() {
        if (!onPath) {
            if (ai.shouldStartChase(gp.player, 3)) onPath = true;
        } else {
            if (ai.shouldEndChase(gp.player, 8)) onPath = false;
            else ai.moveToward(gp.player);
        }

        if (ai.shouldAttack(gp.player, 1, 20)) attacking = true;
    }
}
