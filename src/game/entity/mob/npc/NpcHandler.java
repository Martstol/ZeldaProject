package game.entity.mob.npc;

import game.Game;
import game.algorithms.pathfinding.Node;
import game.algorithms.pathfinding.Path;
import game.entity.mob.Mob;
import game.entity.mob.MobHandler;
import game.io.InputHandler;
import game.math.Vec2D;

import java.util.Stack;

public class NpcHandler extends MobHandler {
	
	private Stack<Node> path = Path.emptyPath;
	private Vec2D oldPlayerPos = new Vec2D();

	@Override
	public void tick(Game game, double dt) {
		setAttacking(false);
		if(InputHandler.aiPause) {
			setMovement(0, 0);
			return;
		}
		
		Mob p = game.getPlayer();
		Mob m = getMob();
		
		if(!path.isEmpty() && path.peek().contains(m)) {
			path.pop();
		}
		
		
		if(path.isEmpty() || Vec2D.distanceSquared(p.getPos(), oldPlayerPos) > 9.0) {
			path = Path.aStar(m.getX(), m.getY(), p.getX(), p.getY(), game.getMap());
			oldPlayerPos.set(p.getX(), p.getY());
		}
		
		if(!path.isEmpty()) {			
			Node n = path.peek();
			double dx = n.x - m.getX();
			double dy = n.y - m.getY();
			
			if(m.getX() > n.x && m.getX()+m.getWidth() < n.x+1) {
				dx=0;
			}
			if(m.getY() > n.y && m.getY()+m.getHeight() < n.y+1) {
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
			
			setMovement(nx, ny);
		} else {
			setMovement(0, 0);
		}
		
	}

}
