package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Wave extends Projectile {

    public OBJ_Wave(GamePanel gp) {
        super(gp);
        name = "Wave";
        speed = 7;
        loadProjectileSprites();
    }

    @Override
    public void loadProjectileSprites() {
        int scale = gp.tileSize;
        up1 = setup("/res/projectiles/wave_up1", scale, scale);
        up2 = setup("/res/projectiles/wave_up1", scale, scale);
        down1 = setup("/res/projectiles/wave_down1", scale, scale);
        down2 = setup("/res/projectiles/wave_down1", scale, scale);
        left1 = setup("/res/projectiles/wave_left1", scale, scale);
        left2 = setup("/res/projectiles/wave_left1", scale, scale);
        right1 = setup("/res/projectiles/wave_right1", scale, scale);
        right2 = setup("/res/projectiles/wave_right1", scale, scale);
    }
}
