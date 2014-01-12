package game.container;

import java.util.Iterator;

public class GLLIterator<E> implements Iterator<E> {
	
	private GameLinkedList<E> list;
	private Element<E> next;
	private Element<E> current;
	
	public GLLIterator(GameLinkedList<E> list) {
		this.list=list;
		this.next=list.getHeadElement();
	}

	@Override
	public boolean hasNext() {
		return next!=null;
	}

	@Override
	public E next() {
		current=next;
		next=next.next;
		return current.data;
	}

	@Override
	public void remove() {
		list.remove(current);
	}

}
