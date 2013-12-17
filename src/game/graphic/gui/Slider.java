package game.graphic.gui;

import game.graphic.gui.model.SliderModel;
import game.io.Keys;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;

public class Slider extends SelectableComponent {
	
	public static final int DEFAULT_WIDTH=240;
	public static final int DEFAULT_HEIGHT=28;
	public static final Paint DEFAULT_BG=Color.DARK_GRAY;
	public static final Paint DEFAULT_FG=Color.BLUE;
	public static final Paint SELECT_HL=Color.WHITE;
	public static final Paint DISABLED_HL=Color.LIGHT_GRAY;
	
	private SliderModel model;
	private Paint bg;
	private Paint fg;
	private Paint hl;
	private Paint ds;
	
	public Slider(int x, int y, SliderModel model) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, model, DEFAULT_BG, DEFAULT_FG, SELECT_HL, DISABLED_HL);
	}
	
	public Slider(int x, int y, int width, int height, SliderModel model, Paint bg, Paint fg, Paint hl, Paint ds) {
		setBounds(x, y, width, height);
		this.model=model;
		this.bg=bg;
		this.fg=fg;
		this.hl=hl;
		this.ds=ds;
	}

	@Override
	public void tick(Keys keys) {
		if(isSelected() && isEnabled()) {
			if(keys.left.isPressed()) {
				model.decrement();
			}
			if(keys.right.isPressed()) {
				model.increment();
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		double r=model.getValue()/(double)(model.getMaxValue()-model.getMinValue());
		int x=getX();
		int y=getY();
		int w=getWidth();
		int h=getHeight();
		int w1=(int)Math.round(w*r);
		
		g.setPaint(fg);
		g.fillRect(x, y, w1, h);
		g.setPaint(bg);
		g.fillRect(x+w1, y, w-w1, h);
		g.setPaint(Color.WHITE);
		
		if(isEnabled()) {
			g.setPaint(hl);				
		} else {
			g.setPaint(ds);
		}
		String txt=" "+Math.round(r*100);
		g.drawString(txt, x+w+2, y+h);
		if(isSelected()) {
			g.drawRect(x-1, y-1, w+1, h+1);
		}
	}

}
