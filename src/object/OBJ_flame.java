package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_flame extends Entity {

    public OBJ_flame(GamePanel gp) {
        super(gp);
        name = "Flame Gem";
        entityType = typeForm;
        direction = "down";

        loadObjectSprites("/res/objects/OBJ_flame", gp.tileSize, gp.tileSize);
    }

    @Override
    public void update() {}
}
