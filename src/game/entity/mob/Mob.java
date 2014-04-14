package game.entity.mob;

import game.Constants;
import game.Game;
import game.entity.Entity;
import game.item.weapon.Weapon;
import game.math.Vec2D;

import java.awt.image.BufferedImage;

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
	
	public Mob(double x, double y, String spriteName, int spriteWidth, int spriteHeight, 
			int maxHealth, double maxMovementSpeed, boolean solid, MobHandler handler) {
		
		super(x, y, Constants.DEFAULT_ENTITY_WIDTH, Constants.DEFAULT_ENTITY_HEIGHT, 
				solid, spriteName, spriteWidth, spriteHeight);
		
		hurtTime = 0;
		hurtDuration = Constants.MOB_HURT_TIME;
		
		this.maxHealth = maxHealth;
		currentHealth = maxHealth;
		
		state = MobState.Idle;
		
		this.maxMovementSpeed = maxMovementSpeed;
		
		this.handler = handler;
		handler.setMob(this);
	}
	
	public Mob(Mob other) {
		super(other);
		
		this.hurtTime=other.hurtTime;
		this.hurtDuration=other.hurtDuration;
		
		this.maxHealth=other.maxHealth;
		this.currentHealth=other.currentHealth;
		
		this.state=other.state;
		
		this.maxMovementSpeed=other.maxMovementSpeed;
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
		Vec2D v = new Vec2D();
		
		if(handler.isAttacking() && weapon != null) {
			setState(MobState.Attacking);
			weapon.attack(this, game, dt);
			
		} else if(handler.isMoving()) {
			int dx = handler.getDX();
			int dy = handler.getDY();
			setDirection(dx, dy);
			v = new Vec2D(dx, dy);
			v.setLength(maxMovementSpeed*dt);	
		}
		
		setVelocity(v);
		animate(dt);
		collisionResponse(collisionDetection(game));
		updatePosition();
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
	
}
