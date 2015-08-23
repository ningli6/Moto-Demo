package server;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utility.GridMap;
import utility.Location;
import utility.PU;
import utility.Response;
import client.Client;

public class ServerKClusteringTest {
	
	Location ul = new Location(40, -90);
	Location ur = new Location(40, -85);
	Location ll = new Location(35, -90);
	Location lr = new Location(35, -85);
	GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
	
	@Test
	public void testServerKClustering() {
		ServerKClustering s = new ServerKClustering(map, 2, 3);
		assertEquals(3, s.getK());
		assertEquals(2, s.getVirtualList().length);
		assertEquals(0, s.getNumberOfVPUs());
	}

	@Test
	public void testClustering() {
		ServerKClustering s = new ServerKClustering(map, 1, 3);
		assertEquals(3, s.getK());
		assertEquals(1, s.getVirtualList().length);
		assertEquals(0, s.getNumberOfVPUs());
		PU p = new PU(0, 37.5, -87.5, map);
		s.addPU(p, 0);
		s.clustering();
		assertEquals(1, s.getNumberOfVPUs());
		
		s = new ServerKClustering(map, 1, 2);
		PU p1 = new PU(1, 37.6, -87.5, map);
		PU p2 = new PU(2, 37.5, -87.6, map);
		s.addPU(p1, 0);
		s.addPU(p2, 0);
		s.clustering();
		assertEquals(2, s.getNumberOfVPUs());
		
		s = new ServerKClustering(map, 1, 2);
		s.addPU(new PU(1, 35.6, -87.5, map), 0);
		s.addPU(new PU(2, 35.9, -87.5, map), 0);
		s.addPU(new PU(2, 39.5, -87.6, map), 0);
		s.clustering();
		assertEquals(2, s.getNumberOfVPUs());
		/* check manually */
//		for (List<PU> puList : s.getVirtualList()) {
//			for (PU pu : puList) {
//				pu.printIndexLocation();
//				System.out.println(pu.getRadius());
//			}
//		}
		
		s = new ServerKClustering(map, 2, 2);
		s.addPU(new PU(1, 39.5, -89.5, map), 0);
		s.addPU(new PU(2, 39.6, -89.7, map), 0);
		s.addPU(new PU(3, 35.5, -85.5, map), 0);
		s.addPU(new PU(4, 35.6, -85.6, map), 0);
		s.addPU(new PU(5, 39.6, -85.5, map), 1);
		s.addPU(new PU(6, 39.9, -85.4, map), 1);
		s.addPU(new PU(7, 35.5, -89.6, map), 1);
		s.addPU(new PU(8, 35.3, -89.7, map), 1);
		s.clustering();
		assertEquals(4, s.getNumberOfVPUs());
		/* check manually */
//		for (List<PU> puList : s.getVirtualList()) {
//			for (PU pu : puList) {
//				pu.printIndexLocation();
//				System.out.println(pu.getRadius());
//			}
//			System.out.println();
//		}
		
		s = new ServerKClustering(map, 1, 1);
		s.addPU(new PU(1, 39.5, -87.5, map), 0);
		s.addPU(new PU(2, 35.5, -87.5, map), 0);
		s.clustering();
		assertEquals(1, s.getNumberOfVPUs());
		/* check manually */
//		s.getVirtualList()[0].get(0).printVirtualPUInfo();
//		System.out.println(new PU(1, 39.5, -87.5, map).distTo(new PU(2, 35.5, -87.5, map)));
	}
	
	@Test
	public void testSetK() {
		ServerKClustering s = new ServerKClustering(map, 1, 1);
		s.addPU(new PU(1, 39.5, -89.5, map), 0);
		s.addPU(new PU(2, 39.6, -89.7, map), 0);
		s.addPU(new PU(0, 35.5, -85.5, map), 0);
		s.clustering();
		assertEquals(1, s.getNumberOfVPUs());
		s.setK(2);
		assertEquals(2, s.getNumberOfVPUs());
		s.setK(3);
		assertEquals(3, s.getNumberOfVPUs());
		s.setK(4);
		assertEquals(3, s.getNumberOfVPUs());
	}
	
	@Test
	public void testResponse() {
		ServerKClustering s = new ServerKClustering(map, 1, 1);
		s.addPU(new PU(1, 37.5, -87.5, map), 0);
//		s.addPU(new PU(2, 35.5, -87.5, map), 0);
		s.clustering();
		Client c = new Client(s);
		c.setLocation(500, 500);
		Response ss = s.response(c);
		assertEquals(0, ss.getPower(), 0.01);
		c.setLocation(520, 500);
		ss = s.response(c);
		assertEquals(0.5, ss.getPower(), 0.01);
		c.setLocation(540, 500);
		ss = s.response(c);
		assertEquals(0.75, ss.getPower(), 0.01);
		c.setLocation(560, 500);
		ss = s.response(c);
		assertEquals(1, ss.getPower(), 0.01);
		
		s = new ServerKClustering(map, 1, 2);
		s.addPU(new PU(1, 35.5, -87.6, map), 0);
		s.addPU(new PU(2, 35.5, -87.5, map), 0);
		s.addPU(new PU(3, 39.5, -87.6, map), 0);
		s.addPU(new PU(4, 39.5, -87.5, map), 0);
		s.clustering();
		c = new Client(s);
		c.setLocation(100, 500);
		ss = s.response(c);
		assertEquals(0, ss.getPower(), 0.01);
		c.setLocation(900, 500);
		ss = s.response(c);
		assertEquals(0, ss.getPower(), 0.01);
		
		s = new ServerKClustering(map, 2, 1);
		s.addPU(new PU(1, 35.5, -87.5, map), 0);
		s.addPU(new PU(2, 35.6, -87.5, map), 0);
		s.addPU(new PU(3, 39.6, -87.5, map), 1);
		s.addPU(new PU(4, 39.5, -87.5, map), 1);
		s.clustering();
		c = new Client(s);
		c.setLocation(100, 500);
		ss = s.response(c);
		assertEquals(1, ss.getPower(), 0.01);
		c.setLocation(900, 500);
		ss = s.response(c);
		assertEquals(1, ss.getPower(), 0.01);
	}

	@Test
	public void testVirtualMTP() {
		ServerKClustering s = new ServerKClustering(map, 1, 2);
		assertEquals(0, s.virtualMTP(5, 0), 0.001);
		assertEquals(0.5, s.virtualMTP(10, 0), 0.001);
		assertEquals(0.75, s.virtualMTP(20, 0), 0.001);
		assertEquals(1, s.virtualMTP(30, 0), 0.001);
		
		assertEquals(0, s.virtualMTP(27, 20), 0.001); // 28
		assertEquals(0.5, s.virtualMTP(29, 20), 0.001); // 28, 34
		assertEquals(0.75, s.virtualMTP(35, 20), 0.001);// 34, 45
		assertEquals(1, s.virtualMTP(46, 20), 0.001);   // 45
	}
}
