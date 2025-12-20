package object;

import entity.Entity;
import entity.Item;
import entity.Projectile;
import main.GamePanel;

public class OBJ_water extends Item {

    private Projectile waveAbility;

    public OBJ_water(GamePanel gp) {
        super(gp);
        name = "Water Gem";
        entityType = typeForm;
        
        defense = 3; 
        description = "[Water Gem]\nWater element.\nHigh Defense.";

        loadObjectSprites("/res/objects/OBJ_water", gp.tileSize, gp.tileSize);

        // pre-load ability to save memory during gameplay
        waveAbility = new OBJ_Wave(gp);
        waveAbility.alive = false;
    }

    @Override
    public String getFormSpritePrefix() {
        return "water"; 
    }

    @Override
    public Projectile getFormAbility() {
        return waveAbility;
    }

    @Override
    public boolean use(Entity user) {
        // update player form reference without deleting the item
        if (user instanceof entity.Player player) {
            player.changeCurrentForm(this);
            return false; 
        }
        return false;
    }
    
    @Override
    public void onPickup(Entity picker) {
        gp.ui.showMessage("You found the " + name + "!");
    }
}