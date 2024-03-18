package utilz;

import Entities.Boxer;
import Main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import static utilz.Constants.EnemyConstants.BOXER;

public class LoadSave {
    public static final String PLAYER_ATLAS="John_Hero_v2.png";
    public static final String LEVEL_ATLAS="level_one_spritesheet.png";
    public static final String PLAYING_BACKGROUND="background.png";
    public static final String BIG_CLOUD="big_cloud.png";
    public static final String SMALL_CLOUD="small_cloud.png";
    public static final String LUNA="luna.png";
    public static final String BOXER_SPRITE="all_inamic_1.png";
    public static final String STATUS_BAR="health_power_bar.png";
    public static final String MENU_BUTTONS="button_atlas.png";
    public static final String PAUSE_BACKGROUND="pause_menu.png";
    public static final String SOUND_BUTTONS="sound_button.png";
    public static final String URM_BUTTONS="urm_buttons.png";
    public static final String COMPLETED_IMG="completed_sprite.png";
    public static final String CHAINER="Chainer.png";
    public static final String DROID="DROID.png";
    public static final String POTION_ATLAS="potions_sprites.png";
    public static final String TRAP_ATLAS="trap_atlas.png";
    public static final String COIN="goldCoin.png";


    public static BufferedImage GetSpriteAtlas(String fileName)
    {
        BufferedImage img=null;
        InputStream is=LoadSave.class.getResourceAsStream("/"+fileName);
        try {
            img = ImageIO.read(is);

        }catch(IOException e)
        {
            e.printStackTrace();
        }finally {
            try
            {
                is.close();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[] GetAllLevels()
    {
        URL url=LoadSave.class.getResource("/lvls");
        File file=null;

        try{
            file=new File(url.toURI());
        }catch(URISyntaxException e)
        {
            e.printStackTrace();
        }

        File[] files=file.listFiles();
        File[] filesSorted=new File[files.length];

        for(int i=0;i<filesSorted.length;i++)
            for(int j=0;j<files.length;j++)
            {
                if(files[j].getName().equals((i+1)+".png"))
                    filesSorted[i]=files[j];
            }

        //for(File f:files)
        //    System.out.println("File: "+f.getName());

        BufferedImage[] imgs=new BufferedImage[filesSorted.length];
        for(int i=0;i<imgs.length;i++)
            try{
                imgs[i]=ImageIO.read(filesSorted[i]);
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        return imgs;
    }
}
