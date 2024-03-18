package objects;

import Entities.Player;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import DB.DB;
import static utilz.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs;
    private BufferedImage[] coinImgs;
    private BufferedImage spikeImg;
    private ArrayList<Potion> potions;
    private ArrayList<Coin> coins;
    private ArrayList<Spike> spikes;
    private DB obj = new DB();
    public int score;
    public ObjectManager(Playing playing)
    {
        this.playing=playing;
        loadImgs();

        obj.create_table();
        score=obj.getScore();
    }

    public void checkSpikesTouched(Player p)
    {
        //for(Spike s:spikes)
          //  if(s.getHitbox().intersects(p.GetHitbox()))
            //    p.kill();
        for(Spike s:spikes)
            if(s.getHitbox().intersects(p.GetHitbox().x,p.GetHitbox().y-40,32,32))
                p.kill();
    }
    public void checkObjectTouched(Rectangle2D.Float hitbox,Player player)
    {
           for(Potion p:potions)
               if(p.isActive())
               {
                   if(hitbox.intersects(p.getHitbox())) {
                       p.setActive(false);
                       applyEffectToPlayer(p);
                       if(player.getHealth()==100)
                       {
                           score+=50;
                           obj.setScore(score);
                       }
                   }
               }
           for(Coin c:coins)
               if(c.isActive())
               {
                   if(hitbox.intersects(c.getHitbox()))
                   {
                       c.setActive(false);
                       score+=10;
                       obj.setScore(score);
                   }
               }
    }

    public void applyEffectToPlayer(Potion p)
    {
        if(p.getObjType()==RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);

    }

    public void loadObjects(Level newLevel) {
        potions=new ArrayList<>(newLevel.getPotions());
        spikes=newLevel.getSpikes();
        coins=new ArrayList<>(newLevel.getCoins());
    }

    private void loadImgs() {
        BufferedImage potionSprite= LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        BufferedImage coinSprite=LoadSave.GetSpriteAtlas(LoadSave.COIN);
        potionImgs=new BufferedImage[2][7];

        for(int j=0;j<potionImgs.length;j++)
            for(int i=0;i<potionImgs[j].length;i++)
                potionImgs[j][i]=potionSprite.getSubimage(12*i,16*j,12,16);
        spikeImg=LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

        coinImgs=new BufferedImage[9];
        for(int i=0;i<coinImgs.length;i++)
            coinImgs[i]=coinSprite.getSubimage(32*i,0,32,32);
    }
    public void update()
    {
        for(Potion p:potions)
            if(p.isActive())
                p.update();
        for(Coin c:coins)
            if(c.isActive())
                c.update();
    }

    public void draw(Graphics g,int xLvlOffset,int yLvlOffset)
    {
        drawPotions(g,xLvlOffset,yLvlOffset);
        drawTraps(g,xLvlOffset,yLvlOffset);
        drawCoins(g,xLvlOffset,yLvlOffset);

    }

    private void drawTraps(Graphics g, int xLvlOffset, int yLvlOffset) {
        for(Spike s:spikes) {
            g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - yLvlOffset), SPIKE_WIDTH, SPIKE_HEIGHT, null);
            //s.drawHitbox(g,xLvlOffset,yLvlOffset);
        }

    }

    private void drawPotions(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Potion p : potions)
            if (p.isActive()) {
                int type = 0;
                if (p.getObjType() == RED_POTION)
                    type = 1;
                g.drawImage(potionImgs[type][p.getAniIndex()],
                        (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
                        (int) (p.getHitbox().y - p.getyDrawOffset() - yLvlOffset),
                        POTION_WIDTH, POTION_HEIGHT, null);
            }
    }

    private void drawCoins(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Coin c : coins)
            if (c.isActive()) {
                g.drawImage(coinImgs[c.getAniIndex()],
                        (int) (c.getHitbox().x - c.getxDrawOffset() - xLvlOffset),
                        (int) (c.getHitbox().y - c.getyDrawOffset() - yLvlOffset),
                        48, 48, null);
            }
    }

    public void resetAllObject()
    {
        for(Potion p:potions)
            p.reset();
        for(Coin c:coins)
            c.reset();
    }

}
