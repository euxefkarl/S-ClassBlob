package ai;

import entity.LivingEntity;
import main.GamePanel;

public class EntityAI {

    private final GamePanel gp;
    private final LivingEntity self;
    private final PathFinder pathFinder;

    public EntityAI(GamePanel gp, LivingEntity self) {
        this.gp = gp;
        this.self = self;
        this.pathFinder = new PathFinder(gp);
    }

    
    public boolean shouldStartChase(LivingEntity target, int tiles) {
        return distanceTo(target) < tiles * gp.tileSize;
    }

    public boolean shouldEndChase(LivingEntity target, int tiles) {
        return distanceTo(target) > tiles * gp.tileSize;
    }

   
    public void moveToward(LivingEntity target) {
        int startCol = self.getCol();
        int startRow = self.getRow();
        int goalCol = target.getCol();
        int goalRow = target.getRow();

        pathFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if (pathFinder.search() && !pathFinder.pathList.isEmpty()) {
            followPath();
        }
    }

    private void followPath() {
        int nextCol = pathFinder.pathList.get(0).col;
        int nextRow = pathFinder.pathList.get(0).row;

        if (self.getCol() < nextCol) self.setDirection("right");
        else if (self.getCol() > nextCol) self.setDirection("left");
        else if (self.getRow() < nextRow) self.setDirection("down");
        else if (self.getRow() > nextRow) self.setDirection("up");

        self.tryMove(self.direction);
    }

    
    public boolean shouldAttack(LivingEntity target, int range, int chance) {
        if (distanceTo(target) > range) return false;
        if (self.isAttacking()) return false;
        return Math.random() * 100 < chance;
    }

    
    private int distanceTo(LivingEntity target) {
        int dx = Math.abs(self.worldX - target.worldX);
        int dy = Math.abs(self.worldY - target.worldY);
        return dx + dy;
    }
}
