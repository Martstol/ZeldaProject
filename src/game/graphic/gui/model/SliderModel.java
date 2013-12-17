package game.graphic.gui.model;

public interface SliderModel {
	
	public void setValue(int newValue);
	public int getValue();
	public int getMinValue();
	public int getMaxValue();
	public int getStepValue();
	public void increment();
	public void decrement();

}
