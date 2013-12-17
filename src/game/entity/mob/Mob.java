package game.entity.mob;

import game.Constants;
import game.Game;
import game.entity.Entity;
import game.item.weapon.Weapon;
import game.math.Vec2D;

public abstract class Mob extends Entity {
	
	public enum MobState{Idle, Attacking, Hurt;}
	
	private int maxHealth;
	private int currentHealth;
	
	private double hurtTime;
	private double hurtDuration=Constants.MOB_HURT_TIME;
	private double kbTime;
	private double kbDuration=Constants.MOB_KB_TIME;
	private boolean canBeKnocked;
	private Vec2D kbVec;
	
	private MobState state;
	
	public Mob(double x, double y, double maxVel, String spriteName, int spriteWidth, int spriteHeight, int maxHealth, boolean canBeKnocked) {
		super(x, y, maxVel, spriteName, spriteWidth, spriteHeight);
		hurtTime=0;
		kbTime=0;
		this.maxHealth=maxHealth;
		currentHealth=maxHealth;
		state=MobState.Idle;
		this.canBeKnocked=canBeKnocked;
	}
	
	public MobState getState() {
		return state;
	}
	
	public void setState(MobState state) {
		this.state=state;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getCurrentHealth() {
		return currentHealth;
	}
	
	public void setKnockbackVelocity(double dt) {
		Vec2D v = new Vec2D(kbVec);
		v.scale(dt);
		setVelocity(v);
	}
	
	public void damage(Weapon weapon, Entity source) {
		if(state!=MobState.Hurt) {
			if(canBeKnocked && weapon.getKnockbackPower()!=0) {
				kbVec=Vec2D.distanceVec(source.getCenterPos(), this.getCenterPos());
				kbVec.normalize();
				kbVec.scale(weapon.getKnockbackPower());
				kbTime=kbDuration;
			}
			
			state=MobState.Hurt;
			hurtTime=hurtDuration;
			currentHealth-=weapon.getDamage();
		}
		if(currentHealth<=0) {
			markForRemoval();
		}
	}
	
	@Override
	public void tick(Game game, double dt) {
		if(state==MobState.Hurt) {
			if(kbTime>0) {
				kbTime-=dt;
				setKnockbackVelocity(dt);
				collisionResolution(collisionDetection(game.getMap()));
				move();
			}
			if(hurtTime>0) {
				hurtTime-=dt;
			}
			if(hurtTime<=0) {
				state=MobState.Idle;
			}
			
		}
	}
}
