package utility;

import static org.junit.Assert.*;

import org.junit.Test;

public class GridMapTest {

	@Test
	public void testGetLength() {
//		40.26276066437183 -91.197509765625 35.137879119634185 -81.134033203125
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(853.4, map.getLength(), 1);
	}

	@Test
	public void testGetHeight() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(569.9, map.getHeight(), 1);
	}

	@Test
	public void testGetRows() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(1025, map.getRows(), 1);
	}

	@Test
	public void testGetCols() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(2012, map.getCols(), 1);
	}
	
	@Test
	public void testgetCellDegree() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(0.005, map.getCellDegree(), 0.00001);
	}

	@Test
	public void testGetTopLeftCorner() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertSame(ul, map.getTopLeftCorner());
	}

	@Test
	public void testGetTopRightCorner() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertSame(ur, map.getTopRightCorner());
	}

	@Test
	public void testGetBotLeftCorner() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertSame(ll, map.getBotLeftCorner());
	}

	@Test
	public void testGetBotRightCorner() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertSame(lr, map.getBotRightCorner());
	}

	@Test
	public void testWithInBoundaryLocation() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		Location loc = new Location(37, -85);
		assertTrue(map.withInBoundary(loc));
		Location loc2 = new Location(37, -100);
		assertFalse(map.withInBoundary(loc2));
		Location loc3 = new Location(33, -88);
		assertFalse(map.withInBoundary(loc3));
		Location loc4 = new Location(42, -79);
		assertFalse(map.withInBoundary(loc4));
	}

	@Test
	public void testGetWestBound() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(-91.197509765625, map.getWestBound(), 0.001);
	}

	@Test
	public void testGetEastBound() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(-81.134033203125, map.getEastBound(), 0.01);
	}

	@Test
	public void testGetTopBound() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(40.26276066437183, map.getTopBound(), 0.001);
	}

	@Test
	public void testGetBotBound() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(35.137879119634185, map.getBotBound(), 0.01);
	}

	@Test
	public void testRowToLat() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(map.getBotBound(), map.rowToLat(map.getRows() - 1), 0.01);
		assertEquals(map.getTopBound(), map.rowToLat(0), 0.01);
	}

	@Test
	public void testColToLng() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(map.getWestBound(), map.colToLng(0), 0.01);
		assertEquals(map.getEastBound(), map.colToLng(map.getCols() - 1), 0.01);
	}

	@Test
	public void testLatToRow() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(map.getRows() - 1, map.latToRow(map.rowToLat(map.getRows() - 1)), 0.01);
		assertEquals(0, map.latToRow(map.rowToLat(0)), 0.01);
	}

	@Test
	public void testLngToCol() {
		Location ul = new Location(40.26276066437183, -91.197509765625);
		Location ur = new Location(40.26276066437183, -81.134033203125);
		Location ll = new Location(35.137879119634185, -91.197509765625);
		Location lr = new Location(35.137879119634185, -81.134033203125);
		GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
		assertEquals(map.getCols() - 1, map.lngToCol(map.colToLng(map.getCols() - 1)), 0.01);
		assertEquals(0, map.lngToCol(map.colToLng(0)), 0.01);
	}

}
