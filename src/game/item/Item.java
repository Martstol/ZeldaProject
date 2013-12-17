package game.item;

import game.Game;
import game.entity.Entity;

import java.awt.image.BufferedImage;

public abstract class Item {
	
	protected String name;
	protected String description;
	protected int maxStackSize;
	protected int stackSize;
	protected boolean usable;
	
	public Item(String name, String description, int stackSize, int maxStackSize, boolean usable) {
		this.name=name;
		this.description=description;
		this.stackSize=stackSize;
		this.usable=usable;
		this.maxStackSize=maxStackSize;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name + ": " + stackSize + " / " + maxStackSize;
	}
	
	public int getMaxStackSize() {
		return maxStackSize;
	}
	
	public void setMaxStackSize(int size) {
		maxStackSize=size;
		if(stackSize>maxStackSize) {
			stackSize=size;
		}
	}
	
	public int getStackSize() {
		return stackSize;
	}
	
	/**
	 * 
	 * @param amount
	 * @return returns the amount that was actually added to the stack, taking max stack size into consideration.
	 */
	public int add(int amount) {
		if(amount <= 0) {return 0;}
		int oldStackSize=stackSize;
		stackSize+=amount;
		if(stackSize>maxStackSize || stackSize<0) {stackSize=maxStackSize;}
		return stackSize-oldStackSize;
	}
	
	/**
	 * 
	 * @param amount
	 * @return returns the amount that was actually removed from the stack, taking into consideration it can't be below 0.
	 */
	public int remove(int amount) {
		if(amount<=0) {return 0;}
		int oldStackSize=stackSize;
		stackSize-=amount;
		if(stackSize<0) {stackSize=0;}
		return oldStackSize-stackSize;
	}
	
	public void setStackSize(int size) {
		if(size<0) {stackSize=0;}
		else if(size>maxStackSize) {stackSize=maxStackSize;}
		else {stackSize=size;}
	}
	
	public void setUsable(boolean usable) {
		this.usable=usable;
	}
	
	public boolean isUsable() {
		return usable;
	}
	
	public abstract BufferedImage getSprite(int dir);
	public abstract void use(Entity usedBy, Game game);

}
