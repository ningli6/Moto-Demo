package server;

import static org.junit.Assert.*;

import org.junit.Test;

import client.Client;
import utility.GridMap;
import utility.Location;
import utility.PU;
import utility.Response;

public class ServerAdditiveNoiseTest {

	Location ul = new Location(40, -90);
	Location ur = new Location(40, -85);
	Location ll = new Location(35, -90);
	Location lr = new Location(35, -85);
	GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
	
	@Test
	public void testServerAdditiveNoise() {
		ServerAdditiveNoise s = new ServerAdditiveNoise(map, 2, 100, 0.5);
		assertEquals(0.5, s.getNoiseLevel(), 0.001);
		assertEquals(0, s.getNumberOfLies());
		assertEquals(50, s.getExpectedLies());
	}
	
	@Test
	public void testResponse() {
		ServerAdditiveNoise s = new ServerAdditiveNoise(map, 1, 100, 0.5);
		PU p = new PU(0, 37.5, -87.5, map);
		s.addPU(p, 0);
		Client c = new Client(s);
		c.setLocation(500, 500);
		Response ss = s.response(c);
		assertEquals(0, s.getNumberOfLies());
		c.setLocation(520, 500);
		ss = s.response(c);
		assertTrue(c.getLocation().distTo(p.getLocation()) < 14 && c.getLocation().distTo(p.getLocation()) > 8);
		assertEquals(1, s.getNumberOfLies());
		assertEquals(0, ss.getPower(), 0.01);
		c.setLocation(540, 500);
		ss = s.response(c);
		assertTrue(c.getLocation().distTo(p.getLocation()) < 25 && c.getLocation().distTo(p.getLocation()) > 14);
		assertEquals(2, s.getNumberOfLies());
		assertEquals(0.5, ss.getPower(), 0.01);
		c.setLocation(600, 500);
		ss = s.response(c);
		assertTrue(c.getLocation().distTo(p.getLocation()) > 25);
		assertEquals(3, s.getNumberOfLies());
		assertEquals(0.75, ss.getPower(), 0.01);
	}

	@Test
	public void testUpdateLiesNeeded() {
		ServerAdditiveNoise s = new ServerAdditiveNoise(map, 2, 100, 0.5);
		assertEquals(0.5, s.getNoiseLevel(), 0.001);
		assertEquals(0, s.getNumberOfLies());
		assertEquals(50, s.getExpectedLies());
		PU p = new PU(0, 37.5, -87.5, map);
		s.addPU(p, 0);
		Client c = new Client(s);
		c.setLocation(600, 500);
		s.response(c);
		assertEquals(1, s.getNumberOfLies());
		s.updateLiesNeeded(200);
		assertEquals(0, s.getNumberOfLies());
		assertEquals(100, s.getExpectedLies());
	}

	@Test
	public void testSetNoiseLevel() {
		ServerAdditiveNoise s = new ServerAdditiveNoise(map, 2, 100, 0.5);
		assertEquals(0.5, s.getNoiseLevel(), 0.001);
		assertEquals(0, s.getNumberOfLies());
		assertEquals(50, s.getExpectedLies());
		s.setNoiseLevel(0.8);
		assertEquals(0.8, s.getNoiseLevel(), 0.001);
		assertEquals(0, s.getNumberOfLies());
		assertEquals(80, s.getExpectedLies());
	}

	@Test
	public void testReset() {
		ServerAdditiveNoise s = new ServerAdditiveNoise(map, 1, 100, 0.5);
		PU p = new PU(0, 37.5, -87.5, map);
		s.addPU(p, 0);
		Client c = new Client(s);
		c.setLocation(500, 500);
		Response ss = s.response(c);
		assertEquals(0, s.getNumberOfLies());
		c.setLocation(520, 500);
		ss = s.response(c);
		assertTrue(c.getLocation().distTo(p.getLocation()) < 14 && c.getLocation().distTo(p.getLocation()) > 8);
		assertEquals(1, s.getNumberOfLies());
		assertEquals(0, ss.getPower(), 0.01);
		c.setLocation(540, 500);
		ss = s.response(c);
		assertTrue(c.getLocation().distTo(p.getLocation()) < 25 && c.getLocation().distTo(p.getLocation()) > 14);
		assertEquals(2, s.getNumberOfLies());
		assertEquals(0.5, ss.getPower(), 0.01);
		c.setLocation(600, 500);
		ss = s.response(c);
		assertTrue(c.getLocation().distTo(p.getLocation()) > 25);
		assertEquals(3, s.getNumberOfLies());
		assertEquals(0.75, ss.getPower(), 0.01);
		s.reset();
		assertEquals(0, s.getNumberOfLies());
	}
}
