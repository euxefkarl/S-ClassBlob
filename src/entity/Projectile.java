package entity;

import main.GamePanel;

public class Projectile extends Entity {

    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;
    }

    @Override
    public void update() {
        if (user == gp.player) {
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if (monsterIndex != 999) {
                switch (name) {
                    case "Fireball" -> {
                        gp.player.damageMonster(monsterIndex, this);
                        alive = false;
                    }
                    case "Wave" -> {
                        gp.player.life += life;
                        if (gp.player.life > gp.player.maxLife) {
                            gp.player.life = gp.player.maxLife;
                        }
                        alive = false;
                    }
                    case "Tornado" -> {
                        gp.player.knockBack(gp.monster[monsterIndex], this);
                        alive = false;
                    }

                }
            }
        }

        switch (direction) {
            case "up" -> worldY -= speed;
            case "down" -> worldY += speed;
            case "left" -> worldX -= speed;
            case "right" -> worldX += speed;
        }
        life--;
        if (life <= 0) {
            alive = false;
        }
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
}
