package utility;

import static org.junit.Assert.*;

import org.junit.Test;

public class PUTest {

	@Test
	public void testSetIndices() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		PU pu = new PU(3, 37, -88, map);
		pu.setLocation(3, 5);
		assertEquals(3, pu.getRowIndex());
		assertEquals(5, pu.getColIndex());
	}

	@Test
	public void testGetLocation() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		PU pu = new PU(3, 37, -88, map);
		assertEquals(37, pu.getLocation().getLatitude(), 0.01);
		assertEquals(-88, pu.getLocation().getLongitude(), 0.01);
	}

	@Test
	public void testGetLatitude() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		PU pu = new PU(3, 37, -88, map);
		assertEquals(37, pu.getLocation().getLatitude(), 0.01);
	}

	@Test
	public void testGetLongitude() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		PU pu = new PU(3, 37, -88, map);
		assertEquals(-88, pu.getLocation().getLongitude(), 0.01);
	}

	@Test
	public void testDistTo() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		PU pu = new PU(3, 37, -88, map);
		PU pu2 = new PU(3, 36, -90, map);
		assertEquals(210.5, pu.distTo(pu2), 1);
		assertEquals(210.5, pu2.distTo(pu), 1);
	}

	@Test
	public void testUpdateRadius() {
		PU pu = new PU();
		pu.updateRadius(20);
		assertEquals(20, pu.getRadius(), 0.01);
	}

	@Test
	public void testIsInCluster() {
		Cluster c = new Cluster();
		PU pu = new PU();
		assertFalse(pu.isInCluster());
		PU pu2 = new PU();
		pu2.putInCluster(c);
		assertTrue(pu2.isInCluster());
	}

	@Test
	public void testGetCluster() {
		Cluster c = new Cluster();
		PU pu2 = new PU();
		pu2.putInCluster(c);
		assertTrue(pu2.isInCluster());
		assertSame(c, pu2.getCluster());
	}

}
