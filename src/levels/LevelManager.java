package levels;

import Main.Game;
import gamestates.Gamestate;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {
   Game game;
   private BufferedImage[] levelSprite;
   private ArrayList<Level> levels;
   private int lvlIndex=0;
    public LevelManager(Game game)
    {
        this.game=game;
       // levelSprite= LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importOutsideSprites();
        levels=new ArrayList<>();
        buildAllLevels();
    }

    public void loadNextLevel()
    {
        lvlIndex++;
        if(lvlIndex>=levels.size())
        {
            lvlIndex=0;
            System.out.println("No more levels!Game Completed!");
            Gamestate.state= Gamestate.MENU;
        }
        Level newLevel=levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setxMaxLvlOffset(newLevel.getLvlOffsetX());
        game.getPlaying().setyMaxLvlOffset(newLevel.getLvlOffsetY());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels=LoadSave.GetAllLevels();
        for(BufferedImage img: allLevels)
            levels.add(new Level(img));
    }

    private void importOutsideSprites() {
        BufferedImage img=LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite=new BufferedImage[96];
        for(int i=0;i<16;i++)
            for(int j=0;j<6;j++)
            {
                int index=j*16+i;
                levelSprite[index]=img.getSubimage(i*32,j*32,32,32);
            }
    }

    public void draw(Graphics g,int lvlOffset,int ylvlOffset)
    {
        for(int j=0;j<levels.get(lvlIndex).getLevelData().length;j++)
            for(int i=0;i<levels.get(lvlIndex).getLevelData()[0].length;i++)
            {
                int index=levels.get(lvlIndex).GetSpriteIndex(i,j);
                g.drawImage(levelSprite[index],Game.TILES_SIZE*i-lvlOffset,Game.TILES_SIZE*j-ylvlOffset,Game.TILES_SIZE,Game.TILES_SIZE,null);
            }

    }
    public void update()
    {

    }

    public Level getCurrentLevel()
    {
        return levels.get(lvlIndex);
    }

    public int getAmountofLevels()
    {
        return levels.size();
    }
}
