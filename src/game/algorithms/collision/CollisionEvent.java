package game.algorithms.collision;

import java.util.ArrayList;
import java.util.Iterator;

public class CollisionEvent implements Iterable<Collision> {
	
	private ArrayList<Collision> collisions;
	
	public CollisionEvent() {
		collisions=new ArrayList<Collision>(2);
	}
	
	public void add(Collision c) {
		collisions.add(c);
	}
	
	public void add(CollisionEvent event) {
		collisions.addAll(event.collisions);
	}

	public boolean collisionOccurred() {
		return !collisions.isEmpty();
	}
	
	@Override
	public Iterator<Collision> iterator() {
		return collisions.iterator();
	}

}
