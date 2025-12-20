package entity;

import combat.CombatComponent;
import combat.HealthComponent;
import combat.PhysicalDamageCalculator;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import main.GamePanel;
import main.KeyHandler;

public class Player extends LivingEntity {

    private final KeyHandler keyH;
    private boolean attackPressedLastFrame = false;

    public final int screenX;
    public final int screenY;

    // stats
    public int strength, agility, intelligence, endurance;
    public int level, exp, nextLevelExp;

    // Inventory
    public List<Item> inventory = new ArrayList<>();
    public Entity currentForm = null;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        // screen center
        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        // hitboxes
        hitBox = new Rectangle(8, 16, 32, 20);
        attackHitbox = new Rectangle(0, 0, 36, 36);

        // combat components
        health = new HealthComponent(6);
        combat = new CombatComponent(gp, this, new PhysicalDamageCalculator());

        this.entityType = typePlayer;

        setDefaultValues();
    }

    public void setDefaultValues() {

        currentForm = null;

        worldX = gp.tileSize * 3;
        worldY = gp.tileSize * 47;
        speed = 4;
        nextLevelExp = 5;
        defaultSpeed = speed;
        direction = "down";
        name = "Player";
        dying = false;
        alive = true;

        maxLife = 6;
        life = maxLife;
        level = 1;
        strength = 1;
        agility = 2;
        endurance = 1;
        intelligence = 1;
        exp = 0; // Ensure XP is reset too

        // sync with combat handlers
        health = new HealthComponent(maxLife);
        // update stats
        updateAttackSpeed();
        attackDamage = getAttack();
        defense = getDefense();

        getSprites();
    }

    // load sprites
    public void getSprites() {
        loadMovementSprites("/res/player/player", gp.tileSize, gp.tileSize);
        loadAttackSprites("/res/player/player", gp.tileSize, gp.tileSize * 2, gp.tileSize * 2, gp.tileSize);
    }

    @Override
    public void update() {
        // if dying no other actions
        if (dying) {
            super.update();
            return;
        }
        // check npc interaction
        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        if (keyH.interactPressed && npcIndex != 999) {

            interactNPC(npcIndex);
            attackPressedLastFrame = keyH.interactPressed;
        }

        super.update();

        checkContactDamage();

        if (isAttacking()) {
            performAttackLogic();
        }
    }

    public void interactNPC(int i) {
        if (i != 999) {
            //interact with npc villager
            if (gp.npc[i] instanceof Villager villager) {
                villager.speak();
            }
        }
    }

    @Override
    protected void onDeathComplete() {

        gp.gameState = gp.gameOverState;

    }

    public void checkLevelUp() {
        if (exp >= nextLevelExp) {

            level++;
            exp = exp - nextLevelExp;

            //multiplier curve
            nextLevelExp = (int) (nextLevelExp * 1.2);

            //stat increase
            maxLife += 2;
            strength++;
            agility++;

            // full heal
            life = maxLife;
            // resync to component
            health = new HealthComponent(maxLife);

            //recalculate stats
            attackDamage = getAttack();
            defense = getDefense();
            updateAttackSpeed();

            gp.ui.showMessage("LEVEL UP! You are now level " + level + "!");
        }
    }

    @Override
    protected void setAction() {
        moving = false;
        //if not attacking check input
        if (!isAttacking()) {
            
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

            // attack
            if (keyH.interactPressed && !attackPressedLastFrame) {
                startAttack();
            }

            // ability
            if (keyH.abilityPressed) {
                performAbility();
                keyH.abilityPressed = false;
            }
        }
        attackPressedLastFrame = keyH.interactPressed;
    }

    //performs attack logic if hitboxes overlap
    private void performAttackLogic() {
        combat.updateHitboxPosition();
        if (spriteCounter > 5 && spriteCounter < 25) {
            for (Entity monster : gp.monster) {
                if (monster != null && monster.alive && !monster.dying) {
                    //dmg monster if attack lands
                    if (combat.getAttackHitbox().intersects(monster.hitBox)) {
                        damageMonster(monster);
                    }
                }
            }
        }
    }

    private void damageMonster(Entity monster) {
        if (!monster.invincible) {
            // formula
            int damage = Math.max(0, attackDamage - monster.getDefense());

            //apply dmg
            monster.takeDamage(damage, this);
            monster.invincible = true; // i-frames
        }
    }

    private void checkContactDamage() {
        //no dmg if iframe
        if (this.invincible)
            return;

        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        if (monsterIndex != 999) {
            Entity monster = gp.monster[monsterIndex];
            if (!monster.alive || monster.dying)
                return;

            
            int damage = Math.max(0, monster.getAttackPower() - this.getDefense());

            takeDamage(damage, monster);
        }
    }

    //cycle through all forms in order of acquisition
    public void cycleForm() {
        if (inventory.isEmpty()) {
            return;
        }
        Item nextForm = null;
        boolean foundCurrent = false;

        for (Item item : inventory) {
            if (item.entityType == typeForm) {
                if (foundCurrent) {
                    nextForm = item;
                    break;
                }
                if (item == currentForm) {
                    foundCurrent = true;
                }
            }
        }
        if (nextForm == null) {
            for (Item item : inventory) {
                if (item.entityType == typeForm) {
                    nextForm = item;
                    break;
                }
            }
        }
        if (nextForm != null && nextForm != currentForm) {
            changeCurrentForm(nextForm);
        }
    }

    //change current form
    public void changeCurrentForm(Entity selectedForm) {
        currentForm = selectedForm;
        attackDamage = getAttack();
        defense = getDefense();
        updateAttackSpeed();
        if (selectedForm instanceof Item item) {
            combat.setForm(item);
        }
    }
    //select item from inventory
    public void selectItem() {
        int itemIndex = gp.ui.getSlotItemIndex();
        if (itemIndex < inventory.size()) {
            Item selectedItem = inventory.get(itemIndex);
            boolean consumed = selectedItem.use(this);
            if (consumed) {
                inventory.remove(itemIndex);
            }
        }
    }
    //pickup object if item instance
    @Override
    protected void pickUpObject(int i) {
        if (i == 999 || gp.obj[i] == null) {
            return;
        }
        Entity object = gp.obj[i];
        if (object instanceof Item item) {
            item.onPickup(this);
            inventory.add(item);
            gp.obj[i] = null;
        }
    }

    
    public void updateAttackSpeed() {
        int formAgility = (currentForm != null) ? currentForm.agility : 0;
        int totalAgility = agility + formAgility;

        // atk speed scales with agi
        int windup = Math.max(2, 5 - (totalAgility / 5));
        int active = Math.max(10, 25 - (totalAgility / 2));
        int cooldown = Math.max(2, 5 - (totalAgility / 5));

        combat.setAttackSpeed(windup, active, cooldown);
    }

    public int getAttack() {
        int formAtk = (currentForm != null) ? currentForm.attackDamage : 0;
        return strength + formAtk;
    }

    @Override
    public int getDefense() {
        int formDef = (currentForm != null) ? currentForm.defense : 0;
        return endurance + formDef;
    }
}