package utility;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ClusterTest {
	Location ul = new Location(40.26276066437183, -91.197509765625);
	Location ur = new Location(40.26276066437183, -81.134033203125);
	Location ll = new Location(34.137879119634185, -91.197509765625);
	Location lr = new Location(34.137879119634185, -81.134033203125);
	GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
	
	@Test
	public void testClusterPU() {
		PU pu = new PU(0, 36, -88, map);
		Cluster c = new Cluster(pu);
		assertEquals(1, c.getNumbersOfPU());
		assertSame(c, pu.getCluster());
	}

	@Test
	public void testGetNumbersOfPU() {
		PU pu = new PU(0, 36, -88, map);
		PU pu1 = new PU(1, 37, -89, map);
		List<PU> list = new LinkedList<PU>();
		list.add(pu);
		list.add(pu1);
		Cluster c = new Cluster(list);
		assertEquals(2, c.getNumbersOfPU());
		assertSame(c, pu.getCluster());
		assertSame(c, pu1.getCluster());
	}

	@Test
	public void testIsEmpty() {
		Cluster c = new Cluster();
		assertTrue(c.isEmpty());
		PU pu = new PU(0, 36, -88, map);
		Cluster c1 = new Cluster(pu);
		assertFalse(c1.isEmpty());
	}

	@Test
	public void testGetMembers() {
		PU pu = new PU(0, 36, -88, map);
		Cluster c = new Cluster(pu);
		assertTrue(!c.isEmpty());
		assertEquals(1, c.getMembers().size());
		
		PU pu2 = new PU(0, 36, -88, map);
		PU pu3 = new PU(1, 37, -89, map);
		List<PU> list = new LinkedList<PU>();
		list.add(pu2);
		list.add(pu3);
		Cluster c1 = new Cluster(list);
		assertEquals(list, c1.getMembers());
	}
	
	@Test
	public void testMerge() {
		PU pu = new PU(0, 36, -88, map);
		PU pu1 = new PU(1, 37, -89, map);
		List<PU> list = new LinkedList<PU>();
		list.add(pu);
		list.add(pu1);
		Cluster c = new Cluster(list);
		PU pu2 = new PU(0, 36, -87, map);
		PU pu3 = new PU(1, 37, -86, map);
		List<PU> list1 = new LinkedList<PU>();
		list1.add(pu2);
		list1.add(pu3);
		Cluster c1 = new Cluster(list1);
		c.merge(c1);
		assertTrue(c1.isEmpty());
		assertEquals(4, c.getNumbersOfPU());
		for (PU p : c.getMembers()) {
			assertSame(c, p.getCluster());
		}
	}

}
