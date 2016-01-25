package server;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import client.Client;
import utility.GridMap;
import utility.Location;
import utility.PU;
import utility.PolyPU;
import utility.Response;

public class ServerTransfigurationTest {
	
	Location ul = new Location(40, -90);
	Location ur = new Location(40, -85);
	Location ll = new Location(35, -90);
	Location lr = new Location(35, -85);
	GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
	
	@Test
	public void testServerTransfiguration() {
		ServerTransfiguration s = new ServerTransfiguration(map, 2, 3);
		assertEquals(2, s.getChannelPolyPU().length);
		for (List<PolyPU> list : s.getChannelPolyPU()) {
			assertTrue(list.isEmpty());
		}
	}
	
	@Test
	public void testTransfigure() {
		ServerTransfiguration s = new ServerTransfiguration(map, 1, 3);
		PU p = new PU(0, 37.5, -87.5, map);
		s.addPU(p, 0);
		s.transfigure();
		assertEquals(1, (s.getChannelPolyPU())[0].size());
		
		s = new ServerTransfiguration(map, 2, 4);
		s.addPU(new PU(0, 37.5, -87.5, map), 0);
		s.addPU(new PU(1, 37.5, -87.5, map), 1);
		s.transfigure();
		assertEquals(1, (s.getChannelPolyPU())[0].size());
		assertEquals(1, (s.getChannelPolyPU())[1].size());
	}
	
	@Test
	public void testTransfigureInt() {
		ServerTransfiguration s = new ServerTransfiguration(map, 2, 4);
		s.addPU(new PU(0, 37.5, -87.5, map), 0);
		s.addPU(new PU(1, 37.5, -87.5, map), 1);
		s.transfigure();
		assertEquals(1, (s.getChannelPolyPU())[0].size());
		assertEquals(1, (s.getChannelPolyPU())[1].size());
		s.transfigure(5);
		assertEquals(1, (s.getChannelPolyPU())[0].size());
		assertEquals(1, (s.getChannelPolyPU())[1].size());
	}

	
	@Test
	public void testResponse() {
		ServerTransfiguration s = new ServerTransfiguration(map, 1, 3);
		PU p = new PU(0, 37.5, -87.5, map);
		s.addPU(p, 0);
		s.transfigure();
		Client c = new Client(s);
		c.setLocation(500, 500);
		Response ss = s.response(c);
		assertEquals(ss.getPower(), 0, 0.001);
		c.setLocation(480, 500);
		ss = s.response(c);
		assertEquals(ss.getPower(), 0.5, 0.001);
		c.setLocation(460, 500);
		ss = s.response(c);
		assertEquals(ss.getPower(), 0.75, 0.001);
		c.setLocation(400, 500);
		ss = s.response(c);
		assertEquals(ss.getPower(), 1, 0.001);
		
		s = new ServerTransfiguration(map, 1, 3);
		PU p3 = new PU(0, 37.5, -87.5, map);
		PU p4 = new PU(1, 35.5, -87.5, map);
		s.addPU(p3, 0);
		s.addPU(p4, 0);
		s.transfigure();
		c = new Client(s);
		c.setLocation(500, 500);
		ss = s.response(c);
		assertSame(p3, ss.getPU());
		assertEquals(0, ss.getPower(), 0.01);
		
		s = new ServerTransfiguration(map, 2, 3);
		PU p0 = new PU(0, 37.5, -87.5, map);
		PU p1 = new PU(1, 35.5, -87.5, map);
		s.addPU(p0, 0);
		s.addPU(p1, 1);
		s.transfigure();
		c = new Client(s);
		c.setLocation(500, 500);
		ss = s.response(c);
		assertSame(p1, ss.getPU());
		assertEquals(1, ss.getPower(), 0.01);
	}
}
