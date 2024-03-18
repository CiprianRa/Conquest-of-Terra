package utilz;

import Main.Game;

public class Constants {
    public static final float GRAVITY=0.04f* Game.SCALE;
    public static final int ANI_SPEED=25;

    public static class ObjectConstants
    {
        public static final int RED_POTION=100;
        public static final int SPIKE=50;
        public static final int COIN=75;

        public static final int RED_POTION_VALUE=15;
        public static final int COIN_VALUE=10;
        public static final int POTION_WIDTH_DEFAULT=12;
        public static final int POTION_HEIGHT_DEFAULT=16;
        public static final int COIN_WIDTH_DEFAULT=18;
        public static final int COIN_HEIGHT_DEFAULT=18;
        public static final int POTION_WIDTH=(int)(Game.SCALE*POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT=(int)(Game.SCALE*POTION_HEIGHT_DEFAULT);
        public static final int COIN_WIDTH=(int)(Game.SCALE*COIN_WIDTH_DEFAULT);
        public static final int COIN_HEIGHT=(int)(Game.SCALE*COIN_HEIGHT_DEFAULT);

        public static final int SPIKE_WIDTH_DEFAULT=32;
        public static final int SPIKE_HEIGHT_DEFAULT=32;
        public static final int SPIKE_WIDTH=(int)(Game.SCALE*SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT=(int)(Game.SCALE*SPIKE_HEIGHT_DEFAULT);

        public static int GetSpriteAmount(int object_type)
        {
            switch(object_type)
            {
                case RED_POTION:
                    return 7;
                case COIN:
                    return 9;
            }
            return 1;
        }
    }
    public static class UI
    {
        public static class Buttons
        {
            public static final int B_WIDTH_DEFAULT=140;
            public static final int B_HEIGHT_DEFAULT=56;
            public static final int B_WIDTH=(int)(B_WIDTH_DEFAULT*Game.SCALE);
            public static final int B_HEIGHT=(int)(B_HEIGHT_DEFAULT*Game.SCALE);
        }
        public static class PauseButtons
        {
            public static final int SOUND_SIZE_DEFAULT=42;
            public static final int SOUND_SIZE=(int)(SOUND_SIZE_DEFAULT*Game.SCALE);
        }
        public static class UrmButtons
        {
            public static final int URM_DEFAULT_SIZE=56;
            public static final int URM_SIZE=(int)(URM_DEFAULT_SIZE*Game.SCALE);

        }
    }
    public static class EnemyConstants
    {
        public static final int BOXER=100;
        public static final int CHAINER=200;
        public static final int DROID=250;
        public static final int IDLE=0;
        public static final int RUNNING=1;
        public static final int ATTACKING=2;
        public static final int HIT=3;
        public static final int DEAD=4;
        public static final int BOXER_WIDTH_DEFAULT=64;
        public static final int CHAINER_WIDTH_DEFAULT=64;
        public static final int BOXER_HEIGHT_DEFAULT=64;
        public static final int CHAINER_HEIGHT_DEFAULT=64;
        public static final int BOXER_DRAWOFFSET_X=(int)(25*2.5);
        public static final int CHAINER_DRAWOFFSET_X=(int)(2.5*5);
        public static final int BOXER_DRAWOFFSET_Y=(int)(23*2.5);
        public static final int CHAINER_DRAWOFFSET_Y=(int)(17*2.5);
        public static final int DROID_DRAWOFFSET_X=(int)(2*2.5);
        public static final int DROID_DRAWOFFSET_Y=(int)(12*2.5);
        public static final int DROID_HEIGHT_DEFAULT=64;
        public static final int DROID_WIDTH_DEFAULT=64;

        public static int GetSpriteAmount(int enemy_type,int enemy_state)
        {
            switch(enemy_type)
            {
                case BOXER:
                    switch(enemy_state)
                    {
                        case IDLE:
                            return 4;
                        case RUNNING:
                            return 6;
                        case ATTACKING:
                            return 6;
                        case HIT:
                            return 2;
                        case DEAD:
                            return 9;
                    }
                case CHAINER:
                    switch(enemy_state)
                    {
                        case IDLE:
                            return 6;
                        case ATTACKING:
                            return 8;
                        case HIT:
                            return 2;
                        case DEAD:
                            return 5;
                        case RUNNING:
                            return 8;
                    }
                case DROID:
                    switch(enemy_state)
                    {
                        case IDLE:
                            return 6;
                        case ATTACKING:
                            return 10;
                        case HIT:
                            return 2;
                        case DEAD:
                            return 6;
                        case RUNNING:
                            return 6;
                    }
            }
            return 0;
        }

        public static int GetMaxHealth(int enemy_type)
        {
            switch(enemy_type)
            {
                case BOXER:
                    return 10;
                case CHAINER:
                    return 10;
                case DROID:
                    return 10;
                default:return 1;
            }
        }

        public static int GetEnemyDmg(int enemy_type)
        {
            switch(enemy_type)
            {
                case BOXER:
                    return 25;
                case CHAINER:
                    return 25;
                case DROID:
                    return 20;
                default:
                    return 0;
            }
        }
    }

    public static class Enviroment
    {
        public static final int BIG_CLOUD_WIDTH_DEFAULT=64;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT=32;
        public static final int BIG_CLOUD_WIDTH=(int)(BIG_CLOUD_WIDTH_DEFAULT* Game.SCALE);
        public static final int BIG_CLOUD_HEIGHT=(int)(BIG_CLOUD_HEIGHT_DEFAULT*Game.SCALE);

        public static final int SMALL_CLOUD_WIDTH_DEFAULT=34;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT=26;
        public static final int SMALL_CLOUD_WIDTH=(int)(SMALL_CLOUD_WIDTH_DEFAULT* Game.SCALE);
        public static final int SMALL_CLOUD_HEIGHT=(int)(SMALL_CLOUD_HEIGHT_DEFAULT*Game.SCALE);
    }
    public static class Directions
    {
        public static final int LEFT=0;
        public static final int UP=1;
        public static final int RIGHT=2;
        public static final int DOWN=3;
    }
    public static class PlayerConstants
    {
        public static final int RUNNING=0;
        public static final int JUMP=1;
        public static final int FALLING=2;
        public static final int GROUND=3;
        public static final int ATTACK1=4;
        public static final int ATTACK2=5;
        public static final int HIT=6;
        public static final int IDLE=7;
        public static final int DASH=8;
        public static int GetSpriteAmount(int player_action)
        {
            switch(player_action)
            {
                case RUNNING:
                    return 4;
                case JUMP:
                    return 1;
                case IDLE:
                    return 1;
                case FALLING:
                    return 1;
                case ATTACK1:
                    return 7;
                case ATTACK2:
                    return 6;
                case HIT:
                    return 1;
                case GROUND:
                    return 2;
                case DASH:
                    return 4;
                default:
                    return 0;
            }
        }
    }
}
