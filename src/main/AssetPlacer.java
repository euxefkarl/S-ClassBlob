package main;

import entity.Villager;
import monster.MON_Boss;
import monster.MON_Goblin;
import monster.MON_Orc;
import object.OBJ_flame;
import object.OBJ_water;
import object.OBJ_wind;

public class AssetPlacer {
    GamePanel gp;
    public AssetPlacer(GamePanel gp){
        this.gp = gp;

    }
    public void setObject(){
      int i = 0;
      gp.obj[i] = new OBJ_flame(gp);
      gp.obj[i].worldX = gp.tileSize * 45;
      gp.obj[i].worldY = gp.tileSize * 36;
      i++;
      gp.obj[i] = new OBJ_water(gp);
      gp.obj[i].worldX = gp.tileSize * 21;
      gp.obj[i].worldY = gp.tileSize * 27;
      i++;
      gp.obj[i] = new OBJ_wind(gp);
      gp.obj[i].worldX = gp.tileSize * 19;
      gp.obj[i].worldY = gp.tileSize * 10;
      i++;

    }

    public void setNPC(){
        gp.npc[0] = new Villager(gp);
        // x 43 y 43
        gp.npc[0].worldX = gp.tileSize * 9;
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
        gp.monster[i] = new MON_Orc(gp);
        gp.monster[i].worldX = gp.tileSize * 3;
        gp.monster[i].worldY = gp.tileSize * 30;
        i++;
        gp.monster[i] = new MON_Orc(gp);
        gp.monster[i].worldX = gp.tileSize * 6;
        gp.monster[i].worldY = gp.tileSize * 34;
        i++;
        gp.monster[i] = new MON_Orc(gp);
        gp.monster[i].worldX = gp.tileSize * 7;
        gp.monster[i].worldY = gp.tileSize * 34;
        i++;
        gp.monster[i] = new MON_Orc(gp);
        gp.monster[i].worldX = gp.tileSize * 9;
        gp.monster[i].worldY = gp.tileSize * 34;
        i++;
        //44 , 2
        gp.monster[i] = new MON_Boss(gp);
        gp.monster[i].worldX = gp.tileSize * 44;
        gp.monster[i].worldY = gp.tileSize * 2;
        i++;
    }
}
