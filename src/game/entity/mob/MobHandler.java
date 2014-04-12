package game.entity.mob;

import game.Game;

public abstract class MobHandler {
	
	private Mob m;
	
	private boolean attack=false;
	private boolean move=false;
	private int dx=0;
	private int dy=0;
	
	public void setMob(Mob m) {
		this.m=m;
	}
	
	public Mob getMob() {
		return m;
	}
	
	public boolean isAttacking() {
		return attack;
	}
	
	public boolean isMoving() {
		return move;
	}
	
	public int getDX() {
		return dx;
	}
	
	public int getDY() {
		return dy;
	}
	
	protected void setMovement(int dx, int dy) {
		if(dx!=0 || dy!=0) {
			move=true;
			this.dx=dx;
			this.dy=dy;
		} else {
			move=false;
			this.dx=0;
			this.dy=0;
		}
	}
	
	protected void setAttacking(boolean attacking) {
		attack = attacking;
	}
	
	public abstract void tick(Game game, double dt);

}
