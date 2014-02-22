package game.algorithms.collision;

import game.Game;
import game.entity.Entity;
import game.map.Map;
import game.math.Vec2D;

// TODO Merge entity and game object

public abstract class GameObject extends AABB {
	
	private Vec2D vel;
	private boolean solid;
	
	public GameObject(double x, double y, double width, double height, boolean solid) {
		super(x, y, width, height);
		
		vel = new Vec2D();
		this.solid = solid;
	}
	
	public GameObject(GameObject other) {
		super(other);
		
		vel = new Vec2D(other.vel);
		this.solid = other.solid;
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
	
	public void move() {
		move(vel);
	}
	
	public CollisionEvent collisionDetection(Map map) {
		CollisionEvent event=new CollisionEvent();
		
		event.add(borderCollisions(map));
		event.add(tileCollisions(map));
		event.add(entityCollisions(map));
		
		return event;
	}
	
	public CollisionEvent borderCollisions(Map map) {
		CollisionEvent event=new CollisionEvent();
		
		if(getX() < 0) {
			event.add(Collision.createBoundaryCollision( -1, (int)getY() ));
		}else if(getX()+getWidth() > map.getWidth()) {
			event.add(Collision.createBoundaryCollision( map.getWidth(), (int)getY() ));
		}
		
		if(getY() < 0) {
			event.add(Collision.createBoundaryCollision( (int)getX(), -1) ); 
		}else if(getY()+getHeight() > map.getHeight()) {
			event.add(Collision.createBoundaryCollision( (int)getX(), map.getHeight() ));
		}
		
		return event;
	}
	
	public CollisionEvent tileCollisions(Map map) {
		CollisionEvent event = new CollisionEvent();
			
		// lower y coordinate
		int yl=((int)getY())-1;
		if(yl<0) {yl=0;}
		
		// upper y coordinate
		int yu=((int)(getY()+getHeight()))+2;
		if(yu>map.getHeight()) {yu=map.getHeight();}
		
		// lower x coordinate
		int xl=((int)getX())-1;
		if(xl<0) {xl=0;}
		
		// upper x coordinate
		int xu=((int)(getX()+getWidth()))+2;
		if(xu>map.getWidth()) {xu=map.getWidth();} 
		
		for(int y=yl; y<yu; ++y) {
			for (int x = xl; x <xu ; ++x) {
				if(map.isSolid(x, y) && intersects(x, y, 1, 1)) {
					event.add(Collision.createTileCollision( x, y ));
				}
			}
		}
			
		return event;
	}
	
	public CollisionEvent entityCollisions(Map map) {
		CollisionEvent event = new CollisionEvent();
		for(Entity e : map.getEntities()) {
			if(this!=e && e.isSolid() && intersects(e)) {
				event.add(Collision.createEntityCollision(e));
			}
		}
		return event;
	}
	
	public abstract void collisionResolution(CollisionEvent event);
	
	public abstract void tick(Game game, double dt);

}
