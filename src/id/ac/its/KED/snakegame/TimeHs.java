package id.ac.its.KED.snakegame;

import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TimeHs {

	File filetext = new File("src/resources/textfile/timeattack.txt");
	private Scanner s;
	private int posY = 380;
	
	public void render(Graphics g)
	{
		try {
			s = new Scanner(filetext);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		g.drawString("Highscore :", 140, posY);
		
		while(s.hasNextLine())
		{
			g.drawString(s.nextLine(), 260, posY);
			posY += 30;
		}
	}
}
