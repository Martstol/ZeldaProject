package game.algorithms.pathfinding;

import game.map.Map;
import game.math.GameMath;
import game.math.Vec2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

public class Path {
	
	public static final Stack<Node> emptyPath = new Stack<Node>();
	
	public static Stack<Node> aStar(double startX, double startY, double goalX, double goalY, Map map) {
		
		// The actual distance from the start to the node. G score.
		HashMap<Node, Double> distance=new HashMap<Node, Double>();
		
		// The actual distance from the start to the node + the estimate to the goal. F score.
		HashMap<Node, Double> heuristic=new HashMap<Node, Double>();
		
		// Closed set. Nodes that have already been examined.
		HashSet<Node> visited=new HashSet<Node>();
		
		// Open set. Nodes that have not been examined yet.
		PriorityQueue<Node> queue=new PriorityQueue<Node>(32, new NodeComparator(heuristic));
		
		// Came from. Which node was the previous node on the path.
		HashMap<Node, Node> previous=new HashMap<Node, Node>();
		
		// Start node and goal node.
		Node start=new Node((int)Math.round(startX), (int)Math.round(startY));
		Node goal=new Node((int)Math.round(goalX), (int)Math.round(goalY));
		
		distance.put(start, 0.0);
		heuristic.put(start, manhattenDistance(start, goal));
		previous.put(start, null);
		queue.add(start);
		
		while(!queue.isEmpty()) {
			Node current=queue.peek();
			if(current.equals(goal)) {
				return simplifyPath(reconstructPath(previous, current), new Vec2D(startX, startY), new Vec2D(goalX, goalY));
			}
			queue.poll();
			visited.add(current);
			Collection<Node> neighbors = generateNeighbors(current, map);
			for(Node neighbor : neighbors) {
				if(!visited.contains(neighbor)) {
					double newDist=distance.get(current)+Math.hypot(current.x-neighbor.x, current.y-neighbor.y);
					if(!distance.containsKey(neighbor) || newDist < distance.get(neighbor)) {
						previous.put(neighbor, current);
						distance.put(neighbor, newDist);
						heuristic.put(neighbor, newDist+manhattenDistance(neighbor, goal));
						queue.add(neighbor);
					}
				}
			}
		}
		return Path.emptyPath;
	}
	
	/**
	 * This slightly modified Manhattan distance is better than the Euclidean distance for this case.
	 * The Manhattan Distance is modified to take into account that you can walk diagonally.
	 * @param a
	 * @param b
	 * @return Manhattan distance from a to b
	 */
	public static double manhattenDistance(Node a, Node b) {
		int dx=Math.abs(a.x-b.x);
		int dy=Math.abs(a.y-b.y);
		double dist=0;
		if(dx>dy) {
			dist=dx-dy;
			dist+=dy*GameMath.sqrt2;
		} else {
			dist=dy-dx;
			dist+=dx*GameMath.sqrt2;
		}
		return dist;
		
	}
	
	public static ArrayList<Node> generateNeighbors(Node current, Map map) { //TODO have each tile in the map class maintain a list of it's neighbors to speed up execution
		ArrayList<Node> neighbors=new ArrayList<Node>(8);
		int x=current.x;
		int y=current.y;
		if (x-1>=0 && !map.isSolid(x-1, y)) {
			neighbors.add(new Node(x-1, y));
		}
		if (x+1<map.getWidth() && !map.isSolid(x+1, y)) {
			neighbors.add(new Node(x+1, y));
		}
		if (y-1>=0 && !map.isSolid(x, y-1)) {
			neighbors.add(new Node(x, y-1));
		}
		if (y+1<map.getHeight() && !map.isSolid(x, y+1)) {
			neighbors.add(new Node(x, y+1));
		}
		if (x-1>=0 && y-1>=0 && !map.isSolid(x, y-1) && !map.isSolid(x-1, y) && !map.isSolid(x-1, y-1)) {
			neighbors.add(new Node(x-1, y-1));
		}
		if (x+1<map.getWidth() && y-1>=0 && !map.isSolid(x+1, y) && !map.isSolid(x, y-1) && !map.isSolid(x+1, y-1)) {
			neighbors.add(new Node(x+1, y-1));
		}
		if (x-1>=0 && y+1<map.getHeight() && !map.isSolid(x-1, y) && !map.isSolid(x, y+1) && !map.isSolid(x-1, y+1)) {
			neighbors.add(new Node(x-1, y+1));
		}
		if (x+1<map.getWidth() && y+1<map.getHeight() && !map.isSolid(x+1, y) && !map.isSolid(x, y+1) && !map.isSolid(x+1, y+1)) {
			neighbors.add(new Node(x+1, y+1));
		}
		return neighbors;
	}
	
	public static Stack<Node> reconstructPath(HashMap<Node, Node> previous, Node goal) {
		Stack<Node> path=new Stack<Node>();
		Node n=goal;
		while(n!=null) {
			path.push(n);
			n=previous.get(n);
		}
		return path;
	}
	
	public static Stack<Node> simplifyPath(Stack<Node> path, Vec2D currentPos, Vec2D targetPos) {
		Node top = path.peek();
		Vec2D topPos = new Vec2D(top.x, top.y);
		
		if(Vec2D.distanceSquared(topPos, targetPos) > Vec2D.distanceSquared(currentPos, targetPos)) {
			path.pop();
		}

		return path;
	}
	
}
