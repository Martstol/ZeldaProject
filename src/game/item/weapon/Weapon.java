package game.item.weapon;

import java.awt.image.BufferedImage;

import game.Constants;
import game.Game;
import game.entity.Entity;
import game.graphic.SpriteResources;
import game.item.Item;
import game.sound.AudioPlayer;

public abstract class Weapon extends Item {
	
	private boolean attacking;
	private int damage;
	private double knockbackPower;
	private int attackStep;
	private int attackLength;
	private int damageTick; //The tick of the animation where damage calculation is done.
	private double animationDelayLength;
	private double currentDelayTime;
	private BufferedImage[][] spriteset;
	private String sfxName;
	
	public Weapon(String name, String description, int damage, double knockbackPower, int spriteWidth, int spriteHeight, String sfxName) {
		super(name, description, 1, 1, true);
		attacking=false;
		this.damage=damage;
		spriteset=SpriteResources.getLibrary().requestSpriteset(name, spriteWidth, spriteHeight, Constants.SPRITE_SCALE);
		attackStep=0;
		animationDelayLength=Constants.DEFAULT_ENTITY_ANIMATION_DELAY;
		currentDelayTime=0;
		attackLength=spriteset[0].length;
		damageTick=attackLength/2;
		this.sfxName=sfxName;
		this.knockbackPower=knockbackPower;
	}
	
	@Override
	public BufferedImage getSprite(int direction) {
		return spriteset[direction][attackStep];
	}
	
	public double getKnockbackPower() {
		return knockbackPower;
	}
	
	public int getDamage() {
		return damage;
	}

	public void attack(Entity usedBy, Game game, double dt) {
		if(currentDelayTime==0 && attackStep==0) {
			attacking=true;
			AudioPlayer.getPlayer().playSfx(sfxName);
		}
		currentDelayTime+=dt;
		if(attackStep==attackLength-1 && currentDelayTime>=animationDelayLength) {
			attackStep=0;
			attacking=false;
			currentDelayTime=0;
			return;
		}
		if(attackStep==damageTick && currentDelayTime>=animationDelayLength) {		
			attackTick(usedBy, game);
		}
		if(currentDelayTime>=animationDelayLength) {
			currentDelayTime-=animationDelayLength;
			attackStep=(attackStep+1)%attackLength;
		}
	}
	
	public boolean isAttacking() {
		return attacking;
	}
	
	@Override
	public void use(Entity usedBy, Game game) {
		// TODO Auto-generated method stub
		
	}
	
	public abstract void attackTick(Entity usedBy, Game game);

}
