package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_LifeGem extends Entity {

    private final int life = 4;

    public OBJ_LifeGem(GamePanel gp) {
        super(gp);
        entityType = typeConsumable;
        name = "Life Gem";
        description = "[Life Gem]\nHeals you by a certain amount";
        getImage();
    }

    private void getImage() {
        down1 = setup("/res/objects/life_gem", gp.tileSize, gp.tileSize);
        down2 = setup("/res/objects/life_gem", gp.tileSize, gp.tileSize);
    }

    public void use(Entity entity) {
        entity.life += life;
        if (gp.player.life > gp.player.maxLife) gp.player.life = gp.player.maxLife;
    }

    @Override
    public void update() {}
}
