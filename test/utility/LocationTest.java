package utility;

import static org.junit.Assert.*;

import org.junit.Test;

public class LocationTest {

	@Test
	public void testSetLocationDoubleDouble() {
		Location loc = new Location();
		loc.setLocation(1, 2);
		assertEquals(1, loc.getLatitude(), 0.001);
		assertEquals(2, loc.getLongitude(), 0.001);
	}

	@Test
	public void testGetLatitude() {
		Location loc = new Location();
		loc.setLocation(1, 2);
		assertEquals(1, loc.getLatitude(), 0.001);
	}

	@Test
	public void testGetLongitude() {
		Location loc = new Location();
		loc.setLocation(1, 2);
		assertEquals(2, loc.getLongitude(), 0.001);
	}

	@Test
	public void testDistTo() {
		Location loc = new Location(43, -111);
		Location loc2 = new Location(30, -80);
		assertEquals(3100, loc.distTo(loc2), 10);
		assertEquals(3100, loc2.distTo(loc), 10);
	}

}
