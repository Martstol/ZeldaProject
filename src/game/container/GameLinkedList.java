package game.container;

import java.util.Iterator;

public class GameLinkedList<E> implements Iterable<E> {
	
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
	
	public void remove(Element<E> el) {
		if(el==head) {
			popFirst();
		} else if (el==tail) {
			popLast();
		} else {
			el.prev.next=el.next;
			el.next.prev=el.prev;
		}
	}
	
	/**
	 * Move the content from the input list into this list. The input list will be empty afterwards.
	 * Nothing is done if the input list is empty.
	 * @param list - input list
	 */
	public void moveGLL(GameLinkedList<E> list) {
		if(list.isEmpty()) {
			return;
		}
		this.count+=list.count;
		if(this.head==null) {
			this.head=list.head;
			this.tail=list.tail;
		} else {
			this.tail.next=list.head;
			list.head.prev=this.tail;
			this.tail=list.tail;
		}
		list.clear();
	}
	
	public void clear() {
		head=tail=null;
		count=0;
	}
	
	public void addFirst(E e) {
		Element<E> el = new Element<>();
		el.data=e;
		count+=1;
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
		count+=1;
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
		if(head==null) return null;
		else return head.data;
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
		if(tail==null) return null;
		else return tail.data;
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

	@Override
	public Iterator<E> iterator() {
		return new GLLIterator<>(this);
	}

}
