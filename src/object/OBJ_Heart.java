package object;

import entity.Entity;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;  

public class OBJ_Heart extends Entity{
    
    public OBJ_Heart(GamePanel gp){
        super(gp);
        name = "heart";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/full_life.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/res/objects/half_life.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/res/objects/empty_life.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
            image2 = uTool.scaleImage(image2, gp.tileSize, gp.tileSize);
            image3 =uTool.scaleImage(image3, gp.tileSize, gp.tileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
