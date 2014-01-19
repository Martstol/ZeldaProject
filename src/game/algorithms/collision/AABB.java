package game.algorithms.collision;

import game.math.Vec2D;

import java.awt.geom.Rectangle2D;

public class AABB { // Axis Aligned Bounding Box
	private Vec2D position;
	private Vec2D dimensions;
	private boolean solid;
	
	public AABB(double x, double y, double width, double height) {
		this(x, y, width, height, true);
	}
	
	public AABB(double x, double y, double width, double height, boolean isSolid) {
		position=new Vec2D(x, y);
		dimensions=new Vec2D(width, height);
		solid=isSolid;
	}
	
	public AABB(AABB other) {
		position=new Vec2D(other.position);
		dimensions=new Vec2D(other.dimensions);
		this.solid=other.solid;
	}
	
	public double getX() {
		return position.getX();
	}
	
	public double getY() {
		return position.getY();
	}
	
	public double getWidth() {
		return dimensions.getX();
	}
	
	public double getHeight() {
		return dimensions.getY();
	}
	
	public Vec2D getCenter() {
		return new Vec2D(position.getX()+dimensions.getX()/2, position.getY()+dimensions.getY()/2);
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public void move(double dx, double dy) {
		position.add(dx, dy);
	}
	
	public void move(Vec2D delta) {
		position.add(delta);
	}
	
	/**
	 * Checks if this AABB, when translated by dx and dy intersects with the other AABB.
	 * 
	 * @param other - Check for intersection
	 * @param dx - Check for intersection when the x position of this AABB have changed this much 
	 * @param dy - Check for intersection when the y position of this AABB have changed this much
	 * @return If this AABB when translated by dx and dy intersects with the other AABB.
	 */
	public boolean intersects(AABB other, double dx, double dy) {
		return intersects(other.getX(), other.getY(), other.getWidth(), other.getHeight(), dx, dy);
	}
	
	public boolean intersects(double x, double y, double w, double h, double dx, double dy) {
		return new Rectangle2D.Double(getX()+dx, getY()+dy, getWidth(), getHeight()).intersects(x, y, w, h);
	}
	
	public boolean contains(AABB other) {
		return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight()).contains(other.getX(), other.getY(), other.getWidth(), other.getHeight());
	}
	
	@Override
	public String toString() {
		return "["+getX()+", "+getY()+", "+getWidth()+", "+getHeight()+"]";
	}
}
