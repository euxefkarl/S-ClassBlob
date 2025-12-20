package object;

import entity.Entity;
import entity.Item; 
import main.GamePanel;

public class OBJ_LifeGem extends Item { 

    private final int lifeValue = 4;

    public OBJ_LifeGem(GamePanel gp) {
        super(gp);
        entityType = typeConsumable;
        name = "Life Gem";
        description = "[Life Gem]\nHeals your life by " + lifeValue + ".";
        getImage();
    }

    private void getImage() {
        down1 = setup("/res/objects/life_gem", gp.tileSize, gp.tileSize);
    }

    // required by item
    @Override
    public boolean use(Entity user) {
        if (user.life < user.maxLife) {
            user.life += lifeValue;
            if (user.life > user.maxLife) {
                user.life = user.maxLife;
            }
          
            return true; // consumed
        } else {
          
            return false; //not consumed
        }
    }

    @Override
    public void onPickup(Entity picker) {
        // logic for when you pick it up off the ground
        if (picker instanceof entity.Player player) {
            gp.ui.showMessage("Got a " + name + "!");
        }
    }
}