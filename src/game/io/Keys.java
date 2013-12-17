package game.io;

import java.util.ArrayList;

public class Keys {
	public class Key {
		private boolean pressed=false;
		public Key() {all.add(this);}
		public boolean isPressed() {return pressed;}
		public void press() {pressed=true;}
		public void release() {pressed=false;}
	}
	
	private ArrayList<Key> all=new ArrayList<Key>();
	
	public Key up=new Key();
	public Key down=new Key();
	public Key left=new Key();
	public Key right=new Key();
	public Key attack=new Key();
	public Key select=new Key();
	public Key back=new Key(); 
	
	public void releaseAll() {
		for (Key k : all) {
			k.release();
		}
	}

}
