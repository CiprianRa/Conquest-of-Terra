package levels;

import Entities.Boxer;
import Entities.Chainer;
import Entities.Droid;
import Main.Game;
import objects.Coin;
import objects.Potion;
import objects.Spike;
import utilz.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Main.Game.*;
import static utilz.HelpMethods.*;
import static utilz.HelpMethods.GetPlayerSpawn;

public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Potion> potions;
    private ArrayList<Spike> spikes;
    private ArrayList<Boxer> boxers;
    private ArrayList<Chainer> chainers;
    private ArrayList<Droid> droids;
    private ArrayList<Coin> coins;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private int lvlTilesHeight;
    private int maxTilesOffsetH;
    private int maxLvlOffsetY;
    private Point playerSpawn;

    public Level(BufferedImage img)
    {
        this.img=img;
        createLevelData();
        createEnemies();
        createPotions();
        createSpikes();
        createCoins();
        calcLvlOffsets();
        calcPlayerSpawn();
    }

    private void createCoins() {
        coins=HelpMethods.GetCoins(img);
    }

    private void createSpikes() {
        spikes=HelpMethods.GetSpikes(img);
    }

    private void createPotions() {
        potions= HelpMethods.GetPotions(img);
    }

    private void calcPlayerSpawn() {
        playerSpawn=GetPlayerSpawn(img);
    }

    private void calcLvlOffsets() {
        lvlTilesWide=img.getWidth();
        maxTilesOffset=lvlTilesWide- TILES_IN_WIDTH;
        maxLvlOffsetX= maxTilesOffset*Game.TILES_SIZE;
        lvlTilesHeight=img.getHeight();
        maxTilesOffsetH=lvlTilesHeight-TILES_IN_HEIGHT;
        maxLvlOffsetY= maxTilesOffsetH* TILES_SIZE;
    }

    private void createEnemies() {
        boxers=GetBoxers(img);
        chainers=GetChainers(img);
        droids=GetDroids(img);
    }

    private void createLevelData() {
        lvlData=GetLevelData(img);
    }

    public int GetSpriteIndex(int x,int y)
    {
        return lvlData[y][x];
    }

    public int[][] getLevelData()
    {
        return lvlData;
    }

    public int getLvlOffsetX()
    {
        return maxLvlOffsetX;
    }

    public int getLvlOffsetY()
    {
        return maxLvlOffsetY;
    }

    public ArrayList<Boxer> getBoxers()
    {
        return boxers;
    }
    public ArrayList<Chainer> getChainers()
    {
        return chainers;
    }
    public ArrayList<Droid> getDroids(){
        return droids;
    }
    public Point getPlayerSpawn()
    {
        return playerSpawn;
    }
    public ArrayList<Potion> getPotions()
    {
        return potions;
    }

    public ArrayList<Spike> getSpikes()
    {
        return spikes;
    }

    public ArrayList<Coin> getCoins()
    {
        return coins;
    }
}
