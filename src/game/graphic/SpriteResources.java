package game.graphic;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class SpriteResources {
	
	private static SpriteResources library;
	public static SpriteResources getLibrary() {
		if (library==null) library=new SpriteResources();
		return library;
	}
	
	private HashMap<String, BufferedImage[][]> spritesets;
	
	public SpriteResources() {
		spritesets=new HashMap<String, BufferedImage[][]>();
	}
	
	public BufferedImage[][] requestSpriteset(String name, int spriteWidth, int spriteHeight, int scale) {
		if (!spritesets.containsKey(name)) {
			spritesets.put(name, SpriteLoader.cut(name, spriteWidth, spriteHeight, scale));
		}
		return spritesets.get(name);
	}
	
	public void releaseSpriteset(String name) {
		if (spritesets.containsKey(name)) {
			spritesets.remove(name);
		}
	}

}
