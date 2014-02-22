package game.container.quadtree;

import game.algorithms.collision.AABB;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Quad {
	
	private static final int QUAD_CAPACITY = 16;
	private static final int MAX_LEVEL = 20;
	
	private static final int NW=0, NE=1, SW=2, SE=3;
	
	private int level;
	private List<AABB> aabbs;
	private AABB boundary;
	private Quad[] children;
	private boolean split;
	
	public Quad(AABB boundary) {
		this(0, boundary);
	}
	
	private Quad(int level, AABB boundary) {
		this.level=level;
		aabbs=new LinkedList<>();
		this.boundary = boundary;
		children=new Quad[4];
		split=false;
	}
	
	public void clear() {
		aabbs.clear();
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
		double w=0.5*boundary.getWidth();
		double h=0.5*boundary.getHeight();
		
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
	 * @param a - the AABB to test for
	 * @return the Quad the entity a belongs to
	 */
	private int getIndex(AABB a) {
		if(!split) {
			throw new RuntimeException("Quad is not split.");
		} else if(children[NW].boundary.contains(a)) {
			return NW;
		} else if(children[NE].boundary.contains(a)) {
			return NE;
		} else if(children[SW].boundary.contains(a)) {
			return SW;
		} else if(children[SE].boundary.contains(a)) {
			return SE;
		} else {
			return -1;
		}
	}
	
	public void insert(AABB a) {
		// If this Quad has any children (is split),
		// try to add the entity to the child Quads.
		if(split) {
			int i = getIndex(a);
			if(i!=-1) {
				children[i].insert(a);
				return;
			}
		}
		// This Quad had no children or the entity did not fit into any of the children.
		aabbs.add(a);
		
		// If this Quad has gone over the Quad capacity and it is not at max level,
		// we want to split if we haven't already and then move all of this Quad's
		// elements to the child Quads.
		if(aabbs.size() > QUAD_CAPACITY && level < MAX_LEVEL) {
			if(!split) {
				split();
			}
			
			Iterator<AABB> it = aabbs.iterator();
			while(it.hasNext()) {
				AABB aabb=it.next();
				int i=getIndex(aabb);
				if(i!=-1) {
					children[i].insert(aabb);
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
	public LinkedList<AABB> retrieve(AABB a) {
		LinkedList<AABB> list = new LinkedList<>();
		int i;
		if(split && (i=getIndex(a))!=-1) {
			list.addAll(children[i].retrieve(a));
		}
		list.addAll(aabbs);
		return list;
	}

}
