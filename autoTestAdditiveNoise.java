package test;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;

import utility.Location;

public class autoTestAdditiveNoise {
	public static String directory = "/Users/ningli/Desktop/Project/output/";

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		// default settings
		// cell size
		double cellDegree = 0.1;
		// multiple times for MTP
		double mult = 1;
		// number of PUs/Channels
		int Number_Of_Channels = 1;
		// noise
		double noise_level = 0.5;

		System.out.println("Cell size in degree: ");
		cellDegree = sc.nextDouble();
		System.out.println("Multiple times on default MTP function: ");
		mult = sc.nextDouble();
		System.out.println("Number of channels: ");
		Number_Of_Channels = sc.nextInt();
		System.out.println("Noise level: ");
		noise_level = sc.nextDouble();
		if (noise_level > 0) {
			System.out.println("Noise is between 0 to 1");
			return;
		}

		double ulLat = 38;
		double ulLon = -82;
		double urLat = 38;
		double urLon = -79;
		double llLat = 36;
		double llLon = -82;
		double lrLat = 36;
		double lrLon = -79;

		Location upperLeft = new Location(ulLat, ulLon);
		Location upperRight = new Location(urLat, urLon);
		Location lowerLeft = new Location(llLat, llLon);
		Location lowerRight = new Location(lrLat, lrLon);

		// initialization
		GridMap map = new GridMap(upperLeft, upperRight, lowerLeft, lowerRight, cellDegree);
		// Location.map = map;
		MTP.ChangeMult(mult);
		Server.Number_Of_Channels = Number_Of_Channels;
		Client.Number_Of_Channels = Number_Of_Channels;

		ArrayList<Double>[] rlist = (ArrayList<Double>[]) new ArrayList[Number_Of_Channels];
		for (int i = 0; i < rlist.length; i++) {
			rlist[i] = new ArrayList<Double>();
		}

		int[] queries = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
		int repeat = 10;

		ServerAdditiveNoise server = new ServerAdditiveNoise(map, noise_level);

		PU pu0 = new PU(0, 9, 9);
		server.addPU(pu0, 0);

		PU pu1 = new PU(1, 9, 50);
		server.addPU(pu1, 1);

		PU pu2 = new PU(2, 30, 9);
		server.addPU(pu2, 1);

		PU pu3 = new PU(3, 30, 50);
		server.addPU(pu3, 0);

		// PU pu4 = new PU(4, 29, 29);
		// server.addPU(pu4, 0);

		// PU pu5 = new PU(5, 10, 30);
		// server.addPU(pu5, 1);

		Client client = new Client(10, 30, map);

		System.out.println("Now perform a series of queries");
		// go thru number of queries in the query list
		for (int q : queries) {
			System.out.println("Number of queries: " + q);
			// specify number of queries for server:
			server.setNoise(q);
			double[] sumIC = new double[Number_Of_Channels];
			// make queries for certain times
			for (int i = 0; i < repeat; i++) {
				// clear client's probability map to 0.5
				client.reset();
				// set actual lies back to 0
				server.reset();
				for (int j = 0; j < q; j++) {
					client.randomLocation();
					client.query(server);
				}
				if (!server.reachNoiseLevel()) {
					System.out.println("Noise condition is not satisfied, try again");
					i--;
					continue;
				}
				/* debug */
				System.out.println(server.getNumberOfLies());
				double[] IC = client.computeIC(server);
				// System.out.println("IC for channel 0 is " + IC[0]);
				int k = 0;
				for (double ic : IC) {
					sumIC[k] += ic;
					k++;
				}
			}
			// compute average
			int cid = 0;
			for (double ic : sumIC) {
				rlist[cid].add(ic / repeat);
				// System.out.println("Average is " + ic / repeat);
				cid++;
			}
		}
		File file = new File(directory + "ic_an.txt");
		try {
			PrintWriter out = new PrintWriter(file);
			System.out.println("Start printing... ");
			for (int test : queries) out.print(test + " ");
			out.println();
			for (ArrayList<Double> list: rlist) {
				for (double ic : list) {
					out.print(ic + " ");
				}
				out.println();
			}
			out.close (); // this is necessary
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		} finally {
			System.out.println("Printing succeeded");
		}
	}
}