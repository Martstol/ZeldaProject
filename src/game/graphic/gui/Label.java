package game.graphic.gui;

import game.graphic.Screen;
import game.io.Keys;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

public class Label extends GuiComponent {
	
	private String text;
	private Paint p;
	
	public Label(int x, int y, String text) {
		this(x, y, text, Color.WHITE);
	}

	public Label(int x, int y, String text, Paint p) {
		setLocation(x, y);
		this.text=text;
		this.p=p;
	}

	@Override
	public void tick(Keys keys) {}

	@Override
	public void render(Graphics2D g) {
		g.setPaint(p);
		Rectangle2D r=Screen.font.getStringBounds(text, g.getFontRenderContext());
		g.drawString(text, getX(), (getY()-(int)r.getHeight())+1);
	}

}
