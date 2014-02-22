package game.item.weapon;

import game.Game;
import game.algorithms.collision.AABB;
import game.entity.Direction;
import game.entity.Entity;
import game.entity.mob.Mob;
import game.math.Vec2D;

public class Sword extends Weapon {
	
	private double r, angle;

	public Sword(String name, String description, double range, double angle, int damage, double knockbackPower, int spriteWidth, int spriteHeight, String sfxName) {
		super(name, description, damage, knockbackPower, spriteWidth, spriteHeight, sfxName);
		this.r=range;
		this.angle=angle;
	}
	
	protected boolean hitCheck(AABB target, AABB user, Vec2D dir) {
		Vec2D t=target.getCenter();
		Vec2D u=user.getCenter();
		return (t.distanceTo(u)<=r+user.getWidth()/2+target.getWidth()/2 && 
				Vec2D.angleBetween(Vec2D.distanceVec(u, t), dir)<=angle);
	}

	@Override
	public void attackTick(Entity usedBy, Game game) {
		Vec2D dir=new Vec2D();
		switch(usedBy.getDirection()) {
		case Direction.UP:
			dir.setY(-1);
			break;
		case Direction.DOWN:
			dir.setY(1);
			break;
		case Direction.LEFT:
			dir.setX(-1);
			break;
		case Direction.RIGHT:
			dir.setX(1);
			break;
		}
		for(Entity target : game.getMap().getEntities()) {
			if(target!=usedBy && target instanceof Mob && hitCheck(target, usedBy, dir)) {
				((Mob)target).damage(this, usedBy);			
			}
		}
	}
}
