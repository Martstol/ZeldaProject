package game;

import java.awt.BorderLayout;

import javax.swing.JApplet;

public class GameApplet extends JApplet {
	
	private static final long serialVersionUID = 1L;
	
	private GameComponent game;

	public void init() {
		game=new GameComponent();
		setSize(Constants.SIZE);
		setLayout(new BorderLayout());
		add(game, BorderLayout.CENTER);
		
	}
	
	public void start() {
		game.start();
	}
	
	public void stop() {
		game.stop();
	}

}
