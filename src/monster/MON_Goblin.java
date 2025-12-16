package monster;

import entity.Entity;
import java.util.Random;
import main.GamePanel;
import object.OBJ_LifeGem;

public class MON_Goblin extends Entity {

    public MON_Goblin(GamePanel gp) {

        super(gp);
        name = "Goblin";
        attackDamage = 2;
        defense = 0;
        entityType = typeMonster;
        maxLife = 5;
        life = maxLife;
        defaultSpeed = 2;
        speed = defaultSpeed;
        exp = 5;

        getImage();
    }

    public void getImage() {
        up1 = setup("/res/monster/goblin_up1", gp.tileSize, gp.tileSize);
        up2 = setup("/res/monster/goblin_up2", gp.tileSize, gp.tileSize);
        down1 = setup("/res/monster/goblin_down1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/monster/goblin_down2", gp.tileSize, gp.tileSize);
        left1 = setup("/res/monster/goblin_left1", gp.tileSize, gp.tileSize);
        left2 = setup("/res/monster/goblin_left2", gp.tileSize, gp.tileSize);
        right1 = setup("/res/monster/goblin_right1", gp.tileSize, gp.tileSize);
        right2 = setup("/res/monster/goblin_right2", gp.tileSize, gp.tileSize);
    }

  

    @Override
    public void setAction() {
        if (onPath == true) {
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
        } else {
            chooseDirection();
        }
        
    }

    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
    }

    @Override
    public void checkDrop() {
        int i = new Random().nextInt(100) + 1;

        if (i < 40) {
            dropItem(new OBJ_LifeGem(gp));
        }

    }

}
