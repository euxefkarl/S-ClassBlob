package ai;

import entity.Entity;
import entity.LivingEntity;
import java.util.Random;
import main.GamePanel;

public class EntityAI {

    private final GamePanel gp;
    private final LivingEntity self; // the one moving (must be  entity to move)
    private final PathFinder pathFinder;
    private final Random random;

    public EntityAI(GamePanel gp, LivingEntity self) {
        this.gp = gp;
        this.self = self;
        this.pathFinder = new PathFinder(gp);
        this.random = new Random();
    }

    // --- decision methods (updated to use generic entity) ---

    public boolean shouldStartChase(Entity target, int tiles) {
        return distanceTo(target) < tiles * gp.tileSize;
    }

    public boolean shouldEndChase(Entity target, int tiles) {
        return distanceTo(target) > tiles * gp.tileSize;
    }

    public boolean shouldAttack(Entity target, int range, int chance) {
        if (distanceTo(target) > range)
            return false;
        if (self.isAttacking())
            return false;
        return random.nextInt(100) < chance;
    }


  
    public void moveToward(Entity target) {
        // calculate grid positions
        int startCol = (self.worldX + self.hitBox.x) / gp.tileSize;
        int startRow = (self.worldY + self.hitBox.y) / gp.tileSize;
        int goalCol = (target.worldX + target.hitBox.x) / gp.tileSize;
        int goalRow = (target.worldY + target.hitBox.y) / gp.tileSize;

        pathFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if (pathFinder.search() && !pathFinder.pathList.isEmpty()) {
            // get next step
            int nextCol = pathFinder.pathList.get(0).col;
            int nextRow = pathFinder.pathList.get(0).row;

            if (isTileOccupiedByAlly(nextCol, nextRow)) {
                // if path blocked by friend, wiggle around
                moveRandomly();
            } else {
                setDirectionToward(nextCol, nextRow);
                self.tryMove(self.direction);
            }
        } else {
            // no path found (or already there), random idle
            moveRandomly();
        }
    }

    // --- HELPER LOGIC ---

    private void setDirectionToward(int nextCol, int nextRow) {
        int selfCol = (self.worldX + self.hitBox.x) / gp.tileSize;
        int selfRow = (self.worldY + self.hitBox.y) / gp.tileSize;

        if (selfCol < nextCol)
            self.setDirection("right");
        else if (selfCol > nextCol)
            self.setDirection("left");
        else if (selfRow < nextRow)
            self.setDirection("down");
        else if (selfRow > nextRow)
            self.setDirection("up");
    }

    public void moveRandomly() {
        int i = random.nextInt(4);
        String dir = switch (i) {
            case 0 -> "up";
            case 1 -> "down";
            case 2 -> "left";
            default -> "right";
        };
        self.setDirection(dir);
        self.tryMove(dir);
    }

    private boolean isTileOccupiedByAlly(int col, int row) {
        int targetX = col * gp.tileSize;
        int targetY = row * gp.tileSize;

        if (gp.monster != null) {
            for (Entity ally : gp.monster) {
                if (ally != null && ally != self && ally.alive) {
                    // check simple distance to see if tile is occupied
                    int dx = Math.abs(ally.worldX - targetX);
                    int dy = Math.abs(ally.worldY - targetY);

                    if (dx < gp.tileSize && dy < gp.tileSize) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    
    public int distanceTo(Entity target) {
        int dx = Math.abs(self.worldX - target.worldX);
        int dy = Math.abs(self.worldY - target.worldY);
        return dx + dy; //manhattan distance formula
    }
}