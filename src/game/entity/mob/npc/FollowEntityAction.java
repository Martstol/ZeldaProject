package game.entity.mob.npc;

import game.Game;
import game.algorithms.pathfinding.Node;
import game.algorithms.pathfinding.Path;
import game.entity.Entity;

import java.util.Stack;

public class FollowEntityAction extends Action {
	
	private Stack<Node> path;
	private Entity target;

	public FollowEntityAction(Entity owner, Entity target) {
		super(owner, false);
		this.path=Path.emptyPath;
		this.target=target;
	}

	@Override
	public void update(Game game, double dt) {
		if(path.isEmpty()) {
			setFinished(true);
		} else {
			Entity o=getOwner();
			Node n=path.peek();
			double dx=n.x-o.getX();
			double dy=n.y-o.getY();
			
			if(o.getX() >= n.x && o.getX()+o.getWidth()<n.x+1) {
				dx=0;
			}
			if(o.getY() >= n.y && o.getY()+o.getHeight()<n.y+1) {
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
			
			o.setVelocity(nx, ny, dt);
			//TODO Have the owner walk
			
			if(n.contains(o)) {
				path.pop();
			}
		}
	}

	@Override
	public void onStart(Game game) {
		path=Path.aStar(getOwner().getX(), getOwner().getY(), target.getX(), target.getY(), game.getMap());
	}

	@Override
	public void onFinish(Game game) {
		//TODO Add a new FollowEntityAction to the action list
	}

}
