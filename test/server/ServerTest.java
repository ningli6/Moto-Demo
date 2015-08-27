package server;

import static org.junit.Assert.*;

import org.junit.Test;

import client.Client;
import utility.GridMap;
import utility.Location;
import utility.PU;
import utility.Response;

public class ServerTest {
	Location ul = new Location(40, -90);
	Location ur = new Location(40, -85);
	Location ll = new Location(35, -90);
	Location lr = new Location(35, -85);
	GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
	
	@Test
	public void testServer() {
		Server s = new Server(map, 3);
		assertSame(map, s.map);
		assertSame(map, s.getMap());
		assertEquals(0, s.numberOfPUs);
		assertEquals(0, s.getNumberOfPUs());
		assertEquals(3, s.numberOfChannels);
		assertEquals(3, s.getNumberOfChannels());
		assertEquals(3, s.channelsList.length);
		assertEquals(3, s.getChannelsList().length);
	}

	@Test
	public void testGetNumberOfChannels() {
		Server s = new Server(map, 2);
		assertEquals(2, s.numberOfChannels);
		assertEquals(2, s.getNumberOfChannels());
	}

	@Test
	public void testAddPU() {
		Server s = new Server(map, 2);
		try {
			s.addPU(new PU(), 0);
		} catch (NullPointerException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		s.addPU(new PU(0, 36, -89, map), 0);
		assertEquals(1, s.getNumberOfPUs());
		assertEquals(1, s.getChannelsList()[0].size());
		s.addPU(new PU(1, 36, -89, map), 1);
		assertEquals(2, s.getNumberOfPUs());
		assertEquals(1, s.getChannelsList()[1].size());
		s.addPU(new PU(2, 36, -89, map), 3);
		assertEquals(2, s.getNumberOfPUs());
		assertEquals(1, s.getChannelsList()[1].size());
	}

	@Test
	public void testResponse() {
		Server s = new Server(map, 1);
		PU p = new PU(0, 37.5, -87.5, map);
		s.addPU(p, 0);
		Client c = new Client(s);
		try {
			s.response(c);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		c.randomLocation();
		Response ss = s.response(c);
		assertSame(p, ss.getPU());
		
		PU p1 = new PU(10, 35.5, -87.5, map);
		s.addPU(p1, 0);
		c.setLocation(500, 500);
		ss = s.response(c);
		assertSame(p, ss.getPU());
		assertEquals(0, ss.getPower(), 0.001);
		c.setLocation(900, 500);
		ss = s.response(c);
		assertSame(p1, ss.getPU());
		
		s = new Server(map, 2);
		p = new PU(0, 37.5, -87.5, map);
		p1 = new PU(1, 35.5, -87.5, map);
		s.addPU(p, 0);
		s.addPU(p1, 1);
		c = new Client(s);
		c.setLocation(500, 500);
		ss = s.response(c);
		assertSame(p1, ss.getPU());
		assertEquals(1, ss.getPower(), 0.001);
	}

	@Test
	public void testReset() {
		Server s = new Server(map, 1);
		PU p = new PU(0, 37.5, -87.5, map);
		s.addPU(p, 0);
		Client c = new Client(s);
		c.randomLocation();
		c.query(s);
//		s.response(c);
		assertEquals(1, p.number_of_response);
		s.reset();
		assertEquals(0, p.number_of_response);
	}
	
	@Test
	public void testMTP() {
		Server s = new Server(map, 1);
		assertEquals(0, s.MTP(0), 0.01);
		assertEquals(0, s.MTP(5), 0.01);
		assertEquals(0.5, s.MTP(8), 0.01);
		assertEquals(0.5, s.MTP(10), 0.01);
		assertEquals(0.75, s.MTP(14), 0.01);
		assertEquals(0.75, s.MTP(20), 0.01);
		assertEquals(1, s.MTP(25), 0.01);
		assertEquals(1, s.MTP(30), 0.01);
	}
}
