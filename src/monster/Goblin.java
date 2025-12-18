package monster;

import ai.EntityAI;
import combat.CombatComponent;
import combat.HealthComponent;
import combat.PhysicalDamageCalculator;
import entity.LivingEntity;
import java.util.Random;
import main.GamePanel;

public class Goblin extends LivingEntity {

    private final Random random = new Random();
    private int actionLockCounter = 0;

    public Goblin(GamePanel gp) {
        super(gp);

        ai = new EntityAI(gp, this);
        speed = 2;
        defaultSpeed = speed;
        collision = true;
        health = new HealthComponent(6);
        combat = new CombatComponent(this, new PhysicalDamageCalculator());

        loadMovementSprites("/res/monster/goblin", gp.tileSize, gp.tileSize);
    }

    @Override
    protected void setAction() {
        actionLockCounter++;
        if (actionLockCounter > 120) {
            int i = random.nextInt(4);
            direction = switch (i) {
                case 0 -> "up";
                case 1 -> "down";
                case 2 -> "left";
                default -> "right";
            };
            tryMove(direction);
            actionLockCounter = 0;
        }
    }
}
