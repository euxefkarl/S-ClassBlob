package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile {

    public OBJ_Fireball(GamePanel gp) {
        super(gp);
        
        // Identity
        name = "Fireball";
        
        // Stats
        speed = 8;
        maxLife = 80; // Duration (frames) before it fizzles out
        life = maxLife;
        //knockBackPower = 0; // Fireballs don't knock back much
        
        // Visuals
        loadProjectileSprites();
    }

    @Override
    public void loadProjectileSprites() {
        int scale = gp.tileSize;
        // Using 'setup' helper from Entity class
        up1 = setup("/res/projectiles/fireball_up1", scale, scale);
        up2 = setup("/res/projectiles/fireball_up2", scale, scale);
        down1 = setup("/res/projectiles/fireball_down1", scale, scale);
        down2 = setup("/res/projectiles/fireball_down2", scale, scale);
        left1 = setup("/res/projectiles/fireball_left1", scale, scale);
        left2 = setup("/res/projectiles/fireball_left2", scale, scale);
        right1 = setup("/res/projectiles/fireball_right1", scale, scale);
        right2 = setup("/res/projectiles/fireball_right2", scale, scale);
    }
    
    // NOTE: update() is NOT overridden here. 
    // It relies on the robust logic in the generic Projectile class.
}