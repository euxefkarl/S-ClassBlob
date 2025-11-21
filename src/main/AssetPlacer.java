package main;

public class AssetPlacer {
    GamePanel gp;
    public AssetPlacer(GamePanel gp){
        this.gp = gp;

    }
    public void setObject(){
      


    }

    public void setNPC(){
        gp.npc[0] = new entity.Villager(gp);
        // x 43 y 43
        gp.npc[0].worldX = gp.tileSize * 43;
        gp.npc[0].worldY = gp.tileSize * 43;
    }
}
