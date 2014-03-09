package game.algorithms;

public class Pair<T, E> {
	
	public final T t;
	public final E e;
	
	public Pair(T t, E e) {
		this.t=t;
		this.e=e;
	}
	
	public T getFirst() {
		return t;
	}
	
	public E getSecond() {
		return e;
	}
	
	public Pair<E, T> getOpposite() {
		return new Pair<>(e, t);
	}
	
	@Override
	public int hashCode() {
		return t.hashCode() ^ e.hashCode();
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof Pair) {
			Pair other = ((Pair)arg0);
			
			return this.getFirst().equals(other.getFirst()) 
					&& this.getSecond().equals(other.getSecond());
			
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "[" + t.toString() + ", " + e.toString() + "]";
	}

}
