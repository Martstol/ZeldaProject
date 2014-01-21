package game.item.weapon;

import game.algorithms.collision.Collision;
import game.algorithms.collision.CollisionEvent;
import game.algorithms.collision.EntityCollision;
import game.entity.Entity;
import game.entity.mob.Mob;
import game.math.Vec2D;

public class BoomerangEntity extends ProjectileEntity {
	
	private double returnTime;
	
	public BoomerangEntity(double width, double height, double maxVelocity, String spriteName, int spriteWidth, int spriteHeight, double lifeTime, double returnTime) {
		super(width, height, maxVelocity, spriteName, spriteWidth, spriteHeight, lifeTime);
		this.returnTime=returnTime;
	}
	
	public BoomerangEntity(BoomerangEntity other) {
		super(other);
		this.returnTime=other.returnTime;
	}
	
	@Override
	public void setVelocity(int direction, double dt) {
		if(returnTime <= 0) {
			Vec2D dir = Vec2D.distanceVec(this.getCenterPos(), getSource().getCenterPos());
			dir.setLength(dt*this.getStraightMaxVelocity());
			setVelocity(dir);
		} else {
			super.setVelocity(getDirection(), dt);
			returnTime-=dt;
		}
	}
	
	@Override
	public void collisionResolution(CollisionEvent event) {
		if(event.collisionOccurred()) {
			for(Collision col : event) {
				if(col instanceof EntityCollision) {
					Entity e = ((EntityCollision)col).getEntity();
					
					if(e==getSource()) {
						markForRemoval();
					} else if(e instanceof Mob) {
						((Mob)e).damage(getWeapon(), this);
					}
				}
				returnTime=0;
			}
		}
	}
	
	@Override
	public ProjectileEntity copy() {
		BoomerangEntity cpy=new BoomerangEntity(this);
		return cpy;
	}

}
