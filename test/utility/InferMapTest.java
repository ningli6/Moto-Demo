package utility;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.Test;

import client.Client;
import server.Server;

public class InferMapTest {
	Location ul = new Location(38, -90);
	Location ur = new Location(38, -88);
	Location ll = new Location(36, -90);
	Location lr = new Location(36, -88);
	GridMap map = new GridMap(ul, ur, ll, lr, 0.005);
	Server server = new Server(map, 1);
	Client client = new Client(server);
	
	@Test
	public void testInferMap() {
		InferMap im = new InferMap(0, map);
		assertEquals(map.getRows(), im.getProbabilityMatrix().length);
		assertEquals(map.getCols(), im.getProbabilityMatrix()[0].length);
		double[][] p = im.getProbabilityMatrix();
		for (int i = 0; i < map.getRows(); i++) {
			for (int j = 0; j < map.getCols(); j++) {
				assertEquals(0.5, p[i][j], 0.0001);
			}
		}
	}

	@Test
	public void testUpdate() {
		String path = "C:\\Users\\Administrator\\Desktop\\motoData\\";
		assertEquals(400, map.getRows());
		assertEquals(400, map.getCols());
		client.setLocation(100, 100);
		InferMap im = new InferMap(0, map);
		im.update(client, MTP.d0, MTP.d1);  // 0, 8
		printMatrix(path, "0_8", im.getProbabilityMatrix());
		im.resetMap();
		im.update(client, MTP.d1, MTP.d2);  // 8, 14
		printMatrix(path, "8_14", im.getProbabilityMatrix());
		im.resetMap();
		im.update(client, MTP.d2, MTP.d3);  // 14, 25
		printMatrix(path, "14_25", im.getProbabilityMatrix());
		im.resetMap();
		im.update(client, MTP.d3, MTP.d3);  // 25, 25
		printMatrix(path, "25_25", im.getProbabilityMatrix());
	}

	@Test
	public void testResetMap() {
		InferMap im = new InferMap(0, map);
		client.setLocation(100, 100);
		im.update(client, MTP.d0, MTP.d1);  // 0, 8
		im.resetMap();
		double[][] p = im.getProbabilityMatrix();
		for (int i = 0; i < map.getRows(); i++) {
			for (int j = 0; j < map.getCols(); j++) {
				assertEquals(0.5, p[i][j], 0.0001);
			}
		}
	}

	@Test
	public void testPrintProbability() {
		String path = "C:\\Users\\Administrator\\Desktop\\motoData\\";
		assertEquals(400, map.getRows());
		assertEquals(400, map.getCols());
		client.setLocation(200, 200);
		InferMap im = new InferMap(0, map);
		im.update(client, MTP.d0, MTP.d1);  // 0, 8
		im.printProbability(path, "test.txt");
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

}
