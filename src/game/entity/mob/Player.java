package game.entity.mob;

import java.awt.image.BufferedImage;

import game.Constants;
import game.Game;
import game.io.Keys;
import game.item.weapon.BoomerangEntity;
import game.item.weapon.ProjectileEntity;
import game.item.weapon.ProjectileWeapon;
import game.item.weapon.Sword;
import game.item.weapon.Weapon;

public class Player extends Mob {
	
	private Keys keys;
	private Weapon weapon;

	public Player(double x, double y, Keys keys) {
		super(x, y, Constants.PLAYER_MAX_VELOCITY, Constants.PLAYER_SPRITESET_NAME, Constants.PLAYER_SPRITE_WIDTH, Constants.PLAYER_SPRITE_HEIGHT, 
				Constants.PLAYER_START_HEALTH, false);
		this.keys=keys;
		//weapon=new Sword("hero sword", "", 1, Math.PI/3, 1, 10, Constants.PLAYER_SPRITE_WIDTH, Constants.PLAYER_SPRITE_HEIGHT, "sword1");
		ProjectileEntity projectile=new ProjectileEntity(1/16.0, 1/16.0, 10, "arrow", 29, 29, 1.2);
		//ProjectileEntity projectile=new BoomerangEntity(10/16.0, 10/16.0, 8, "boomerang", 10, 10, 5, 1);
		weapon = new ProjectileWeapon("hero bow", "", 1, 10, Constants.PLAYER_SPRITE_WIDTH, Constants.PLAYER_SPRITE_HEIGHT, "bow1", projectile);
	}
	
	@Override
	public BufferedImage getSprite() {
		switch(getState()) {
		case Idle:
			return super.getSprite();
		case Attacking:
			return weapon.getSprite(getDirection());
		default:
			return super.getSprite();
		}
	}

	@Override
	public void tick(Game game, double dt) {
		super.tick(game, dt);
		switch(getState()) {
		case Attacking:
			weapon.attack(this, game, dt);
			if (!weapon.isAttacking()) {setState(MobState.Idle);}
			break;
		case Idle: case Hurt:
			if(keys.attack.isPressed()) {
				setState(MobState.Attacking);
				weapon.attack(this, game, dt);
			} else {
				int dx=0, dy=0;
				if (keys.down.isPressed()) {
					++dy;
				}
				if (keys.up.isPressed()) {
					--dy;
				}
				if (keys.right.isPressed()) {
					++dx;
				}
				if (keys.left.isPressed()) {
					--dx;
				}
				setDirection(dx, dy);
				animate(dx!=0 || dy!=0, dt);
				setVelocity(dx, dy, dt);
				collisionResolution(collisionDetection(game.getMap()));
				move();
			}
			break;
		}
	}

}
