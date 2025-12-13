package main;

import entity.Villager;
import monster.MON_Goblin;
import object.OBJ_flame;
import object.OBJ_water;

public class AssetPlacer {
    GamePanel gp;
    public AssetPlacer(GamePanel gp){
        this.gp = gp;

    }
    public void setObject(){
      int i = 0;
      gp.obj[i] = new OBJ_flame(gp);
      gp.obj[i].worldX = gp.tileSize * 8;
      gp.obj[i].worldY = gp.tileSize * 44;
      i++;
      gp.obj[i] = new OBJ_water(gp);
      gp.obj[i].worldX = gp.tileSize * 7;
      gp.obj[i].worldY = gp.tileSize * 44;
      i++;
     

    }

    public void setNPC(){
        gp.npc[0] = new Villager(gp);
        // x 43 y 43
        gp.npc[0].worldX = gp.tileSize * 43;
        gp.npc[0].worldY = gp.tileSize * 43;
    }
    public void setMonster(){
        int i = 0;
        gp.monster[i] = new MON_Goblin(gp);
        // x 20 y 20
        gp.monster[i].worldX = gp.tileSize * 45;
        gp.monster[i].worldY = gp.tileSize * 43;
        i++;
        gp.monster[i] = new MON_Goblin(gp);
        gp.monster[i].worldX = gp.tileSize * 45;
        gp.monster[i].worldY = gp.tileSize * 40;
        i++;
        gp.monster[i] = new MON_Goblin(gp);
        gp.monster[i].worldX = gp.tileSize * 45;
        gp.monster[i].worldY = gp.tileSize * 42;
        i++;
        gp.monster[i] = new MON_Goblin(gp);
        gp.monster[i].worldX = gp.tileSize * 45;
        gp.monster[i].worldY = gp.tileSize * 39;
        i++;
        gp.monster[i] = new MON_Goblin(gp);
        gp.monster[i].worldX = gp.tileSize * 45;
        gp.monster[i].worldY = gp.tileSize * 45;
        i++;
    }
}
