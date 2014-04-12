package game.item.weapon;

import game.Game;
import game.algorithms.collision.CollisionList;
import game.algorithms.collision.GameObject;
import game.entity.Entity;

public class ProjectileEntity extends Entity {
	
	private Weapon weapon;
	private Entity source;
	
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
	
	public void setSource(Entity e) {
		this.source=e;
	}
	
	public Entity getSource() {
		return source;
	}

	@Override
	public void tick(Game game, double dt) {
		updateAnimation(dt);
		move();
		lifeTime-=dt;
		if(lifeTime<=0) {
			markForRemoval();
		}
	}
	
	@Override
	public void collisionResponse(CollisionList event) {
		
	}
	
	public GameObject createCopy() {
		return new ProjectileEntity(this);
	}

}
