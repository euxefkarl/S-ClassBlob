package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_flame extends Entity{

    public OBJ_flame(GamePanel gp){
        super(gp);
        entityType = typeForm;
        name = "Flame Gem";
        direction = "down";
        down1 = setup("/res/objects/OBJ_flame1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/objects/OBJ_flame2", gp.tileSize, gp.tileSize);
        itemAttackDamage = 3;
        defense = 1;
        intelligence = 2;
        damageAmp = 2;
        description = "["+name+"]"+"\nA gem of pure fire energy.";


    }
}
