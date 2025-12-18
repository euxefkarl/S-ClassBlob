package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_water extends Entity {

    public OBJ_water(GamePanel gp) {
        super(gp);
        name = "Water Gem";
        entityType = typeForm;
        defense = 3;

        loadObjectSprites("/res/objects/OBJ_water", gp.tileSize, gp.tileSize);
    }

    @Override
    public void update() {}
}
