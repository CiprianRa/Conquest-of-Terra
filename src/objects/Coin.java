package objects;

import Main.Game;

public class Coin extends GameObject{
    public Coin(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation=true;
        initHitbox(18,18);
        xDrawOffset=(int)(3* Game.SCALE);
        yDrawOffset=(int)(2*Game.SCALE);
    }

    public void update()
    {
        updateAnimationTick();
    }
}
