package utility.geometry;

import static org.junit.Assert.*;

import org.junit.Test;

public class PolygonTest {

	@Test
	public void testPolygon() {
		Point[] plist = new Point[4];
		plist[0] = new Point(0, 10);
		plist[1] = new Point(10, 0);
		plist[2] = new Point(0, 0);
		plist[3] = new Point(10, 10);
		Polygon square = new Polygon(plist);
		assertEquals(4, square.getSides());
		assertArrayEquals(plist, square.getVertexes());
	}

	@Test
	public void testInPolygon() {
		Point[] plist = new Point[4];
		plist[0] = new Point(10, 10);
		plist[1] = new Point(-10, -10);
		plist[2] = new Point(-10, 10);
		plist[3] = new Point(10, -10);
		Polygon square = new Polygon(plist);
		assertTrue(square.inPolygon(plist[0]));
		assertTrue(square.inPolygon(plist[1]));
		assertTrue(square.inPolygon(plist[2]));
		assertTrue(square.inPolygon(plist[3]));
		assertTrue(square.inPolygon(new Point(5, 5)));
		assertTrue(square.inPolygon(new Point(0, 7)));
		assertTrue(square.inPolygon(new Point(2, 0)));
		assertTrue(square.inPolygon(new Point(5, 10)));
		assertTrue(square.inPolygon(new Point(10, 7)));
		assertTrue(square.inPolygon(new Point(0, 10)));
		assertTrue(square.inPolygon(new Point(0, -10)));
		assertTrue(square.inPolygon(new Point(-10, 7)));
		assertTrue(square.inPolygon(new Point(10, 7)));
	}

}
