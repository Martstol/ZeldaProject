package game.algorithms.pathfinding;

import java.util.Comparator;
import java.util.HashMap;

class NodeComparator implements Comparator<Node> {
	
	private HashMap<Node, Double> heuristic;
	
	public NodeComparator(HashMap<Node, Double> heuristic) {
		this.heuristic=heuristic;
	}

	@Override
	public int compare(Node a, Node b) {
		double aVal=heuristic.get(a);
		double bVal=heuristic.get(b);
		if(aVal>bVal) {
			return 1;
		} else if (aVal<bVal) {
			return -1;
		} else {
			return 0;
		}
	}
}
