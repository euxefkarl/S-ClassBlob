package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile {

    public OBJ_Fireball(GamePanel gp) {
        super(gp);
        name = "Fireball";
        speed = 7;
        maxLife = 80;
        life = maxLife;
        loadProjectileSprites();
    }

    @Override
    public void loadProjectileSprites() {
        int scale = gp.tileSize;
        up1 = setup("/res/projectiles/fireball_up1", scale, scale);
        up2 = setup("/res/projectiles/fireball_up2", scale, scale);
        down1 = setup("/res/projectiles/fireball_down1", scale, scale);
        down2 = setup("/res/projectiles/fireball_down2", scale, scale);
        left1 = setup("/res/projectiles/fireball_left1", scale, scale);
        left2 = setup("/res/projectiles/fireball_left2", scale, scale);
        right1 = setup("/res/projectiles/fireball_right1", scale, scale);
        right2 = setup("/res/projectiles/fireball_right2", scale, scale);
    }
}
