package game.container.quadtree;

import game.Constants;
import game.algorithms.collision.AABB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
		Random r = new Random(0);
		Quad<AABB> tree=new Quad<>(new AABB(0, 0, mapWidth, mapHeight));
		
		int count=20000+(int)(r.nextDouble()*2); // Making sure the compiler can't optimize away the counting
		List<AABB> list=new ArrayList<>(count);
		
		for(int i=0; i<count; i++) {
			double x = (r.nextDouble() * (mapWidth-Constants.DEFAULT_ENTITY_WIDTH));
			double y = (r.nextDouble() * (mapHeight-Constants.DEFAULT_ENTITY_HEIGHT));
			AABB e = new AABB(x, y, 1, 1);
			list.add(e);
		}
		System.out.println("Map areal: "+(mapWidth*mapHeight));
		System.out.println("Count: "+count);
		
		/*
		 * Warm up loop
		 */
		long l=0;
		long c1=0;
		long t=0;
		for(AABB a : list) {
			for(AABB b : list) {
				if(a!=b) {
					l++;
					if(a.intersects(b)) {
						c1++;
					}
				}
			}
		}
		System.out.println(l); // Just to make sure the compiler doesn't optimize away these loops
		
		/*
		 * Actually measured loop
		 */
		l=0;
		c1=0;
		t=System.currentTimeMillis();
		for(AABB a : list) {
			for(AABB b : list) {
				if(a!=b) {
					l++;
					if(a.intersects(b)) {
						c1++;
					}
				}
			}
		}
		System.out.println(l);
		System.out.println("Double for: " + (System.currentTimeMillis()-t));
		
		/*
		 * Warm up loop, also the duration it takes to clear a full tree needs to be measured
		 */
		l=0;
		long c2=0;
		t=0;
		for(AABB e : list) {
			tree.insert(e);
		}
		for(AABB a : list) {
			Collection<AABB> colList=tree.retrieve(a);
			for(AABB b : colList) {
				if(a!=b) {
					l++;
					if(a.intersects(b)) {
						c2++;
					}
				}
			}
		}
		System.out.println(l);
		
		/*
		 * Actually measured loop
		 */
		l=0;
		c2=0;
		t=System.currentTimeMillis();
		tree.clear();
		for(AABB e : list) {
			tree.insert(e);
		}
		for(AABB a : list) {
			Collection<AABB> colList=tree.retrieve(a);
			for(AABB b : colList) {
				if(a!=b) {
					l++;
					if(a.intersects(b)) {
						c2++;
					}
				}
			}
		}
		System.out.println(l);
		System.out.println("Quad tree: "+(System.currentTimeMillis()-t));
		
		if(c1 == c2) {
			System.out.println("Test was successful!");
		} else {
			System.out.println("The Quad implementation failed the test!");
		}
		System.out.println("c1 = " + c1);
		System.out.println("c2 = " + c2);
	}

}
