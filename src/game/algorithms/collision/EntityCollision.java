package game.algorithms.collision;

import game.entity.Entity;

public class EntityCollision extends Collision {
	
	private Entity entity;
	
	public EntityCollision(Entity entity) {
		this.entity=entity;
	}
	
	public Entity getEntity() {
		return entity;
	}

	@Override
	public AABB getAABB() {
		return entity.getAABB();
	}
	
	

}
