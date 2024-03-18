package Main;

import gamestates.Gamestate;

import gamestates.Playing;
import gamestates.Menu;
import utilz.LoadSave;

import java.awt.*;
import java.io.IOException;

import static gamestates.Gamestate.*;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 144;
    private final int UPS_SET=200;
    private Playing playing;
    private Menu menu;
    int frames=0;
    long lastcheck=System.currentTimeMillis();
    public final static int TILES_DEFAULT_SIZE=32;
    public final static float SCALE=1.5f;
    public final static int TILES_IN_WIDTH=26;
    public final static int TILES_IN_HEIGHT=14;
    public final static int TILES_SIZE=(int)(TILES_DEFAULT_SIZE*SCALE);
    public final static int GAME_WIDTH=TILES_SIZE*TILES_IN_WIDTH;
    public final static int GAME_HEIGHT=TILES_SIZE*TILES_IN_HEIGHT;


    //playing-variabile
    private int xLvlOffset;
    private int leftBorder=(int)(0.2*GAME_WIDTH);
    private int rightBorder=(int)(0.8*GAME_WIDTH);


    private int yLvlOffset;
    private int roofBorder=(int)(0.1*GAME_HEIGHT);
    private int floorBorder=(int)(0.5*GAME_HEIGHT);

    public Game() throws IOException {

        initClasses();

        gamePanel = GamePanel.getInstance(this);

        gameWindow = new GameWindow(gamePanel);

        gamePanel.setFocusable(true);

        gamePanel.requestFocus();

        startGameLoop();
    }

    private void initClasses() {
        menu=new Menu(this);
        playing=new Playing(this);
    }

    private void startGameLoop()
    {
        gameThread=new Thread(this);
        gameThread.start();
    }
    public void update()
    {

        switch(Gamestate.state)
        {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g)
    {
        switch(Gamestate.state)
        {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        double timePerFrame=1000000000/FPS_SET;
        double timePerUpdate=1000000000/UPS_SET;

        long previousTime=System.nanoTime();

        int frames=0;
        int updates=0;

        long lastCheck=System.currentTimeMillis();

        double deltaU=0;
        double deltaF=0;

        while (true) {
            long currentTime=System.nanoTime();

            deltaU+=(currentTime-previousTime)/timePerUpdate;
            deltaF+=(currentTime-previousTime)/timePerFrame;
            previousTime=currentTime;

            if(deltaU>=1)
            {
                update();
                updates++;
                deltaU--;
            }

            if(deltaF>=1)
            {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            /*if(now-lastFrame>=timePerFrame)
            {

                gamePanel.repaint();
                lastFrame=now;
                frames++;
            }*/
            if(System.currentTimeMillis()-lastcheck>=1000)
            {
                lastcheck=System.currentTimeMillis();
                System.out.println("FPS: "+frames +" | UPS:"+updates);
                frames=0;
                updates=0;
            }
        }
    }

    public void windowFocusLost()
    {
        if(Gamestate.state== PLAYING)
            playing.getPlayer().resetDirBooleans();
    }
    public Menu getMenu()
    {
        return menu;
    }

    public Playing getPlaying()
    {
        return playing;
    }
}
