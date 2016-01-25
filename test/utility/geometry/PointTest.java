package utility.geometry;

import static org.junit.Assert.*;

import org.junit.Test;

public class PointTest {

	@Test
	public void testSamePoint() {
		Point p = new Point(3, 5);
		Point p1 = new Point(3, 5);
		Point p2 = new Point(4, 5);
		assertTrue(p.SamePoint(p));
		assertTrue(p.SamePoint(p1));
		assertTrue(!p.SamePoint(p2));
		assertFalse(p.SamePoint(new Point(3.01, 5)));
		assertTrue(p.SamePoint(new Point(3.0001, 5)));
	}
}
