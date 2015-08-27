package client;

import static org.junit.Assert.*;

import org.junit.Test;

import server.Server;
import utility.GridMap;
import utility.Location;
import utility.PU;

public class ClientTest {
	Location ul = new Location(40, -90);
	Location ur = new Location(40, -85);
	Location ll = new Location(35, -90);
	Location lr = new Location(35, -85);
	GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
	
	
	@Test
	public void testClient() {
		Server server = new Server(map, 2);
		server.addPU(new PU(0, 36, -88, map), 0);
		server.addPU(new PU(1, 38, -86, map), 1);
		Client client = new Client(server);
		assertEquals(2, client.getNumberOfChannels());
		
		Server server1 = new Server(map, 1);
		Client client1 = new Client(server1);
		assertEquals(1, client1.getNumberOfChannels());
	}

	@Test
	public void testSetLocation() {
		Server server = new Server(map, 2);
		Client client = new Client(server);
		client.setLocation(500, 300);
		assertEquals(500, client.getRowIndex());
		assertEquals(300, client.getColIndex());
		assertEquals(37.4975, client.getLocation().getLatitude(), 0.01);
		assertEquals(-88.4975, client.getLocation().getLongitude(), 0.01);
		
		client.setLocation(0, 0);
		assertEquals(0, client.getRowIndex());
		assertEquals(0, client.getColIndex());
		assertEquals(39.9975, client.getLocation().getLatitude(), 0.01);
		assertEquals(-89.9975, client.getLocation().getLongitude(), 0.01);
		
		client.setLocation(999, 999);
		assertEquals(999, client.getRowIndex());
		assertEquals(999, client.getColIndex());
		assertEquals(35.0025, client.getLocation().getLatitude(), 0.01);
		assertEquals(-85.0025, client.getLocation().getLongitude(), 0.01);
		
		try {
			client.setLocation(50, 1000);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			throw e;
		}
		try {
			client.setLocation(1000, 50);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			throw e;
		}
		
		try {
			client.setLocation(1203, 999);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			throw e;
		}
	}

	@Test
	public void testRandomLocation() {
		Server server = new Server(map, 2);
		Client client = new Client(server);
		for (int i = 0; i < 1000; i++) {
			client.randomLocation();
			assertTrue(client.getRowIndex() < map.getRows());
			assertTrue(client.getColIndex() < map.getCols());
		}
	}

	@Test
	public void testGetLocation() {
		Server server = new Server(map, 2);
		Client client = new Client(server);
		client.setLocation(0, 999);
		assertEquals(0, client.getRowIndex());
		assertEquals(999, client.getColIndex());
		assertEquals(39.9975, client.getLocation().getLatitude(), 0.01);
		assertEquals(-85.0025, client.getLocation().getLongitude(), 0.01);
		
		client.setLocation(10, 20);
		assertEquals(10, client.getRowIndex());
		assertEquals(20, client.getColIndex());
		assertEquals(39.9475, client.getLocation().getLatitude(), 0.01);
		assertEquals(-89.8975, client.getLocation().getLongitude(), 0.01);
		
		Client c2 = new Client(server);
		assertEquals(0, c2.getLocation().getLatitude(), 0.001);
		assertEquals(0, c2.getLocation().getLongitude(), 0.001);
	}

	@Test
	public void testGetRowIndex() {
		Server server = new Server(map, 2);
		Client client = new Client(server);
		client.setLocation(223, 344);
		assertEquals(223, client.getRowIndex());
		assertFalse(344 == client.getRowIndex());
	}

	@Test
	public void testGetColIndex() {
		Server server = new Server(map, 2);
		Client client = new Client(server);
		client.setLocation(223, 344);
		assertEquals(344, client.getColIndex());
		assertFalse(223 == client.getColIndex());
	}

