package game.map;

import game.Constants;
import game.graphic.SpriteResources;

import java.awt.image.BufferedImage;

public class Tileset {
	
	private String tilesetName;
	private String spritesetName;
	private BufferedImage[][] spriteset;
	private boolean[][] solid;
	private int width;
	private int height;
	
	public Tileset(String tilesetName, String spritesetName) {
		this.tilesetName=tilesetName;
		this.spritesetName=spritesetName;
		spriteset=SpriteResources.getLibrary().requestSpriteset(spritesetName, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, Constants.SPRITE_SCALE);
		width=spriteset[0].length;
		height=spriteset.length;
		solid=new boolean[height][width];
		
		//TODO debug
		solid[3][9]=true;
	}
	
	public boolean isSolid(int x, int y) {
		return solid[y][x];
	}
	
	public BufferedImage getSprite(int x, int y) {
		return spriteset[y][x];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

}
