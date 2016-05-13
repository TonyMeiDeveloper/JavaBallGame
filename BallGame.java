
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;



/**
 * Name: Tony Mei
 * 
 * Details: Program is a ball clicking game. The ball drops down and you must click it to earn points. 
 * The more times the ball is clicked, the faster it goes. If the ball hits the ground, you lose points, 
 * and ball slows down to a certain point.  
 *
 */
public class BallGame extends JFrame
{

	public static void main(String[] args) {
        JFrame f = new JFrame("Click the Ball Game");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ballPanel panel = new ballPanel();
        f.add(panel, BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
	}
}




class ballPanel extends JPanel implements Runnable
{
	//Variables
	Thread anim;
	int ball_x, ball_y;
	int box_x, box_y;
	ScoreKeeper score;

	//Panel Size
	private static final int PWIDTH = 650; 
	private static final int PHEIGHT = 500;

	//How much faster ball drops
	private static int drop=15; 
	//Constructor
	public ballPanel()
	{
		super();
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
		//Starting positions for the ball
		ball_x = PWIDTH/2; ball_y = PHEIGHT/3;
		box_x = PWIDTH - 100; box_y = 100;
		//Adding score and mouse listeners
		score = new ScoreKeeper();
		mseL ml = new mseL();
		addMouseListener(ml);
		addMouseMotionListener(ml);
	}
	
	public void addNotify()
	{
		super.addNotify();
		anim = new Thread(this);
		anim.start();
	}
	//Running, updating, repainting
	public void run()
	{
		while(anim != null)
		{
			updateball();
			repaint();
			try{
				Thread.sleep(100);
			} catch (Exception e) {}
		}
	}

	public void updateball()
	{
		//This makes the ball go down 
		ball_y += drop;

		// If the ball touches the edge or ground, ball resets and score is deducted
		if (ball_x < 0 || ball_x > PWIDTH || ball_y < 0 || ball_y > PHEIGHT)
		{
			if(drop>15)
			{
				drop=drop-1; 
			}
			ball_x = PWIDTH/2; ball_y = PHEIGHT/3;
			score.minusScore();
		}

	}

	public void paintComponent(Graphics g)
	{
		//For background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, PWIDTH,  PHEIGHT);	
		//For the ball
		g.setColor(Color.GREEN);
		g.fillOval(ball_x-10, ball_y-10, 20, 20);
		//For the "mouse box"
		g.setColor(Color.GRAY);
		g.fillRect(box_x-20, box_y-20, 40, 40);
		score.draw(g);
	}
	//For the class that keeps track of score and high score
	public class ScoreKeeper {

		int score = 0;
		int highScore = 0;

		Font font;
		public ScoreKeeper(){
			font = new Font("SansSerif", Font.BOLD, 24);
		}

		public void draw(Graphics g){
			g.setColor(Color.black);
			g.setFont(font);
			g.drawString("Score: "+score, 50, 100);
			g.drawString("High Score: "+highScore, 50, 120);
		}

		//Increment score
		public void plusScore(){
			score++;
			if(score>highScore)
			{
				highScore++;
			}
		}
		//Decrement score. I would've set it back to zero, but I'm nice. 
		public void minusScore(){
			if(score!=0)
			{
				score--;
			}
		}

	}

	//Mouse Listeners
	class mseL extends MouseAdapter implements MouseMotionListener
	{
		public void mousePressed(MouseEvent e)
		{
			int x = e.getX();
			int y = e.getY();
			if (x-20 < ball_x && ball_x < x+20 &&
					y-20 < ball_y && ball_y < y+20)
			{
				// If clicked on
				score.plusScore();
				ball_y = PHEIGHT/3;
				drop=drop+1; 
				Random num= new Random();
				int num2 = num.nextInt(600 - 100) + 100;
				ball_x= num2;  
			}
		}

		public void mouseMoved(MouseEvent e)
		{
			int x = e.getX();
			int y = e.getY();
			box_x = x;
			box_y = y;
			repaint();

		}
	}
}