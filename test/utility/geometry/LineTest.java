package utility.geometry;

import static org.junit.Assert.*;

import org.junit.Test;

public class LineTest {

	@Test
	public void testLine() {
		Point p = new Point(3, 6);
		Point p1 = new Point(3, 20);
		Line l = new Line(p, p1);
		assertEquals(Double.POSITIVE_INFINITY, l.k, 0.0001);
		Point p2 = new Point(3, 5);
		Point p3 = new Point(8, 1);
		Line l1 = new Line(p2, p3);
		assertEquals((5.0 - 1.0) / (3.0 - 8.0), l1.k, 0.0001);
		assertEquals(-4 / 5.0, l1.a, 0.0001);
		assertEquals(37 / 5.0, l1.c, 0.0001);
	}

	@Test
	public void testOnline() {
		Point p = new Point(3, 3);
		Point p1 = new Point(6, 6);
		Line l = new Line(p, p1);
		assertTrue(l.online(p));
		assertTrue(l.online(new Point(9, 9)));
		assertFalse(l.online(new Point(9, 9.1)));
	}

	@Test
	public void testSameSide() {
		Point p = new Point(3, 3);
		Point p1 = new Point(6, 6);
		Line l = new Line(p, p1);
		assertTrue(l.sameSide(new Point(3, 10), new Point(6, 10)));
		assertTrue(l.sameSide(new Point(3, 7), new Point(3, 6)));
		assertFalse(l.sameSide(new Point(3, 10), new Point(3, 1)));

		Line l1 = new Line(new Point(-2, -2), new Point(-2, 10));
		assertTrue(l1.sameSide(new Point(3, 10), new Point(6, 10)));
		assertTrue(l1.sameSide(new Point(-4, 10), new Point(-6, 10)));
		assertFalse(l1.sameSide(new Point(-3, 10), new Point(3, 1)));
		
		Line l2 = new Line(new Point(-2, -2), new Point(-10, 12));
		assertTrue(l2.sameSide(new Point(0, 0), new Point(6, 10)));
	}

}
