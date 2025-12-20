package object;

import entity.Entity;
import entity.Item;
import entity.Projectile;
import main.GamePanel;

public class OBJ_flame extends Item {

    private Projectile fireballAbility;

    public OBJ_flame(GamePanel gp) {
        super(gp);
        name = "Flame Gem";
        entityType = typeForm;
        attackDamage = 5;
        description = "[Flame Gem]\nFire element.\nHigh Attack.";

        loadObjectSprites("/res/objects/OBJ_flame", gp.tileSize, gp.tileSize);

        // store a reusable projectile instance to avoid frequent gc
        fireballAbility = new OBJ_Fireball(gp);
        fireballAbility.alive = false;
    }

    @Override
    public String getFormSpritePrefix() {
        return "flame"; 
    }

    @Override
    public Projectile getFormAbility() {
        return fireballAbility;
    }

    @Override
    public boolean use(Entity user) {
        // swap player state to fire form via reference update
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