package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
	private boolean play = false;
	private int score = 0;

	private int totalBricks = 21;

	private Timer timer;
	private int delay = 8;

	private int playerX = 310;

	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -2;
	private int ballYdir = -3;

	private BrickGenerator map_layout;

	public Gameplay() {
		map_layout = new BrickGenerator(3, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();

	}

	public void paint(Graphics g) {
		// background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);

		// drawing map
		map_layout.draw((Graphics2D) g);

		// start of the game instructions

		if (!play && ballposY <= 570 && totalBricks > 0) {
			g.setColor(Color.CYAN);
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Play with left and right arrow keys", 200, 530);
		}

		// borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(0, 0, 3, 592);

		// score
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("" + score, 590, 30);

		// the paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);

		// the ball
		g.setColor(Color.pink);
		g.fillOval(ballposX, ballposY, 20, 20);

		// GameOver

		if (ballposY > 570) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You lose || Your Score: " + score, 200, 300);

			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 260, 350);

		}

		// Game is won

		if (totalBricks <= 0) {
			{
				play = false;
				ballXdir = 0;
				ballYdir = 0;
				g.setColor(Color.red);
				g.setFont(new Font("serif", Font.BOLD, 30));
				g.drawString("Super! You have won!", 150, 300);

				g.setFont(new Font("serif", Font.BOLD, 20));
				g.drawString("Press Enter to Restart", 260, 350);

			}
		}

		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if (play) {
			if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
				ballYdir = -ballYdir;
			}

			A: for (int i = 0; i < map_layout.map.length; i++) {
				for (int j = 0; j < map_layout.map[0].length; j++) {
					if (map_layout.map[i][j] > 0) {
						int brickX = j * map_layout.brickWidth + 80;
						int brickY = i * map_layout.brickHeight + 50;
						int brickWidth = map_layout.brickWidth;
						int brickHeight = map_layout.brickHeight;

						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;

						if (ballRect.intersects(brickRect)) {
							map_layout.setBrickValue(0, i, j);
							totalBricks--;
							score += 5;
							if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							} else {
								ballYdir = -ballYdir;
							}
							break A;
						}
					}
				}
			}

			ballposX += ballXdir;
			ballposY += ballYdir;
			if (ballposX < 0) {
				ballXdir = -ballXdir;
			}
			if (ballposY < 0) {
				ballYdir = -ballYdir;
			}
			if (ballposX > 670) {
				ballXdir = -ballXdir;
			}
		}
		repaint();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (playerX >= 600) {
				playerX = 600;
			} else {
				moveRight();
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (playerX < 10) {
				playerX = 10;
			} else {
				moveLeft();
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!play) {
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -2;
				ballYdir = -3;
				playerX = 310;
				score = 0;
				totalBricks = 21;
				map_layout = new BrickGenerator(3, 7);

				repaint();
			}
		}

	}

	public void moveRight() {
		play = true;
		playerX += 20;
	}

	public void moveLeft() {
		play = true;
		playerX -= 20;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
