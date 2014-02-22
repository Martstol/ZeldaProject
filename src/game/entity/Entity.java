package game.entity;

import game.Constants;
import game.algorithms.collision.AABB;
import game.algorithms.collision.GameObject;
import game.graphic.SpriteResources;
import game.math.Vec2D;

import java.awt.image.BufferedImage;

public abstract class Entity extends GameObject {
	
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
		super(x, y, width, height, solid);
		
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
		super(other);
		
		spritePos=new Vec2D(other.spritePos);
		spriteset=other.spriteset;
		
		direction=other.direction;
		
		currentAnimationStep=other.currentAnimationStep;
		animationLength=other.animationLength;
		animationDelayLength=other.animationDelayLength;
		currentAnimationDelay=other.currentAnimationDelay;
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
	
	@Override
	public void move() {
		Vec2D v = getVel();
		spritePos.add(v);
		super.move(v);
	}
	
	@Override
	public void move(double dx, double dy) {
		spritePos.add(dx, dy);
		super.move(dx, dy);
	}
	
	@Override
	public void move(Vec2D delta) {
		spritePos.add(delta);
		super.move(delta);
	}
	
	@Override
	public void moveTo(Vec2D newPos) {
		Vec2D v = Vec2D.sub( newPos, getPos() );
		spritePos.add(v);
		super.moveTo(v);
	}
	
	public void updateAnimation(double dt) {
		currentAnimationDelay += dt;
		if (currentAnimationDelay >= animationDelayLength) {
			currentAnimationDelay -= animationDelayLength;
			currentAnimationStep = (currentAnimationStep+1) % animationLength;
		}
	}
	
	public void resetAnimation() {
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
	public int compareTo(AABB o) {
		if(o instanceof Entity) {
			Entity e = (Entity)o;
			if(this.getSpriteY() > e.getSpriteY()) {
				return 1;
			} else if(this.getSpriteY() < e.getSpriteY()) {
				return -1;
			} else {
				return 0;
			}
		} else {
			return super.compareTo(o);
		}
	}

}
