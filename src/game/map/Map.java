package game.map;

import game.Constants;
import game.Game;
import game.algorithms.collision.AABB;
import game.container.quadtree.Quad;
import game.entity.Entity;
import game.entity.mob.Mob;
import game.entity.mob.npc.NpcHandler;
import game.sound.AudioPlayer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class Map {
	
	private String mapName;
	private int height;
	private int width;
	private Tile[] tiles;
	private ArrayList<Tileset> tilesets;
	private LinkedList<Entity> entities;
	private LinkedList<Entity> addEntities;
	private Quad tree;
	
	public Map(int width, int height, Mob player) {
		this.width=width;
		this.height=height;
		tiles=new Tile[width*height];
		tilesets=new ArrayList<Tileset>(1);
		entities=new LinkedList<>();
		addEntities=new LinkedList<>();
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
		for(int i=0; i<1; i++) {
			entities.add(new Mob(0, 0, "green knight", 18, 28, 2, 
					Constants.DEFAULT_ENTITY_MAX_VEL*0.8, false, new NpcHandler()));
		}
		AudioPlayer.getPlayer().playBgm("dark world");
	}
	
	public Quad getQuadTree() {
		return tree;
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
	
	public LinkedList<Entity> getEntities() {
		return entities;
	}
	
	public void tick(Game game, double dt) {
		entities.addAll(addEntities);
		addEntities.clear();
		
		tree = new Quad(new AABB(0, 0, width*Constants.TILE_WIDTH, height*Constants.TILE_HEIGHT));
		for(Entity e : entities) {
			tree.insert(e);
		}
		
		Iterator<Entity> it=entities.iterator();
		while(it.hasNext()) {
			Entity e = it.next();
			if(e.shouldBeRemoved()) {
				it.remove();
			} else {
				e.tick(game, dt);
			}
		}
	}

}
