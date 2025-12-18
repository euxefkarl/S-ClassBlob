package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {

    public OBJ_Heart(GamePanel gp) {
        super(gp);
        name = "Heart";

        // Load heart sprites
        image = setup("/res/objects/full_life", gp.tileSize, gp.tileSize);
        image2 = setup("/res/objects/half_life", gp.tileSize, gp.tileSize);
        image3 = setup("/res/objects/empty_life", gp.tileSize, gp.tileSize);
    }

    @Override
    public void update() {}
}
