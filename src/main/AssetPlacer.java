package main;

import object.OBJ_flame;
import object.OBJ_water;
import object.OBJ_wind;

public class AssetPlacer {
    GamePanel gp;
    public AssetPlacer(GamePanel gp){
        this.gp = gp;

    }
    public void setObject(){
        gp.obj[0] = new OBJ_flame();
        gp.obj[0].worldX = 23 * gp.tileSize;  
        gp.obj[0].worldY = 17 * gp.tileSize;

        gp.obj[1] = new OBJ_flame();
        gp.obj[1].worldX = 30 * gp.tileSize;  
        gp.obj[1].worldY = 20 * gp.tileSize;

        gp.obj[2] = new OBJ_wind();
        gp.obj[2].worldX = 23 * gp.tileSize;  
        gp.obj[2].worldY = 15 * gp.tileSize;

        gp.obj[3] = new OBJ_water();
        gp.obj[3].worldX = 27 * gp.tileSize;  
        gp.obj[3].worldY = 15 * gp.tileSize;


    }
}
