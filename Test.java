import java.util.Random;
import java.util.Scanner;

/*
 * This class is written for testing relationship between number of queries and IC for each channel
 */ 

public class Test {
	// public static String directory = "/Users/ningli/Desktop/Project/output/";
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		// default settings
		// cell size
		double cd = 0.1;
		// multiple times for MTP
		double mult = 1;
		// number of PUs/Channels
		int Number_Of_Channels = 1;
		// query times
		int number_of_Queries = 50;

		System.out.println("Cell size in degree: ");
		cd = sc.nextDouble();
		System.out.println("Multiple times on default MTP function: ");
		mult = sc.nextDouble();
		System.out.println("Number of channels: ");
		Number_Of_Channels = sc.nextInt();

		final double cellDegree = cd; // in degree
		MTP.ChangeMult(mult);
		Server.setNumberOfChannels(Number_Of_Channels);
		Client.setNumberOfChannels(Number_Of_Channels);
		// InferMap.getDirectory(directory);

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
		// initialize a gridmap
		GridMap map = new GridMap(upperLeft, upperRight, lowerLeft, lowerRight, cellDegree);

		Random rand = new Random();

		Server server = new Server(map);

		PU pu0 = new PU(0, 37.5, -81);
		server.addPU(pu0, 0);

		PU pu1 = new PU(1, 37.5, -80);
		server.addPU(pu1, 1);

		// PU pu2 = new PU(2, 36.5, -80.5);
		// server.addPU(pu2, 0);

		// PU pu3 = new PU(3, 37.5, -80.5);
		// server.addPU(pu3, 1);

		PU pu4 = new PU(4, 36.5, -81);
		server.addPU(pu4, 1);

		PU pu5 = new PU(5, 36.5, -80);
		server.addPU(pu5, 0);

		Client client = new Client(37, -81.5, map);

		System.out.println("Now you can perform a series of queries");
		System.out.println("Number of queries: ");
		while (sc.hasNextInt()) {
			number_of_Queries = sc.nextInt();
			/* 
			 * Since it's a while loop and we are not creating new objects,
			 * it's necessary to reset client and server
			 */
			client.reset();
			server.reset();
			// for (int i = 0; i < number_of_Queries; i++) client.query(server);
			for (int i = 0; i < number_of_Queries; i++) {
				double rLat = llLat + (ulLat - llLat) * rand.nextDouble();
				double rLon = ulLon + (urLon - ulLon) * rand.nextDouble();
				client.setLocation(rLat, rLon);
				client.query(server);
			}
			/* debug information */
			client.updateWhich();
			server.printInfoPU();
			for (int i = 0; i < Number_Of_Channels; i++) {
				client.plotInferMap(i);
			}
			double[] IC = client.computeIC(server);
			for (int i = 0; i < IC.length; i++) {
				System.out.println("Channel " + i + " : " + IC[i]);
			}
			System.out.println("Number of queries: ");
		}
		System.out.println("Test is over");
	}
}