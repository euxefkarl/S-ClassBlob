package monster;

import entity.Entity;
import java.util.Random;
import main.GamePanel;
import object.OBJ_LifeGem;

public class MON_Orc extends Entity {

    public MON_Orc(GamePanel gp) {

        super(gp);
        this.gp = gp;
        name = "Orc";
        attackDamage = 8;
        defense = 4;
        entityType = typeMonster;
        maxLife = 15;
        life = maxLife;
        defaultSpeed = 2;
        speed = defaultSpeed;
        exp = 10;
        motion1Duration = 40;
        motion2Duration = 85;
        hitBox.x = 4;
        hitBox.y = 4;
        hitBox.height = 40;
        hitBox.width = 44;
        defaultHitBoxX = hitBox.x;
        defaultHitBoxY = hitBox.y;
        attackHitBox.width = 44;
        attackHitBox.height = 44;

        getImage();
        getAttackImage();
    }

    public void getImage() {
        up1 = setup("/res/monster/orc_up1", gp.tileSize, gp.tileSize);
        up2 = setup("/res/monster/orc_up2", gp.tileSize, gp.tileSize);
        down1 = setup("/res/monster/orc_down1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/monster/orc_down2", gp.tileSize, gp.tileSize);
        left1 = setup("/res/monster/orc_left1", gp.tileSize, gp.tileSize);
        left2 = setup("/res/monster/orc_left2", gp.tileSize, gp.tileSize);
        right1 = setup("/res/monster/orc_right1", gp.tileSize, gp.tileSize);
        right2 = setup("/res/monster/orc_right2", gp.tileSize, gp.tileSize);
    }
    
    public void getAttackImage(){
        attackUp1 = setup("/res/monster/orc_up_atk1", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setup("/res/monster/orc_up_atk2", gp.tileSize, gp.tileSize * 2);
        attackDown1 = setup("/res/monster/orc_down_atk1", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setup("/res/monster/orc_down_atk2", gp.tileSize, gp.tileSize * 2);
        attackLeft1 = setup("/res/monster/orc_left_atk1", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setup("/res/monster/orc_left_atk2", gp.tileSize * 2, gp.tileSize);
        attackRight1 = setup("/res/monster/orc_right_atk1", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setup("/res/monster/orc_right_atk2", gp.tileSize * 2, gp.tileSize);

    }
    @Override
    public void setAction() {
        if (onPath == true) {
            checkEndChase(gp.player, 8);
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
        } else {
            checkStartChase(gp.player, 3);
            chooseDirection();
        }
        if(attacking == false){
            checkAttack(30, gp.tileSize * 4, gp.tileSize);
        }

    }

    

    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
    }

    @Override
    public void checkDrop() {
        int i = new Random().nextInt(100) + 1;

        if (i < 40) {
            dropItem(new OBJ_LifeGem(gp));
        }

    }
}
