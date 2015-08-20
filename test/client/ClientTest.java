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
	Server server = new Server(map, 2);
	
	@Test
	public void testClient() {
		server.addPU(new PU(0, 36, -88, map), 0);
		server.addPU(new PU(1, 38, -86, map), 1);
		Client client = new Client(server);
		assertEquals(2, client.numberOfChannels);
	}

	@Test
	public void testSetLocation() {
		Client client = new Client(server);
		client.setLocation(500, 300);
		assertEquals(500, client.getRowIndex());
		assertEquals(300, client.getColIndex());
		assertEquals(37.4975, client.getLocation().getLatitude(), 0.01);
		assertEquals(-88.4975, client.getLocation().getLongitude(), 0.01);
	}

	@Test
	public void testRandomLocation() {
		Client client = new Client(server);
		for (int i = 0; i < 1000; i++) {
			client.randomLocation();
			assertTrue(client.getRowIndex() < map.getRows());
			assertTrue(client.getColIndex() < map.getCols());
		}
	}

	@Test
	public void testGetLocation() {
		Client client = new Client(server);
		client.setLocation(0, 999);
		assertEquals(0, client.getRowIndex());
		assertEquals(999, client.getColIndex());
		assertEquals(39.9975, client.getLocation().getLatitude(), 0.01);
		assertEquals(-85.0025, client.getLocation().getLongitude(), 0.01);
	}

	@Test
	public void testGetRowIndex() {
		Client client = new Client(server);
		client.setLocation(223, 344);
		assertEquals(223, client.getRowIndex());
		assertFalse(344 == client.getRowIndex());
	}

	@Test
	public void testGetColIndex() {
		Client client = new Client(server);
		client.setLocation(223, 344);
		assertEquals(344, client.getColIndex());
		assertFalse(223 == client.getColIndex());
	}

	@Test
	public void testQuery() {
		PU pu = new PU(0, 37.5, -87.5, map);
		server.addPU(pu, 1);
		Client client = new Client(server);
		client.setLocation(500, 500);
		System.out.println(pu.getLocation().distTo(client.getLocation()));
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
		System.out.println(pu.getLocation().distTo(client.getLocation()));
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
	}

	@Test
	public void testComputeIC() {
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
		PU pu = new PU(0, 39.5, -87.5, map);
		server.addPU(pu, 0);
		PU pu1 = new PU(1, 35.5, -87.5, map);
		server.addPU(pu1, 1);
		Client client = new Client(server);
		assertTrue(client.distanceToClosestPU(0, 100, 500) < client.distanceToClosestPU(1, 100, 500));
		assertTrue(client.distanceToClosestPU(0, 900, 500) > client.distanceToClosestPU(1, 900, 500));
	}

	@Test
	public void testReset() {
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
