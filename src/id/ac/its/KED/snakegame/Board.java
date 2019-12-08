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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    private final int SPRINT = 70;
 
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    private final int obsX[] = new int[20];
    private final int obsY[] = new int[20];
    private final int obsXP[] = new int[182];
    private final int obsYP[] = new int[182];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int score;
    private int gold;
    private int health;
    private int obs_x;
    private int obs_y;
    private int typeG;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    public boolean inGame = false;
    public boolean inMenu = true;
    private boolean onFire = false;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;
    private Image g_apple;
    private Image obstacle;

    private TimeCounter timeCounter;

    private Menu menu;

    private NormalHs normalhs;
    private TimeHs timehs;
    
    public Board() {

        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        addMouseListener(new MouseInput());

        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        backgroundMusic();

        menu = new Menu();

        normalhs = new NormalHs();
        timehs = new TimeHs();
        
        initGame();
    }

    private void loadImages() {

        final ImageIcon iid = new ImageIcon("src/resources/images/dot.png");
        ball = iid.getImage();

        final ImageIcon iia = new ImageIcon("src/resources/images/apple.png");
        apple = iia.getImage();

        final ImageIcon iig = new ImageIcon("src/resources/images/gold_apple.png");
        g_apple = iig.getImage();

        final ImageIcon iih = new ImageIcon("src/resources/images/righthead.png");
        head = iih.getImage();

        final ImageIcon iio = new ImageIcon("src/resources/images/obstacle.png");
        obstacle = iio.getImage();
    }

    private void backgroundMusic() {

        final AudioPlayer MGP = AudioPlayer.player;
        AudioStream BGM;

        final ContinuousAudioDataStream loop = null;

        try {
            final InputStream BGMfile = new FileInputStream("src/resources/music/background.wav");
            BGM = new AudioStream(BGMfile);
            AudioPlayer.player.start(BGM);
        } catch (final FileNotFoundException e) {
            System.out.print(e.toString()); // file not found
        } catch (final IOException error) {
            System.out.print(error.toString());
        }

        MGP.start(loop);
    }

    private void initGame() {

        if (inGame == true && inMenu == false) {
            timeCounter = new TimeCounter(0, 0, 0);
            score = 0;
            dots = 3;
            gold = 5;
            health = 150;

            for (int z = 0; z < dots; z++) {

                x[z] = 50 - z * 10;
                y[z] = 80;
            }

            if (typeG == 1)
            {
            	for (int i = 0; i < 20; i++) {
                    obsX[i] = 0;
                    obsY[i] = 0;
                }
            }
            else if (typeG == 2)
            {
            	obsXP[0] = 0;
                obsYP[0] = 70;
            	for (int i = 1; i < 43; i++) {
                    obsXP[i] = 0;
                    obsYP[i] = obsYP[i - 1] + 10;
                }
            	for (int i = 43; i < 92; i++) {
                    obsXP[i] = obsXP[i - 1] + 10;
                    obsYP[i] = 70;
                }
            	for (int i = 92; i < 134; i++) {
                    obsXP[i] = obsXP[i - 1];
                    obsYP[i] = obsYP[i - 1] + 10;
                }
            	for (int i = 134; i < 182; i++) {
                    obsXP[i] = obsXP[i - 1] - 10;
                    obsYP[i] = obsYP[i - 1];
                }
            }

            locateApple();

            timer = new Timer(DELAY, this);
            timer.start();
        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(final Graphics g) { // Drawing the snake

        final Graphics2D g2d = (Graphics2D) g;

        if (inMenu) {

            menu.render(g);
        } else {
            if (inGame) {

                g.setColor(Color.white);
                g.drawRect(0, 70, 498, 430);

                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, 499, 69);

                if (typeG == 1) {

                    drawScore(g2d);
                    drawHealth(g2d);

                    if (dots == gold - 1) {

                        g.drawImage(g_apple, apple_x, apple_y, this);
                    } else {

                        g.drawImage(apple, apple_x, apple_y, this);
                    }

                    if (score >= 1) {
                        for (int a = 0; a < 20; a++) {
                        	
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
                }

                else if (typeG == 2) {

                    drawTimeCounter(g2d);
                    drawScore(g2d);

                    if (dots == gold - 1) {

                        g.drawImage(g_apple, apple_x, apple_y, this);
                    } else {

                        g.drawImage(apple, apple_x, apple_y, this);
                    }

                    // --> Bagian untuk syntax obstacles
                    for (int a = 0; a < 182; a++) {
                    	
                        g.drawImage(obstacle, obsXP[a], obsYP[a], this);
                    }
                    
                    for (int z = 0; z < dots; z++) {
                        if (z == 0) {

                            g.drawImage(head, x[z], y[z], this);
                        } else {

                            g.drawImage(ball, x[z], y[z], this);
                        }
                    }
                }

                Toolkit.getDefaultToolkit().sync();

            } else {

            	timer.stop();					// Matikan Game Loop
                gameOver(g);
                     
//                System.out.print(inGame);
//                System.out.print(inMenu);
            }
        }
    }

    private void drawScore(final Graphics2D g) {
    	
        String s;

        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 7, SCREEN_SIZE / 10);
    }

    private void drawHealth(final Graphics2D g) {
    	
        String h;

        if (health > 100) {

            g.setColor(new Color(96, 128, 255));
        } else if (health > 50 && health <= 100) {

            g.setColor(new Color(255, 255, 0));
        } else {

            g.setColor(new Color(255, 0, 0));
        }

        g.drawRect(290, 27, 150, 20);
        g.fillRect(290, 27, health, 20);
        
        h = "Health: ";
        g.drawString(h, SCREEN_SIZE / 4 + 85, SCREEN_SIZE / 10);
    }

    private void drawTimeCounter(final Graphics2D g) {
    	
        String ts;

        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        ts = "Time: " + timeCounter.result();
        g.drawString(ts, SCREEN_SIZE / 5 + 180, SCREEN_SIZE / 10);
    }

    private void gameOver(final Graphics g) { // Game Over UI

    	final Font small = new Font("Helvetica", Font.BOLD, 20);
    	final FontMetrics metr = getFontMetrics(small);
    	g.setColor(Color.white);
        g.setFont(small);
    	
    	final String retry = "Press Spacebar to retry";
    	
        if (typeG == 1) {

            final String msg = "Game Over !";
            final String msg1 = "Score : " + score;
            
            g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2 - 40);
              
            if (score < 10) {

                g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2, B_HEIGHT / 2);
            } else if (score < 100 && score > 10) {

                g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2 - 5, B_HEIGHT / 2);
            } else {

                g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2 - 10, B_HEIGHT / 2);
            }
            
            g.drawString(retry, (B_WIDTH - metr.stringWidth(retry)) / 2 , (B_HEIGHT / 2) + 50);
            
            normalhs.render(g);
        }

        else if (typeG == 2) {

            final String msg = "Game Over !";
            final String msg1 = "Score : " + score + "    Time: " + timeCounter.result();

            g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
            
            if (score < 10) {

                g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2, B_HEIGHT / 2 + 40);
            } else if (score < 100 && score > 10) {

                g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2 - 5, B_HEIGHT / 2 + 40);
            } else {

                g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2 - 10, B_HEIGHT / 2 + 40);
            }
            
            g.drawString(retry, (B_WIDTH - metr.stringWidth(retry)) / 2 , (B_HEIGHT / 2) + 90);
            
            timehs.render(g);
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (onFire == true) {

            timer.stop();
            timer = new Timer(SPRINT, this);
            timer.start();
        } else {

            timer.stop();
            timer = new Timer(DELAY, this);
            timer.start();
        }

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

        if (typeG == 1) {
            if (health > 0) // Setiap move menghabiskan darah player
            {
                health--;
            } else {
            	
                inGame = false;
            }
        }
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            if (typeG == 1) {

                dots++;
                locateApple();

                if (dots == gold) {
                    score += 10;
                    gold += 5;
                    if(health + 50 > 150)
                    {
                    	health = 150;
                    }
                    else
                    {
                    	health += 50;
                    }
                    
                    onFire = true;
                } else {
                    score++;
                    if(health + 30 > 150)
                    {
                    	health = 150;
                    }
                    else
                    {
                    	health += 30;
                    }
                    
                    onFire = false;
                }
                

                
                if (score >= 1) {
                    for (int i = 0; i < 20; i++) {
                        locateObstacle();
                        obsX[i] = obs_x;
                        obsY[i] = obs_y;

                        if (obsX[i] <= x[0] + 10 && obsX[i] >= x[0] - 10 && obsY[i] <= y[0] + 10 && obsY[i] >= y[0] - 10) {
                            locateObstacle();
                            obsX[i] = obs_x;
                            obsY[i] = obs_y;
                        }

                        if (obsX[i] == apple_x && obsY[i] == apple_y) {
                            locateObstacle();
                            obsX[i] = obs_x;
                            obsY[i] = obs_y;
                        }
                    }
                }
            }

            else if (typeG == 2) {

                dots++;
                score++;
                locateApple();

                if (dots == gold) {
                    gold += 5;
                    score++;
                    onFire = true;
                } else {
                    onFire = false;
                }
            }
        }
    }

    private void checkObstacle() {

    	if (typeG == 1)
    	{
    		for (int i = 0; i < 20; i++) {
                if ((x[0] == obsX[i]) && (y[0] == obsY[i])) {

                    inGame = false;
                }
            }
    	}
    	else if (typeG == 2)
    	{
    		for (int i = 0; i < 182; i++) {
                if ((x[0] == obsXP[i]) && (y[0] == obsYP[i])) {

                    inGame = false;
                }
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

        if (y[0] < 70) {
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

    private void locateObstacle() {
        int s = (int) (Math.random() * RAND_POS);
        obs_x = ((s * DOT_SIZE));

        s = (int) (Math.random() * RAND_POS);
        obs_y = ((s * DOT_SIZE) + 80);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {

        if (inGame) {
            checkApple();
            checkCollision();
            checkObstacle();

            move();
            
            timeCounter.actionPerformed();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(final KeyEvent e) {

            final int key = e.getKeyCode(); // Input Key (Analog)

            if (inGame == true) {

                if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && (!rightDirection)) {

                	final ImageIcon iih = new ImageIcon("src/resources/images/lefthead.png");
                    head = iih.getImage();
                    
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                }

                if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && (!leftDirection)) {
                	
                	final ImageIcon iih = new ImageIcon("src/resources/images/righthead.png");
                    head = iih.getImage();
                    
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                }

                if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && (!downDirection)) {
                	
                	final ImageIcon iih = new ImageIcon("src/resources/images/uphead.png");
                    head = iih.getImage();
                    
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }

                if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && (!upDirection)) {
                	
                	final ImageIcon iih = new ImageIcon("src/resources/images/downhead.png");
                    head = iih.getImage();
                    
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            } 
            else {

                if (key == KeyEvent.VK_SPACE) {
                	
                	final ImageIcon iih = new ImageIcon("src/resources/images/righthead.png");
                    head = iih.getImage();
                    
                    inGame = true;
                    inMenu = false;
                    onFire = false;
                    upDirection = false;
                    downDirection = false;
                    leftDirection = false;
                    rightDirection = true;
                    timer.stop();
                    initGame();
                }
            }
        }
    }

    public class MouseInput implements MouseListener {

        @Override
        public void mouseClicked(final MouseEvent e) {

        }

        @Override
        public void mousePressed(final MouseEvent e) {
        	
            if (inMenu == true) {
                final int mx = e.getX();
                final int my = e.getY();
//                System.out.println(mx);
//                System.out.println(my);

                if (mx >= 110 && mx <= 380) {
                    if (my >= 240 && my <= 300) {
                        inGame = true;
                        inMenu = false;
                        typeG = 1;
                        initGame();
                    }
                }

                if (mx >= 110 && mx <= 380) {
                    if (my >= 320 && my <= 380) {
                        inGame = true;
                        inMenu = false;
                        typeG = 2;
                        initGame();
                    }
                }
            }
        }

        @Override
        public void mouseReleased(final MouseEvent e) {

        }

        @Override
        public void mouseEntered(final MouseEvent e) {

        }

        @Override
        public void mouseExited(final MouseEvent e) {
    		
    	}
    }    
}