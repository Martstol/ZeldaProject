package game.container.quadtree;

import java.util.Iterator;
import java.util.LinkedList;

import game.algorithms.collision.AABB;
import game.entity.Entity;

public class Quad {
	
	private static final int QUAD_CAPACITY = 16;
	private static final int MAX_LEVEL = 20;
	
	private static final int NW=0, NE=1, SW=2, SE=3;
	
	private int level;
	private LinkedList<Entity> entities;
	private AABB boundary;
	private Quad[] children;
	private boolean split;
	
	public Quad(AABB boundary) {
		this(0, boundary);
	}
	
	private Quad(int level, AABB boundary) {
		this.level=level;
		entities=new LinkedList<>();
		this.boundary = boundary;
		children=new Quad[4];
		split=false;
	}
	
	public void clear() {
		entities.clear();
		if(split) {
			split=false;
			for(int i=0; i<children.length; i++) {
				children[i].clear();
				children[i]=null;
			}				
		}
	}
	
	private void split() {
		if(split) {
			throw new RuntimeException("Quad is already split.");
		}
		double x=boundary.getX();
		double y=boundary.getY();
		double w=boundary.getWidth()/2;
		double h=boundary.getHeight()/2;
		
		children[NW] = new Quad(level+1, new AABB(x, y, w, h));
		children[NE] = new Quad(level+1, new AABB(x+w, y, w, h));
		children[SW] = new Quad(level+1, new AABB(x, y+h, w, h));
		children[SE] = new Quad(level+1, new AABB(x+w, y+h, w, h));
		
		split = true;
		
	}
	
	/**
	 * Determine which child Quad the entity belongs to.
	 * -1 means the entity cannot completely fit within a child Quad.
	 * 
	 * @param e - the Entity to test for
	 * @return the Quad the entity e belongs to
	 */
	private int getIndex(Entity e) {
		if(!split) {
			throw new RuntimeException("Quad is not split.");
		} else if(children[NW].boundary.contains(e.getAABB())) {
			return NW;
		} else if(children[NE].boundary.contains(e.getAABB())) {
			return NE;
		} else if(children[SW].boundary.contains(e.getAABB())) {
			return SW;
		} else if(children[SE].boundary.contains(e.getAABB())) {
			return SE;
		} else {
			return -1;
		}
	}
	
	public void insert(Entity e) {
		// If this Quad has any children (is split),
		// try to add the entity to the child Quads.
		if(split) {
			int i = getIndex(e);
			if(i!=-1) {
				children[i].insert(e);
				return;
			}
		}
		// This Quad had no children or the entity did not fit into any of the children.
		entities.add(e);
		
		// If this Quad has gone over the Quad capacity and it is not at max level,
		// we want to split if we haven't already and then move all of this Quad's
		// elements to the child Quads.
		if(entities.size() > QUAD_CAPACITY && level < MAX_LEVEL) {
			if(!split) {
				split();
			}
			
			Iterator<Entity> it = entities.iterator();
			while(it.hasNext()) {
				Entity en=it.next();
				int i=getIndex(en);
				if(i!=-1) {
					children[i].insert(en);
					it.remove();
				}
			}
		}
	}
	
	/**
	 * Return a list of all Entities that could collide with the entity e.
	 * 
	 * @param e - Entity to check with
	 * @return list of possible collisions
	 */
	public LinkedList<Entity> retrieve(Entity e) {
		LinkedList<Entity> list = new LinkedList<>();
		int i;
		if(split && (i=getIndex(e))!=-1) {
			list.addAll(children[i].retrieve(e));
		}
		list.addAll(entities);
		return list;
	}

}
