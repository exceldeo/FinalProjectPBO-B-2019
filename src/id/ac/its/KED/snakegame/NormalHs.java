package id.ac.its.KED.snakegame;

import java.awt.Graphics;
import java.io.*;
import java.util.Scanner; 

public class NormalHs {
	 
	File filetext = new File("src/resources/textfile/normal.txt");
	private Scanner s;
	private int posY = 360;
	
	public void render(Graphics g)
	{
		try {
			s = new Scanner(filetext);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		g.drawString("Highscore :", 130, posY);
		
		while(s.hasNextLine())
		{
			g.drawString(s.nextLine(), 250, posY);
			posY += 30;
		}
	}
}
