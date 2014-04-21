package game.algorithms.collision;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import game.math.Vec2D;

import java.util.Random;

import org.junit.Test;

public class AABBTest {

	@Test
	public void testMoveDoubleDouble() {
		double x = 1;
		double y = 0;
		double dx = 1;
		double dy = 1;
		AABB a = new AABB(x, y, 1, 1);
		a.move(dx, dy);
		
		assertTrue("New X position should be " + (x+dx), x+dx == a.getX());
		assertTrue("New Y position should be " + (y+dy), y+dy == a.getY());
	}

	@Test
	public void testMoveVec2D() {
		Vec2D delta = new Vec2D(0.5, 0.5);
		Vec2D pos = new Vec2D(1, 0.5);
		Vec2D dim = new Vec2D(1, 1);
		
		AABB a = new AABB(pos, dim);
		a.move(delta);
		
		Vec2D newPos = Vec2D.add(pos, delta);
		
		assertEquals(a.getPos(), newPos);
		
	}

	@Test
	public void testMoveTo() {
		long seed = 0;
		Random r = new Random(seed);
		Vec2D pos = new Vec2D(r.nextDouble(), r.nextDouble());
		Vec2D dim = new Vec2D(1, 1);
		AABB a = new AABB(pos, dim);
		
		Vec2D newPos = new Vec2D(r.nextDouble(), r.nextDouble());
		
		a.moveTo(newPos);
		
		assertEquals(a.getPos(), newPos);
	}

	@Test
	public void testIntersectsAABB() {
		double x=1;
		double y=1;
		double w=1;
		double h=1;
		AABB a = new AABB(x, y, w, h);
		
		assertTrue(a + " should intersect itself.", a.intersects(a));
		
		// Test intersections with all the edges and corners 
		for(int dx=-1; dx<2; dx++) {
			for(int dy=-1; dy<2; dy++) {
				if(dx==0 && dy==0) continue;
				AABB b = new AABB(x+dx, y+dy, w, h);
				assertFalse(a + " and " + b + " are not intersecting, their edges are touching.", a.intersects(b));
				assertFalse(b + " and " + a + " are not intersecting, their edges are touching.", b.intersects(a));
			}
		}
		
		AABB c = new AABB(x+w/2, y, w, h);
		assertTrue(a + " should intersect " + c, a.intersects(c));
		assertTrue(c + " should intersect " + a, c.intersects(a));
		
		AABB d = new AABB(x+w/4, y+h/4, w/4, h/4);
		assertTrue(a + " should intersect " + d, a.intersects(d));
		assertTrue(d + " should intersect " + a, d.intersects(a));
	}

	@Test
	public void testContains() {
		AABB a = new AABB(1, 1, 1, 1);
				
		AABB b = new AABB(1, 1, 0.5, 0.5);
		assertTrue(a + " should contain " + b, a.contains(b));
		
		AABB c = new AABB(0, 0, 1, 1);
		assertFalse(a + " should not contain " + c, a.contains(c));
		
		AABB d = new AABB(0, 0, 2, 2);
		assertFalse(a + " should not contain " + d, a.contains(d));
		
		AABB e = new AABB(1.5, 1, 1, 1);
		assertFalse(a + " should not contain " + e, a.contains(e));
	}

	@Test
	public void testEqualsObject() {
		AABB a = new AABB(0, 0, 1, 1);
		
		AABB b = new AABB(a);
		assertEquals(a + " should be equal to " + b, a, b);
		
		Object o = new Object();
		assertNotEquals(a + " should not be equal to " + o, a, o);
		
		AABB c = new AABB(0, 0, 1, 1);
		assertEquals(a + " should be equal to " + c, a, c);
		
		AABB d = new AABB(0.5, 0, 1, 1);
		assertNotEquals(a + " should not be equal to " + d, a, d);
	}

}
