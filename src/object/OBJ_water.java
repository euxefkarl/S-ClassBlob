package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_water extends Entity{
   
    public OBJ_water(GamePanel gp){
        super(gp);
        name = "water";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/OBJ_water1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}