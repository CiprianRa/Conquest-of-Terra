package Entities;

import DB.DB;
import Main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity{
    protected int enemyType;
    protected boolean firstUpdate=true;
    protected float walkSpeed=0.35f*Game.SCALE;
    protected int walkDir=LEFT;
    protected int tileY;
    protected float attackDistance=Game.TILES_SIZE;
    protected boolean active=true;
    protected boolean attackChecked;
    private DB obj = new DB();
    public int score;

    public Enemy(float x, float y, int width, int height,int enemyType) {
        super(x, y, width, height);
        this.enemyType=enemyType;
        initHitbox(x,y,width,height);
        maxHealth=GetMaxHealth(enemyType);
        currentHealth=maxHealth;
    }

    protected void firstUpdateCheck(int[][] lvlData)
    {
        if (!isEntityOnFloor(hitbox, lvlData))
            inAir = true;

    }

    protected void updateInAir(int[][] lvlData)
    {
        if(canMoveHere(hitbox.x,hitbox.y+airSpeed,hitbox.width,hitbox.height,lvlData))
        {
            hitbox.y+=airSpeed;
            airSpeed+=GRAVITY;
        }else
        {
            inAir=false;
            hitbox.y=GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
            tileY=(int)(hitbox.y/Game.TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData)
    {
        float xSpeed=0;

        if(walkDir==LEFT)
            xSpeed=-walkSpeed;
        else xSpeed=walkSpeed;
        if(canMoveHere(hitbox.x+xSpeed,hitbox.y,hitbox.width,hitbox.height,lvlData))
            if(isFloor(hitbox,xSpeed,lvlData))
            {
                hitbox.x+=xSpeed;
                return;
            }
        changeWalkDir();
    }
    protected void turnTowardsPlayer(Player player)
    {
        if(player.hitbox.x>hitbox.x)
            walkDir=RIGHT;
        else
            walkDir=LEFT;
    }
    protected boolean canSeePlayer(int[][] lvlData,Player player)
    {
        int playerTileY=(int)(player.GetHitbox().y/Game.TILES_SIZE);
        if(playerTileY==tileY)
            if(isPlayerInRange(player))
            {
                if(IsSightClear(lvlData,hitbox,player.hitbox,tileY))
                    return true;
            }
        return false;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue=(int)Math.abs(player.hitbox.x-hitbox.x);
        return absValue<=attackDistance*5;
    }

    protected boolean isPlayerCloseForAttack(Player player)
    {
        int absValue=(int)Math.abs(player.hitbox.x-hitbox.x);
        if(enemyType==CHAINER)
            return absValue<=attackDistance*2;
        else if (enemyType==DROID)
            return absValue<=attackDistance*1.25;
        return absValue<=attackDistance;
    }
    protected void newState(int enemyState)
    {
        this.state=enemyState;
        aniTick=0;
        aniIndex=0;
    }

    public void hurt(int amount)
    {
        currentHealth-=amount;
        if(currentHealth<=0) {
            newState(DEAD);
        }
    }

    protected void checkEnemyHit(Rectangle2D.Float attackBox,Player player) {
        if(attackBox.intersects(player.hitbox)) {
            player.changeHealth(-GetEnemyDmg(enemyType));
        }
        attackChecked=true;
    }
    protected void updateAnimationTick()
    {
        aniTick++;
        if(aniTick>=ANI_SPEED)
        {
            aniTick=0;
            aniIndex++;
            if(aniIndex>=GetSpriteAmount(enemyType,state))
            {
                aniIndex=0;
                if(state==ATTACKING)
                    state=IDLE;
                else if(state==DEAD) {
                    active = false;
                }
            }
        }
    }
    protected void changeWalkDir() {
        if(walkDir==LEFT)
            walkDir=RIGHT;
        else walkDir=LEFT;
    }

    public void resetEnemy()
    {
        hitbox.x=x;
        hitbox.y=y;
        firstUpdate=true;
        currentHealth=maxHealth;
        newState(IDLE);
        active=true;
        airSpeed=0;
    }

    public int getAniIndex()
    {
        return aniIndex;
    }
    public boolean isActive()
    {
        return active;
    }
}
