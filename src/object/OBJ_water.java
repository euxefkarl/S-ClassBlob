package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_water extends SuperObject{
    public OBJ_water(){
        name = "water";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/OBJ_water1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}