package game.entity.mob.npc;

import game.Game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class ActionList {
	
	private LinkedList<Action> list=new LinkedList<>();
	
	private ArrayList<Action> insertFirst=new ArrayList<>();
	private ArrayList<Action> insertLast=new ArrayList<>();
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public void addFirst(Action a) {
		insertFirst.add(a);
	}
	
	public void addLast(Action a) {
		insertLast.add(a);
	}
	
	public void update(Game game, double dt) {
		if(!insertLast.isEmpty()) {
			for(int i=0; i<insertLast.size(); i++) {
				list.addLast(insertLast.get(i));
				insertLast.get(i).onStart(game);
			}
			insertLast=new ArrayList<>();
		}
		
		if(!insertFirst.isEmpty()) {
			for(int i=insertFirst.size()-1; i>=0; i--) {
				list.addFirst(insertFirst.get(i));
				insertFirst.get(i).onStart(game);
			}
			insertFirst=new ArrayList<>();
		}
		
		for(Iterator<Action> it=list.iterator(); it.hasNext();) {
			Action a = it.next();
			a.update(game, dt);
			if(a.isFinished()) {
				a.onFinish(game);
				it.remove();
			}
			if(a.isBlocking()) {
				break;
			}
		}
	}

}
