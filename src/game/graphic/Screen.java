package game.graphic;

import game.Constants;
import game.Game;
import game.entity.Entity;
import game.entity.mob.Player;
import game.io.InputHandler;
import game.map.Map;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class Screen {
	
	private int width, height;
	private int tileWidth=Constants.TILE_WIDTH*Constants.SPRITE_SCALE;
	private int tileHeight=Constants.TILE_HEIGHT*Constants.SPRITE_SCALE;
	private int playerWidth=Constants.PLAYER_SPRITE_WIDTH*Constants.SPRITE_SCALE;
	private int playerHeight=Constants.PLAYER_SPRITE_HEIGHT*Constants.SPRITE_SCALE;
	private int nTilesWidth; //Number of tiles in the width
	private int nTilesHeight; //Number of tiles in the height
	public static final Font font;
	
	static {
		InputStream stream = null;
		try {
			stream=Screen.class.getResourceAsStream(Constants.FONT_RESOURCE);
			font=Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(32f);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
		} catch (FontFormatException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if(stream!=null) {				
				try {
					stream.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public Screen(int width, int height) {
		this.width=width;
		this.height=height;
		this.nTilesWidth=width/tileWidth+1;
		this.nTilesHeight=height/tileHeight+1;
	}
	
	public void render(Game game, Graphics2D g) {
		g.setFont(font);
		switch(game.getState()) {
		case Playing: case IngameMenu: case ShowText:
			renderGame(game, g);
			break;
		case MainMenu:
			game.getGui().render(g);
			break;
		default:
			break;
		}
		
	}
	
	private void renderGame(Game game, Graphics2D g) {
		//Get the data from the game
		Map map=game.getMap();
		Player player=game.getPlayer();
		double playerX=player.getX();
		double playerY=player.getY();
		int mapWidth=map.getWidth();
		int mapHeight=map.getHeight();
		
		//Calculate offset
		int xULCorner=(int)(playerX-nTilesWidth/2); //The upper left corner of the map that should be rendered. X coordinate.
		int yULCorner=(int)(playerY-nTilesHeight/2); //The upper left corner of the map that should be rendered. Y coordinate.
		if(xULCorner<0) {
			xULCorner=0;
		} else if (xULCorner>mapWidth-nTilesWidth) {
			xULCorner=mapWidth-nTilesWidth;
		}
		if(yULCorner<0) {
			yULCorner=0;
		} else if (yULCorner>mapHeight-nTilesHeight) {
			yULCorner=mapHeight-nTilesHeight;
		}
		
		int xOffset=(int)(playerX*tileWidth)-(width-playerWidth)/2-tileWidth;
		int yOffset=(int)(playerY*tileHeight)-(height-playerHeight)/2-tileHeight;
		if(xOffset<0) {
			xOffset=0;
		} else if(xOffset>mapWidth*tileWidth-width) {
			xOffset=mapWidth*tileWidth-width;
		}
		if(yOffset<0) {
			yOffset=0;
		} else if(yOffset>mapHeight*tileHeight-height) {
			yOffset=mapHeight*tileHeight-height;
		}
		
		//Rendering
		for(int y=yULCorner; y<yULCorner+nTilesHeight+1; ++y) {
			int yScreen=y*tileHeight-yOffset;
			if (yScreen<=-tileHeight || yScreen>=height) continue;
			for (int x=xULCorner; x<xULCorner+nTilesWidth+1; ++x) {
				int xScreen=x*tileWidth-xOffset;
				if (xScreen<=-tileWidth || xScreen>=width) continue;
				g.drawImage(map.getSprite(x, y), xScreen, yScreen, null);
			}
		}
		
		ArrayList<Entity> entities=new ArrayList<>(map.getEntities().size());
		for(Entity e : map.getEntities()) {
			entities.add(e); //TODO Only add visible entities
		}
		Collections.sort(entities);
		for(Entity e : entities) {
			g.drawImage(e.getSprite(), (int)(e.getSpriteX()*tileWidth)-xOffset, (int)(e.getSpriteY()*tileHeight)-yOffset, null);
			if(InputHandler.debug) {
				g.setColor(Color.RED);
				g.fillRect((int)(e.getX()*tileWidth)-xOffset, (int)(e.getY()*tileHeight)-yOffset, (int)(e.getWidth()*tileWidth), (int)(e.getHeight()*tileHeight));
			}
		}
		
		//TODO Render the GUI
		
		if(game.getState()==Game.GameState.ShowText) {
			game.getGui().render(g);
		}
		
		if(InputHandler.debug) {
			g.setColor(Color.RED);
			g.drawLine(400, 0, 400, 600);
			g.drawLine(0, 300, 800, 300);
			g.drawString("x: "+player.getX(), 8, 60);
			g.drawString("y: "+player.getY(), 8, 100);
		}
	}
}
