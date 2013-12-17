package game.graphic.gui;

import game.io.Keys;

public class SelectionGroup extends GuiContainer<SelectableComponent> {
	
	private int index;
	private boolean wraparound;
	
	/*
	 * Put a delay on responding to input, or the menu will be moving too fast for comfort.
	 */
	private int inputDelay=4;
	private int currentDelay=0;
	
	public SelectionGroup() {
		this(false);
	}
	
	public SelectionGroup(boolean wraparound) {
		index=-1;
		this.wraparound=wraparound;
	}
	
	public void setSelected(int i) {
		if(index!=-1) {			
			getComponent(index).setSelected(false);
		}
		index=i;
		getComponent(index).setSelected(true);
	}
	
	@Override
	public void addComponent(SelectableComponent component) {
		super.addComponent(component);
		if(index==-1) {
			setSelected(0);
		}
	}
	
	@Override
	public void removeComponent(SelectableComponent component) {
		super.removeComponent(component);
		if(numberOfComponents()==0) {
			index=-1;
		}
	}
	
	public void selectNext() {
		if(wraparound) {
			setSelected((index+1)%numberOfComponents());					
		} else if(index!=numberOfComponents()-1) {
			setSelected(index+1);
		}
		currentDelay=inputDelay;
	}
	
	public void selectPrevious() {
		if(wraparound) {
			setSelected((index-1+numberOfComponents())%numberOfComponents());
			//setSelected(index==0 ? numberOfComponents()-1 : index-1);			
		} else if(index!=0) {
			setSelected(index-1);
		}
		currentDelay=inputDelay;
	}
	
	
	@Override
	public void tick(Keys keys) {
		super.tick(keys);
		if(currentDelay>0) {
			currentDelay--;
		} else {
			if(keys.down.isPressed()) {
				selectNext();
			}
			if(keys.up.isPressed()) {
				selectPrevious();
			}
		}
	}

}
