package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_wind extends SuperObject{
    
    public OBJ_wind(GamePanel gp){
        super(gp);
        name = "Wind";
        image = setup("/res/objects/OBJ_wind1", gp.tileSize, gp.tileSize);

    }
}