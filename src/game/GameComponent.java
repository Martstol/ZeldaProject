package game;

import game.graphic.Screen;
import game.io.InputHandler;
import game.io.Keys;
import game.sound.AudioPlayer;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class GameComponent extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	private boolean running=false;
	private Thread t;
	
	private Screen screen;
	private Game game;

	public GameComponent() {
		setPreferredSize(Constants.SIZE);
		setMaximumSize(Constants.SIZE);
		setMinimumSize(Constants.SIZE);
		setBackground(Color.BLACK);
		setFocusable(true);
		
		Keys keys=new Keys();
		InputHandler inputHandler=new InputHandler(keys);
		addKeyListener(inputHandler);
		addFocusListener(inputHandler);
		
		game=new Game(keys);
		screen=new Screen(Constants.SIZE.width, Constants.SIZE.height);
	}
	
	public void start() {
		running=true;
		t=new Thread(this, "Game");
		t.start();
	}
	
	public void stop() {
		running=false;
		AudioPlayer.getPlayer().shutdown();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		Graphics2D g=(Graphics2D) bs.getDrawGraphics();
		g.clearRect(0, 0, getWidth(), getHeight());
		
		screen.render(game, g);
		
		g.dispose();
		bs.show();
	}
	
	public void tick() {
		game.tick(Constants.TARGET_TIME_BETWEEN_UPDATES*1E-9);
	}
	
	public void gameLoop() {
		double lastUpdateTime = System.nanoTime();
		
		while(running) {
			double now = System.nanoTime();
			int updateCount = 0;
			
			while((now - lastUpdateTime) > Constants.TARGET_TIME_BETWEEN_UPDATES 
					&& updateCount < Constants.MAX_UPDATES_BEFORE_RENDER) {
				
				tick();
				lastUpdateTime += Constants.TARGET_TIME_BETWEEN_UPDATES;
				updateCount++;
				
			}
			
			render();
			Thread.yield();
		}
	}
	
	@Override
	public void run() {
		createBufferStrategy(3);
		gameLoop();
	}
	
	public static void main(String[] args) {
		GameComponent game=new GameComponent();
		JFrame frame=new JFrame(Constants.GAME_TITLE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.setIgnoreRepaint(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game.start();
	}
}
