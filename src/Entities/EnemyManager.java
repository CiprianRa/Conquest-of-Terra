package Entities;

import DB.DB;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] boxerArr;
    private BufferedImage[][] chainerArr;
    private BufferedImage[] chainerRun,chainerIdle,chainerAttack,chainerDead,chainerHit;
    private BufferedImage[] boxerRun,boxerIdle,boxerAttack,boxerDead,boxerHit;
    private BufferedImage[][] droidArr;
    private BufferedImage[] droidRun,droidIdle,droidAttack,droidDead,droidHit;
    private ArrayList<Boxer> boxers=new ArrayList<>();
    private ArrayList<Chainer> chainers=new ArrayList<>();
    private ArrayList<Droid> droids=new ArrayList<>();
    private DB obj = new DB();
    public int score;
    public EnemyManager(Playing playing)
    {
        this.playing=playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        boxers=level.getBoxers();
        System.out.println("size of boxers:"+boxers.size());
        chainers=level.getChainers();
        System.out.println("size of chainers:"+chainers.size());
        droids=level.getDroids();
        System.out.println("size of droids:"+droids.size());
    }

    public void update(int[][] lvlData,Player player)
    {
        boolean isAnyActive=false;
        for(Boxer b:boxers)
            if(b.isActive()) {
                b.update(lvlData, player);
                isAnyActive = true;
            }
        for(Chainer c:chainers)
            if(c.isActive())
            {
                c.update(lvlData,player);
                isAnyActive=true;
            }
        for(Droid d:droids)
            if(d.isActive())
            {
                d.update(lvlData,player);
                isAnyActive=true;
            }
        if(!isAnyActive)
            playing.setLevelCompleted(true);
    }
    public void draw(Graphics g,int xLvlOffset,int yLvlOffset)
    {
        drawBoxers(g,xLvlOffset,yLvlOffset);
        drawChainers(g,xLvlOffset,yLvlOffset);
        drawDroids(g,xLvlOffset,yLvlOffset);
    }

    private void drawBoxers(Graphics g,int xLvlOffset,int yLvlOffset) {
        for(Boxer b: boxers)
            if(b.isActive())
            {
                g.drawImage(boxerArr[b.getEntityState()][b.getAniIndex()],
                        (int)b.GetHitbox().x-BOXER_DRAWOFFSET_X-xLvlOffset+b.flipX(),
                        (int)b.GetHitbox().y-BOXER_DRAWOFFSET_Y-yLvlOffset,
                        160*b.flipW(),160,
                        null);
                //b.drawHitbox(g,xLvlOffset,yLvlOffset); //debug enemies
                //b.drawAttackBox(g,xLvlOffset,yLvlOffset); debug attackbox for enemies
            }
    }

    private void drawChainers(Graphics g,int xLvlOffset,int yLvlOffset) {
        for(Chainer c: chainers)
            if(c.isActive())
            {
                g.drawImage(chainerArr[c.getEntityState()][c.getAniIndex()],
                        (int)c.GetHitbox().x-CHAINER_DRAWOFFSET_X-xLvlOffset+c.flipX(),
                        (int)c.GetHitbox().y-CHAINER_DRAWOFFSET_Y-yLvlOffset,
                        128*c.flipW(),128,
                        null);
                //c.drawHitbox(g,xLvlOffset,yLvlOffset);
                // debug enemies
               // c.drawAttackBox(g,xLvlOffset,yLvlOffset);
                // debug attackbox for enemies
            }
    }

    private void drawDroids(Graphics g,int xLvlOffset,int yLvlOffset) {
        for(Droid d: droids)
            if(d.isActive())
            {
                g.drawImage(droidArr[d.getEntityState()][d.getAniIndex()],
                        (int)d.GetHitbox().x-DROID_DRAWOFFSET_X-xLvlOffset+d.flipX(),
                        (int)d.GetHitbox().y-DROID_DRAWOFFSET_Y-yLvlOffset,
                        96*d.flipW(),96,
                        null);
               // d.drawHitbox(g,xLvlOffset,yLvlOffset); //debug enemies
               // d.drawAttackBox(g,xLvlOffset,yLvlOffset); //debug attackbox for enemies
            }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox)
    {
        for(Boxer b:boxers)
            if(b.isActive())
                if(attackBox.intersects(b.GetHitbox()))
                {
                    b.hurt(10);
                    return;
                }
        for(Chainer c:chainers)
            if(c.isActive())
                if(attackBox.intersects(c.GetHitbox()))
                {
                    c.hurt(10);
                    return;
                }
        for(Droid d:droids)
            if(d.isActive())
                if(attackBox.intersects(d.GetHitbox()))
                {
                    d.hurt(10);
                    return;
                }
    }

    private void loadEnemyImgs() {
        boxerArr=new BufferedImage[5][9];
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.BOXER_SPRITE);
        boxerIdle=new BufferedImage[4];
        for(int i=0;i<4;i++)
            boxerIdle[i]=img.getSubimage((i+3)*64,2*64,BOXER_WIDTH_DEFAULT,BOXER_HEIGHT_DEFAULT);
        boxerRun=new BufferedImage[6];
        for(int i=0;i<4;i++)
            boxerRun[i]=img.getSubimage((i+6)*64,0,BOXER_WIDTH_DEFAULT,BOXER_HEIGHT_DEFAULT);
        boxerRun[4]=img.getSubimage(0,64,BOXER_WIDTH_DEFAULT,BOXER_HEIGHT_DEFAULT);
        boxerRun[5]=img.getSubimage(64,64,BOXER_WIDTH_DEFAULT,BOXER_HEIGHT_DEFAULT);
        boxerAttack=new BufferedImage[6];
        for(int i=0;i<6;i++)
            boxerAttack[i]=img.getSubimage(i*64,0,BOXER_WIDTH_DEFAULT,BOXER_HEIGHT_DEFAULT);
        boxerHit=new BufferedImage[2];
        boxerHit[0]=img.getSubimage(2*64,64,BOXER_WIDTH_DEFAULT,BOXER_HEIGHT_DEFAULT);
        boxerHit[1]=img.getSubimage(3*64,64,BOXER_WIDTH_DEFAULT,BOXER_HEIGHT_DEFAULT);
        boxerDead=new BufferedImage[9];
        for(int i=0;i<6;i++)
            boxerDead[i]=img.getSubimage((i+4)*64,64,BOXER_WIDTH_DEFAULT,BOXER_HEIGHT_DEFAULT);
        for(int i=0;i<3;i++)
            boxerDead[i+6]=img.getSubimage(i*64,2*64,BOXER_WIDTH_DEFAULT,BOXER_HEIGHT_DEFAULT);
        boxerArr[0]=boxerIdle;
        boxerArr[1]=boxerRun;
        boxerArr[2]=boxerAttack;
        boxerArr[3]=boxerHit;
        boxerArr[4]=boxerDead;

        chainerArr=new BufferedImage[5][8];
        BufferedImage img2=LoadSave.GetSpriteAtlas(LoadSave.CHAINER);
        chainerAttack=new BufferedImage[8];
        for(int i=0;i<7;i++)
            chainerAttack[i]=img2.getSubimage(i*64,0,CHAINER_WIDTH_DEFAULT,CHAINER_HEIGHT_DEFAULT);
        chainerDead=new BufferedImage[5];
        for(int i=0;i<5;i++)
            chainerDead[i]=img2.getSubimage(i*64,64,CHAINER_WIDTH_DEFAULT,CHAINER_HEIGHT_DEFAULT);
        chainerHit=new BufferedImage[2];
        for(int i=0;i<2;i++)
            chainerHit[i]=img2.getSubimage(i*64,2*64,CHAINER_WIDTH_DEFAULT,CHAINER_HEIGHT_DEFAULT);
        chainerIdle=new BufferedImage[6];
        for(int i=0;i<6;i++)
            chainerIdle[i]=img2.getSubimage(i*64,3*64,CHAINER_WIDTH_DEFAULT,CHAINER_HEIGHT_DEFAULT);
        chainerRun=new BufferedImage[8];
        for(int i=0;i<8;i++)
            chainerRun[i]=img2.getSubimage(i*64,4*64,CHAINER_WIDTH_DEFAULT,CHAINER_HEIGHT_DEFAULT);
        chainerArr[0]=chainerIdle;
        chainerArr[1]=chainerRun;
        chainerArr[2]=chainerAttack;
        chainerArr[3]=chainerHit;
        chainerArr[4]=chainerDead;

        droidArr=new BufferedImage[5][10];
        BufferedImage img3=LoadSave.GetSpriteAtlas(LoadSave.DROID);
        droidAttack=new BufferedImage[10];
        for(int i=0;i<10;i++)
            droidAttack[i]=img3.getSubimage(i*64,0,DROID_WIDTH_DEFAULT,DROID_HEIGHT_DEFAULT);
        droidDead=new BufferedImage[6];
        for(int i=0;i<6;i++)
            droidDead[i]=img3.getSubimage(i*64,64,DROID_WIDTH_DEFAULT,DROID_HEIGHT_DEFAULT);
        droidHit=new BufferedImage[2];
        for(int i=0;i<2;i++)
            droidHit[i]=img3.getSubimage(i*64,2*64,DROID_WIDTH_DEFAULT,DROID_HEIGHT_DEFAULT);
        droidIdle=new BufferedImage[6];
        for(int i=0;i<6;i++)
            droidIdle[i]=img3.getSubimage(i*64,3*64,DROID_WIDTH_DEFAULT,DROID_HEIGHT_DEFAULT);
        droidRun=new BufferedImage[6];
        for(int i=0;i<6;i++)
            droidRun[i]=img3.getSubimage(i*64,4*64,DROID_WIDTH_DEFAULT,DROID_HEIGHT_DEFAULT);
        droidArr[0]=droidIdle;
        droidArr[1]=droidRun;
        droidArr[2]=droidAttack;
        droidArr[3]=droidHit;
        droidArr[4]=droidDead;
    }

    public void resetAllEnemies()
    {
        for(Boxer b:boxers)
            b.resetEnemy();
        for(Chainer c:chainers)
            c.resetEnemy();
        for(Droid d:droids)
            d.resetEnemy();
    }
}
