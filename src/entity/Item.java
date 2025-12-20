package entity;

import java.awt.Graphics2D;
import main.GamePanel;

public abstract class Item extends Entity {

    // name is inherited from entity base
    public String description = "";

    public Item(GamePanel gp) {
        super(gp);
    }
    @Override
    public void update() {
    }
    @Override
    public void draw(Graphics2D g2) {
        // determine rendering position relative to the camera
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // cull drawing if item is outside the visible screen area
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            
            g2.drawImage(down1, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
    @Override
    // handle logic for when an entity touches the item
    public abstract void onPickup(Entity picker); 
    @Override
    // execute item effect and return if it should be deleted
    public abstract boolean use(Entity user); 

    public String getFormSpritePrefix() {
        return null;
    }

    public Projectile getFormAbility() {
        return null;
    }
}