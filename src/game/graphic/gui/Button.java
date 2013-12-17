package game.graphic.gui;

import game.graphic.Screen;
import game.io.Keys;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

public class Button extends SelectableComponent {
	
	public static final String BUTTON_PRESSED="Button pressed.";
	public static final Paint DEFAULT_PAINT=Color.WHITE;
	public static final Paint DISABLED_PAINT=Color.LIGHT_GRAY;
	
	private String text;
	private Paint enabledPaint;
	private Paint disabledPaint;
	
	/*
	 * Put a delay on responding to input, or the menu will be moving too fast for comfort.
	 */
	private int inputDelay=4;
	private int currentDelay=0;
	
	public Button(int x, int y, String text) {
		this(x, y, text, DEFAULT_PAINT, DISABLED_PAINT);
	}
	
	public Button(int x, int y, String text, Paint enabled, Paint disabled) {
		setLocation(x, y);
		this.text=text;
		this.enabledPaint=enabled;
		this.disabledPaint=disabled;
	}

	@Override
	public void tick(Keys keys) {
		if(currentDelay>0) {
			currentDelay--;
		} else {
			if(keys.select.isPressed() && isSelected() && isEnabled()) {
				fireActionEvent(BUTTON_PRESSED);
				currentDelay=inputDelay;
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		if(isEnabled()) {
			g.setPaint(enabledPaint);			
		} else {
			g.setPaint(disabledPaint);
		}
		Rectangle2D r=Screen.font.getStringBounds(text, g.getFontRenderContext());
		g.drawString(text, getX(), (getY()-(int)r.getHeight())+1);
		if(isSelected()) {
			g.fillRect(getX()-1, (getY()-(int)r.getHeight())+1, (int)r.getWidth()+1, 2);
		}
	}

}
