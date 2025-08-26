package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_wind extends SuperObject{
    public OBJ_wind(){
        name = "wind";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/OBJ_wind1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}