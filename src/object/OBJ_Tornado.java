package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Tornado extends Projectile {

    public OBJ_Tornado(GamePanel gp) {
        super(gp);

        name = "Tornado";
        speed = 10;
        maxLife = 60;
        life = maxLife;
        attackDamage = 0;

        
        this.generatesKnockback = true;
        this.knockBackPower = 8; 

        loadProjectileSprites();
    }

    @Override
    public void loadProjectileSprites() {
        int scale = gp.tileSize;
        up1 = setup("/res/projectiles/tornado_down1", scale, scale);
        up2 = setup("/res/projectiles/tornado_down1", scale, scale);
        down1 = setup("/res/projectiles/tornado_down1", scale, scale);
        down2 = setup("/res/projectiles/tornado_down1", scale, scale);
        left1 = setup("/res/projectiles/tornado_down1", scale, scale);
        left2 = setup("/res/projectiles/tornado_down1", scale, scale);
        right1 = setup("/res/projectiles/tornado_down1", scale, scale);
        right2 = setup("/res/projectiles/tornado_down1", scale, scale);
    }
}