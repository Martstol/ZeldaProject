package game.algorithms.collision;

import game.math.Vec2D;

public class AABB implements Comparable<AABB> { // Axis Aligned Bounding Box
	
	private Vec2D position;
	private Vec2D dimensions;
	
	public AABB(double x, double y, double width, double height) {
		position=new Vec2D(x, y);
		dimensions=new Vec2D(width, height);
	}
	
	public AABB(AABB other) {
		position=new Vec2D(other.position);
		dimensions=new Vec2D(other.dimensions);
	}
	
	public double getX() {
		return position.getX();
	}
	
	public double getY() {
		return position.getY();
	}
	
	public Vec2D getPos() {
		return position;
	}
	
	public Vec2D getCenter() {
		return new Vec2D(position.getX()+dimensions.getX()/2, position.getY()+dimensions.getY()/2);
	}
	
	public double getWidth() {
		return dimensions.getX();
	}
	
	public double getHeight() {
		return dimensions.getY();
	}
	
	public void move(double dx, double dy) {
		position.add(dx, dy);
	}
	
	public void move(Vec2D delta) {
		position.add(delta);
	}
	
	public void moveTo(Vec2D newPos) {
		move( Vec2D.sub( newPos, position ) );
	}
	
	public boolean intersects(AABB other) {
		return intersects(other.getX(), other.getY(), other.getWidth(), other.getHeight());
	}
	
	public boolean intersects(AABB other, Vec2D v) {
		return intersects(other.getX()+v.getX(), other.getY()+v.getY(), other.getWidth(), other.getHeight());
	}
	
	public boolean intersects(double x, double y, double w, double h) {
		return this.getX() < (x+w)
				&& x < (this.getX()+this.getWidth())
				&& this.getY() < (y+h)
				&& y < (this.getY()+this.getHeight());
	}
	
	public boolean contains(AABB other) {
		return this.getX() <= other.getX()
				&& this.getY() <= other.getY()
				&& this.getX()+this.getWidth() >= other.getX()+other.getWidth()
				&& this.getY()+this.getHeight() >= other.getY()+other.getHeight();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj  instanceof AABB) {
			AABB other = (AABB) obj;
			return this.position.equals(other.position) 
					&& this.dimensions.equals(other.dimensions);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return position.hashCode() ^ dimensions.hashCode();
	}
	
	@Override
	public String toString() {
		return "["+getX()+", "+getY()+", "+getWidth()+", "+getHeight()+"]";
	}

	@Override
	public int compareTo(AABB o) {
		if(this.getY()>o.getY()) {
			return 1;
		} else if(this.getY()<o.getY()) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
