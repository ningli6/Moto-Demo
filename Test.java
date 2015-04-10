// package test;

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
		double cellDegree = 0.1;
		// multiple times for MTP
		double mult = 1;
		// number of PUs/Channels
		int Number_Of_Channels = 1;
		// query times
		int number_of_Queries = 50;

		System.out.println("Cell size in degree: ");
		cellDegree = sc.nextDouble();
		System.out.println("Multiple times on default MTP function: ");
		mult = sc.nextDouble();
		System.out.println("Number of channels: ");
		Number_Of_Channels = sc.nextInt();

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

		Server server = new Server(map);

		PU pu0 = new PU(0, 9, 9);
		server.addPU(pu0, 0);

		PU pu1 = new PU(1, 9, 50);
		server.addPU(pu1, 0);

		PU pu2 = new PU(2, 30, 9);
		server.addPU(pu2, 0);

		PU pu3 = new PU(3, 30, 50);
		server.addPU(pu3, 0);

		Client client = new Client(0, 36, map);

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
				client.randomLocation();
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