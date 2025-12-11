package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
   
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    
    

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        hitBox = new Rectangle();
        hitBox.x = 8;
        hitBox.y = 16;
        hitBox.width = 32;
        hitBox.height = 20;
        attackHitBox.width = 36;
        attackHitBox.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();

    }

    public void setDefaultValues(){
        // desired start tile (tile coordinates)
        int startTileX = 2;
        int startTileY = 47;

        // convert to world pixels
        worldX = gp.tileSize * startTileX;
        worldY = gp.tileSize * startTileY;
        speed = 4;
        direction = "down";

        //player status
        maxLife = 6;
        life = maxLife;
        level = 1;
        strength = 1;
        agility = 1;
        intelligence = 1;
        endurance = 1;
        damageAmp = 1;
        exp = 0;
        nextLevelExp = 5;
        currentForm = null;
        attackDamage = getAttack();
        defense = getDefense();

    }

    public int getAttack(){
        if(currentForm == null){
            return 1;
        }
        return (currentForm.attackDamage + strength + agility + intelligence) * damageAmp;
    }

    public int getDefense(){
        if(currentForm == null){
            return 1;
        }
        return currentForm.defense * endurance;
    }

    //load player sprites
    public void getPlayerImage(){
            up1 = setup("/res/player/player_up1", gp.tileSize, gp.tileSize);
            up2 = setup("/res/player/player_up2", gp.tileSize, gp.tileSize);
            down1 = setup("/res/player/player_down1", gp.tileSize, gp.tileSize);
            down2 = setup("/res/player/player_down2", gp.tileSize, gp.tileSize);
            left1 = setup("/res/player/player_left1", gp.tileSize, gp.tileSize);
            left2 = setup("/res/player/player_left2", gp.tileSize, gp.tileSize);
            right1 = setup("/res/player/player_right1", gp.tileSize, gp.tileSize);
            right2 = setup("/res/player/player_right2", gp.tileSize, gp.tileSize);
    }
    
    public void getPlayerAttackImage(){
         attackUp1 = setup("/res/player/player_up_atk1", gp.tileSize, gp.tileSize*2);
         attackUp2 = setup("/res/player/player_up_atk2", gp.tileSize, gp.tileSize*2);
         attackDown1 = setup("/res/player/player_down_atk1", gp.tileSize, gp.tileSize*2);
         attackDown2 = setup("/res/player/player_down_atk2", gp.tileSize, gp.tileSize*2);
         attackLeft1 = setup("/res/player/player_left_atk1", gp.tileSize*2, gp.tileSize);
         attackLeft2 = setup("/res/player/player_left_atk2", gp.tileSize*2, gp.tileSize);
         attackRight1 = setup("/res/player/player_right_atk1", gp.tileSize*2, gp.tileSize);
         attackRight2 = setup("/res/player/player_right_atk2", gp.tileSize*2, gp.tileSize);
    }
    

    

    //checks key input to change player sprite 
    @Override
    public void update(){
        if(attacking == true){
            attack();
        }
        else if(keyH.upPressed == true || keyH.downPressed == true 
            || keyH.leftPressed == true || keyH.rightPressed == true || keyH.interactPressed == true){

            if(keyH.upPressed){
                direction = "up";
            }
            else if(keyH.downPressed){
                direction = "down";
            }
            else if(keyH.leftPressed){
                direction = "left";
            }
            else if(keyH.rightPressed){
                direction = "right";
            }
            
        //collison check
        collisionOn = false;
        gp.cChecker.checkTile(this);
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex);
        
        //check npc collision
        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        interactNPC(npcIndex);
        
        //check monster collision
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        contactMonster(monsterIndex);
        
        if(collisionOn == false && keyH.interactPressed == false){
            switch(direction){
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;

            }
        }
        gp.keyH.interactPressed = false;
      }
      //changes sprite for walking animation
      if(!attacking){
        if(spriteCounter > 12){
        if(spriteNum == 1){
            spriteNum = 2;
        }
        else if(spriteNum == 2){
            spriteNum = 1;
        }
            spriteCounter = 0;
        }
      }
      spriteCounter++;
      
        //invincibility timer
        if (invincible == true) {
            invinceCounter++;
            if (invinceCounter > 60) {
                invincible = false;
                invinceCounter = 0;
            }
        }
    }
    public void attack(){
        spriteCounter++;
        if(spriteCounter <= 5){
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25){
            spriteNum = 2;
            //save current hitbox and position
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int currentHitBoxWidth = hitBox.width;
            int currentHitBoxHeight = hitBox.height;

            switch(direction){
                case"up":worldY -= hitBox.height;break;
                case"down":worldY += hitBox.height;break;
                case"left":worldX -= hitBox.width;break;
                case"right":worldX += hitBox.width;break;
            }
            //modify hitbox to be attacking hitbox
            hitBox.width = attackHitBox.width;
            hitBox.height = attackHitBox.height;
            //check collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster); 
            damageMonster(monsterIndex);
            worldX = currentWorldX;
            worldY = currentWorldY;
            hitBox.width = currentHitBoxWidth;
            hitBox.height = currentHitBoxHeight;


        }
        if(spriteCounter > 25){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
            System.out.println("reached");   
        }
        
        
    }
    public void damageMonster(int i){
        if(i != 999){
            if(gp.monster[i].invincible == false){
                gp.monster[i].life -= 1;
                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();
                if (gp.monster[i].life <=0){
                    gp.monster[i].dying = true;
                }
            }
        }
    }


    //checks collision between pick upable objects
    public void pickUpObject(int i){
        if(i != 999){
           
        }
    }
    public void interactNPC(int i){
        if(keyH.interactPressed == true){
            if(i != 999){
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
            else{
                attacking = true;
            }
        }
    }
    public void contactMonster(int i){
        if(i != 999){
            //damage player
            if (invincible == false) {
                life -= 1;
                invincible = true;
                invinceCounter = 0;
            }
    ;
        } 
    }
    //draws player current sprite
    @Override
    public void draw(Graphics2D g2){

        //draw player image based on direction
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        switch(direction){
            case "up" -> {
                if(attacking == false){
                    if(spriteNum == 1){image = up1;}
                    if(spriteNum == 2){image = up2;}
                }
                if(attacking == true){
                    tempScreenY = screenY - gp.tileSize;
                    if(spriteNum == 1){image = attackUp1;}
                    if(spriteNum == 2){image = attackUp2;}
                }
                break;
            }
            case "down" -> {
                if(attacking == false){
                    if(spriteNum == 1){image = down1;}
                    if(spriteNum == 2){image = down2;}
                }
                if(attacking == true){
                    if(spriteNum == 1){image = attackDown1;}
                    if(spriteNum == 2){image = attackDown2;}
                }
                break;
            }
            case "left" -> { 
                if(attacking == false){
                    if(spriteNum == 1){image = left1;}
                    if(spriteNum == 2){image = left2;}
                }
                if(attacking == true){
                    tempScreenX = screenX - gp.tileSize;
                    if(spriteNum == 1){image = attackLeft1;}
                    if(spriteNum == 2){image = attackLeft2;}
                }
                break;
            }
            case "right" -> {
                if(attacking == false){
                    if(spriteNum == 1){image = right1;}
                    if(spriteNum == 2){image = right2;}
                }
                if(attacking == true){
                    if(spriteNum == 1){image = attackRight1;}
                    if(spriteNum == 2){image = attackRight2;}
                }
                break;
            }
        }
        // make player transparent when invincible
if (invincible == true){
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
}

// compute draw position/size using tempScreenX/tempScreenY
int drawX = tempScreenX;
int drawY = tempScreenY;
int drawWidth = gp.tileSize;
int drawHeight = gp.tileSize;

if (attacking) {
    switch (direction) {
        case "up":
            drawY = screenY - gp.tileSize;        // one tile above
            drawWidth = gp.tileSize;
            drawHeight = gp.tileSize * 2;
            break;
        case "down":
            drawY = screenY;                      // draws below player
            drawWidth = gp.tileSize;
            drawHeight = gp.tileSize * 2;
            break;
        case "left":
            drawX = screenX - gp.tileSize;        // one tile to the left
            drawWidth = gp.tileSize * 2;
            drawHeight = gp.tileSize;
            break;
        case "right":
            drawX = screenX;                      // draws to the right of player
            drawWidth = gp.tileSize * 2;
            drawHeight = gp.tileSize;
            break;
    }
}

// draw using computed values so larger attack sprites render at correct size
g2.drawImage(image, drawX, drawY, drawWidth, drawHeight, null);
g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

}
