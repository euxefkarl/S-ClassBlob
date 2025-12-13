package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_wind extends Entity{
    
    public OBJ_wind(GamePanel gp){
        super(gp);
        entityType = typeForm;
        name = "Wind Gem";
        down1 = setup("/res/objects/OBJ_wind1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/objects/OBJ_wind2", gp.tileSize, gp.tileSize);


    }
}