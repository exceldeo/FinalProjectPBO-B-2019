package id.ac.its.KED.snakegame;

import java.awt.Graphics;
import java.awt.Image;
import java.io.*;
import java.util.Scanner;

import javax.swing.ImageIcon; 

public class NormalHs {
	 
	File filetext = new File("src/resources/textfile/normal.txt");
	private Scanner s;
	private int posY = 360;
	private String[] data = new String[10];
	private Image normalhsback;
	 
	public void getData()
	{
		
		try {
			s = new Scanner(filetext);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		int i = 0;
		while(s.hasNextLine())
		{
			
			data[i] = s.nextLine();
//			System.out.println(data[i]);
			i++;
		}
	}
	
	public void renderGame(Graphics g)
	{
		
		g.drawString("Highscore :", 130, posY);
		
		int n = 0;
		while(data[n] != null)
		{
			
			g.drawString(data[n], 250, posY);
			posY += 30;
			n++;
		}
	}
	
	public void reset()
	{
		
		this.posY = 360;
	}
	
	public void renderMenu(Graphics g)
	{	
		
		ImageIcon normhs = new ImageIcon("src/resources/normalhs.png");
        normalhsback = normhs.getImage();
        
        g.drawImage(normalhsback, 0, 0, null);
	}
}
