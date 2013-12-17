package game.map;

public class Tile {
	
	public final int x, y;
	public final int tileset;
	
	public Tile(int tileset, int x, int y) {
		this.tileset=tileset;
		this.x=x;
		this.y=y;
	}
	
	@Override
	public String toString() {
		return tileset+", "+x+", "+y;
	}

}
