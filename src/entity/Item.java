package entity;
import java.awt.Graphics2D;
import main.GamePanel;

public abstract class Item extends Entity {

    public String name;
    public String description;

    public Item(GamePanel gp) {
        super(gp);
    }

    public void update() {
       
    }

    public void draw(Graphics2D g2, int screenX, int screenY) {
        g2.drawImage(down1, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    public abstract void onPickup(Entity picker);
}
