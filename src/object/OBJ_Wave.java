package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Wave extends Projectile{
    GamePanel gp;

    public OBJ_Wave(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "Wave";
        speed = 7;
        maxLife = 80;
        life = maxLife;
        getImage();
    }

    public void getImage() {
        up1 = setup("/res/projectiles/wave_up1", gp.tileSize, gp.tileSize);
        up2 = setup("/res/projectiles/wave_up1", gp.tileSize, gp.tileSize);
        down1 = setup("/res/projectiles/wave_down1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/projectiles/wave_down1", gp.tileSize, gp.tileSize);
        left1 = setup("/res/projectiles/wave_left1", gp.tileSize, gp.tileSize);
        left2 = setup("/res/projectiles/wave_left1", gp.tileSize, gp.tileSize);
        right1 = setup("/res/projectiles/wave_right1", gp.tileSize, gp.tileSize);
        right2 = setup("/res/projectiles/wave_right1", gp.tileSize, gp.tileSize);
    }
}
