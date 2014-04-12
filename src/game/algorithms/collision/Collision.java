package game.algorithms.collision;

public abstract class Collision {
	
	private AABB aabb;
	
	public Collision(AABB aabb) {
		this.aabb = aabb;
	}
	
	public AABB getAABB() {
		return aabb;
	}

}
