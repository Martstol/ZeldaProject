package game.algorithms.collision;

public class BorderCollision extends Collision {
	
	public enum BorderId{NORTH, SOUTH, EAST, WEST;}

	public BorderCollision(BorderId id, int mapWidth, int mapHeight) {
		super(createBorderAABB(id, mapWidth, mapHeight));
	}
	
	private static AABB createBorderAABB(BorderId id, int w, int h) {
		switch (id) {
		case NORTH:
			return new AABB(0, -1, w, 1);
		case SOUTH:
			return new AABB(0, h, w, 1);
		case WEST:
			return new AABB(-1, 0, 1, h);
		case EAST:
			return new AABB(w, 0, 1, h);
		default:
			throw new RuntimeException("Border id did not match an actual border.");
		}
	}

}
