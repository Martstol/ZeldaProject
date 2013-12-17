package game.entity.mob.npc;

import game.entity.mob.Mob;

public abstract class NPC extends Mob {

	public NPC(double x, double y, double maxVel, String spriteName,
			int spriteWidth, int spriteHeight, int maxHealth, boolean canBeKnocked) {
		super(x, y, maxVel, spriteName, spriteWidth, spriteHeight, maxHealth, canBeKnocked);
	}
	
	//TODO ActionList

	

}
