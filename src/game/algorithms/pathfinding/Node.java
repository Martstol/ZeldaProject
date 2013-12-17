package game.algorithms.pathfinding;

import game.entity.Entity;

import java.awt.geom.Rectangle2D;

public class Node{
	
	public final int x, y;
	
	public Node(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	@Override
	public boolean equals(Object other) {
		return ((Node)other).x==this.x && ((Node)other).y==this.y;
	}
	
	@Override
	public int hashCode() {
		return x | (y<<16);
	}
	
	@Override
	public String toString() {
		return "["+x+", "+y+"]";
	}
	
	public boolean contains(Entity e) {
		return new Rectangle2D.Double(x, y, 1, 1).contains(e.getX(), e.getY(), e.getWidth(), e.getHeight());
	}
}
