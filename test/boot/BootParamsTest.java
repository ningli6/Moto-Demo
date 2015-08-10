package boot;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import utility.Location;
import boot.BootParams;
import boot.Parser;

public class BootParamsTest {

	@Test
	public void testGetNorthLat() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertEquals(39.0959629363055, bp.getNorthLat(), 0.0001);
	}

	@Test
	public void testSetNorthLat() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setNorthLat(1.5);
		assertEquals(1.5, bp.getNorthLat(), 0.0001);
	}

	@Test
	public void testGetSouthLat() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertEquals(37.06394430056685, bp.getSouthLat(), 0.0001);
	}

	@Test
	public void testSetSouthLat() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setSouthLat(1.5);;
		assertEquals(1.5, bp.getSouthLat(), 0.0001);
	}

	@Test
	public void testGetEastLng() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertEquals(-81.8646240234375, bp.getEastLng(), 0.0001);
	}

	@Test
	public void testSetEastLng() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setEastLng(1.5);;
		assertEquals(1.5, bp.getEastLng(), 0.0001);
	}

	@Test
	public void testGetWestLng() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertEquals(-85.1385498046875, bp.getWestLng(), 0.0001);
	}

	@Test
	public void testSetWestLng() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setWestLng(1.5);
		assertEquals(1.5, bp.getWestLng(), 0.0001);
	}

	@Test
	public void testGetPUOnChannel() {
		int noc = 2;
		@SuppressWarnings("unchecked")
		List<Location>[] puLocations = (List<Location>[]) new List[noc];
		for (int j = 0; j < puLocations.length; j++) {
			puLocations[j] = new LinkedList<Location>();
		}
		puLocations[0].add(new Location(0, 0));
		puLocations[1].add(new Location(1, 1));
		BootParams bp = new BootParams();
		bp.setNumberOfChannels(noc);
		bp.setPUonChannels(puLocations);
		assertSame(puLocations[0], bp.getPUOnChannel(0));
		assertSame(puLocations[1], bp.getPUOnChannel(1));
	}

	@Test
	public void testPutCountermeasure() {
		BootParams bp = new BootParams();
		bp.putCountermeasure("fakeCM", 0);
		assertTrue(bp.containsCM("fakeCM"));
		assertEquals(0, bp.getCMParam("fakeCM"), 0.0001);
	}

	@Test
	public void testDelCountermeasure() {
		BootParams bp = new BootParams();
		bp.putCountermeasure("fakeCM", 0);
		bp.delCountermeasure("fakeCM");
		assertFalse(bp.containsCM("fakeCM"));
	}

	@Test
	public void testContainsCM() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertTrue(bp.containsCM("NOCOUNTERMEASURE"));
		assertTrue(bp.containsCM("ADDITIVENOISE"));
		assertTrue(bp.containsCM("TRANSFIGURATION"));
		assertTrue(bp.containsCM("KANONYMITY"));
		assertTrue(bp.containsCM("KCLUSTERING"));
		assertFalse(bp.containsCM("fakeCM"));
	}

	@Test
	public void testGetCMParam() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertEquals(bp.getCMParam("ADDITIVENOISE"), 0.5, 0.001);
		assertEquals(bp.getCMParam("TRANSFIGURATION"), 3, 0.001);
		assertEquals(bp.getCMParam("KANONYMITY"), 1, 0.001);
		assertEquals(bp.getCMParam("KCLUSTERING"), 1, 0.001);
	}

	@Test
	public void testPlotGooglMapNO() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertTrue(bp.plotGooglMapNO());
	}

	@Test
	public void testSetGoogleMapNO() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setGoogleMapNO(false);
		assertFalse(bp.plotGooglMapNO());
	}

	@Test
	public void testPlotGooglMapAD() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertTrue(bp.plotGooglMapAD());
	}

	@Test
	public void testSetGoogleMapAD() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setGoogleMapAD(false);
		assertFalse(bp.plotGooglMapAD());
	}

	@Test
	public void testPlotGooglMapTF() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertTrue(bp.plotGooglMapTF());
	}

	@Test
	public void testSetGoogleMapTF() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setGoogleMapTF(false);
		assertFalse(bp.plotGooglMapTF());
	}

	@Test
	public void testPlotGooglMapKA() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertTrue(bp.plotGooglMapKA());
	}

	@Test
	public void testSetGoogleMapKA() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setGoogleMapKA(false);
		assertFalse(bp.plotGooglMapKA());
	}

	@Test
	public void testPlotGooglMapKC() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertTrue(bp.plotGooglMapKC());
	}

	@Test
	public void testSetGoogleMapKC() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setGoogleMapKC(false);
		assertFalse(bp.plotGooglMapKC());
	}

	@Test
	public void testTradeOffAD() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertTrue(bp.tradeOffAD());
	}

	@Test
	public void testTradeOffTF() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertTrue(bp.tradeOffTF());
	}

	@Test
	public void testSetTradeOffAD() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setTradeOffAD(false);
		assertFalse(bp.tradeOffAD());
	}

	@Test
	public void testSetTradeOffTF() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setTradeOffTF(false);
		assertFalse(bp.tradeOffTF());
	}

	@Test
	public void testGetNumberOfChannels() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertEquals(2, bp.getNumberOfChannels(), 0.0001);
	}

	@Test
	public void testSetNumberOfChannels() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setNumberOfChannels(3);
		assertEquals(3, bp.getNumberOfChannels(), 0.0001);
	}

	@Test
	public void testGetNumberOfQueries() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertEquals(100, bp.getNumberOfQueries(), 0.0001);
	}

	@Test
	public void testSetNumberOfQueries() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setNumberOfQueries(200);
		assertEquals(200, bp.getNumberOfQueries(), 0.0001);
	}

	@Test
	public void testSetEmail() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setEmail("foo@vt.edu");
		assertEquals("foo@vt.edu", bp.getEmail());
	}

	@Test
	public void testGetEmail() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertEquals("ningli@vt.edu", bp.getEmail());
	}

	@Test
	public void testGetInputParams() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		assertTrue(bp.getInputParams());
	}

	@Test
	public void testSetInputParams() {
		String args = "-a 39.0959629363055 -85.1385498046875 37.06394430056685 -81.8646240234375 -c 2 -C 38.41916639395372 -83.7103271484375 38.16047628099622 -84.1937255859375 -C 38.06539235133249 -82.7764892578125 37.65773212628274 -83.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		bp.setInputParams(false);
		assertFalse(bp.getInputParams());
	}

}
