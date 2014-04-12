package game.algorithms.collision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CollisionList implements Iterable<Collision> {
	
	private Collection<Collision> collisions;
	
	public CollisionList() {
		collisions=new ArrayList<Collision>();
	}
	
	public void add(Collision c) {
		collisions.add(c);
	}
	
	public void add(CollisionList l) {
		collisions.addAll(l.collisions);
	}

	public boolean collisionOccurred() {
		return !collisions.isEmpty();
	}
	
	@Override
	public Iterator<Collision> iterator() {
		return collisions.iterator();
	}

}
