package game.entity.mob;

import game.Game;

public abstract class MobHandler {
	
	private Mob m;
	
	protected boolean attack=false;
	protected boolean move=false;
	protected int dx=0;
	protected int dy=0;
	
	public void setMob(Mob m) {
		this.m=m;
	}
	
	public Mob getMob() {
		return m;
	}
	
	public boolean requestingAttack() {
		return attack;
	}
	
	public boolean requestingMovement() {
		return move;
	}
	
	public int getDX() {
		return dx;
	}
	
	public int getDY() {
		return dy;
	}
	
	public abstract void tick(Game game, double dt);

}
