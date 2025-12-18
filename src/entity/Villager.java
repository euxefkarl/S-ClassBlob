package entity;

import java.util.Random;
import main.GamePanel;

public class Villager extends Entity {

    private final String[] dialogue;
    private int dialogueIndex = 0;
    private final Random random = new Random();
    private int actionLockCounter = 0;

    public Villager(GamePanel gp, int worldX, int worldY, String[] dialogue) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;
        this.dialogue = dialogue;
        this.name = "Villager";
        this.entityType = typeNPC;
        collision = true;
        speed = 1;
        loadSprite();
    }

    private void loadSprite() {
        loadMovementSprites("/res/npc/villager", gp.tileSize, gp.tileSize);
    }

    @Override
    public void update() {
        if (!alive) return;
        setAction();
        moveEntity();
        animate();
    }

    private void setAction() {
        actionLockCounter++;
        if (actionLockCounter >= 120) {
            int i = random.nextInt(4);
            direction = switch (i) {
                case 0 -> "up";
                case 1 -> "down";
                case 2 -> "left";
                default -> "right";
            };
            actionLockCounter = 0;
        }
    }

    private void moveEntity() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkEntity(this, gp.npc,true);

        if (!collisionOn) {
            switch (direction) {
                case "up" -> worldY -= speed;
                case "down" -> worldY += speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        }
    }

    private void animate() {
        spriteCounter++;
        if (spriteCounter > 30) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public void speak() {
        if (dialogueIndex < dialogue.length) {
            gp.ui.showMessage(dialogue[dialogueIndex]);
            dialogueIndex++;
        } else dialogueIndex = 0;
    }
}
