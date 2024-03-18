package Main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import static Main.Game.*;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;
public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private static GamePanel singleInstance=null;
    private Game game;

    public static GamePanel getInstance(Game game) throws IOException{
        if(singleInstance==null)
            singleInstance=new GamePanel(game);
        return singleInstance;
    }
    public GamePanel(Game game){
        mouseInputs=new MouseInputs(this);
        this.game=game;

        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void setPanelSize()
    {
        Dimension size=new Dimension(GAME_WIDTH,GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println("size:"+GAME_WIDTH+":"+GAME_HEIGHT);
    }

    public void updateGame()
    {
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        game.render(g);
    }

    public Game getGame()
    {
        return game;
    }

}
