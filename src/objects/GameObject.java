package objects;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.ObjectConstants.*;

public class GameObject {
    protected int x,y,objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation,active=true;
    protected int aniTick,aniIndex;
    protected int xDrawOffset,yDrawOffset;

    public GameObject(int x,int y,int objType)
    {
        this.x=x;
        this.y=y;
        this.objType=objType;
    }

    protected void updateAnimationTick()
    {
        aniTick++;
        if(aniTick>=ANI_SPEED)
        {
            aniTick=0;
            aniIndex++;
            if(aniIndex>=GetSpriteAmount(objType))
            {
                aniIndex=0;
            }
        }
    }

    public void reset()
    {
        aniIndex=0;
        aniTick=0;
        active=true;

        doAnimation=true;
    }

    public int getObjType() {
        return objType;
    }

    public void setObjType(int objType) {
        this.objType = objType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle2D.Float hitbox) {
        this.hitbox = hitbox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public void setxDrawOffset(int xDrawOffset) {
        this.xDrawOffset = xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public void setyDrawOffset(int yDrawOffset) {
        this.yDrawOffset = yDrawOffset;
    }

    protected void initHitbox(int width, int height) {
        hitbox=new Rectangle2D.Float(x,y,(int)(width* Game.SCALE),(int)(height*Game.SCALE));
    }

    public void drawHitbox(Graphics g, int xLvlOffset, int yLvlOffset)
    {
        //for debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x-xLvlOffset,(int)hitbox.y-yLvlOffset,(int)hitbox.width,(int)hitbox.height);
    }

    public int getAniIndex()
    {
        return aniIndex;
    }
}
