package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_water extends Entity{
   
    public OBJ_water(GamePanel gp){
        super(gp);
        name = "Water";
        image = setup("/res/objects/OBJ_water1", gp.tileSize, gp.tileSize);

    }
}