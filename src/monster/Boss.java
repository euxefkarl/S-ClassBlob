package monster;

import ai.EntityAI;
import combat.CombatComponent;
import combat.HealthComponent;
import combat.PhysicalDamageCalculator;
import entity.LivingEntity;
import main.GamePanel;

public class Boss extends LivingEntity {

    private int phase = 1;

    public Boss(GamePanel gp) {
        super(gp);
        
        ai = new EntityAI(gp, this);
        speed = 1;
        defaultSpeed = speed;
        collision = true;
        health = new HealthComponent(50);
        combat = new CombatComponent(this, new PhysicalDamageCalculator());

        loadMovementSprites("/res/monster/boss", gp.tileSize, gp.tileSize);
        loadAttackSprites("/res/monster/boss", gp.tileSize, gp.tileSize * 2, gp.tileSize * 2, gp.tileSize);
    }

    @Override
    protected void setAction() {
        if (health.getLife() < 25) {
            phase = 2;
            speed = 2;
        }

        if (phase == 1) patrol();
        else aggressiveChase();
    }

    private void patrol() {
        
    }

    private void aggressiveChase() {
        
    }
}
