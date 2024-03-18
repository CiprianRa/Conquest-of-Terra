package Entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;

public class Boxer extends Enemy{

    //AttackBox
    public Boxer(float x, float y) {
        super(x, y, 160, 160, BOXER);
        initHitbox(x,y,(int)(45),(int)(44));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox=new Rectangle2D.Float(x,y,(int)(20* Game.SCALE),(int)(20*Game.SCALE));
    }

    private void updateAttackBox() {
        if(walkDir==RIGHT)
        {
            attackBox.x=hitbox.x+hitbox.width-(int)(Game.SCALE*5);
        }
        else if(walkDir==LEFT)
        {
            attackBox.x=hitbox.x-hitbox.width+(int)(Game.SCALE*5);
        }
        attackBox.y=hitbox.y+(Game.SCALE*5);
    }

    public void drawAttackBox(Graphics g, int xLvlOffset,int yLvlOffset)
    {
        g.setColor(Color.red);
        g.drawRect((int)(attackBox.x-xLvlOffset),(int)(attackBox.y-yLvlOffset),(int)attackBox.width,(int)attackBox.height);
    }
    public void update(int[][] lvlData,Player player)
    {
        updateBehavior(lvlData,player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateBehavior(int[][] lvlData,Player player)
    {
        if(firstUpdate)
            firstUpdateCheck(lvlData);
        if(inAir)
            updateInAir(lvlData);
        else
        {
            switch(state)
            {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if(canSeePlayer(lvlData,player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACKING);
                    }
                    move(lvlData);
                    break;
                case ATTACKING:
                    if(aniIndex==0)
                        attackChecked=false;
                    if(!attackChecked && aniIndex==3)
                        checkEnemyHit(attackBox,player);
                break;

            }
        }
    }



    public int flipX()
    {
        if(walkDir==RIGHT)
            return 0;
        else return width;
    }
    public int flipW()
    {
        if(walkDir==RIGHT)
            return 1;
        else return -1;
    }
}
