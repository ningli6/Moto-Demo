package tests;

import java.util.Scanner;
import java.io.File;
import java.util.Random;

import client.Client;
import server.*;
import utility.*;

public class Main_KAnonymity {
	public static String directory = "/Users/ningli/Desktop/Project/output/";

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		// get default settings from user
		// cell size
		double cellDegree = 0.1;
		// multiple times for MTP
		double mult = 1;
		// number of PUs/Channels
		int Number_Of_Channels = 1;
		// query times
		int number_of_Queries = 50;
		// k for k-anonymity
		int K = 1;

		System.out.println("Cell size in degree: ");
		cellDegree = sc.nextDouble();
		System.out.println("Multiple times on default MTP function: ");
		mult = sc.nextDouble();
		System.out.println("Number of channels: ");
		Number_Of_Channels = sc.nextInt();
		System.out.println("Number of queries: ");
		number_of_Queries = sc.nextInt();
		System.out.println("K for k-anonymity: ");
		K = sc.nextInt();
		if (K <= 0) throw new IllegalArgumentException();

		double ulLat = 38;
		double ulLon = -82;
		double urLat = 38;
		double urLon = -79;
		double llLat = 36;
		double llLon = -82;
		double lrLat = 36;
		double lrLon = -79;
		/*****************************/
		Location upperLeft = new Location(ulLat, ulLon);
		Location upperRight = new Location(urLat, urLon);
		Location lowerLeft = new Location(llLat, llLon);
		Location lowerRight = new Location(lrLat, lrLon);

		// initialization
		GridMap map = new GridMap(upperLeft, upperRight, lowerLeft, lowerRight, cellDegree);
		MTP.ChangeMult(mult);
		Server.Number_Of_Channels = Number_Of_Channels;
		Client.Number_Of_Channels = Number_Of_Channels;
		InferMap.directory = directory;
		/* debug information */
		System.out.println("Map length: " + map.getLength() + " km, Map height: " + map.getHeight() + " km");
		System.out.println("map rows: " + map.getRows() + ", map cols: " + map.getCols());
		map.showBoundary();
		System.out.println("Average distance between each cell is: " + map.getAverageDistance() + " km");

		ServerKAnonymity server = new ServerKAnonymity(map, K);

		/* 
		 * Add a PU to the server's grid map, speficify the PU's location
		 */
		PU pu0 = new PU(0, 9, 9);
		server.addPU(pu0, 0);

		PU pu1 = new PU(1, 9, 50);
		server.addPU(pu1, 0);

		PU pu2 = new PU(2, 30, 9);
		server.addPU(pu2, 0);

		PU pu3 = new PU(3, 30, 50);
		server.addPU(pu3, 0);

		/* debug information */
		System.out.println("Number of PUs: " + server.getNumberOfPUs());
		server.printInfoPU();
		server.printInfoChannel();
		System.out.println();

		server.kAnonymity();

		// Client client = new Client(10, 10, map);

		// for (int i = 0; i < number_of_Queries; i++) {
		// 	client.randomLocation();
		// 	client.query(server);
		// }

		// for (int i = 0; i < Number_Of_Channels; i++) {
		// 	client.plotInferMap(i);
		// }

		// double[] IC = client.computeIC(server);
		// for (int i = 0; i < IC.length; i++) {
		// 	System.out.println("Channel " + i + " : " + IC[i]);
		// }
	}
}