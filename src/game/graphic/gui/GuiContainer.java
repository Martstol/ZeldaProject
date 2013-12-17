package game.graphic.gui;

import game.io.Keys;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public abstract class GuiContainer<T extends GuiComponent> extends GuiComponent {

	private List<T> components;
	
	public GuiContainer() {
		components=new ArrayList<T>();
	}
	
	public void addComponent(T component) {
		components.add(component);
	}
	
	public void removeComponent(T component) {
		components.remove(component);
	}
	
	public T getComponent(int index) {
		return components.get(index);
	}
	
	public int numberOfComponents() {
		return components.size();
	}
	
	@Override
	public void tick(Keys keys) {
		for(GuiComponent c : components) {
			c.tick(keys);
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		for(GuiComponent c : components) {
			c.render(g);
		}
	}
}
