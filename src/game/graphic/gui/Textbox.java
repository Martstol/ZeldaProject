package game.graphic.gui;

import game.Constants;
import game.graphic.SpriteResources;
import game.io.Keys;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.image.BufferedImage;

public class Textbox extends GuiComponent {
	
	private String[] text;
	private BufferedImage[][] skin;
	private int currentLine;
	private int numberOfLines;
	private Paint paint;
	private int maxIODelay;
	private int currentIODelay;
	private int animationLength;
	private int animationStep;
	private int currentAnimationDelay;
	private int maxAnimationDelay;
	private boolean tickKey;
	
	public Textbox(int x, int y, int width, int height, String skinName) {
		setBounds(x, y, width, height);
		this.skin=SpriteResources.getLibrary().requestSpriteset(skinName, 8, 8, Constants.SPRITE_SCALE);
		currentLine=0;
		numberOfLines=4;
		paint=Color.WHITE;
		maxIODelay=6;
		currentIODelay=maxIODelay;
		animationStep=0;
		animationLength=2;
		maxAnimationDelay=15;
		currentAnimationDelay=maxAnimationDelay;
		tickKey=false;
	}
	
	public void printText(String text) {
		this.text=text.split("\n");
		currentLine=0;
	}

	@Override
	public void tick(Keys keys) {
		if(keys.select.isPressed()) {
			tickKey=true;
		} else {
			tickKey=false;
		}
		if(text!=null) {
			if(keys.select.isPressed() && currentLine<text.length && currentIODelay<=0) {
				currentLine++;
				currentIODelay=maxIODelay;
			} else if(currentIODelay>0) {
				currentIODelay--;
			}
			if(currentLine>text.length-numberOfLines) {
				fireActionEvent("Text complete.");
			}
			if(currentAnimationDelay<=0) {
				animationStep=(animationStep+1)%animationLength;
				currentAnimationDelay=maxAnimationDelay;
			}
			currentAnimationDelay--;
		}
	}

	@Override
	public void render(Graphics2D g) {
		int x=getX();
		int y=getY();
		int w=getWidth();
		int h=getHeight();
		
		int s=8*Constants.SPRITE_SCALE;
		
		// Render background
		g.drawImage(skin[1][1], x, y, w, h, null);
		
		// Render corners
		g.drawImage(skin[0][0], x, y, null);
		g.drawImage(skin[0][2], x+w-s, y, null);
		g.drawImage(skin[2][0], x, y+h-s, null);
		g.drawImage(skin[2][2], x+w-s, y+h-s, null);
		
		// Render edges
		g.drawImage(skin[0][1], x+s, y, w-2*s, s, null);
		g.drawImage(skin[2][1], x+s, y+h-s, w-2*s, s, null);
		g.drawImage(skin[1][0], x, y+s, s, h-2*s, null);
		g.drawImage(skin[1][2], x+w-s, y+s, s, h-2*s, null);
		
		g.setPaint(paint);
		Shape clip=g.getClip();
		g.setClip(x+s, y+s-8, w-2*s, h-2*s+16);
		double f=currentIODelay/(double)maxIODelay;
		for(int i=-1; i<numberOfLines; i++) {
			int l=i+currentLine;
			if(l==-1) {
				continue;
			} else if(l>=text.length) {
				break;
			} else {
				g.drawString(text[l], x+s, y+40*i+48+Math.round(f*40));				
			}
		}
		g.setClip(clip);
		
		if(currentIODelay<=0 && !tickKey) {
			g.drawImage(skin[0][3+animationStep], x+(w-s)/2, y+h-s+1, null);
		}
	}

}
