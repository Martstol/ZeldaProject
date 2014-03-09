package game.container.quadtree;

import game.algorithms.collision.AABB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Quad<T extends AABB> {
	
	private static final int QUAD_CAPACITY = 16;
	private static final int MAX_LEVEL = 20;
	
	private static final int NW=0, NE=1, SW=2, SE=3, INVALID=-1;
	
	private int level;
	private AABB boundary;
	private Collection<T> content;
	private List<Quad<T>> children;
	private boolean split;
	
	public Quad(AABB boundary) {
		this(0, boundary);
	}
	
	private Quad(int level, AABB boundary) {
		split = false;
		this.level = level;
		this.boundary = boundary;
		children = new ArrayList<>(4);
		content = new ArrayList<>(QUAD_CAPACITY);
	}
	
	private void split() {
		if(split) {
			throw new RuntimeException("Quad is already split.");
		}
		
		double x=boundary.getX();
		double y=boundary.getY();
		double w=0.5*boundary.getWidth();
		double h=0.5*boundary.getHeight();
		
		children.add(new Quad<T>(level+1, new AABB(x, y, w, h))); // NW
		children.add(new Quad<T>(level+1, new AABB(x+w, y, w, h))); // NE
		children.add(new Quad<T>(level+1, new AABB(x, y+h, w, h))); // SW
		children.add(new Quad<T>(level+1, new AABB(x+w, y+h, w, h))); // SE
		
		split = true;
	}
	
	public int size() {
		int s = content.size();
		if(split) {
			s += children.get(NW).size();
			s += children.get(NE).size();
			s += children.get(SW).size();
			s += children.get(SE).size();
		}
		return s;
	}
	
	public void clear() {
		split = false;
		children = new ArrayList<>(4);
		content = new ArrayList<>(QUAD_CAPACITY);
	}
	
	private int getIndex(T a) {
		if(!split) {
			throw new RuntimeException("Quad is not split.");
		} else if(children.get(NW).boundary.contains(a)) {
			return NW;
		} else if(children.get(NE).boundary.contains(a)) {
			return NE;
		} else if(children.get(SW).boundary.contains(a)) {
			return SW;
		} else if(children.get(SE).boundary.contains(a)) {
			return SE;
		} else {
			return INVALID;
		}
	}
	
	public void insertAll(Collection<T> col) {
		for(T a : col) {
			insert(a);
		}
	}
	
	public void insert(T a) {
		// If this Quad has any children (is split),
		// try to add the entity to the child Quads.
		if(split) {
			int i = getIndex(a);
			if(i != INVALID) {
				children.get(i).insert(a);
				return;
			}
		}
		// This Quad had no children or the entity did not fit into any of the children.
		content.add(a);
		
		// If this Quad has gone over the Quad capacity and it is not at max level,
		// we want to split if we haven't already and then move all of this Quad's
		// elements to the child Quads.
		if(!split && content.size() > QUAD_CAPACITY && level < MAX_LEVEL) {
			split();
			
			Iterator<T> it = content.iterator();
			while(it.hasNext()) {
				T t = it.next();
				int i=getIndex(t);
				if(i!=INVALID) {
					children.get(i).insert(t);
					it.remove();
				}
			}
			
		}
	}
	
	public Collection<T> retrieve(T a) {
		Collection<T> col = new ArrayList<>();
		
		if(split) {
			int i=getIndex(a);
			if(i == INVALID) {
				col.addAll(children.get(NW).content);
				col.addAll(children.get(NE).content);
				col.addAll(children.get(SW).content);
				col.addAll(children.get(SE).content);
			} else {
				col.addAll(children.get(i).retrieve(a));				
			}
		}
		col.addAll(content);
		
		return col;
	}

}
