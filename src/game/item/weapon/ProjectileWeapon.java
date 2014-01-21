package game.item.weapon;

import game.Game;
import game.entity.Direction;
import game.entity.Entity;
import game.math.Vec2D;

public class ProjectileWeapon extends Weapon {
	
	private ProjectileEntity projectile;

	public ProjectileWeapon(String name, String description, int damage, double knockbackPower, int spriteWidth, int spriteHeight, String sfxName, ProjectileEntity projectileEntity) {
		super(name, description, damage, knockbackPower, spriteWidth, spriteHeight, sfxName);
		this.projectile=projectileEntity;
	}

	@Override
	public void attackTick(Entity usedBy, Game game) {
		if(projectile!=null) {
			ProjectileEntity e = projectile.copy();
			e.setWeapon(this);
			e.setSource(usedBy);
			
			int dir=usedBy.getDirection();
			e.setDirection(dir);
			
			Vec2D pos=new Vec2D(usedBy.getCenterPos());
			switch(dir) {
			case Direction.DOWN:
				pos.add(0, 0.5*(usedBy.getHeight()+e.getHeight()));
				break;
			case Direction.UP:
				pos.add(0, -0.5*(usedBy.getHeight()+e.getHeight()));
				break;
			case Direction.LEFT:
				pos.add(-0.5*(usedBy.getWidth()+e.getWidth()), 0);
				break;
			case Direction.RIGHT:
				pos.add(0.5*(usedBy.getWidth()+e.getWidth()), 0);
				break;
			default:
				throw new RuntimeException("Invalid direction: "+dir);
			}
			e.moveTo(pos);
			
			game.getMap().addEntity(e);			
		} else {
			System.out.println("No projectile!");
			//TODO No projectile to fire, play error sound or something
		}
	}

}
