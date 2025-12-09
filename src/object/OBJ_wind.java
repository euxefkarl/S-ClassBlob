package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_wind extends SuperObject{
    
    public OBJ_wind(GamePanel gp){
        super(gp);
        name = "flame";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/OBJ_wind1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}