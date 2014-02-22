package game.entity.mob;

import game.Game;
import game.io.Keys;

public class PlayerHandler extends MobHandler {
	
	private Keys keys;
	
	public PlayerHandler(Keys keys) {
		this.keys=keys;
	}

	@Override
	public void tick(Game game, double dt) {
		attack = keys.attack.isPressed();
		
		dx=0;
		dy=0;
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
		
		move = dx!=0 || dy!=0;
	}

}
