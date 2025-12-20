package monster;

import entity.LivingEntity;
import main.GamePanel;


//abstraction layer for monsters, since npc also lives in living entity
//for hostile mobs
public abstract class Monster extends LivingEntity {

    public Monster(GamePanel gp) {
        super(gp);
        this.entityType = typeMonster;
    }

   
   @Override
    protected void onCollisionWithPlayer(boolean contactPlayer) {
        if (contactPlayer) {
            if (!gp.player.isInvincible()) {
                int damage = Math.max(0, attackDamage - gp.player.getDefense());
                gp.player.takeDamage(damage, this);
            }
        }
    }
}
