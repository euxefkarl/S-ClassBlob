package main;

import entity.Villager;
import monster.MON_Goblin;

public class AssetPlacer {
    GamePanel gp;
    public AssetPlacer(GamePanel gp){
        this.gp = gp;

    }
    public void setObject(){
      


    }

    public void setNPC(){
        gp.npc[0] = new Villager(gp);
        // x 43 y 43
        gp.npc[0].worldX = gp.tileSize * 43;
        gp.npc[0].worldY = gp.tileSize * 43;
    }
    public void setMonster(){
        gp.monster[0] = new MON_Goblin(gp);
        // x 20 y 20
        gp.monster[0].worldX = gp.tileSize * 45;
        gp.monster[0].worldY = gp.tileSize * 43;
    }
}
