package game.map;

import game.Constants;
import game.Game;
import game.entity.Entity;
import game.entity.mob.Player;
import game.entity.mob.npc.PathFindingNpc;
import game.sound.AudioPlayer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Map {
	
	private String mapName;
	private int height;
	private int width;
	private Tile[] tiles;
	private ArrayList<Tileset> tilesets;
	private LinkedList<Entity> entities;
	private LinkedList<Entity> addEntities;
	
	public Map(int width, int height, Player player) {
		this.width=width;
		this.height=height;
		tiles=new Tile[width*height];
		tilesets=new ArrayList<Tileset>(1);
		entities=new LinkedList<Entity>();
		addEntities=new LinkedList<Entity>();
		entities.add(player);
		
		//TODO DEBUG
		Tileset tileset=new Tileset("Forest", "light forest");
		tilesets.add(tileset);
		Random random=new Random(0);
		for(int y=0; y<height; ++y) {
			for(int x=0; x<width; ++x) {
				int w=random.nextInt(tileset.getWidth()+1);
				if (w==tileset.getWidth()) {
					tiles[x+y*width]=new Tile(0, 9, 3);
				} else {
					tiles[x+y*width]=new Tile(0, w, 0);
				}
			}
		}
		entities.add(new PathFindingNpc(0, 0, Constants.DEFAULT_ENTITY_MAX_VEL*0.8, "green knight", 18, 28, 20, 1, 12, 1));
		AudioPlayer.getPlayer().playBgm("dark world");
	}
	
	public void addEntity(Entity e) {
		addEntities.add(e);
	}
	
	public void removeEntity(Entity e) {
		e.markForRemoval();
	}
	
	public BufferedImage getSprite(int x, int y) {
		try{
			Tile t=tiles[x+y*width];
			return tilesets.get(t.tileset).getSprite(t.x, t.y);			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(x+", "+y+", "+width);
			System.out.println(tiles.length);
			throw (ArrayIndexOutOfBoundsException)e;
		}
	}
	
	public boolean isSolid(int x, int y) {
		Tile t=tiles[x+y*width];
		return tilesets.get(t.tileset).isSolid(t.x, t.y);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public void tick(Game game, double dt) {
		if(!addEntities.isEmpty()) {
			entities.addAll(addEntities);
			addEntities=new LinkedList<Entity>();
		}
		for(Iterator<Entity> it=entities.iterator(); it.hasNext();) {
			Entity e=it.next();
			if(e.shouldBeRemoved()) {
				it.remove();
			} else {
				e.tick(game, dt);
			}
		}
	}

}
