package utility;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import utility.geometry.Point;

public class PolyPUTest {
	
	private static final double PMAX = 1;
	Location ul = new Location(38, -88);
	Location ur = new Location(38, -86);
	Location ll = new Location(36, -88);
	Location lr = new Location(36, -86);
	GridMap map = new GridMap(ul, ur, ll, lr, 0.005);

	@Test
	public void testPolyPU() {
		String path = "C:\\Users\\Administrator\\Desktop\\motoData\\";
		PU pu = new PU(0, 37.95, -87, map);
		PolyPU ppu = new PolyPU(pu, 3);
		assertSame(pu, ppu.getPU());
		double[][] p = new double[map.getRows()][map.getCols()];
		for (int i = 0; i < map.getRows(); i++) {
			for (int j = 0; j < map.getCols(); j++) {
				p[i][j] = ppu.response(i, j);
			}
		}
		printMatrix(path, "testPolyPUResponse", p);
		PU q = new PU(1, 37, -87, map);
		double[][] cmp = new double[map.getRows()][map.getCols()];
		for (int i = 0; i < map.getRows(); i++) {
			for (int j = 0; j < map.getCols(); j++) {
				q.setLocation(i, j);
				cmp[i][j] = MTP(q.distTo(pu));
			}
		}
		printMatrix(path, "testPolyPUResponseCMP", cmp);
	}

	@Test
	public void testIndexTodist() {
		PU pu = new PU(0, 37, -87, map);
		assertEquals(200, pu.getRowIndex());
		assertEquals(200, pu.getColIndex());
		PolyPU ppu = new PolyPU(pu, 3);
		Point p = ppu.indexTodist(0,  0);
		System.out.println(p.x + ", " + p.y);
		
		p = ppu.indexTodist(0, 390);
		System.out.println(p.x + ", " + p.y);
		
		p = ppu.indexTodist(390, 0);
		System.out.println(p.x + ", " + p.y);
		
		p = ppu.indexTodist(390,  390);
		System.out.println(p.x + ", " + p.y);
	}
	
	@Test
	public void testBearing() {
		Location a = new Location(37, -87);
		Location b = new Location(37, -86.5);
		System.out.println(PolyPU.bearing(a, b));
	}

	@Test
	public void testHashcode() {
		PU pu = new PU(0, 37, -87, map);
		PolyPU ppu = new PolyPU(pu, 3);
		Set<Long> set = new HashSet<Long>();
		for (int i = 0; i < map.getRows(); i++) {
			for (int j = 0; j < map.getCols(); j++) {
				long hs = ppu.hashcode(i, j);
				assertFalse(set.contains(hs));
			}
		}
	}

	private void printMatrix(String dir, String fileName, double[][] p) {
		File file = new File(dir + fileName + ".txt");
		try {
			PrintWriter out = new PrintWriter(file);
			for (int i = 0; i < p.length; i++) {
				for (int j = 0; j < p[0].length; j++) {
					out.print(p[i][j] + " ");
				}
				out.println();
			}
			out.close (); // this is necessary
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private double MTP(double distance) {
		// System.out.println("Distance between PU and SU is: " + distance + " km");
		if (distance < MTP.d1) return 0;
		if (distance >= MTP.d1 && distance < MTP.d2) return 0.5 * PMAX;
		if (distance >= MTP.d2 && distance < MTP.d3) return 0.75 * PMAX;
		return PMAX;
	}
}
