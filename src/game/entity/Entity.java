package game.entity;

import game.Constants;
import game.Game;
import game.algorithms.collision.AABB;
import game.algorithms.collision.CollisionList;
import game.graphic.SpriteResources;
import game.math.Vec2D;

import java.awt.image.BufferedImage;

public abstract class Entity implements Comparable<Entity> {
	
	private AABB aabb;
	private Vec2D vel;
	private boolean solid;
	
	private BufferedImage[][] spriteset;
	private Vec2D spritePos;
	
	private int direction;
	
	private int currentAnimationStep;
	private int animationLength;
	private double currentAnimationDelay;
	private double animationDelayLength;
	
	private boolean remove=false; // The entity will be removed from the game if this variable is set to true.
	
	public Entity(double x, double y, String spriteName, int spriteWidth, int spriteHeight) {
		this(x, y, Constants.DEFAULT_ENTITY_WIDTH, Constants.DEFAULT_ENTITY_HEIGHT, spriteName, spriteWidth, spriteHeight);
	}
	
	public Entity(double x, double y, double width, double height, String spriteName, int spriteWidth, int spriteHeight) {
		this(x, y, width, height, true, spriteName, spriteWidth, spriteHeight);
	}
	
	public Entity(double x, double y, double width, double height, boolean solid, String spriteName, int spriteWidth, int spriteHeight) {
		this(x, y, width, height, solid, spriteName, spriteWidth, spriteHeight, Constants.DEFAULT_ENTITY_ANIMATION_DELAY);
	}
	
	public Entity(double x, double y, double width, double height, boolean solid, String spriteName, int spriteWidth, int spriteHeight, double animationDelayLength) {
		this.solid = solid;
		aabb = new AABB(x, y, width, height);
		vel=new Vec2D();
		
		double spriteX=(x + 0.5*width - spriteWidth/(2.0*Constants.TILE_WIDTH));
		double spriteY=(y + 0.5*height - spriteHeight/(2.0*Constants.TILE_HEIGHT));
		spritePos=new Vec2D(spriteX, spriteY);
		spriteset=SpriteResources.getLibrary().requestSpriteset(spriteName, spriteWidth, spriteHeight, Constants.SPRITE_SCALE);
		
		direction=Direction.SOUTH;
		
		currentAnimationStep=0;
		animationLength=spriteset[0].length;
		this.animationDelayLength=animationDelayLength;
		currentAnimationDelay=0;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param other - Entity to copy
	 */
	public Entity(Entity other) {
		solid = other.solid;
		aabb = new AABB(other.aabb);
		
		spritePos=new Vec2D(other.spritePos);
		spriteset=other.spriteset;
		
		direction=other.direction;
		
		currentAnimationStep=other.currentAnimationStep;
		animationLength=other.animationLength;
		animationDelayLength=other.animationDelayLength;
		currentAnimationDelay=other.currentAnimationDelay;
	}
	
	public AABB getAABB() {
		return aabb;
	}
	
	public double getX() {
		return aabb.getX();
	}
	
	public double getY() {
		return aabb.getY();
	}
	
	public Vec2D getCenter() {
		return aabb.getCenter();
	}
	
	public Vec2D getPos() {
		return aabb.getPos();
	}
	
	public double getWidth() {
		return aabb.getWidth();
	}
	
	public double getHeight() {
		return aabb.getHeight();
	}
	
	public int getDirection() {
		return direction;
	}
	
	public double getSpriteX() {
		return spritePos.getX();
	}
	
	public double getSpriteY() {
		return spritePos.getY();
	}
	
	public BufferedImage getSprite() {
		return spriteset[direction][currentAnimationStep];
	}
	
	public void setDirection(int direction) {
		this.direction=direction%4;
	}
	
	//TODO: Replace with a lookup table
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
	
	public double getVx() {
		return vel.getX();
	}
	
	public double getVy() {
		return vel.getY();
	}
	
	public Vec2D getVel() {
		return vel;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	
	public void setVelocity(Vec2D v) {
		vel.set(v);
	}
	
	public void setVelocity(double vx, double vy) {
		vel.set(vx, vy);
	}
	
	public void updatePosition() {
		move(vel);
	}
	
	public void move(Vec2D delta) {
		spritePos.add(delta);
		aabb.move(delta);
	}
	
	public void setPosition(Vec2D newPos) {
		Vec2D delta = Vec2D.sub( newPos, getPos() );
		move(delta);
	}
	
	public void animate(double dt) {
		if(vel.isNullVector()) {
			resetAnimation();
		} else {
			updateAnimation(dt);
		}
	}
	
	private void updateAnimation(double dt) {
		currentAnimationDelay += dt;
		if (currentAnimationDelay >= animationDelayLength) {
			currentAnimationDelay -= animationDelayLength;
			currentAnimationStep = (currentAnimationStep+1) % animationLength;
		}
	}
	
	private void resetAnimation() {
		currentAnimationDelay = 0;
		currentAnimationStep = 0;
	}
	
	public boolean shouldBeRemoved() {
		return remove;
	}
	
	public void markForRemoval() {
		remove=true;
	}
	
	@Override
	public int compareTo(Entity o) {
		return aabb.compareTo(o.aabb);
	}
	
	public abstract void tick(Game game, double dt);
	
	public abstract void collisionResponse(CollisionList list);
	
	public abstract CollisionList collisionDetection(Game game);

}
