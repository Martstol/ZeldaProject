package game.entity.mob;

import game.Constants;
import game.Game;
import game.algorithms.collision.AABB;
import game.algorithms.collision.BorderCollision;
import game.algorithms.collision.Collision;
import game.algorithms.collision.CollisionList;
import game.algorithms.collision.EntityCollision;
import game.algorithms.collision.TileCollision;
import game.entity.Entity;
import game.item.weapon.Weapon;
import game.map.Map;
import game.math.Vec2D;

import java.awt.image.BufferedImage;
import java.util.Collection;

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
	
	@Override
	public CollisionList collisionDetection(Game game) {
		CollisionList list = new CollisionList();
		
		list.add(borderCollision(game));
		list.add(tileCollision(game));		
		list.add(entityCollision(game));
		
		return list;
	}
	
	public CollisionList tileCollision(Game game) {
		CollisionList list = new CollisionList();
		
		Map map = game.getMap();
		
		int low_x = ((int)Math.floor(getX()))-1;
		int low_y = ((int)Math.floor(getY()))-1;
		
		int high_x = ((int)Math.ceil(getX()+getWidth()))+1;
		int high_y = ((int)Math.ceil(getY()+getHeight()))+1;
		
		if(low_x < 0) low_x=0;
		if(low_y < 0) low_y=0;
		if(high_x >= map.getWidth()) high_x=map.getWidth();
		if(high_y >= map.getHeight()) high_y=map.getHeight();
		
		for(int i=low_y; i<high_y; i++) {
			for(int j=low_x; j<high_x; j++) {
				if(map.isSolid(j, i)) {
					AABB other = new AABB(j, i, 1, 1);
					if(other.intersects(getAABB(), getVel())) {
						list.add(new TileCollision(other));
					}
				}
			}
		}
		
		return list;
	}
	
	public CollisionList borderCollision(Game game) {
		CollisionList list = new CollisionList();
		Vec2D newPos = Vec2D.add(getPos(), getVel());
		int mapWidth = game.getMap().getWidth();
		int mapHeight = game.getMap().getHeight();
		
		if(newPos.getX() < 0) {
			list.add(new BorderCollision(BorderCollision.BorderId.WEST, mapWidth, mapHeight));
		} else if(newPos.getX()+getWidth() > mapWidth) {
			list.add(new BorderCollision(BorderCollision.BorderId.EAST, mapWidth, mapHeight));
		}
		
		if(newPos.getY() < 0) {
			list.add(new BorderCollision(BorderCollision.BorderId.NORTH, mapWidth, mapHeight));
		} else if(newPos.getY()+getHeight() > mapHeight) {
			list.add(new BorderCollision(BorderCollision.BorderId.SOUTH, mapWidth, mapHeight));
		}
		
		return list;
	}
	
	public CollisionList entityCollision(Game game) {
		CollisionList list = new CollisionList();
		
		if(isSolid()) {			
			Collection<Entity> potentials = game.getMap().getQuadTree().retrieve(this);
			for(Entity e : potentials) {
				if(e != this && e.isSolid() && e.getAABB().intersects(getAABB(), getVel())) {
					list.add(new EntityCollision(e));
				}
			}
		}
		
		return list;
	}

	@Override
	public void collisionResponse(CollisionList list) {
		AABB aabb = getAABB();
		double x = aabb.getX();
		double y = aabb.getY();
		double w = aabb.getWidth();
		double h = aabb.getHeight();
		
		double vx = getVx();
		double vy = getVy();
		
		for(Collision c : list) {
			AABB other = c.getAABB();
			
			if(other.intersects(x+vx, y, w, h)) {
				if(vx < 0) {
					vx = (other.getX()+other.getWidth()) - x;
				} else if(vx > 0) {
					vx = other.getX() - (x+w);
				}
			}
			
			if(other.intersects(x, y+vy, w, h)) {
				if(vy < 0) {
					vy = (other.getY()+other.getHeight()) - y;
				} else if(vy > 0) {
					vy = other.getY() - (y+h);
				}
			}
			
		}
		
		setVelocity(vx, vy);
	}
	
}
