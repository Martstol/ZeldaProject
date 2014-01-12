package game.container;

public class GameLinkedList<E> {
	
	private Element<E> head;
	private Element<E> tail;
	private int count;
	
	public GameLinkedList() {
		head=tail=null;
		count=0;
	}
	
	public Element<E> getHeadElement() {
		return head;
	}
	
	public Element<E> getTailElement() {
		return tail;
	}
	
	public void addFirst(E e) {
		Element<E> el = new Element<>();
		el.data=e;
		if(head==null) {
			head=tail=el;
		} else {
			el.next=head;
			head.prev=el;
			head=el;
		}
	}
	
	public void addLast(E e) {
		Element<E> el = new Element<>();
		el.data=e;
		if(tail==null) {
			head=tail=el;
		} else {
			el.prev=tail;
			tail.next=el;
			tail=el;
		}
	}
	
	public E popFirst() {
		if(head==null) {
			return null;
		} else {
			E data=head.data;
			count-=1;
			if(head==tail) {
				head=tail=null;
			} else {
				head=head.next;
				head.prev=null;
			}
			return data;
		}
	}
	
	public E peekFirst() {
		return head.data;
	}
	
	public E popLast() {
		if(tail==null) {
			return null;
		} else {
			E data=tail.data;
			count-=1;
			if(head==tail) {
				head=tail=null;
			} else {
				tail=tail.prev;
				tail.next=null;
			}
			return data;
		}
	}
	
	public E peekLast() {
		return tail.data;
	}
	
	public boolean isEmpty() {
		return count == 0;
	}
	
	public int size() {
		return count;
	}
	
	@Override
	public String toString() {
		Element<E> el=head;
		StringBuilder sb=new StringBuilder("[");
		while(el!=null) {
			sb.append(el.data.toString() + (el.next==null ? "]" : ", "));
			el=el.next;
		}
		return sb.toString();
	}

}

class Element<E> {
	Element<E> next;
	Element<E> prev;
	E data;
}
