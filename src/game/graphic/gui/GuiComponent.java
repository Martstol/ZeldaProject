package game.graphic.gui;

import game.io.Keys;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public abstract class GuiComponent {
	
	private int actionID=ActionEvent.ACTION_FIRST;
	private int x, y, width, height;
	private List<ActionListener> listeners;
	
	public GuiComponent() {
		x=y=width=height=0;
		listeners=new ArrayList<ActionListener>();
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setBounds(int x, int y, int width, int height) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public void setLocation(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public void setSize(int width, int height) {
		this.width=width;
		this.height=height;
	}
	
	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}
	
	public void removeActionListener(ActionListener listener) {
		listeners.remove(listener);
	}
	
	public void fireActionEvent(String command) {
		ActionEvent e = new ActionEvent(this, actionID, command);
		for(ActionListener l : listeners) {
			l.actionPerformed(e);
		}
		actionID=(actionID+1)%ActionEvent.ACTION_LAST;
	}

	public abstract void tick(Keys keys);
	public abstract void render(Graphics2D g);
}
