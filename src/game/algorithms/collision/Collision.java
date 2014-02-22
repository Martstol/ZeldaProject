package game.algorithms.collision;

import game.entity.Entity;


public class Collision {
	
	public static Collision createEntityCollision(Entity e) {
		return new Collision(e, CollisionType.Entity);
	}
	
	public static Collision createTileCollision(double x, double y) {
		return new Collision(new AABB(x, y, 1, 1), CollisionType.Tile);
	}
	
	public static Collision createBoundaryCollision(double x, double y) {
		return new Collision(new AABB(x, y, 1, 1), CollisionType.Boundary);
	}
	
	public enum CollisionType {Entity, Tile, Boundary}
	
	private AABB aabb;
	private CollisionType type;
	
	private Collision(AABB aabb, CollisionType type) {
		this.aabb = aabb;
		this.type = type;
	}
	
	public CollisionType getCollisionType() {
		return type;
	}
	
	public AABB getAABB() {
		return aabb;
	}

}
