package entity;

import ai.EntityAI;
import java.awt.Rectangle;
import java.util.Random;
import main.GamePanel;

public class Villager extends LivingEntity {

    private int actionLockCounter = 0;
    private final Random random = new Random();

    // components
    private DialogueComponent dialogueComponent;

    // state
    private boolean isGuiding = false;

    // quest target (the object we lead the player to)
    private Entity target;

  
    public Villager(GamePanel gp, int worldX, int worldY, Entity target, String[] dialogueLines) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;

        // save the quest target reference
        this.target = target;

        this.name = "Villager";
        this.entityType = typeNPC;
        this.speed = 2;
        this.direction = "down";
        this.collision = true;

        this.ai = new EntityAI(gp, this);

        // initialize dialogue
        this.dialogueComponent = new DialogueComponent(dialogueLines);

        // define behavior when dialogue finishes
        this.dialogueComponent.setOnDialogueExhausted(() -> {
            this.isGuiding = true;
            System.out.println("Villager is guiding to " + target.name);
        });

        hitBox = new Rectangle(8, 16, 32, 32);
        loadMovementSprites("/res/npc/villager", gp.tileSize, gp.tileSize);
    }

    public void speak() {
        facePlayer();
        dialogueComponent.speak(gp);
    }

    public void facePlayer() {
        switch (gp.player.direction) {
            case "up" -> direction = "down";
            case "down" -> direction = "up";
            case "left" -> direction = "right";
            case "right" -> direction = "left";
        }
    }

    @Override
    protected void setAction() {
        // double check: if we are talking, do absolutely nothing.
        if (gp.gameState == gp.dialogueState) {
            moving = false;
            return;
        }

        if (isGuiding && target != null) {
            // Check distance
            int dist = ai.distanceTo(target);
            if (dist < gp.tileSize * 1.2) { // 1.2 padding to avoid jitter
                moving = false;
                isGuiding = false; // turn off the quest logic
                return; // exit method so ai isnt called
            }
            ai.moveToward(target);
            moving = true;
        } else {
            // random wande
        }
    }

    @Override
    public void takeDamage(int damage, Entity source) {
        // peaceful npc
    }
}