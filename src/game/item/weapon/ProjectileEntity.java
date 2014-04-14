package game.item.weapon;

import game.Game;
import game.algorithms.collision.Collision;
import game.algorithms.collision.CollisionList;
import game.algorithms.collision.EntityCollision;
import game.entity.Entity;
import game.entity.mob.Mob;

public class ProjectileEntity extends Entity {
	
	private Weapon weapon;
	
	private double lifeTime;
	
	private double maxVelocity;

	public ProjectileEntity(double width, double height, double maxVelocity, 
			String spriteName, int spriteWidth, int spriteHeight, double lifeTime) {
		super(width, height, spriteName, spriteWidth, spriteHeight);
		this.lifeTime=lifeTime;
		this.maxVelocity=maxVelocity;
	}
	
	public ProjectileEntity(ProjectileEntity other) {
		super(other);
		this.lifeTime=other.lifeTime;
		this.maxVelocity=other.maxVelocity;
		
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon=weapon;
	}
	
	public double getMaxVelocity() {
		return maxVelocity;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}

	@Override
	public void tick(Game game, double dt) {
		animate(dt);
		updatePosition();
		lifeTime-=dt;
		if(lifeTime<=0) {
			markForRemoval();
		}
	}
	
	@Override
	public void collisionResponse(CollisionList list) {
		for(Collision col : list) {
			markForRemoval();
			
			if(col instanceof EntityCollision) {
				Entity e = ((EntityCollision)col).getEntity();
				if(e instanceof Mob) {
					((Mob)e).damage(weapon, this);
				}
			}
		}
	}

}
