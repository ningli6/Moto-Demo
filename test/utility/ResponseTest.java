package utility;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ResponseTest {

	@Test
	public void testGetChannelID() {
		PU p = new PU();
		p.setID(3);
		p.setChannelID(1);
		Response r = new Response(p, 14);
		assertEquals(1, r.getChannelID());
	}

	@Test
	public void testGetPower() {
		PU p = new PU();
		p.setID(3);
		p.setChannelID(1);
		Response r = new Response(p, 14);
		assertEquals(14, r.getPower(), 0.001);
	}

	@Test
	public void testGetPUID() {
		PU p = new PU();
		p.setID(3);
		p.setChannelID(1);
		Response r = new Response(p, 14);
		assertEquals(3, r.getPUID());
	}

	@Test
	public void testDecrease() {
		PU p = new PU();
		p.setID(3);
		p.setChannelID(1);
		Response r = new Response(p, MTP.P_100);
		r.decrease(1);
		assertEquals(MTP.P_75, r.getPower(), 0.001);
		Response r1 = new Response(p, MTP.P_75);
		r1.decrease(1);
		assertEquals(MTP.P_50, r1.getPower(), 0.001);
		Response r2 = new Response(p, MTP.P_50);
		r2.decrease(1);
		assertEquals(MTP.P_0, r2.getPower(), 0.001);
		Response r3 = new Response(p, MTP.P_0);
		r3.decrease(1);
		assertEquals(MTP.P_0, r3.getPower(), 0.001);
	}

	@Test
	public void testCompareTo() {
		PU p = new PU();
		p.setID(3);
		p.setChannelID(1);
		Response r = new Response(p, MTP.P_100);
		Response r1 = new Response(p, MTP.P_75);
		Response r2 = new Response(p, MTP.P_50);
		Response r3 = new Response(p, MTP.P_0);
		List<Response> ls = new LinkedList<Response>();
		ls.add(r);
		ls.add(r3);
		ls.add(r1);
		ls.add(r2);
		Collections.sort(ls);
		for (int x = 1; x < ls.size(); x++) {
			assertTrue(ls.get(x).getPower() > ls.get(x - 1).getPower());
		}
	}

}
