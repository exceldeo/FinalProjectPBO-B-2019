package id.ac.its.KED.snakegame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Menu {

	private Image playButton;
    private Image timeattButton;
    
	public void render(Graphics g)
	{
		
		Font font = new Font("Helvetica", Font.BOLD, 50);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Sssnake !", 125 , 125);
		
		ImageIcon pb = new ImageIcon("src/resources/button/buttonstart.png");
        playButton = pb.getImage();
        
        ImageIcon tt = new ImageIcon("src/resources/button/buttontime.png");
        timeattButton = tt.getImage();
        
        g.drawImage(playButton, 110, 200, null);
        g.drawImage(timeattButton, 110, 300, null);
	}
}
