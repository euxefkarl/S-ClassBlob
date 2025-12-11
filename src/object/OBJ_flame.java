package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_flame extends Entity{

    public OBJ_flame(GamePanel gp){
        super(gp);
        name = "Flame";
        image = setup("/res/objects/OBJ_flame1", gp.tileSize, gp.tileSize);
        itemAttackDamage = 5;


    }
}
