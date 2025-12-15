package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_LifeGem extends Entity {


    public OBJ_LifeGem(GamePanel gp) {
        super(gp);
        life = 4;
        entityType = typeConsumable;
        getImage();
        description = "[" + name + "]" + "\nHeals you by a certain amount";
    }

    public void getImage() {
        down1 = setup("/res/objects/life_gem", gp.tileSize, gp.tileSize);
        down2 = setup("/res/objects/life_gem", gp.tileSize, gp.tileSize);
    }
    @Override
    public void use(Entity entity) {
        entity.life += life;
        if (gp.player.life > gp.player.maxLife) {
            gp.player.life = gp.player.maxLife;
        }
    }

}
