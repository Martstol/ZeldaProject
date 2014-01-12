package game.entity.mob.npc;

import game.Game;
import game.algorithms.pathfinding.Node;
import game.algorithms.pathfinding.Path;
import game.entity.mob.Player;
import game.io.InputHandler;

import java.util.Stack;

public class PathFindingNpc extends NPC {
	
	private Stack<Node> path=Path.emptyPath;
	private double attackRange=1;
	
	public PathFindingNpc(double x, double y, double maxVelocity, String spritesetName, int spriteWidth, int spriteHeight, int maxHealth,
			int attack, int attackTime, double attackRange) {
		super(x, y, maxVelocity, spritesetName, spriteWidth, spriteHeight, maxHealth, true);
	}
	
	public void pathfindToPlayer(Game game, double dt) {
		Player p=game.getPlayer();
		if(path.isEmpty()) {
			path=Path.aStar(getX(), getY(), p.getX(), p.getY(), game.getMap());
		} else {
			Node n=path.peek();
			double dx=n.x-getX();
			double dy=n.y-getY();
			
			if(getX()>n.x && getX()+getWidth()<n.x+1) {
				dx=0;
			}
			if(getY()>n.y && getY()+getHeight()<n.y+1) {
				dy=0;
			}
			
			int nx=0, ny=0;
			if(dx>0) {
				nx=1;
			} else if(dx<0) {
				nx=-1;
			}
			if(dy>0) {
				ny=1;
			} else if(dy<0) {
				ny=-1;
			}
			setVelocity(nx, ny, dt);
			setDirection(nx, ny);
			animate(nx!=0 || ny!=0, dt);
			collisionResolution(collisionDetection(game.getMap()));
			move();
			
			if(n.contains(this)) {
				path.pop();
			}
		}
	}

	@Override
	public void tick(Game game, double dt) {
		super.tick(game, dt);
		if(InputHandler.aiPause) return;
		switch(getState()) {
		case Idle:
			Player p=game.getPlayer();
			double d=(attackRange+p.getWidth()+getWidth())*0.5;
			if(p.getCenterPos().distanceToSq(getCenterPos()) < d*d) {
				path=Path.emptyPath;
				// Attack!
			} else {
				pathfindToPlayer(game, dt);
			}
			break;
		default:
			break;
		}
	}
	
	

}
