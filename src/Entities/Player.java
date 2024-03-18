package Entities;

import Main.Game;
import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.ANI_SPEED;

import static utilz.Constants.GRAVITY;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import static utilz.HelpMethods.canMoveHere;

public class Player extends Entity{
    public BufferedImage[][] animationsHero;
    public BufferedImage[] runHero,jumpHero,dashHero,attackHero1,attackHero2,idleHero,hitHero,fallingHero,groundHero;
    private boolean moving=false,attacking=false;
    private boolean left,up,right,down,jump;
    //Jumping / Gravity
    private float jumpSpeed=-2.25f*Game.SCALE;
    private float fallSpeedAfterCollision=0.5f*Game.SCALE;
    private boolean ground;
    private float playerSpeed=2.0f;
    private int[][] lvlData;
    private float xDrawOffset = 20* Game.SCALE;
    private float yDrawOffset=62*Game.SCALE;

    //StatusBarUI
    private BufferedImage statusBarImg;

    private int statusBarWidth=(int)(192*Game.SCALE);
    private int statusBarHeight=(int)(58*Game.SCALE);
    private int statusBarX=(int)(10*Game.SCALE);
    private int statusBarY=(int)(10*Game.SCALE);
    private int healthBarWidth=(int)(150*Game.SCALE);
    private int healthBarHeight=(int)(4*Game.SCALE);
    private int healthBarXStart=(int)(34*Game.SCALE);
    private int healthBarYStart=(int)(14*Game.SCALE);

    private int healthWidth=healthBarWidth;

    private int flipX=0;
    private int flipW=1;

    private boolean attackChecked;
    private Playing playing;

    public Player(float x,float y,int width,int height,Playing playing)
    {
        super(x,y,width,height);
        this.playing=playing;
        this.state=IDLE;
        this.maxHealth=100;
        this.currentHealth=maxHealth;
        loadAnimations();
        initHitbox(x,y,13*2*Game.SCALE,10*2*Game.SCALE);
        initattackBox();
    }

    public void setSpawn(Point spawn)
    {
        this.x=spawn.x;
        this.y=spawn.y;
        hitbox.x=x;
        hitbox.y=y;
    }

    private void initattackBox() {
        attackBox=new Rectangle2D.Float(x,y,(int)(50*Game.SCALE),(int)(20*Game.SCALE));
    }

    public void update()
    {
        updateHealthBar();
        if(currentHealth<=0) {
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();
        updatePos();
        if(moving) {
            checkPotionTouched();
            checkSpikesTouched();
        }
        if(attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if(attackChecked || aniIndex!=3)
            return;
        attackChecked=true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if(right)
        {
            attackBox.x=hitbox.x+hitbox.width-(int)(Game.SCALE*20);
        }
        else if(left)
        {
            attackBox.x=hitbox.x-hitbox.width-(int)(Game.SCALE*5);
        }
        attackBox.y=hitbox.y;//+(Game.SCALE*10);
    }

    private void updateHealthBar() {
        healthWidth=(int)((currentHealth/(float)maxHealth)*healthBarWidth);
    }

    public void render(Graphics g,int lvlOffset,int ylvlOffset)
    {
        g.drawImage(animationsHero[state][aniIndex].getSubimage(0,0,64,64),
                (int)(hitbox.x-xDrawOffset)-lvlOffset+flipX,
                (int)(hitbox.y-yDrawOffset)-ylvlOffset,
                128*flipW,128,null);
        //drawHitbox(g,lvlOffset,ylvlOffset);
        // debug
        //drawAttackBox(g,lvlOffset,ylvlOffset);
        // debug hitbox
        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int lvlOffset,int ylvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x-lvlOffset,(int)attackBox.y-ylvlOffset,(int)attackBox.width,(int)attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg,statusBarX,statusBarY,statusBarWidth,statusBarHeight,null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart+statusBarX,healthBarYStart+statusBarY,healthWidth,healthBarHeight);
        g.drawString("SCORE : " +Integer.toString( playing.objectManager.score),statusBarX+50,statusBarY+60);
    }

    private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
            runHero= new BufferedImage[4];
            for(int i=0;i<4;i++)
            {
                runHero[i]=img.getSubimage(i*64,0,64,64);
            }
            jumpHero=new BufferedImage[1];
            jumpHero[0]=img.getSubimage(4*64,0,64,64);
            attackHero1=new BufferedImage[7];
            for(int i=1;i<5;i++)
            {
                attackHero1[i-1]=img.getSubimage(i*64,2*64,64,64);
            }
            for(int i=0;i<3;i++)
            {
                attackHero1[i+4]=img.getSubimage(i*64,3*64,64,64);
            }
            attackHero2=new BufferedImage[6];
            attackHero2[0]=img.getSubimage(3*64,3*64,64,64);
            attackHero2[1]=img.getSubimage(4*64,3*64,64,64);
            for(int i=0;i<4;i++)
            {
                attackHero2[i+2]=img.getSubimage(i*64,4*64,64,64);
            }
            dashHero=new BufferedImage[4];
            dashHero[0]=img.getSubimage(4*64,4*64,64,64);
            for(int i=0;i<3;i++)
            {
                dashHero[i+1]=img.getSubimage(i*64,5*64,64,64);
            }
            idleHero=new BufferedImage[1];
            idleHero[0]=img.getSubimage(4*64,5*64,64,64);
            hitHero=new BufferedImage[1];
            hitHero[0]=img.getSubimage(3*64,5*64,64,64);
            fallingHero=new BufferedImage[4];
            for(int i=0;i<3;i++)
            {
                fallingHero[i]=img.getSubimage(i*64,64,64,64);
            }
            fallingHero[3]=img.getSubimage(64,3*64,64,64);
            groundHero=new BufferedImage[2];
            groundHero[0]=img.getSubimage(4*64,64,64,64);
            groundHero[1]=img.getSubimage(0,2*64,64,64);

            animationsHero=new BufferedImage[9][];
            animationsHero[0]=runHero;
            animationsHero[1]=jumpHero;
            animationsHero[4]=attackHero1;
            animationsHero[5]=attackHero2;
            animationsHero[8]=dashHero;
            animationsHero[2]=fallingHero;
            animationsHero[3]=groundHero;
            animationsHero[7]=idleHero;
            animationsHero[6]=hitHero;

            statusBarImg=LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }
    private void updatePos() {
        moving=false;

        if(jump)
            jump();
        if(!left && !right && !inAir)
            return;

        float xSpeed=0;

        if(left)
        {
            xSpeed-=playerSpeed;
            flipX=96;
            flipW=-1;
        }
        else if(right)
        {
            xSpeed+=playerSpeed;
            flipX=0;
            flipW=1;
        }

        if(!inAir)
            if(!isEntityOnFloor(hitbox,lvlData))
                inAir=true;

        if(inAir)
        {
            if(canMoveHere(hitbox.x,hitbox.y+airSpeed,hitbox.width,hitbox.height,lvlData))
            {
                hitbox.y+=airSpeed;
                airSpeed+=GRAVITY;
                updateXPos(xSpeed);
            }
            else
            {
                hitbox.y=GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
                if(airSpeed>0)
                    resetInAir();
                else
                    airSpeed=fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }
        }
        else
            updateXPos(xSpeed);
        moving=true;
    }