	@Test
	public void testQuery() {
		Server server = new Server(map, 2);
		PU pu = new PU(0, 37.5, -87.5, map);
		server.addPU(pu, 1);
		Client client = new Client(server);
		client.setLocation(500, 500);
//		System.out.println(pu.getLocation().distTo(client.getLocation()));
		client.query(server);
		assertEquals(1, client.count[1]);
		assertEquals(0, client.d1, 0.001);
		assertEquals(8, client.d2, 0.001);
		client.setLocation(200, 200);
		client.query(server);
		assertEquals(2, client.count[1]);
		assertEquals(25, client.d1, 0.001);
		assertEquals(25, client.d2, 0.001);
		client.setLocation(520, 500);
//		System.out.println(pu.getLocation().distTo(client.getLocation()));
		assertTrue(pu.getLocation().distTo(client.getLocation()) < 14 && pu.getLocation().distTo(client.getLocation()) > 8);
		client.query(server);
		assertEquals(3, client.count[1]);
		assertEquals(8, client.d1, 0.001);
		assertEquals(14, client.d2, 0.001);
		client.setLocation(540, 500);
		assertTrue(pu.getLocation().distTo(client.getLocation()) < 25 && pu.getLocation().distTo(client.getLocation()) > 14);
		client.query(server);
		assertEquals(4, client.count[1]);
		assertEquals(14, client.d1, 0.001);
		assertEquals(25, client.d2, 0.001);
		
		Server s1 = new Server(map, 1);
		PU pu1 = new PU(2, 37.5, -87.5, map);
		s1.addPU(pu1, 0);
		Client c1 = new Client(s1);
		for (int i = 0; i < map.getRows(); i += 50) {
			for (int j = 0; j < map.getCols(); j += 50) {
				c1.setLocation(i, j);
				c1.query(s1);
				double dist = c1.getLocation().distTo(pu1.getLocation());
				if (dist < 8) {
					assertEquals(0, c1.d1, 0.001);
					assertEquals(8, c1.d2, 0.001);
				}
				else if (dist < 14) {
					assertEquals(8, c1.d1, 0.001);
					assertEquals(14, c1.d2, 0.001);	
				}
				else if (dist < 25) {
					assertEquals(14, c1.d1, 0.001);
					assertEquals(25, c1.d2, 0.001);	
				}
				else if (dist > 25) {
//					System.out.println(c1.d1);
					assertEquals(25, c1.d1, 0.001);
					assertEquals(25, c1.d2, 0.001);	
				}
			}
		}
	}

	@Test
	public void testComputeIC() {
		Server server = new Server(map, 2);
		PU pu = new PU(0, 37.5, -87.5, map);
		server.addPU(pu, 1);
		PU pu1 = new PU(1, 35.5, -87.5, map);
		server.addPU(pu1, 0);
		Client client = new Client(server);
//		assertEquals(client.computeIC()[0], client.computeIC()[1], 0.01);
		assertEquals(client.computeIC().length, 2);
		client.setLocation(500, 500);
		client.query(server);
		assertTrue(client.computeIC()[0] > client.computeIC()[1]);
	}
	
	@Test
	public void testDistanceToClosestPU() {
		Server server = new Server(map, 2);
		PU pu = new PU(0, 39.5, -87.5, map);
		server.addPU(pu, 0);
		PU pu1 = new PU(1, 35.5, -87.5, map);
		server.addPU(pu1, 1);
		Client client = new Client(server);
		assertTrue(client.distanceToClosestPU(0, 100, 500) < client.distanceToClosestPU(1, 100, 500));
		assertTrue(client.distanceToClosestPU(0, 900, 500) > client.distanceToClosestPU(1, 900, 500));
		
		Server s1 = new Server(map, 1);
		Client c1 = new Client(s1);
		s1.addPU(new PU(0, 39, -89, map), 0);
		s1.addPU(new PU(1, 39, -86, map), 0);
		s1.addPU(new PU(2, 36, -89, map), 0);
		s1.addPU(new PU(3, 36, -86, map), 0);
		c1.setLocation(100, 100);
		assertEquals(c1.distanceToClosestPU(0, 100, 100), c1.getLocation().distTo(new PU(0, 39, -89, map).getLocation()), 0.01);
		c1.setLocation(100, 900);
		assertEquals(c1.distanceToClosestPU(0, 100, 900), c1.getLocation().distTo(new PU(0, 39, -86, map).getLocation()), 0.01);
		c1.setLocation(900, 100);
		assertEquals(c1.distanceToClosestPU(0, 900, 100), c1.getLocation().distTo(new PU(0, 36, -89, map).getLocation()), 0.01);
		c1.setLocation(900, 900);
		assertEquals(c1.distanceToClosestPU(0, 900, 900), c1.getLocation().distTo(new PU(0, 36, -86, map).getLocation()), 0.01);
	}

	@Test
	public void testReset() {
		Server server = new Server(map, 2);
		PU pu = new PU(0, 37.5, -87.5, map);
		server.addPU(pu, 1);
		Client client = new Client(server);
		client.setLocation(500, 500);
		client.query(server);
		client.reset();
		for (int i = 0; i < 2; i++) {
			assertTrue(client.count[i] == 0);
		}
	}
}
