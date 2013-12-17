package game.item.weapon;

import game.Game;
import game.algorithms.collision.Collision;
import game.algorithms.collision.CollisionEvent;
import game.algorithms.collision.EntityCollision;
import game.entity.Entity;
import game.entity.mob.Mob;
import game.math.Vec2D;

public class ProjectileEntity extends Entity {
	
	private Weapon weapon;
	private Entity source;
	
	private double lifeTime;
	private double returnTime;
	private boolean boomerangBehaviour;

	public ProjectileEntity(double width, double height, double maxVelocity, 
			String spriteName, int spriteWidth, int spriteHeight, double lifeTime) {
		this(width, height, maxVelocity, spriteName, spriteWidth, spriteHeight, lifeTime, 0.0, false);
		
	}
	
	public ProjectileEntity(double width, double height, double maxVelocity, String spriteName, 
			int spriteWidth, int spriteHeight, double lifeTime, double returnTime) {
		this(width, height, maxVelocity, spriteName, spriteWidth, spriteHeight, lifeTime, returnTime, true);
	}
	
	private ProjectileEntity(double width, double height, double maxVelocity, String spriteName,
			int spriteWidth, int spriteHeight, double lifeTime, double returnTime, boolean boomerangeBehaviour) {
		super(0, 0, width, height, false, maxVelocity, spriteName, spriteWidth, spriteHeight);
		this.lifeTime=lifeTime;
		this.returnTime=returnTime;
		this.boomerangBehaviour=boomerangeBehaviour;
	}
	
	public ProjectileEntity(ProjectileEntity other) {
		super(other);
		this.lifeTime=other.lifeTime;
		this.returnTime=other.returnTime;
		this.boomerangBehaviour=other.boomerangBehaviour;
		
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon=weapon;
	}
	
	public void setSource(Entity e) {
		this.source=e;
	}
	
	private void setBoomerangVelocity(double dt) {
		if(returnTime <= 0) {
			Vec2D dir = Vec2D.distanceVec(this.getCenterPos(), source.getCenterPos());
			dir.setLength(dt*this.getStraightMaxVelocity());
			setVelocity(dir);
		} else { 
			setVelocity(getDirection(), dt);
			returnTime-=dt;
		}
	}

	@Override
	public void tick(Game game, double dt) {
		if(boomerangBehaviour) {
			setBoomerangVelocity(dt);
			boomerangCollisionResolution(collisionDetection(game.getMap()));
		} else {
			setVelocity(getDirection(), dt);
			collisionResolution(collisionDetection(game.getMap()));
		}
		move();
		lifeTime-=dt;
		if(lifeTime<=0) {
			markForRemoval();
		}
	}
	
	public void boomerangCollisionResolution(CollisionEvent event) {
		if(event.collisionOccurred()) {
			for(Collision col : event) {
				if(col instanceof EntityCollision) {
					Entity e = ((EntityCollision)col).getEntity();
					
					if(e==source) {
						markForRemoval();
					} else if(e instanceof Mob) {
						((Mob)e).damage(weapon, this);
					}
				}
				returnTime=0;
			}
		}
	}
	
	@Override
	public void collisionResolution(CollisionEvent event) {
		if(event.collisionOccurred()) {
			markForRemoval();
			for(Collision col : event) {
				if(col instanceof EntityCollision) {
					Entity e = ((EntityCollision)col).getEntity();
					if(e!=source && e instanceof Mob) {
						((Mob)e).damage(weapon, this);
					}
				}
			}
		}
	}

}
