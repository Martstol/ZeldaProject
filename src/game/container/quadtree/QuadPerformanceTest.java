package game.container.quadtree;

import game.Constants;
import game.algorithms.collision.AABB;

import java.util.LinkedList;
import java.util.Random;

public class QuadPerformanceTest {
	
	public static void main(String[] args) {
		/*
		 * It is interesting to note the variation of performance with varying map sizes and AABB counts.
		 * I imagine that when AABB per map area increases, the performance of the Quad tree decreases.
		 * (Given that the entities are uniformly distributed across the map).
		 * Will of course also work nicely if the entities are in several clusters across the map.
		 */
		double mapWidth=1000;
		double mapHeight=1000;
		Random r = new Random(System.currentTimeMillis());
		Quad tree=new Quad(new AABB(0, 0, mapWidth, mapHeight));
		LinkedList<AABB> list=new LinkedList<>();
		
		int count=20000+(int)(r.nextDouble()*2); // Making sure the compiler can't optimize away the counting
		for(int i=0; i<count; i++) {
			double x = (r.nextDouble()*mapWidth)-Constants.PLAYER_SPRITE_WIDTH;
			double y = (r.nextDouble()*mapHeight)-Constants.PLAYER_SPRITE_HEIGHT;
			AABB e = new AABB(x, y, 1, 1);
			list.add(e);
		}
		System.out.println("Map areal: "+(mapWidth*mapHeight));
		System.out.println("Count: "+count);
		
		/*
		 * Warm up loop
		 */
		long l=0;
		long t=0;
		for(AABB a : list) {
			for(AABB b : list) {
				if(a!=b) {
					l++;
				}
			}
		}
		System.out.println(l); // Just to make sure the compiler doesn't optimize away these loops
		
		/*
		 * Actually measured loop
		 */
		l=0;
		t=System.currentTimeMillis();
		for(AABB a : list) {
			for(AABB b : list) {
				if(a!=b) {
					l++;
				}
			}
		}
		System.out.println(l);
		System.out.println("Double for: " + (System.currentTimeMillis()-t));
		
		/*
		 * Warm up loop, also the duration it takes to clear a full tree needs to be measured
		 */
		l=0;
		t=0;
		for(AABB e : list) {
			tree.insert(e);
		}
		for(AABB a : list) {
			LinkedList<AABB> colList=tree.retrieve(a);
			for(AABB b : colList) {
				if(a!=b) {
					l++;					
				}
			}
		}
		System.out.println(l);
		
		/*
		 * Actually measured loop
		 */
		l=0;
		t=System.currentTimeMillis();
		tree.clear();
		for(AABB e : list) {
			tree.insert(e);
		}
		for(AABB a : list) {
			LinkedList<AABB> colList=tree.retrieve(a);
			for(AABB b : colList) {
				if(a!=b) {
					l++;					
				}
			}
		}
		System.out.println(l);
		System.out.println("Quad tree: "+(System.currentTimeMillis()-t));
	}

}
