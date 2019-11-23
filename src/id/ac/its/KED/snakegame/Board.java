package id.ac.its.KED.snakegame;
 
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;
 
public class Board extends JPanel implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
 
    private final int N_BLOCKS = 20;
    private final int BLOCK_SIZE = 23;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 25);
    private final int B_WIDTH = 500;
    private final int B_HEIGHT = 500;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 41;
    private final int DELAY = 150;
//    private final int SPRINT = 120;
 
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    private int obsX[] = new int[10];
    private int obsY[] = new int[10];
    
    private int dots;
    private int apple_x;
    private int apple_y;
    private int score;
    private int gold;
    private int health;
    private int obs_x;
    private int obs_y;
 
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean onFire = false;
 
    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;
    private Image g_apple;
    private Image obstacle;			
    
    public Board() {
 
        initBoard();
    }
 
    private void initBoard() {
 
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
 
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        backgroundMusic();
        initGame();
    }
 
    private void loadImages() {
 
        ImageIcon iid = new ImageIcon("src/resources/images/dot.png");
        ball = iid.getImage();
 
        ImageIcon iia = new ImageIcon("src/resources/images/apple.png");
        apple = iia.getImage();
 
        ImageIcon iig = new ImageIcon("src/resources/images/gold_apple.png");
        g_apple = iig.getImage();
 
        ImageIcon iih = new ImageIcon("src/resources/images/head.png");
        head = iih.getImage();
        
        ImageIcon iio = new ImageIcon("src/resources/images/obstacle.png");
        obstacle = iio.getImage();
    }
 
    private void backgroundMusic() 
    {
    	
    	AudioPlayer MGP = AudioPlayer.player;
    	AudioStream BGM;
    	
    	ContinuousAudioDataStream loop = null;
    	
    	try {
    		InputStream BGMfile = new FileInputStream("src/resources/music/background.wav");
            BGM = new AudioStream(BGMfile);
            AudioPlayer.player.start(BGM);
    	}
    	catch(FileNotFoundException e){
            System.out.print(e.toString());			// file not found
        }
        catch(IOException error)
        {
            System.out.print(error.toString());
        }
    	
    	MGP.start(loop);
    }
    
    private void initGame() {
 
        score = 0;
        dots = 3;
        gold = 5;
        health = 300;
 
        for (int z = 0; z < dots; z++) {
        	
            x[z] = 50 - z * 10;
            y[z] = 80;
        }
 
        for (int i = 0; i < 10; i++)
        {
        	obsX[i] = 0;
            obsY[i] = 0;
        }
        
        locateApple();
         
        timer = new Timer(DELAY, this);
        timer.start();
    }
 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
 
        doDrawing(g);
    }
 
    private void doDrawing(Graphics g) {					// Drawing the snake
 
        Graphics2D g2d = (Graphics2D) g;
        if (inGame) {
 
            g.setColor(Color.white);
            g.drawRect(0, 70, 498, 430);
 
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, 499, 69);
 
            drawScore(g2d);
        	drawHealth(g2d);
 
            if (dots == gold - 1) {
            	
                g.drawImage(g_apple, apple_x, apple_y, this);
            }
            else {
            	
                g.drawImage(apple, apple_x, apple_y, this);
            }
 
            if(score >= 1)
            {
            	for(int a = 0; a < 10; a++)
            	{
            		g.drawImage(obstacle, obsX[a], obsY[a], this);
            	}
            } 
            
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                	
                    g.drawImage(head, x[z], y[z], this);
                } else {
                	
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
 
            Toolkit.getDefaultToolkit().sync();
 
        } else {
 
            gameOver(g);
        }
    }
 
    private void drawScore(Graphics2D g) {
        String s;
 
        g.setFont(smallFont);
        g.setColor(new Color(96,128,255));
        s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 5, SCREEN_SIZE / 10);
    }
 
    private void drawHealth(Graphics2D g) {
        String h;
 
        g.setFont(smallFont);
        
        if (health > 200) {
        	
        	g.setColor(new Color(96,128,255));
        }
        else if (health > 100 && health <= 200) {
        	
        	g.setColor(new Color(255,255,0));
        }
        else {
        	
        	g.setColor(new Color(255,0,0));
        }
        
        h = "Health: " + health;
        g.drawString(h, SCREEN_SIZE / 3 + 100, SCREEN_SIZE / 10);
    }
 
    private void gameOver(Graphics g) {				// Game Over UI
 
        String msg = "Game Over !";
        String msg1 = "Score : " + score;
        Font small = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metr = getFontMetrics(small);
 
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
 
        if (dots < 10) {
 
            g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2, B_HEIGHT / 2 + 40);
        }
        else if (dots < 100 && dots > 10 ) {
 
            g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2 - 5, B_HEIGHT / 2 + 40);
        }
        else {
 
            g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2 - 10, B_HEIGHT / 2 + 40);
        }
 
    }
 
    private void move() {
 
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
 
        if (onFire == true)				// belum fix
        {
 
            if (leftDirection) {
                x[0] -= (DOT_SIZE);
            }
            if (rightDirection) {
                x[0] += (DOT_SIZE);
            }
            if (upDirection) {
                y[0] -= (DOT_SIZE);
            }
            if (downDirection) {
                y[0] += (DOT_SIZE);
            }
        }
        else
        {
            if (leftDirection) {
                x[0] -= DOT_SIZE;
            }
            if (rightDirection) {
                x[0] += DOT_SIZE;
            }
            if (upDirection) {
                y[0] -= DOT_SIZE;
            }
            if (downDirection) {
                y[0] += DOT_SIZE;
            }
        }
 
        if(health > 0)		// Setiap move menghabiskan darah player
        {
            health--;
        }
        else
        {
            inGame = false;
        }
    }
 
    private void checkApple() {
    	 
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
 
            dots++;
            health += 15;
            locateApple();
            
            if (dots == gold) {
                score += 10;
                gold += 5;
                health += 20;
                
                //belum fix
//                timer = new Timer(SPRINT, this);
//                timer.start();
 
                onFire = true;
            }
            else {
                score++;
 
                onFire = false;
            }
            
            if (score >= 1)
            {
            	for (int i = 0; i < 10; i++)
                {
                	locateObstacle();
                	obsX[i] = obs_x;
                    obsY[i] = obs_y;
                }
            }
        }
    }
    
    private void checkObstacle() {
    	
    	for(int i = 0; i < 10; i++)
    	{
    		if ((x[0] == obsX[i]) && (y[0] == obsY[i])) {
       		 
                inGame = false;
        	}
    	}
    	
    	if (!inGame) {
            timer.stop();
        }
    }
    	
    private void checkCollision() {
 
        for (int z = dots; z > 0; z--) {
 
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }
 
        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }
 
        if (y[0] < 80) {
            inGame = false;
        }
 
        if (x[0] >= B_WIDTH) {
            inGame = false;
        }
 
        if (x[0] < 0) {
            inGame = false;
        }
 
        if (!inGame) {
            timer.stop();
        }
    }
 
    private void locateApple() {
 
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));
 
        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE) + 80);
    }
 
    private void locateObstacle()
    {
    	int s = (int) (Math.random() * RAND_POS);
        obs_x = ((s * DOT_SIZE));
 
        s = (int) (Math.random() * RAND_POS);
        obs_y = ((s * DOT_SIZE) + 80);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
 
        if (inGame) {
 
            checkApple();
            checkCollision();
            checkObstacle();
            
            move();
        }
 
        repaint();
    }
 
    private class TAdapter extends KeyAdapter {
 
        @Override
        public void keyPressed(KeyEvent e) {
 
            int key = e.getKeyCode();	// Input Key (Analog)
 
            if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
 
            if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
 
            if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
 
            if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}