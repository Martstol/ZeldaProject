package game.entity.mob.npc;

import game.Game;
import game.algorithms.pathfinding.Node;
import game.algorithms.pathfinding.Path;
import game.entity.mob.Mob;
import game.entity.mob.MobHandler;

import java.util.Stack;

public class NpcHandler extends MobHandler {
	
	private Stack<Node> path=Path.emptyPath;

	@Override
	public void tick(Game game, double dt) {
		attack = false;
		
		if(!path.isEmpty() && path.peek().contains(getMob())) {
			path.pop();
		}
		
		Mob p=game.getPlayer();
		if(path.isEmpty()) {
			path=Path.aStar(getMob().getX(), getMob().getY(), p.getX(), p.getY(), game.getMap());
		} else {
			Node n=path.peek();
			double dx=n.x-getMob().getX();
			double dy=n.y-getMob().getY();
			
			if(getMob().getX()>n.x && getMob().getX()+getMob().getWidth()<n.x+1) {
				dx=0;
			}
			if(getMob().getY()>n.y && getMob().getY()+getMob().getHeight()<n.y+1) {
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
			this.dx=nx;
			this.dy=ny;
			move=nx!=0||ny!=0;
		}
		
	}

}
