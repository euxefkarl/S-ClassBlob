package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_flame extends SuperObject{
    public OBJ_flame(){
        name = "flame";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/OBJ_flame1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
