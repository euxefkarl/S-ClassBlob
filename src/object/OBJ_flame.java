package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_flame extends Entity{

    public OBJ_flame(GamePanel gp){
        super(gp);
        name = "flame";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/OBJ_flame1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
