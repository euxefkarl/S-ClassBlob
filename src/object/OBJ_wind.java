package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_wind extends Entity {

    public OBJ_wind(GamePanel gp) {
        super(gp);
        name = "Wind Gem";
        entityType = typeForm;
        defense = 1;

        loadObjectSprites("/res/objects/OBJ_wind", gp.tileSize, gp.tileSize);
    }

    @Override
    public void update() {}
}
