package gamestates;

import Entities.EnemyManager;
import Entities.Player;
import Main.Game;
import levels.LevelManager;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utilz.Constants.Enviroment.*;

import static Main.Game.*;

public class Playing  extends State implements Statemethods{
    private Player player;
    public ObjectManager objectManager;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private GameOverOverlay gameOverOverlay;
    private  LevelCompletedOverlay levelCompletedOverlay;

    private int xLvlOffset;
    private int leftBorder=(int)(0.2*GAME_WIDTH);
    private int rightBorder=(int)(0.8*GAME_WIDTH);
    private int maxLvlOffsetX;

    private int yLvlOffset;
    private int roofBorder=(int)(0.1*GAME_HEIGHT);
    private int floorBorder=(int)(0.5*GAME_HEIGHT);
    private int maxLvlOffsetY;

    private BufferedImage backgroundImg,bigCloud,smallCloud,luna;
    private int[] smallCloudsPos;
    private int[] bigCloudPos;
    private Random rnd=new Random();

    private boolean gameOver;
    private boolean lvlCompleted=false;
    private PauseOverlay pauseOverlay;
    private boolean paused=false;

    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg=LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND);
        bigCloud=LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUD);
        smallCloud=LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUD);
        luna=LoadSave.GetSpriteAtlas(LoadSave.LUNA);
        bigCloudPos=new int[4];
        smallCloudsPos=new int[8];
        for(int i=0;i<smallCloudsPos.length;i++)
            smallCloudsPos[i]=(int)(70* SCALE)+rnd.nextInt((int)(150*Game.SCALE));
        for(int i=0;i<bigCloudPos.length;i++)
            bigCloudPos[i]=(int)(20*SCALE)+rnd.nextInt((int)(150* SCALE));

        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel()
    {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX=levelManager.getCurrentLevel().getLvlOffsetX();
        maxLvlOffsetY=levelManager.getCurrentLevel().getLvlOffsetY();
    }

    private void initClasses() {
        levelManager=new LevelManager(game);
        enemyManager=new EnemyManager(this);
        objectManager=new ObjectManager(this);

        player=new Player(200,1100,64*(int) Game.SCALE,64*(int)Game.SCALE,this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay=new PauseOverlay(this);
        gameOverOverlay=new GameOverOverlay(this);
        levelCompletedOverlay=new LevelCompletedOverlay(this);
    }

    @Override
    public void update() {
        if(paused)
        {
            pauseOverlay.update();
        }else if(lvlCompleted)
        {
            levelCompletedOverlay.update();
        }
        else if(!gameOver)
        {
            levelManager.update();
            objectManager.update();
            player.update();

            enemyManager.update(levelManager.getCurrentLevel().getLevelData(),player);
            checkCloseToBorder();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg,0,0, Game.GAME_WIDTH,Game.GAME_HEIGHT,null);

        drawClouds(g);

        levelManager.draw(g,xLvlOffset,yLvlOffset);
        player.render(g,xLvlOffset,yLvlOffset);
        enemyManager.draw(g,xLvlOffset,yLvlOffset);
        objectManager.draw(g,xLvlOffset,yLvlOffset);
        if(paused)
            pauseOverlay.draw(g);
        else if(gameOver)
        {
            gameOverOverlay.draw(g);
        }else if(lvlCompleted)
            levelCompletedOverlay.draw(g);
    }

    private void drawClouds(Graphics g) {
        g.drawImage(luna,500,30,(int)(32*SCALE*4),(int)(32*SCALE*4),null);
        for(int i=0;i<bigCloudPos.length;i++)
            g.drawImage(bigCloud,BIG_CLOUD_WIDTH*5*i-(int)(xLvlOffset*0.3),bigCloudPos[i],BIG_CLOUD_WIDTH,BIG_CLOUD_HEIGHT,null);
        for(int i=0;i<smallCloudsPos.length;i++)
            g.drawImage(smallCloud,SMALL_CLOUD_WIDTH*4*i-(int)(xLvlOffset*0.7),smallCloudsPos[i],SMALL_CLOUD_WIDTH,SMALL_CLOUD_HEIGHT,null);
    }

    public void resetAll()
    {
        //resets everything;
        gameOver=false;
        paused=false;
        lvlCompleted=false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObject();
    }

    public void setGameOver(boolean gameOver)
    {
        this.gameOver=gameOver;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox)
    {
        enemyManager.checkEnemyHit(attackBox);
    }
    public void checkPotionTouched(Rectangle2D.Float hitbox)
    {
        objectManager.checkObjectTouched(hitbox,player);
    }
    private void checkCloseToBorder() {
        int playerX=(int)player.GetHitbox().x;
        int diff=playerX-xLvlOffset;

        if(diff>rightBorder)
            xLvlOffset+=diff-rightBorder;
        else if(diff<leftBorder)
            xLvlOffset+=diff-leftBorder;

        if(xLvlOffset>maxLvlOffsetX)
            xLvlOffset=maxLvlOffsetX;
        else if (xLvlOffset<0) {
            xLvlOffset=0;
        }

        int playerY=(int)player.GetHitbox().y;
        int diffY=playerY-yLvlOffset;

        if(diffY>floorBorder)
            yLvlOffset+=diffY-floorBorder;
        else if(diffY<roofBorder)
            yLvlOffset+=diffY-roofBorder;

        if(yLvlOffset>maxLvlOffsetY)
            yLvlOffset=maxLvlOffsetY;
        else if(yLvlOffset<0)
        {
            yLvlOffset=0;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if(lvlCompleted)
                levelCompletedOverlay.mousePressed(e);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver)
            if(paused)
                pauseOverlay.mouseReleased(e);
            else if(lvlCompleted)
                levelCompletedOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver)
            if(paused)
                pauseOverlay.mouseMoved(e);
            else if(lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver)
            gameOverOverlay.keyPressed(e);
        else
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_W:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_S:
                    player.setDown(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setAttack(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused=!paused;
                    break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver)
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_W:
                    player.setJump(false);
                    break;
                case KeyEvent.VK_S:
                    player.setDown(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
            }
    }
    public void unpauseGame()
    {
        paused=false;
    }
    public void windowFocusLost()
    {
        player.resetDirBooleans();
    }
    public Player getPlayer()
    {
        return player;
    }

    public EnemyManager getEnemyManager()
    {
        return enemyManager;
    }

    public void setxMaxLvlOffset(int lvlOffset)
    {
        this.maxLvlOffsetX=lvlOffset;
    }

    public void setyMaxLvlOffset(int lvlOffset)
    {
        this.maxLvlOffsetY=lvlOffset;
    }

    public void setLevelCompleted(boolean levelCompleted)
    {
        this.lvlCompleted=levelCompleted;
    }

    public ObjectManager getObjectManager()
    {
        return objectManager;
    }

    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(p);
    }
}

