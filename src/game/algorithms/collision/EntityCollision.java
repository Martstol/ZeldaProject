package game.algorithms.collision;

import game.entity.Entity;

public class EntityCollision extends Collision {
	
	private Entity e;

	public EntityCollision(Entity e) {
		super(e.getAABB());
		this.e=e;
	}
	
	public Entity getEntity() {
		return e;
	}

}
