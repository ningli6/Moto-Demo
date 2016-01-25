package server;

import static org.junit.Assert.*;

import org.junit.Test;

import client.Client;
import utility.GridMap;
import utility.Location;
import utility.PU;
import utility.Response;

public class ServerKAnonymityTest {

	Location ul = new Location(40, -90);
	Location ur = new Location(40, -85);
	Location ll = new Location(35, -90);
	Location lr = new Location(35, -85);
	GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
	
	@Test
	public void testServerKAnonymity() {
		ServerKAnonymity s = new ServerKAnonymity(map, 2, 2);
		assertEquals(2, s.getK());
		assertEquals(2, s.getVirtualChannelList().length);
		assertEquals(0, s.getNumberOfVPUs());
	}
	
	@Test
	public void testSetK() {
		ServerKAnonymity s = new ServerKAnonymity(map, 1, 2);
		s.addPU(new PU(0, 37.5, -87.5, map), 0);
		s.addPU(new PU(1, 36.5, -86.5, map), 0);
		s.groupKPUs();
		assertEquals(1, s.getNumberOfVPUs());
		s.setK(1);
		assertEquals(2, s.getNumberOfVPUs());
		assertEquals(1, s.getVirtualChannelList().length);
	}
	
	@Test
	public void testGroupKPUs() {
		ServerKAnonymity s = new ServerKAnonymity(map, 2, 2);
		s.addPU(new PU(0, 37.5, -87.5, map), 0);
		s.addPU(new PU(1, 36.5, -86.5, map), 0);
		s.addPU(new PU(2, 37.5, -87.5, map), 1);
		s.addPU(new PU(3, 36.5, -86.5, map), 1);
		s.groupKPUs();
		assertEquals(2, s.getNumberOfVPUs());
		assertEquals(1, s.getVirtualChannelList()[0].size());
		assertEquals(1, s.getVirtualChannelList()[1].size());
		System.out.println("Distance between 2 pu: " + new PU(0, 37.5, -87.5, map).getLocation().distTo(new PU(1, 36.5, -86.5, map).getLocation()) + ", radius: " + s.getVirtualChannelList()[0].get(0).getRadius());
		
		s = new ServerKAnonymity(map, 1, 5);
		s.addPU(new PU(0, 37.5, -87.5, map), 0);
		s.groupKPUs();
		assertEquals(1, s.getNumberOfVPUs());
		assertEquals(0, s.getVirtualChannelList()[0].get(0).getRadius(), 0.01);
		
		s = new ServerKAnonymity(map, 1, 5);
		s.addPU(new PU(0, 37.5, -87.5, map), 0);
		s.addPU(new PU(1, 36.5, -87.5, map), 0);
		s.groupKPUs();
		assertEquals(1, s.getNumberOfVPUs());
	}
	
	@Test
	public void testResponse() {
		ServerKAnonymity s = new ServerKAnonymity(map, 1, 2);
		s.addPU(new PU(0, 35.5, -87.5, map), 0);
		s.addPU(new PU(1, 35.6, -87.5, map), 0);
		s.addPU(new PU(2, 39.5, -87.5, map), 0);
		s.addPU(new PU(3, 39.3, -87.5, map), 0);
		s.groupKPUs();
		assertEquals(2, s.getNumberOfVPUs());
		Client c = new Client(s);
		c.setLocation(100, 500);
		Response ss = s.response(c);
		assertEquals(0, ss.getPower(), 0.01);
		
		s = new ServerKAnonymity(map, 2, 2);
		s.addPU(new PU(0, 35.5, -87.5, map), 0);
		s.addPU(new PU(1, 35.6, -87.5, map), 0);
		s.addPU(new PU(2, 39.5, -87.5, map), 1);
		s.addPU(new PU(3, 39.3, -87.5, map), 1);
		s.groupKPUs();
		assertEquals(2, s.getNumberOfVPUs());
		c = new Client(s);
		c.setLocation(100, 500);
		ss = s.response(c);
		assertEquals(1, ss.getPower(), 0.01);
	}
	
	@Test
	public void testVirtualMTP() {
		ServerKAnonymity s = new ServerKAnonymity(map, 1, 2);
		assertEquals(0, s.virtualMTP(5, 0), 0.001);
		assertEquals(0.5, s.virtualMTP(10, 0), 0.001);
		assertEquals(0.75, s.virtualMTP(20, 0), 0.001);
		assertEquals(1, s.virtualMTP(30, 0), 0.001);
		
		assertEquals(0, s.virtualMTP(27, 20), 0.001);
		assertEquals(0.5, s.virtualMTP(30, 20), 0.001);
		assertEquals(0.75, s.virtualMTP(35, 20), 0.001);
		assertEquals(1, s.virtualMTP(46, 20), 0.001);
	}
}
