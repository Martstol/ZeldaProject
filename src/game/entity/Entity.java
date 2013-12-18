package game.entity;

import game.Constants;
import game.Game;
import game.algorithms.collision.AABB;
import game.algorithms.collision.Collision;
import game.algorithms.collision.CollisionEvent;
import game.graphic.SpriteResources;
import game.map.Map;
import game.math.GameMath;
import game.math.Vec2D;

import java.awt.image.BufferedImage;

public abstract class Entity implements Comparable<Entity> {
	
	private double mv, mvv; // mv = straight max velocity, mvv = diagonal max velocity
	
	private BufferedImage[][] spriteset;
	private AABB aabb; // Axis Aligned Bounding Box
	private Vec2D spritePos;
	private Vec2D vel;
	
	private int direction;
	private int animationStep;
	private int animationLength;
	private double animationDelayLength;
	private double currentDelayTime;
	
	private boolean remove=false; // The entity will be removed from the game if this variable is set to true.
	
	public Entity(double x, double y, String spriteName, int spriteWidth, int spriteHeight) {
		this(x, y, Constants.DEFAULT_ENTITY_MAX_VEL, spriteName, spriteWidth, spriteHeight);
	}
	
	public Entity(double x, double y, double maxVelocity, String spriteName, int spriteWidth, int spriteHeight) {
		this(x, y, Constants.DEFAULT_ENTITY_WIDTH, Constants.DEFAULT_ENTITY_HEIGHT, maxVelocity, spriteName, spriteWidth, spriteHeight);
	}
	
	public Entity(double x, double y, double width, double height, double maxVelocity, String spriteName, int spriteWidth, int spriteHeight) {
		this(x, y, width, height, true, maxVelocity, spriteName, spriteWidth, spriteHeight);
	}
	
	public Entity(double x, double y, double width, double height, boolean solid, double maxVelocity, String spriteName, int spriteWidth, int spriteHeight) {
		this(x, y, width, height, solid, maxVelocity, spriteName, spriteWidth, spriteHeight, Constants.DEFAULT_ENTITY_ANIMATION_DELAY);
	}
	
