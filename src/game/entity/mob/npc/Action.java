package game.entity.mob.npc;

import game.Game;
import game.entity.Entity;

public abstract class Action {
	
	private Entity owner;
	private boolean blocking;
	private boolean finished;
	
	public Action(Entity owner, boolean blocking) {
		this.owner=owner;
		this.blocking=blocking;
		this.finished=false;
	}
	
	public boolean isBlocking() {
		return blocking;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public Entity getOwner() {
		return owner;
	}
	
	public void setFinished(boolean finished) {
		this.finished=finished;
	}
	
	public abstract void update(Game game, double dt);
	public abstract void onStart(Game game);
	public abstract void onFinish(Game game);

}
