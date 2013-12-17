package game.algorithms.collision;


public class TileCollision extends Collision {
	
	private AABB aabb;
	
	public TileCollision(int x, int y) {
		aabb=new AABB(x, y, 1, 1);
	}
	
	@Override
	public AABB getAABB() {
		return aabb;
	}

}
