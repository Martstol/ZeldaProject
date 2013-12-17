package game.graphic.gui;

import game.Constants;
import game.io.Keys;
import game.sound.AudioPlayer;

import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Gui {
	
	private Keys keys;
	private Stack<GuiComponent> guiStack;
	private Textbox textbox;
	
	public Gui(Keys keys, ActionListener l) {
		this.keys=keys;
		guiStack=new Stack<GuiComponent>();
		textbox=new Textbox(5, (2*Constants.SIZE.height)/3+5, Constants.SIZE.width-10, (Constants.SIZE.height/3)-10, Constants.TEXTBOX_SKIN_RESOURCE);
		textbox.addActionListener(l);
		
		SelectionGroup bg=new SelectionGroup(true);
		
		Button b = new Button(100, 100, "Hello, World!");
		bg.addComponent(b);
		b.addActionListener(l);
		
		Button c=new Button(100, 140, "What the fuck?");
		bg.addComponent(c);
		c.addActionListener(l);
		c.setEnabled(false);
		
		Slider s=new Slider(100, 180, AudioPlayer.getPlayer().getBgmVolumeModel());
		bg.addComponent(s);
		
		guiStack.push(bg);
	}
	
	public void displayText(String s) {
		if(guiStack.isEmpty() || guiStack.peek()!=textbox) {
			textbox.printText(s);
			guiStack.push(textbox);				
		} else if(!guiStack.isEmpty() && guiStack.peek()==textbox) {
			throw new RuntimeException("Textbox is already being displayed.");
		}
	}
	
	public void hideText() {
		if(!guiStack.isEmpty() && guiStack.peek()==textbox) {
			guiStack.pop();			
		} else {
			throw new RuntimeException("Textbox is already hidden.");
		}
	}
	
	public void tick(double dt) { //TODO Use dt
		guiStack.peek().tick(keys);
	}
	
	public void render(Graphics2D g) {
		guiStack.peek().render(g);
	}

}
