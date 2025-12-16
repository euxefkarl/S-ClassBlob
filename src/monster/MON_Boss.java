package monster;

import entity.Entity;
import main.GamePanel;

public class MON_Boss extends Entity {

    public MON_Boss(GamePanel gp) {

        super(gp);
        this.gp = gp;
        name = "Evil Blob";
        attackDamage = 10;
        defense = 5;
        entityType = typeMonster;
        maxLife = 50;
        life = maxLife;
        defaultSpeed = 1;
        speed = defaultSpeed;
        exp = 50;
        motion1Duration = 30;
        motion2Duration = 55;

        int bossSize = gp.tileSize * 2; // boss sprite is 2x tile size

        // Hitbox: centered horizontally, slightly inset vertically so feet land on tiles
        hitBox.width = bossSize - 32;                // 96 - 32 = 64 (example)
        hitBox.height = bossSize - 24;               // 96 - 24 = 72 (example)
        hitBox.x = (bossSize - hitBox.width) / 2;    // center horizontally -> 16
        hitBox.y = bossSize - hitBox.height - 8;     // align near bottom with small margin -> 16

        defaultHitBoxX = hitBox.x;
        defaultHitBoxY = hitBox.y;

        // Attack hitbox (area the boss can hit) — two tiles in front of the boss
        attackHitBox.width = gp.tileSize * 2; // lateral span (matches boss width)
        attackHitBox.height = gp.tileSize * 2; // reach: two tiles forward

        getImage();
        getAttackImage();
    }

    public void getImage() {
        int i = 2;
        up1 = setup("/res/monster/boss_up1", gp.tileSize * i, gp.tileSize * i);
        up2 = setup("/res/monster/boss_up2", gp.tileSize * i, gp.tileSize * i);
        down1 = setup("/res/monster/boss_down1", gp.tileSize * i, gp.tileSize * i);
        down2 = setup("/res/monster/boss_down2", gp.tileSize * i, gp.tileSize * i);
        left1 = setup("/res/monster/boss_left1", gp.tileSize * i, gp.tileSize * i);
        left2 = setup("/res/monster/boss_left2", gp.tileSize * i, gp.tileSize * i);
        right1 = setup("/res/monster/boss_right1", gp.tileSize * i, gp.tileSize * i);
        right2 = setup("/res/monster/boss_right2", gp.tileSize * i, gp.tileSize * i);
    }

    public void getAttackImage() {
        int i = 2;
        attackUp1 = setup("/res/monster/boss_up_atk1", gp.tileSize * i, gp.tileSize * i * 2);
        attackUp2 = setup("/res/monster/boss_up_atk_2", gp.tileSize * i, gp.tileSize * i * 2);
        attackDown1 = setup("/res/monster/boss_down_atk1", gp.tileSize * i, gp.tileSize * i * 2);
        attackDown2 = setup("/res/monster/boss_down_atk2", gp.tileSize * i, gp.tileSize * i * 2);
        attackLeft1 = setup("/res/monster/boss_left_atk1", gp.tileSize * i * 2, gp.tileSize * i);
        attackLeft2 = setup("/res/monster/boss_left_atk2", gp.tileSize * i * 2, gp.tileSize * i);
        attackRight1 = setup("/res/monster/boss_right_atk1", gp.tileSize * i * 2, gp.tileSize * i);
        attackRight2 = setup("/res/monster/boss_right_atk2", gp.tileSize * i * 2, gp.tileSize * i);

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
        if (attacking == false) {
            // Only attempt attack if player is within two tiles ahead and within two tiles laterally
            checkAttack(30, gp.tileSize * 2, gp.tileSize * 2);
        }

    }

    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
    }

}
