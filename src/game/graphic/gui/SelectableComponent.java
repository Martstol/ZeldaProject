package game.graphic.gui;

public abstract class SelectableComponent extends GuiComponent {

	private boolean selected;
	private boolean enabled;
	
	public SelectableComponent() {
		selected=false;
		enabled=true;
	}
	
	public void setEnabled(boolean enable) {
		enabled=enable;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setSelected(boolean select) {
		selected=select;
	}

	public boolean isSelected() {
		return selected;
	}

}