    private void jump() {
        if(inAir)
            return;
        inAir=true;
        airSpeed=jumpSpeed;
    }

    private void resetInAir() {
        inAir=false;
        airSpeed=0;
    }

    private void updateXPos(float xSpeed) {
        if(canMoveHere(hitbox.x+xSpeed,hitbox.y,hitbox.width,hitbox.height,lvlData))
        {
            hitbox.x +=xSpeed;
        }
        else
        {
            hitbox.x=GetEntityXPosNextToWall(hitbox,xSpeed);
        }
    }

    public void changeHealth(int value)
    {
        currentHealth+=value;

        if(currentHealth<=0)
        {
            currentHealth=0;
        }
        else if(currentHealth>=maxHealth)
            currentHealth=maxHealth;
    }

    private void setAnimation() {
        int startAni=state;
        if(moving && !attacking)
            state=RUNNING;
        if(ground && !attacking)
        {
            state=GROUND;

        }
        else if(!moving && !attacking && !ground)
        {
            state=IDLE;
            aniIndex=0;
        }
        if(inAir && !attacking)
        {
            if(airSpeed<0)
                state=JUMP;
            else state=FALLING;
            if(airSpeed>4)
                ground=true;
        }
        if(attacking) {
            state = ATTACK1;
        }
        if(startAni!=state)
            resetAniTick();
    }

    private void resetAniTick() {
        aniTick=0;
        aniIndex=0;
    }

    private void updateAnimationTick() {
        aniTick++;
        if(aniTick>=ANI_SPEED)
        {
            aniTick=0;
            aniIndex++;
            if(aniIndex>=GetSpriteAmount(state)) {
                aniIndex = 0;
                attacking=false;
                ground=false;
                attackChecked=false;
            }
        }
    }

    public boolean isLeft() {
        return left;
    }
    public void setAttack(boolean attacking)
    {
        this.attacking=attacking;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump)
    {
        this.jump=jump;
    }
    public void resetDirBooleans()
    {
        left=false;
        right=false;
        down=false;
        up=false;
    }

    public void loadLvlData(int[][] lvlData)
    {
        this.lvlData=lvlData;
        if(!isEntityOnFloor(hitbox,lvlData))
            inAir=true;
    }

    public void resetAll()
    {
        resetDirBooleans();
        inAir=false;
        attacking=false;
        moving=false;
        state=IDLE;
        currentHealth=maxHealth;

        hitbox.x=x;
        hitbox.y=y;

        if(!isEntityOnFloor(hitbox,lvlData))
            inAir=true;
    }

    public void kill() {
        currentHealth=0;
    }

    public int getHealth()
    {
        return currentHealth;
    }
}