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

    public void setObject() {
        placeObject(new OBJ_flame(gp), 45, 36);
        placeObject(new OBJ_water(gp), 21, 27);
        placeObject(new OBJ_wind(gp), 19, 10);
    }

    private void placeObject(Entity obj, int tileX, int tileY) {
        // iterate through array to find the first empty slot
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) {
                gp.obj[i] = obj;
                // convert grid units to absolute world pixels
                gp.obj[i].worldX = gp.tileSize * tileX;
                gp.obj[i].worldY = gp.tileSize * tileY;
                break;
            }
        }
    }

    public void setNPC() {
        String[] dialog = new String[4];
        dialog[0] = "You are going to defeat the evil blob, right?";
        dialog[1] = "There are elemental gems somewhere here.";
        dialog[2] = "I know the location of one. Follow me!";
        dialog[3] = "Stay close!";

        // capture reference to specific object for npc pathfinding
        Entity targetGem = gp.obj[0];

        placeNPC(new Villager(gp, 9 * gp.tileSize, 43 * gp.tileSize, targetGem, dialog), 9, 43);
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


    // takes a entity to construct and sets their position in the world based on arguments
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