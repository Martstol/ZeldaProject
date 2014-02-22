package game.entity.mob;

import java.awt.image.BufferedImage;

import game.Constants;
import game.Game;
import game.algorithms.collision.CollisionEvent;
import game.entity.Entity;
import game.item.weapon.Weapon;
import game.math.Vec2D;

public class Mob extends Entity {
	
	public enum MobState{Idle, Attacking, Hurt;}
	
	private int maxHealth;
	private int currentHealth;
	
	private double hurtTime;
	private double hurtDuration;
	
	private MobState state;
	
	private double maxMovementSpeed;
	
	private MobHandler handler;
	
	private Weapon weapon;
	
	public Mob(double x, double y, String spriteName, int spriteWidth, int spriteHeight, int maxHealth, double maxMovementSpeed, MobHandler handler) {
		super(x, y, spriteName, spriteWidth, spriteHeight);
		
		hurtTime = 0;
		hurtDuration = Constants.MOB_HURT_TIME;
		
		this.maxHealth = maxHealth;
		currentHealth = maxHealth;
		
		state = MobState.Idle;
		
		this.maxMovementSpeed = maxMovementSpeed;
		
		this.handler = handler;
		handler.setMob(this);
	}
	
	@Override
	public BufferedImage getSprite() {
		switch(state) {
		case Idle:
			return super.getSprite();
		case Attacking:
			return weapon.getSprite(getDirection());
		case Hurt:
			return super.getSprite();
		default:
			return super.getSprite();
		}
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
	public MobState getState() {
		return state;
	}
	
	public void setState(MobState state) {
		this.state = state;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getCurrentHealth() {
		return currentHealth;
	}
	
	public void damage(Weapon weapon, Entity source) {
		if(state != MobState.Hurt) {
			state = MobState.Hurt;
			hurtTime = hurtDuration;
			currentHealth -= weapon.getDamage();
		}
		if(currentHealth<=0) {
			markForRemoval();
		}
	}
	
	private void attackingTick(Game game, double dt) {
		weapon.attack(this, game, dt);
		if (!weapon.isAttacking()) {
			setState(MobState.Idle);
		}
	}
	
	private void hurtTick(Game game, double dt) {
		hurtTime -= dt;
		if(hurtTime <= 0) {
			setState(MobState.Idle);
		}
	}
	
	private void idleTick(Game game, double dt) {
		handler.tick(game, dt);
		
		if(handler.requestingAttack() && weapon != null) {
			setState(MobState.Attacking);
			weapon.attack(this, game, dt);
			
		} else if(handler.requestingMovement()) {
			int dx = handler.getDX();
			int dy = handler.getDY();
			
			updateAnimation(dt);
			setDirection(dx, dy);
			Vec2D v = new Vec2D(dx, dy);
			v.setLength(maxMovementSpeed*dt);
			setVelocity(v);
			move();
			collisionResolution(collisionDetection(game.getMap()));
			
		} else { // Handler isn't making any requests
			resetAnimation();
		}
	}
	
	@Override
	public void tick(Game game, double dt) {
		switch(state) {
		case Idle:
			idleTick(game, dt);
			break;
		case Attacking:
			attackingTick(game, dt);
			break;
		case Hurt:
			hurtTick(game, dt);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void collisionResolution(CollisionEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