	public Entity(double x, double y, double width, double height, boolean solid, double maxVelocity, String spriteName, int spriteWidth, int spriteHeight, double animationDelayLength) {
		aabb=new AABB(x, y, width, height, solid);
		mv=maxVelocity;
		mvv=mv/GameMath.sqrt2;
		vel=new Vec2D();
		double spriteX=(x + 0.5*width - spriteWidth/(2.0*Constants.TILE_WIDTH));
		double spriteY=(y + 0.5*height - spriteHeight/(2.0*Constants.TILE_HEIGHT));
		spritePos=new Vec2D(spriteX, spriteY);
		spriteset=SpriteResources.getLibrary().requestSpriteset(spriteName, spriteWidth, spriteHeight, Constants.SPRITE_SCALE);
		direction=Direction.SOUTH;
		animationStep=0;
		animationLength=spriteset[0].length;
		this.animationDelayLength=animationDelayLength;
		currentDelayTime=0;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param other - Entity to copy
	 */
	public Entity(Entity other) {
		mv=other.mv;
		mvv=other.mvv;
		
		aabb=new AABB(other.aabb);
		spritePos=new Vec2D(other.spritePos);
		spriteset=other.spriteset;
		vel=new Vec2D(other.vel);
		
		direction=other.direction;
		animationStep=other.animationStep;
		animationLength=other.animationLength;
		animationDelayLength=other.animationDelayLength;
		currentDelayTime=other.currentDelayTime;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public double getStraightMaxVelocity() {
		return mv;
	}
	
	public double getDiagonalMaxVelocity() {
		return mvv;
	}
	
	public double getX() {
		return aabb.getX();
	}
	
	public double getY() {
		return aabb.getY();
	}
	
	public Vec2D getPos() {
		return new Vec2D(aabb.getX(), aabb.getY());
	}
	
	public Vec2D getCenterPos() {
		return aabb.getCenter();
	}
	
	public double getWidth() {
		return aabb.getWidth();
	}
	
	public double getHeight() {
		return aabb.getHeight();
	}
	
	public double getSpriteX() {
		return spritePos.getX();
	}
	
	public double getSpriteY() {
		return spritePos.getY();
	}
	
	public double getVx() {
		return vel.getX();
	}
	
	public double getVy() {
		return vel.getY();
	}
	
	public Vec2D getVel() {
		return new Vec2D(vel);
	}
	
	public void setVelocity(Vec2D v) {
		vel.set(v);
	}
	
	public boolean isSolid() {
		return aabb.isSolid();
	}
	
	public AABB getAABB() {
		return aabb;
	}
	
	public BufferedImage getSprite() {
		return spriteset[direction][animationStep];
	}
	
	public void setDirection(int direction) {
		this.direction=direction%4;
	}
	
	public void setDirection(int dx, int dy) {
		if (direction==Direction.SOUTH) {
			if (dy==-1) {
				direction=Direction.NORTH;
			} else if (dx==1 && dy!=1) {
				direction=Direction.EAST;
			} else if (dx==-1 && dy!=1) {
				direction=Direction.WEST;
			}
		} else if(direction==Direction.NORTH) {
			if (dy==1) {
				direction=Direction.SOUTH;
			} else if (dx==1 && dy!=-1) {
				direction=Direction.EAST;
			} else if (dx==-1 && dy!=-1) {
				direction=Direction.WEST;
			}
		} else if(direction==Direction.EAST) {
			if (dx==-1) {
				direction=Direction.WEST;
			} else if (dy==1 && dx!=1) {
				direction=Direction.SOUTH;
			} else if (dy==-1 && dx!=1) {
				direction=Direction.NORTH;
			}
		} else if(direction==Direction.WEST) {
			if (dx==1) {
				direction=Direction.EAST;
			} else if (dy==1 && dx!=-1) {
				direction=Direction.SOUTH;
			} else if (dy==-1 && dx!=-1) {
				direction=Direction.NORTH;
			}
		}
	}
	
	public void collisionResolution(CollisionEvent event) {
		for(Collision col : event) {
			AABB other = col.getAABB();
			
			if(aabb.intersects(other, vel.getX(), 0)) {
				if(vel.getX()>0) {
					vel.setX(other.getX()-(aabb.getX()+aabb.getWidth()));
				} else {
					vel.setX((other.getX()+other.getWidth())-aabb.getX());
				}
			}
			
			if (aabb.intersects(other, 0, vel.getY())) {
				if(vel.getY()>0) {
					vel.setY(other.getY()-(aabb.getY()+aabb.getHeight()));
				} else {
					vel.setY((other.getY()+other.getHeight())-aabb.getY());
				}
			}
			
			if (aabb.intersects(other, vel.getX(), vel.getY())) {
				if(vel.getX()>0) {
					vel.setX(other.getX()-(aabb.getX()+aabb.getWidth()));
				} else {
					vel.setX((other.getX()+other.getWidth())-aabb.getX());
				} // TODO This have to be fixed
				if(vel.getY()>0) {
					vel.setY(other.getY()-(aabb.getY()+aabb.getHeight()));
				} else {
					vel.setY((other.getY()+other.getHeight())-aabb.getY());
				}
				
			}
		}
	}
	
	public CollisionEvent collisionDetection(Map map) {
		CollisionEvent event=new CollisionEvent();
		event.addEvent(borderCollisions(map));
		event.addEvent(tileCollisions(map));
		event.addEvent(entityCollisions(map));
		
		return event;
	}
	
	public CollisionEvent borderCollisions(Map map) {
		CollisionEvent event=new CollisionEvent();
		
		if(getX()+vel.getX()<0) {
			event.addTile(-1, (int)getY());
		}else if(getX()+getWidth()+vel.getX()>map.getWidth()) {
			event.addTile(map.getWidth(), (int)getY());
		}
		
		if(getY()+vel.getY()<0) {
			event.addTile((int)getX(), -1); 
		}else if(getY()+getHeight()+vel.getY()>map.getHeight()) {
			event.addTile((int)getX(), map.getHeight());
		}
		
		return event;
	}
	
	public CollisionEvent tileCollisions(Map map) {
		CollisionEvent event = new CollisionEvent();
			
		// lower y coordinate
		int yl=((int)aabb.getY())-1;
		if(yl<0) {yl=0;}
		
		// upper y coordinate
		int yu=((int)(aabb.getY()+aabb.getHeight()))+2;
		if(yu>map.getHeight()) {yu=map.getHeight();}
		
		// lower x coordinate
		int xl=((int)aabb.getX())-1;
		if(xl<0) {xl=0;}
		
		// upper x coordinate
		int xu=((int)(aabb.getX()+aabb.getWidth()))+2;
		if(xu>map.getWidth()) {xu=map.getWidth();} 
		
		for(int y=yl; y<yu; ++y) {
			for (int x = xl; x <xu ; ++x) {
				if(map.isSolid(x, y) && aabb.intersects(x, y, 1, 1, vel.getX(), vel.getY())) {
					event.addTile(x, y);
				}
			}
		}
			
		return event;
	}
	
	public CollisionEvent entityCollisions(Map map) {
		CollisionEvent event = new CollisionEvent();
		for(Entity e : map.getEntities()) {
			if(this!=e && e.isSolid() && aabb.intersects(e.getAABB(), vel.getX(), vel.getY())) {
				event.addEntity(e);
			}
		}
		return event;
	}
	
	private void move(Vec2D trans) {
		spritePos.add(trans);
		aabb.move(trans);
	}
	
	public void move() {
		move(vel);
	}
	
	public void moveTo(Vec2D newPos) {
		move(Vec2D.sub(newPos, aabb.getCenter()));
	}
	
	public void animate(boolean moving, double dt) {
		currentDelayTime+=dt;
		if (moving) {
			if (currentDelayTime>=animationDelayLength) {
				currentDelayTime-=animationDelayLength;
				animationStep=(animationStep+1)%animationLength;
			}
		} else {
			currentDelayTime=0;
			animationStep=0;
		}
	}
	
	public void setVelocity(int dx, int dy, double dt) {
		if(dx != 0 || dy != 0) {
			vel.set(dx*mv, dy*mv);
			vel.setLength(mv*dt);
		} else {
			vel.set(0);
		}
	}
	
	public void setVelocity(int direction, double dt) {
		switch(direction) {
		case Direction.DOWN:
			vel.set(0, mv);
			break;
		case Direction.UP:
			vel.set(0, -mv);
			break;
		case Direction.LEFT:
			vel.set(-mv, 0);
			break;
		case Direction.RIGHT:
			vel.set(mv, 0);
			break;
		default:
			vel.set(0);
			break;
		}
		vel.scale(dt);
	}
	
	public boolean intersects(Entity other) {
		return aabb.intersects(other.getAABB(), 0, 0);
	}
	
	public boolean collidesWith(Entity other) {
		return this.isSolid() && other.isSolid() && aabb.intersects(other.getAABB(), vel.getX(), vel.getY());
	}
	
	/**
	 * Returns true if this entity should be removed from the game.
	 * 
	 * @return If this entity should be removed from the game.
	 */
	public boolean shouldBeRemoved() {
		return remove;
	}
	
	/**
	 * Mark this entity to be removed from the game.
	 */
	public void markForRemoval() {
		remove=true;
	}
	
	@Override
	public int compareTo(Entity o) {
		if(this.getSpriteY()>o.getSpriteY()) {
			return 1;
		} else if(this.getSpriteY()<o.getSpriteY()) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public abstract void tick(Game game, double dt);

}
