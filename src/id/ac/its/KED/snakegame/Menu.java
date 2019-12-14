package id.ac.its.KED.snakegame;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Menu {

	private Image playButton;
    private Image timeattButton;
    private Image highscoreButton;
    private Image background;
    
	public void render(Graphics g)
	{
		
		ImageIcon back = new ImageIcon("src/resources/menus/backgroundmenu.png");
        background = back.getImage();
        
        g.drawImage(background, 0, 0, null);
        
		ImageIcon pb = new ImageIcon("src/resources/button/buttonstart.png");
        playButton = pb.getImage();
        
        ImageIcon tt = new ImageIcon("src/resources/button/buttontime.png");
        timeattButton = tt.getImage();
        
        ImageIcon hs = new ImageIcon("src/resources/button/buttonhighscore.png");
        highscoreButton = hs.getImage();
        
        g.drawImage(playButton, 110, 240, null);
        g.drawImage(timeattButton, 110, 320, null);
        g.drawImage(highscoreButton, 110, 400, null);
	}
}
