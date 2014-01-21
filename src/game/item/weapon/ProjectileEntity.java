package game.item.weapon;

import game.Game;
import game.algorithms.collision.Collision;
import game.algorithms.collision.CollisionEvent;
import game.algorithms.collision.EntityCollision;
import game.entity.Entity;
import game.entity.mob.Mob;

public class ProjectileEntity extends Entity {
	
	private Weapon weapon;
	private Entity source;
	
	private double lifeTime;

	public ProjectileEntity(double width, double height, double maxVelocity, 
			String spriteName, int spriteWidth, int spriteHeight, double lifeTime) {
		super(width, height, maxVelocity, spriteName, spriteWidth, spriteHeight);
		this.lifeTime=lifeTime;
	}
	
	public ProjectileEntity(ProjectileEntity other) {
		super(other);
		this.lifeTime=other.lifeTime;
		
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon=weapon;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public void setSource(Entity e) {
		this.source=e;
	}
	
	public Entity getSource() {
		return source;
	}

	@Override
	public void tick(Game game, double dt) {
		animate(true, dt);
		setVelocity(getDirection(), dt);
		collisionResolution(collisionDetection(game.getMap()));
		move();
		lifeTime-=dt;
		if(lifeTime<=0) {
			markForRemoval();
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
	
	public ProjectileEntity copy() {
		ProjectileEntity cpy=new ProjectileEntity(this);
		return cpy;
	}

}
