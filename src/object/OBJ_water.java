package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_water extends Entity {

    public OBJ_water(GamePanel gp) {
        super(gp);
        entityType = typeForm;
        name = "Water Gem";
        down1 = setup("/res/objects/OBJ_water1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/objects/OBJ_water2", gp.tileSize, gp.tileSize);
        itemAttackDamage = 0;
        defense = 3;
        endurance = 2;
        damageAmp = 0;

    }
}
