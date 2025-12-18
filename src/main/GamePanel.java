package main;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JPanel;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // SYSTEM
    Thread gameThread;
    public KeyHandler keyH = new KeyHandler(this);
    public PathFinder pFinder = new PathFinder(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetPlacer aPlacer = new AssetPlacer(this);
    public TileManager tileM = new TileManager(this);
    public SpriteManager spriteManager = new SpriteManager();
    public UI ui = new UI(this);

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxWorldWidth = tileSize * maxWorldCol;
    public final int maxWorldHeight = tileSize * maxWorldRow;

    // ENTITIES
    public Player player = new Player(this, keyH);
    public Entity[] obj = new Entity[30];
    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[40];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> itemList = new ArrayList<>();

    // GAMESTATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int statusScreenState = 4;
    public final int gameOverState = 5;
    public final int gameWinState = 6;

    // FPS
    int FPS = 60;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupObjects() {
        aPlacer.setNPC();
        aPlacer.setObject();
        aPlacer.setMonster();
        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void retry() {
        player.inventory.clear();
        player.setDefaultValues();
        setupObjects();
        player.loadSprites();
        
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // --- GAME UPDATE ---
    public void update() {
        // Update all entities
        player.update();
        updateEntityArray(npc);
        updateEntityArray(monster);
        updateEntityArray(obj);
        updateEntityList(projectileList);
        updateEntityList(itemList);

        
    }

    private void updateEntityArray(Entity[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) array[i].update();
        }
    }

    private void updateEntityList(ArrayList<Entity> list) {
        for (int i = 0; i < list.size(); i++) {
            Entity e = list.get(i);
            if (e != null && e.alive) {
                e.update();
            } else {
                list.remove(i);
                i--;
            }
        }
    }

    // --- DRAW ---
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == titleState) {
            ui.draw(g2, this);
        } else {
            // Draw tiles first
            tileM.drawTile(g2);
            
            // Draw all entities sorted by worldY for layering
            ArrayList<Entity> drawList = new ArrayList<>();
            drawList.add(player);
            addNonNull(drawList, npc);
            addNonNull(drawList, monster);
            addNonNull(drawList, obj);
            drawList.addAll(projectileList);
            drawList.addAll(itemList);

            Collections.sort(drawList, (e1, e2) -> Integer.compare(e1.worldY, e2.worldY));

            for (Entity e : drawList) {
                e.draw(g2);
            }

            ui.draw(g2, this);
        }

        g2.dispose();
    }

    private void addNonNull(ArrayList<Entity> list, Entity[] array) {
        for (Entity e : array) {
            if (e != null) list.add(e);
        }
    }
}
