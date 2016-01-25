package boot;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import utility.Location;

public class ParserTest {
	
//	-a NorthLat WestLng SouthLat EastLng -c noc (-C (lat lon)+){noc} -cm [(-no -1) (-an -val) (-tf -val) (-ka -val) (-kc -val)]+ -gm no? ad? tf? ka? kc? ((-q number_of_queries)|(-f filename)) -e email -opt pa?
	@Test
	public void testParse() {
		String args = "-cd 0.005 -a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -an 0.9 -tf 3 -kc 4 -gm no ad ka -tr tf -q 200 -e foo@vt.edu -opt";
		BootParams bp = Parser.parse(args.split(" "));
		assertEquals(0.005, bp.getCellSize(), 0.001);
		assertEquals(39.0959629363055, bp.getNorthLat(), 0.0001);
		assertEquals(-85.1385498046875, bp.getWestLng(), 0.0001);
		assertEquals(37.06394430056685, bp.getSouthLat(), 0.0001);
		assertEquals(-81.8646240234375, bp.getEastLng(), 0.0001);
		assertEquals(2, bp.getNumberOfChannels(), 0.0001);
		List<Location> ls1 = bp.getPUOnChannel(0);
		List<Location> ls2 = bp.getPUOnChannel(1);
		assertEquals(2, ls1.size(), 0.0001);
		assertEquals(38.41916639395372, ls1.get(0).getLatitude(), 0.0001);
		assertEquals(-83.7103271484375, ls1.get(0).getLongitude(), 0.0001);
		assertEquals(38.16047628099622, ls1.get(1).getLatitude(), 0.0001);
		assertEquals(-84.1937255859375, ls1.get(1).getLongitude(), 0.0001);
		assertEquals(2, ls2.size(), 0.0001);
		assertEquals(38.06539235133249, ls2.get(0).getLatitude(), 0.0001);
		assertEquals(-82.7764892578125, ls2.get(0).getLongitude(), 0.0001);
		assertEquals(37.65773212628274, ls2.get(1).getLatitude(), 0.0001);
		assertEquals(-83.0401611328125, ls2.get(1).getLongitude(), 0.0001);
		assertFalse(bp.containsCM("NOCOUNTERMEASURE"));
		assertTrue(bp.containsCM("ADDITIVENOISE"));
		assertTrue(bp.containsCM("TRANSFIGURATION"));
		assertFalse(bp.containsCM("KANONYMITY"));
		assertTrue(bp.containsCM("KCLUSTERING"));
		assertFalse(bp.containsCM("fakeCM"));
		assertEquals(bp.getCMParam("ADDITIVENOISE"), 0.9, 0.001);
		assertEquals(bp.getCMParam("TRANSFIGURATION"), 3, 0.001);
//		assertEquals(bp.getCMParam("KANONYMITY"), 1, 0.001);
		assertEquals(bp.getCMParam("KCLUSTERING"), 4, 0.001);
		assertTrue(bp.plotGooglMapNO());
		assertTrue(bp.plotGooglMapAD());
		assertFalse(bp.plotGooglMapTF());
		assertTrue(bp.plotGooglMapKA());
		assertFalse(bp.plotGooglMapKC());
		assertFalse(bp.isTradeOffAD());
		assertTrue(bp.isTradeOffTF());
		assertEquals(2, bp.getNumberOfChannels(), 0.0001);
		assertEquals(200, bp.getNumberOfQueries(), 0.0001);
		assertEquals("foo@vt.edu", bp.getEmail());
		assertFalse(bp.getInputParams());
	}

}
