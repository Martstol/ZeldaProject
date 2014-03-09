package game.container.quadtree;

import game.algorithms.Pair;
import game.algorithms.collision.AABB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
		int count=50000;
		long seed = System.currentTimeMillis();
		Random r = new Random(seed);
		System.out.println("Seed: "+seed);
		List<AABB> list=new ArrayList<>(count);
		Quad<AABB> tree=new Quad<>(new AABB(0, 0, mapWidth, mapHeight));
		HashSet<Pair<AABB, AABB>> collisions1 = new HashSet<>();
		HashSet<Pair<AABB, AABB>> collisions2 = new HashSet<>();
		
		
		for(int i=0; i<count; i++) {
			double x = (r.nextDouble() * (mapWidth-2));
			double y = (r.nextDouble() * (mapHeight-2));
			AABB e = new AABB(x, y, 2, 2);
			list.add(e);
		}
		System.out.println("Map areal: "+(mapWidth*mapHeight));
		System.out.println("Count: "+count +"\n");
		
		/*
		 * Double for
		 */
		long l=0;
		long t=System.currentTimeMillis();
		for(AABB a : list) {
			for(AABB b : list) {
				if(a!=b) {
					l++;
					if(a.intersects(b)) {
						collisions1.add(new Pair<>(a, b));
					}
				}
			}
		}
		System.out.println("DOUBLE FOR LOOP DATA: ");
		System.out.println("Number of intersection tests = " + l);
		System.out.println("Intersections found = " + collisions1.size());
		System.out.println("Time taken = " + (System.currentTimeMillis()-t) + "\n");
		
		/*
		 * Quad tree
		 */
		l=0;
		t=System.currentTimeMillis();
		tree.clear();
		for(AABB e : list) {
			tree.insert(e);
		}
		for(AABB a : list) {
			Collection<AABB> colList=tree.retrieve(a);
			for(AABB b : colList) {
				if(a != b) {
					l++;
					if(a.intersects(b)) {
						collisions2.add(new Pair<>(a, b));
					}
				}
			}
		}
		System.out.println("QUAD TREE DATA: ");
		System.out.println("Number of intersection tests = " + l);
		System.out.println("Intersections found = " + collisions2.size());
		System.out.println("Time taken = "+(System.currentTimeMillis()-t) + "\n");
		
		if(collisions1.size() == collisions2.size()) {
			System.out.println("Test was successful!");
		} else {
			System.out.println("The Quad implementation failed the test!");
			
			Collection<Pair<AABB, AABB>> fails = new HashSet<>();
			for(Pair<AABB, AABB> p : collisions1) {
				if(!collisions2.contains(p)) {
					fails.add(p);
				}
			}
			System.out.println(fails);
		}
	}

}
