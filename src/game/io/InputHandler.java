package game.io;

import game.io.Keys.Key;
import game.sound.AudioPlayer;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class InputHandler implements KeyListener, FocusListener {
	
	public static boolean aiPause=false, bgmPause=false, debug=false; //TODO DEBUG!
	private HashMap<Integer, Key> keybindings;
	private Keys keys;
	
	public InputHandler(Keys keys) {
		this.keys=keys;
		keybindings=new HashMap<Integer, Key>();
		
		keybindings.put(KeyEvent.VK_W, keys.up);
		keybindings.put(KeyEvent.VK_UP, keys.up);
		keybindings.put(KeyEvent.VK_S, keys.down);
		keybindings.put(KeyEvent.VK_DOWN, keys.down);
		keybindings.put(KeyEvent.VK_A, keys.left);
		keybindings.put(KeyEvent.VK_LEFT, keys.left);
		keybindings.put(KeyEvent.VK_D, keys.right);
		keybindings.put(KeyEvent.VK_RIGHT, keys.right);
		keybindings.put(KeyEvent.VK_K, keys.attack);
		keybindings.put(KeyEvent.VK_ENTER, keys.select);
		keybindings.put(KeyEvent.VK_BACK_SPACE, keys.back);
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		AudioPlayer.getPlayer().setPause(false);
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		keys.releaseAll();
		AudioPlayer.getPlayer().setPause(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_F3) {
			debug=!debug;
		}
		if(e.getKeyCode()==KeyEvent.VK_F2) {
			aiPause=!aiPause;
		}
		if(e.getKeyCode()==KeyEvent.VK_F1) {
			bgmPause=!bgmPause;
			AudioPlayer.getPlayer().setPause(bgmPause);
		}
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		if(keybindings.containsKey(e.getKeyCode())) {
			keybindings.get(e.getKeyCode()).press();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(keybindings.containsKey(e.getKeyCode())) {
			keybindings.get(e.getKeyCode()).release();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
