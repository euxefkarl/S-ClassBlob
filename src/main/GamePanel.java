package main;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JPanel;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 16; //16 x 16 tile size
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    //System 
    Thread gameThread;
    public KeyHandler keyH = new KeyHandler(this);
    public PathFinder pFinder = new PathFinder(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetPlacer aPlacer = new AssetPlacer(this);
    public TileManager tileM = new TileManager(this);
    public UI ui = new UI(this);

    //world settings
    public final int maxWorldCol = 50;//tweak when final map is available
    public final int maxWorldRow = 50;//resize to fit final map
    public final int maxWorldWidth = tileSize * maxWorldCol;
    public final int maxWorldHeight = tileSize * maxWorldRow;

    //entity and objects
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[30];
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[40];
    ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectileList = new ArrayList<>();

    //gamestate
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int statusScreenState = 4;

    //FPS
    int FPS = 60;

    //constructor 
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    //call asset placer to set world objects
    public void setupObjects() {
        aPlacer.setNPC();
        aPlacer.setObject();
        aPlacer.setMonster();
        gameState = titleState;
    }

    //starts game loop
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    //override run method, this is our game clock
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while (gameThread != null) {
            //delta clock method
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1) {
                /*calls update and repaint 60 
                times per second based on game 
                clock calculation*/
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    //call update method
    public void update() {
        if (gameState == playState) {
            player.update();
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive == true && monster[i].dying == false) {
                        monster[i].update();
                    }
                    if (monster[i].alive == false) {
                        monster[i].checkDrop();
                        monster[i] = null;
                    }
                }
            }
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].update();
                }
            }
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive == true) {
                        projectileList.get(i).update();

                    }
                    if (projectileList.get(i).alive == false) {
                        projectileList.remove(i);

                    }
                }
            }
        }
        if (gameState == pauseState) {
            //nothing for now
        }

    }

    //draws the game graphics
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //draw titlescreen
        if (gameState == titleState) {
            ui.draw(g2, this);
        } //otherwise draw game
        else {
            //draw tiles
            tileM.drawTile(g2);
            //add player
            entityList.add(player);
            //add other entities to entity list
            for (Entity npc1 : npc) {
                if (npc1 != null) {
                    entityList.add(npc1);
                }
            }

            for (Entity obj1 : obj) {
                if (obj1 != null) {
                    entityList.add(obj1);
                }
            }
            for (Entity monster1 : monster) {
                if (monster1 != null) {
                    entityList.add(monster1);
                }
            }
            for (Entity projectile1 : projectileList) {
                if (projectile1 != null) {
                    entityList.add(projectile1);
                }
            }
            Collections.sort(entityList, (Entity e1, Entity e2) -> {
                int result = Integer.compare(e1.worldY, e2.worldY);
                return result;
            });

            //draw entity
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            //empty entity list
            entityList.clear();
            ui.draw(g2, this);

            //debug text for dev purposes only
            if (keyH.showDebug == true) {
                g2.setFont(new Font("arial", Font.PLAIN, 20));
                int x = 10;
                int y = 400;
                int lineHeight = 20;
                g2.drawString("World X" + player.worldX, x, y);
                y += lineHeight;
                g2.drawString("World Y" + player.worldY, x, y);
                y += lineHeight;
                g2.drawString("Col" + (player.worldX + player.hitBox.x) / tileSize, x, y);
                y += lineHeight;
                g2.drawString("Row" + (player.worldY + player.hitBox.y) / tileSize, x, y);
            }

            g2.dispose();
        }

    }
}
