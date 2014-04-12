package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.entity.mob.Mob;
import game.entity.mob.PlayerHandler;
import game.graphic.gui.Button;
import game.graphic.gui.Gui;
import game.graphic.gui.Textbox;
import game.io.Keys;
import game.map.Map;

public class Game implements ActionListener {
	
	public enum GameState{Playing, ShowText, IngameMenu, MainMenu};
	
	private GameState state;
	private Mob player;
	private Gui gui;
	private Map map;
	
	public Game(Keys keys) {
		state=GameState.Playing;
		gui=new Gui(keys, this);
		
		//TODO This is just debug stuff
		player=new Mob(10, 0, Constants.PLAYER_SPRITESET_NAME, Constants.PLAYER_SPRITE_WIDTH, 
				Constants.PLAYER_SPRITE_HEIGHT, Constants.PLAYER_START_HEALTH, 
				Constants.PLAYER_MAX_VELOCITY, true, new PlayerHandler(keys));
		map=new Map(30, 30, player);
	}
	
	public Mob getPlayer() {
		return player;
	}
	
	public void tick(double dt) {
		switch(state) {
			case Playing:
				map.tick(this, dt);
				break;
			case MainMenu:
				gui.tick(dt);
				break;
			case ShowText:
				gui.tick(dt);
				break;
			case IngameMenu:
				gui.tick(dt);
				break;
			default:
				break;
		}
	}
	
	public Map getMap() {
		return map;
	}
	
	public GameState getState() {
		return state;
	}
	
	public Gui getGui() {
		return gui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO This is just debug code
		System.out.println(e.getSource()+". "+e.getActionCommand());
		if(e.getSource() instanceof Button) {
			state=GameState.ShowText;
			gui.displayText("We are no strangers to love\nYou know the rules, and so do I\nA true commitment is what I am thinking of\nYou wouldn't get this from any other guy\nAnd if you ask me how I am feeling\n"
					+ "Dont tell me you are to blind to see\nNever going to give you up\nNever going to let you down\nNever going to run around and desert you\n");			
		}
		if(e.getSource() instanceof Textbox) {
			gui.hideText();
			state=GameState.Playing;
		}
	}

}
