package game.container;

public class Element<E> {
	Element<E> next;
	Element<E> prev;
	E data;
	
	public Element() {
		next=null;
		prev=null;
		data=null;
	}
	
	public Element(Element<E> other) {
		this.next=other.next;
		this.prev=other.prev;
		this.data=other.data;
	}
	
	public Element<E> getNext() {
		return next;
	}
	
	public Element<E> getPrevious() {
		return prev;
	}
	
	public E getData() {
		return data;
	}
}