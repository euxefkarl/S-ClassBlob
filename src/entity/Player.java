package entity;

import combat.CombatComponent;
import combat.HealthComponent;
import combat.PhysicalDamageCalculator;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Fireball;
import object.OBJ_Tornado;
import object.OBJ_Wave;

public class Player extends LivingEntity {

    private final KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int life, maxLife;
    public int strength, defense, agility, intelligence, endurance, attackDamage;
    public int level, exp, nextLevelExp;
    public List<Item> inventory = new ArrayList<>();
    public Entity currentForm = null;
    public boolean invincible;
    public boolean attacking;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
        collision = true;
        speed = 4;
        defaultSpeed = speed;
        hitBox = new Rectangle();
        hitBox.x = 8;
        hitBox.y = 16;
        hitBox.width = 32;
        hitBox.height = 20;
        attackHitbox.width = 36;
        attackHitbox.height = 36;
        health = new HealthComponent(10);
        combat = new CombatComponent(this, new PhysicalDamageCalculator());
        setDefaultValues();
        loadSprites();
    }

    public void loadSprites() {
        loadMovementSprites("/res/player/player", gp.tileSize, gp.tileSize);
        loadAttackSprites("/res/player/player", gp.tileSize, gp.tileSize * 2, gp.tileSize * 2, gp.tileSize);
    }

    @Override
    protected void setAction() {
        moving = false;

        if (keyH.upPressed) {
            direction = "up";
            moving = true;
        } else if (keyH.downPressed) {
            direction = "down";
            moving = true;
        } else if (keyH.leftPressed) {
            direction = "left";
            moving = true;
        } else if (keyH.rightPressed) {
            direction = "right";
            moving = true;
        }

        attacking = keyH.interactPressed;
    }

    
    @Override
    public void update() {
        setAction();

        collisionOn = false;

        if (moving) {
            gp.cChecker.checkTile(this);
            gp.cChecker.checkEntity(this, gp.npc, true);
            gp.cChecker.checkEntity(this, gp.monster, true);

            if (!collisionOn) {
                tryMove(direction);
            }
        }

        updateInvincibility();
        updateAnimation();
    }

    public void selectItem() {
         }

    public void cycleForm() {
         }

    public void setDefaultValues() {
        // start tile
        int startCol = 3;
        int startRow = 47;

        // convert to world pixels
        worldX = gp.tileSize * startCol;
        worldY = gp.tileSize * startRow;

        defaultSpeed = 4;
        speed = defaultSpeed;
        direction = "down";
        name = "Player";
        //player status
        maxLife = health.getLife();
        life = maxLife;
        level = 1;
        strength = 1;
        agility = 1;
        intelligence = 1;
        endurance = 1;
        exp = 0;
        nextLevelExp = 5;
        motion1Duration = 5;
        motion2Duration = 25;
        currentForm = null;
        attackDamage = getAttackPower();
        defense = getDefense();
        Entity fireball = new OBJ_Fireball(gp);
        fireball.alive = false;
        Entity wave = new OBJ_Wave(gp);
        wave.alive = false;
        Entity tornado = new OBJ_Tornado(gp);
        tornado.alive = false;
    }

}
