package utility;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class PairTest {
	Location ul = new Location(40.26276066437183, -91.197509765625);
	Location ur = new Location(40.26276066437183, -81.134033203125);
	Location ll = new Location(34.137879119634185, -91.197509765625);
	Location lr = new Location(34.137879119634185, -81.134033203125);
	GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
	
	@Test
	public void testCompareTo() {
		PU pu1 = new PU(1, 37, -88, map);
		PU pu2 = new PU(2, 36, -85, map);
		Pair p1 = new Pair(pu1, pu2);
		PU pu3 = new PU(1, 36, -88, map);
		PU pu4 = new PU(2, 39, -85, map);
		Pair p2 = new Pair(pu3, pu4);
		PU pu5 = new PU(1, 36, -90, map);
		PU pu6 = new PU(2, 37, -89, map);
		Pair p3 = new Pair(pu5, pu6);
		List<Pair> list = new LinkedList<Pair>();
		list.add(p1);
		list.add(p2);
		list.add(p3);
		Collections.sort(list);
		for (int i = 1; i < list.size(); i++) {
			assertTrue(list.get(i).getDist() > list.get(i - 1).getDist());
		}
	}

	@Test
	public void testPair() {
		PU pu1 = new PU(1, 37, -88, map);
		PU pu2 = new PU(2, 36, -85, map);
		Pair p = new Pair(pu1, pu2);
		assertSame(pu1, p.getPU1());
		assertSame(pu2, p.getPU2());
		assertEquals(290.3, p.getDist(), 1);
	}

	@Test
	public void testGetPU1() {
		PU pu1 = new PU(1, 37, -88, map);
		PU pu2 = new PU(2, 36, -85, map);
		Pair p = new Pair(pu1, pu2);
		assertSame(pu1, p.getPU1());
	}

	@Test
	public void testGetPU2() {
		PU pu1 = new PU(1, 37, -88, map);
		PU pu2 = new PU(2, 35, -85, map);
		Pair p = new Pair(pu1, pu2);
		assertSame(pu1, p.getPU1());
		assertSame(pu2, p.getPU2());
		assertEquals(349.7, p.getDist(), 1);
	}

	@Test
	public void testSamePair() {
		PU pu1 = new PU(1, 37, -88, map);
		PU pu2 = new PU(2, 35, -85, map);
		PU pu3 = new PU(3, 39, -90, map);
		Pair p = new Pair(pu1, pu2);
		Pair p1 = new Pair(pu1, pu2);
		Pair p2 = new Pair(pu2, pu1);
		Pair p3 = new Pair(pu1, pu3);
		assertTrue(p.samePair(p));
		assertTrue(p.samePair(p1));
		assertTrue(p.samePair(p2));
		assertFalse(p.samePair(p3));
	}

	@Test
	public void testContains() {
		PU pu1 = new PU(1, 37, -88, map);
		PU pu2 = new PU(2, 35, -85, map);
		PU pu3 = new PU(3, 39, -90, map);
		Pair p = new Pair(pu1, pu2);
		assertTrue(p.contains(pu1));
		assertTrue(p.contains(pu2));
		assertFalse(p.contains(pu3));
	}

	@Test
	public void testGetDist() {
		PU pu1 = new PU(1, 37, -88, map);
		PU pu2 = new PU(2, 35, -85, map);
		Pair p = new Pair(pu1, pu2);
		assertEquals(349.7, p.getDist(), 1);
	}

}
