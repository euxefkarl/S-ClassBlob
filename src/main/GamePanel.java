package main;

import entity.Entity;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
    //SCREEN SETTINGS
    final int originalTileSize = 16; //16 x 16 tile size
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth =  tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    
    //System 
    public KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    
    public CollisionChecker cChecker =  new CollisionChecker(this);
    public AssetPlacer aPlacer = new AssetPlacer(this);
    TileManager tileM = new TileManager(this);
    
    public UI ui = new UI(this);
    
    //world settings
    public final int maxWorldCol = 50;//tweak when final map is available
    public final int maxWorldRow = 50;//resize to fit final map
    public final int maxWorldWidth = tileSize * maxWorldCol;
    public final int maxWorldHeight = tileSize * maxWorldRow;

    //entity and objects
    public Player player = new Player(this,keyH);
    public Entity obj[] = new Entity[10];
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];


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
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    //call asset placer to set world objects
    public void setupObjects(){
        aPlacer.setNPC();
        aPlacer.setObject();
        aPlacer.setMonster();
        gameState = titleState;
    }
    //starts game loop
    public void startGameThread(){
        gameThread =  new Thread(this);
        gameThread.start();
    }
/* 
    @Override
    public void run() {
        //game clock uses thread sleep method
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while(gameThread != null){
        
            update();
            repaint();
        try{
            double remainingTime = nextDrawTime - System.nanoTime();
            remainingTime = remainingTime/1000000;
            if(remainingTime < 0){
                remainingTime = 0;
            }
            Thread.sleep((long) remainingTime);

            nextDrawTime += drawInterval;
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        }
    }
*/

    //override run method, this is our game clock
    @Override
    public void run(){
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while(gameThread !=  null){
            //delta clock method
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if(delta>=1){
                /*calls update and repaint 60 
                times per second based on game 
                clock calculation*/
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer>= 1000000000){
                System.out.println("FPS:"+ drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
    //call update method
    public void update(){
        if (gameState == playState){
            player.update();
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    npc[i].update();
                }
            }
            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    if(monster[i].alive == true && monster[i].dying == false){monster[i].update();}
                    if(monster[i].alive == false){monster[i] = null;}
                }
            }
        }
        if (gameState == pauseState){
            //nothing for now
        }
        
    }

    //draws the game graphics
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        //draw titlescreen
        if (gameState == titleState){
            ui.draw(g2, this);
        }
        //otherwise draw game
        else{
        //draw tiles
        tileM.drawTile(g2);
        //draw objects
        for (Entity obj1 : obj) {
            if (obj1 != null) {
                //obj1.drawObject(g2, this);
            }
        }
        //draw npc
        for (Entity npc1 : npc) {
            if (npc1 != null) {
                npc1.draw(g2);
            }
        }
        //draw npc
        for (Entity monster1 : monster) {
            if (monster1 != null) {
                monster1.draw(g2);
            }
        }
        player.draw(g2);
        ui.draw(g2, this);
        g2.dispose();
        }
        
    }
}

