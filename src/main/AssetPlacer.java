package main;

import entity.Entity;
import entity.Villager;
import monster.Boss;
import monster.Goblin;
import monster.Orc;
import object.OBJ_flame;
import object.OBJ_water;
import object.OBJ_wind;

public class AssetPlacer {
    GamePanel gp;

    public AssetPlacer(GamePanel gp) {
        this.gp = gp;
    }

    //place objects
    public void setObject() {
        placeObject(new OBJ_flame(gp), 45, 36);
        placeObject(new OBJ_water(gp), 21, 27);
        placeObject(new OBJ_wind(gp), 19, 10);
    }

    private void placeObject(Entity obj, int tileX, int tileY) {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) {
                gp.obj[i] = obj;
                gp.obj[i].worldX = gp.tileSize * tileX;
                gp.obj[i].worldY = gp.tileSize * tileY;
                break;
            }
        }
    }

   //place npcs
    public void setNPC() {
        String [] dialog = {"Dialog"};
        placeNPC(new Villager(gp, 9* gp.tileSize, 43* gp.tileSize, dialog), 9, 43);
    }

    private void placeNPC(Entity npcEntity, int tileX, int tileY) {
        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] == null) {
                gp.npc[i] = npcEntity;
                gp.npc[i].worldX = gp.tileSize * tileX;
                gp.npc[i].worldY = gp.tileSize * tileY;
                break;
            }
        }
    }

    //place monsters
    public void setMonster() {
        placeMonster(new Goblin(gp), 45, 43);
        placeMonster(new Goblin(gp), 45, 40);
        placeMonster(new Goblin(gp), 45, 42);
        placeMonster(new Goblin(gp), 45, 39);
        placeMonster(new Goblin(gp), 45, 45);

        placeMonster(new Orc(gp), 3, 30);
        placeMonster(new Orc(gp), 6, 34);
        placeMonster(new Orc(gp), 7, 34);
        placeMonster(new Orc(gp), 9, 34);

        placeMonster(new Boss(gp), 44, 2);
    }

    private void placeMonster(Entity monsterEntity, int tileX, int tileY) {
        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] == null) {
                gp.monster[i] = monsterEntity;
                gp.monster[i].worldX = gp.tileSize * tileX;
                gp.monster[i].worldY = gp.tileSize * tileY;
                break;
            }
        }
    }
}
