package game.algorithms.collision;

import game.entity.Entity;

import java.util.ArrayList;
import java.util.Iterator;

public class CollisionEvent implements Iterable<Collision> {
	
	private ArrayList<Collision> collisions;
	
	public CollisionEvent() {
		collisions=new ArrayList<Collision>(2);
	}
	
	public int getNumberOfCollisions() {
		return collisions.size();
	}
	
	public Collision getCollision(int i) {
		return collisions.get(i);
	}
	
	public void addEntity(Entity e) {
		collisions.add(new EntityCollision(e));
	}
	
	public void addTile(int x, int y) {
		collisions.add(new TileCollision(x, y));
	}
	
	public void addEvent(CollisionEvent event) {
		collisions.addAll(event.collisions);
	}

	@Override
	public Iterator<Collision> iterator() {
		return collisions.iterator();
	}
	
	public boolean collisionOccurred() {
		return collisions.size()!=0;
	}

}
