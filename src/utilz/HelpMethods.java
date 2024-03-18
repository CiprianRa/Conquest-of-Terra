package utilz;

import Entities.Boxer;
import Entities.Chainer;
import Entities.Droid;
import Main.Game;
import objects.Coin;
import objects.Potion;
import objects.Spike;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.ObjectConstants.*;

public class HelpMethods {
    public static boolean canMoveHere(float x,float y,float width,float height,int[][] lvlData)
    {
        if(!isSolid(x,y,lvlData))
            if(!isSolid(x+width,y+height,lvlData))
                if(!isSolid(x+width,y,lvlData))
                    if(!isSolid(x,y+height,lvlData))
                        return true;
        return false;
    }

    private static boolean isSolid(float x,float y,int[][] lvlData)
    {
        int maxWidth=lvlData[0].length*Game.TILES_SIZE;
        int maxHeight= lvlData.length*Game.TILES_SIZE;
        if(x<0||x>= maxWidth)
            return true;
        if(y<0||y>=maxHeight)
            return true;
        float xIndex=x/Game.TILES_SIZE;
        float yIndex=y/Game.TILES_SIZE;

        return IsTileSolid((int)xIndex,(int)yIndex,lvlData);
    }

    public static boolean IsTileSolid(int xTile,int yTile,int[][] lvlData)
    {
        int value=lvlData[yTile][xTile];
        if(value>=96 || value<0 ||value!=62)
            return true;
        else return false;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox,float xSpeed)
    {
        int currentTile=(int)(hitbox.x/Game.TILES_SIZE);
        if(xSpeed>0) //Right
        {
            int tileXPos=currentTile*Game.TILES_SIZE;
            int xOffset=(int)(Game.TILES_SIZE-hitbox.width);
            return tileXPos+xOffset-1;
        }
        else //Left
        {
            return currentTile*Game.TILES_SIZE;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox,float airSpeed)
    {
        int currentTile=(int)(hitbox.y/Game.TILES_SIZE);
        if(airSpeed>0)
        {
            //Falling -touching floor
            int tileYPos=currentTile*Game.TILES_SIZE;
            int yOffset=(int)(Game.TILES_SIZE-hitbox.height);
            return tileYPos+yOffset-1;
        }
        else
        {
            //Jumping
            return currentTile*Game.TILES_SIZE;
        }
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox,int[][] lvlData)
    {
        //Check the pixel below bottom left and bottom right
        if(!isSolid(hitbox.x,hitbox.y+hitbox.height+1,lvlData))
            if(!isSolid(hitbox.x+hitbox.width,hitbox.y+hitbox.height+1,lvlData)) {
                return false;
            }
        return true;
    }

    public static boolean isFloor(Rectangle2D.Float hitbox,float xSpeed,int[][] lvlData)
    {
        if(xSpeed>0)
            return isSolid(hitbox.x+hitbox.width,hitbox.y+hitbox.height+1,lvlData);
        else
            return isSolid(hitbox.x+xSpeed,hitbox.y+hitbox.height+1,lvlData);
    }

    public static boolean IsAllTileWalkable(int xStart,int xEnd,int y,int[][] lvlData)
    {
        for(int i=0;i<xEnd-xStart;i++) {
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
            if(!IsTileSolid(xStart+i,y+1,lvlData))
                return false;
        }
        return true;
    }
    public static boolean IsSightClear(int[][] lvlData,Rectangle2D.Float firstHitbox,Rectangle2D.Float secondHitbox,int yTile)
    {
        int firstXTile=(int)(firstHitbox.x/Game.TILES_SIZE);
        int secondXTile=(int)(secondHitbox.x/Game.TILES_SIZE);

        if(firstXTile>secondXTile)
            return IsAllTileWalkable(secondXTile,firstXTile,yTile,lvlData);
        else
            return IsAllTileWalkable(firstXTile,secondXTile,yTile,lvlData);
    }

    public static int[][] GetLevelData(BufferedImage img)
    {
        int[][] lvlData=new int[img.getHeight()][img.getWidth()];

        for(int j=0;j<img.getHeight();j++)
        {
            for(int i=0;i<img.getWidth();i++)
            {
                Color color=new Color(img.getRGB(i,j));
                int value=color.getRed();
                if(value>=96)
                    value=0;
                lvlData[j][i]=value;
            }
        }
        return lvlData;
    }

    public static ArrayList<Boxer> GetBoxers(BufferedImage img)
    {
        ArrayList<Boxer> list=new ArrayList<>();
        for(int j=0;j<img.getHeight();j++)
        {
            for(int i=0;i<img.getWidth();i++)
            {
                Color color=new Color(img.getRGB(i,j));
                int value=color.getGreen();
                if(value==BOXER)
                    list.add(new Boxer(i*Game.TILES_SIZE,j*Game.TILES_SIZE));
            }
        }
        return list;
    }

    public static ArrayList<Chainer> GetChainers(BufferedImage img)
    {
        ArrayList<Chainer> list=new ArrayList<>();
        for(int j=0;j<img.getHeight();j++)
        {
            for(int i=0;i<img.getWidth();i++)
            {
                Color color=new Color(img.getRGB(i,j));
                int value=color.getGreen();
                if(value==CHAINER)
                    list.add(new Chainer(i*Game.TILES_SIZE,j*Game.TILES_SIZE));
            }
        }
        return list;
    }

    public static ArrayList<Droid> GetDroids(BufferedImage img)
    {
        ArrayList<Droid> list=new ArrayList<>();
        for(int j=0;j<img.getHeight();j++)
        {
            for(int i=0;i<img.getWidth();i++)
            {
                Color color=new Color(img.getRGB(i,j));
                int value=color.getGreen();
                if(value==DROID)
                    list.add(new Droid(i*Game.TILES_SIZE,j*Game.TILES_SIZE));
            }
        }
        return list;
    }

    public static Point GetPlayerSpawn(BufferedImage img)
    {
        for(int j=0;j<img.getHeight();j++)
        {
            for(int i=0;i<img.getWidth();i++)
            {
                Color color=new Color(img.getRGB(i,j));
                int value=color.getGreen();
                if(value==150)
                    return new Point(i*Game.TILES_SIZE,j*Game.TILES_SIZE);
            }
        }
        return new Point(1*Game.TILES_SIZE,1*Game.TILES_SIZE);
    }

    public static ArrayList<Potion> GetPotions(BufferedImage img)
    {
        ArrayList<Potion> list=new ArrayList<>();
        for(int j=0;j<img.getHeight();j++)
        {
            for(int i=0;i<img.getWidth();i++)
            {
                Color color=new Color(img.getRGB(i,j));
                int value=color.getBlue();
                if(value==RED_POTION)
                    list.add(new Potion(i*Game.TILES_SIZE,j*Game.TILES_SIZE,RED_POTION));
            }
        }
        return list;
    }

    public static ArrayList<Spike> GetSpikes(BufferedImage img)
    {
        ArrayList<Spike> list=new ArrayList<>();
        for(int j=0;j<img.getHeight();j++)
        {
            for(int i=0;i<img.getWidth();i++)
            {
                Color color=new Color(img.getRGB(i,j));
                int value=color.getBlue();
                if (value == SPIKE) {
                    list.add(new Spike(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
                }
            }
        }
        return list;
    }

    public static ArrayList<Coin> GetCoins(BufferedImage img)
    {
        ArrayList<Coin> list=new ArrayList<>();
        for(int j=0;j<img.getHeight();j++)
        {
            for(int i=0;i<img.getWidth();i++)
            {
                Color color=new Color(img.getRGB(i,j));
                int value=color.getBlue();
                if(value==COIN)
                    list.add(new Coin(i*Game.TILES_SIZE,j*Game.TILES_SIZE,COIN));
            }
        }
        return list;
    }
}
