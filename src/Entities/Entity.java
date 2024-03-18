package Entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected int aniTick,aniIndex;
    protected float x,y;
    protected int state;
    protected float airSpeed;
    protected int maxHealth;
    protected int currentHealth=maxHealth;
    protected Rectangle2D.Float attackBox;
    protected boolean inAir=false;

    protected int width,height;
    protected Rectangle2D.Float hitbox;
    public Entity(float x,float y,int width,int height)
    {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }
    protected void drawHitbox(Graphics g,int xLvlOffset,int yLvlOffset)
    {
        //for debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x-xLvlOffset,(int)hitbox.y-yLvlOffset,(int)hitbox.width,(int)hitbox.height);
    }
    protected void initHitbox(float x,float y,float width,float height) {
        hitbox=new Rectangle2D.Float(x,y,width,height);
    }
//    protected void updateHitbox()
//    {
//        hitbox.x=(int) x;
//        hitbox.y=(int) y;
//    }



    public int getEntityState()
    {
        return state;
    }
    public Rectangle2D.Float GetHitbox()
    {
        return hitbox;
    }
}
