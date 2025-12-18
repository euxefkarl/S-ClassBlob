package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Tornado extends Projectile {

    public OBJ_Tornado(GamePanel gp) {
        super(gp);
        name = "Tornado";
        speed = 7;
        loadProjectileSprites();
    }

    @Override
    public void loadProjectileSprites() {
        int scale = gp.tileSize;
        down1 = setup("/res/projectiles/tornado_down1", scale, scale);
    }
}
