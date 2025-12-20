package object;

import entity.Entity;
import entity.Item;
import entity.Projectile;
import main.GamePanel;

public class OBJ_wind extends Item {

    private Projectile tornadoAbility;

    public OBJ_wind(GamePanel gp) {
        super(gp);
        name = "Wind Gem";
        entityType = typeForm;

        defense = 1;
        agility = 10; 
        description = "[Wind Gem]\nWind element.\nHigh Attack Speed."; 
        
        loadObjectSprites("/res/objects/OBJ_wind", gp.tileSize, gp.tileSize);
        
        // cache ability instance to avoid repeated instantiation
        tornadoAbility = new OBJ_Tornado(gp);
        tornadoAbility.alive = false;
    }

    @Override
    public String getFormSpritePrefix() {
        return "wind"; 
    }

    @Override
    public Projectile getFormAbility() {
        return tornadoAbility;
    }

    @Override
    public boolean use(Entity user) {
        // trigger player state change without removing item from inventory
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