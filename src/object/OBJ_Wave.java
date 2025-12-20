package object;

import entity.Entity;
import entity.LivingEntity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Wave extends Projectile {

    public OBJ_Wave(GamePanel gp) {
        super(gp);

        name = "Wave";

        speed = 6;
        maxLife = 80;
        life = maxLife;

        attackDamage = 0;
        generatesKnockback = false;

        // Visuals
        loadProjectileSprites();
    }

    @Override
    protected void damageTarget(Entity target) {

        // heal the user (player)
        if (user instanceof LivingEntity livingUser) {
            // check if user actually needs healing
            if (livingUser.life < livingUser.maxLife) {
                int healAmount = 2;

                livingUser.life += healAmount;
                if (livingUser.life > livingUser.maxLife) {
                    livingUser.life = livingUser.maxLife;
                }

                // sync with healthcomponent if present
                if (livingUser.health != null) {
                    livingUser.health.heal(healAmount);
                }

                gp.ui.showMessage("Life drained!");
            } else {
                gp.ui.showMessage("Life full!");
            }
        }

        alive = false;

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