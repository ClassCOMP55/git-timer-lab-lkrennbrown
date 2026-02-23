import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Timer;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class DodgeBall extends GraphicsProgram implements ActionListener {
	private ArrayList<GOval> balls;
	private ArrayList<GRect> enemies;
	private GLabel text;
	private Timer movement;
	private RandomGenerator rgen;
	private int numTimes;
	private int destroyed;
	private GLabel destroyedLabel;
	
	public static final int SIZE = 25;
	public static final int SPEED = 15;
	public static final int MS = 20;
	public static final int MAX_ENEMIES = 10;
	public static final int WINDOW_HEIGHT = 600;
	public static final int WINDOW_WIDTH = 300;
	
	public void run() {
		rgen = RandomGenerator.getInstance();
		numTimes = 0;
		destroyed = 0;
		destroyedLabel = new GLabel("Destroyed: 0", 0, 20);
		add(destroyedLabel);
		balls = new ArrayList<GOval>();
		enemies = new ArrayList<GRect>();
		
		
		text = new GLabel(""+enemies.size(), 0, WINDOW_HEIGHT);
		add(text);
		
		movement = new Timer(MS, this);
		movement.start();
		addMouseListeners();
	}
	
	public void actionPerformed(ActionEvent e) {
		numTimes++;
		moveAllBallsOnce();
		moveAllEnemiesOnce();
		checkCollisions();
		if (numTimes % 40 == 0) {
			addAnEnemy();
		}
	}
	
	public void mousePressed(MouseEvent e) {
		for(GOval b:balls) {
			if(b.getX() < SIZE * 2.5) {
				return;
			}
		}
		addABall(e.getY());     
	}
	
	private void addABall(double y) {
		GOval ball = makeBall(SIZE/2, y);
		add(ball);
		balls.add(ball);
	}
	
	public GOval makeBall(double x, double y) {
		GOval temp = new GOval(x-SIZE/2, y-SIZE/2, SIZE, SIZE);
		temp.setColor(Color.RED);
		temp.setFilled(true);
		return temp;
	}
	
	private void addAnEnemy() {
		GRect e = makeEnemy(rgen.nextInt(0, WINDOW_HEIGHT-SIZE/2));
		enemies.add(e);
		text.setLabel("" + enemies.size());
		add(e);
	}
	
	public GRect makeEnemy(double y) {
		GRect temp = new GRect(WINDOW_WIDTH-SIZE, y-SIZE/2, SIZE, SIZE);
		temp.setColor(Color.GREEN);
		temp.setFilled(true);
		return temp;
	}

	private void moveAllBallsOnce() {
		for(GOval ball:balls) {
			ball.move(SPEED, 0);
		}
	}
	
	private void moveAllEnemiesOnce() {
		for(GRect enemy : enemies) {
			enemy.move(0, rgen.nextInt(-2, 2));
		}
	}
	
	private void checkCollisions() {
		for(GOval ball : balls) {
			GObject hit = getElementAt(ball.getX() + SIZE, ball.getY() + SIZE/2);
			GObject hit2 = getElementAt(ball.getX() + SIZE, ball.getY());
			GObject hit3 = getElementAt(ball.getX() + SIZE, ball.getY() + SIZE);
			if(hit instanceof GRect) {
				remove(hit);
				enemies.remove(hit);
				destroyed++;
				destroyedLabel.setLabel("Destroyed: " + destroyed);
			} else if(hit2 instanceof GRect) {
				remove(hit2);
				enemies.remove(hit2);
				destroyed++;
				destroyedLabel.setLabel("Destroyed: " + destroyed);
			} else if(hit3 instanceof GRect) {
				remove(hit3);
				enemies.remove(hit3);
				destroyed++;
				destroyedLabel.setLabel("Destroyed: " + destroyed);
			}
		}
	}
	
	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	}
	
	public static void main(String args[]) {
		new DodgeBall().start();
	}
}
