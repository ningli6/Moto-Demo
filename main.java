import java.util.Random;
import java.util.Scanner;
/*
 * Main class is the entrace of the program
 */

public class Main {
	/* The example map area is:
	 * 50°00'00.0"N, 100°00'00.0"W
	 * 50°00'00.0"N, 95°00'00.0"W
	 * 45°00'00.0"N, 100°00'00.0"W
	 * 45°00'00.0"N, 95°00'00.0"W
	 * allow user to define a rectangle area with four lat-lon coordinates
	 * user can input location as string that matches to
	 * ([0-9])+(\.[0-9]+){0,1}\|([0-9])+(\.[0-9]+){0,1}'([0-9])+(\.[0-9]+){0,1}''(N|S|E|W)\.
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		// get default settings from user
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
		System.out.println("Number of PUs: ");
		Number_Of_Channels = sc.nextInt();
		System.out.println("Number of queries: ");
		number_of_Queries = sc.nextInt();

		// Cell size, when cellDegree equals to 0.1, distance between each cell is about 10 km.
		// When cellDegree equals to 0.01, distance between each cell is about 1 km.
		final double cellDegree = cd; // in degree
		MTP.ChangeMult(mult);
		Server.setNumberOfChannels(Number_Of_Channels);
		Client.setNumberOfChannels(Number_Of_Channels);

		/*****************************/
		// String ulLat = "50|00'00''N.";
		// String ulLon = "100|00'00''W.";
		// String urLat = "50|00'00''N.";
		// String urLon = "99|48'00''W.";
		// String llLat = "49|48'00''N.";
		// String llLon = "100|00'00''W.";
		// String lrLat = "49|48'00''N.";
		// String lrLon = "99|48'00''W.";
		/*****************************/
		// String ulLat = "50|00'00''N.";
		// String ulLon = "100|00'00''W.";
		// String urLat = "50|00'00''N.";
		// String urLon = "99.8|00'00''W.";
		// String llLat = "49.8|00'00''N.";
		// String llLon = "100|00'00''W.";
		// String lrLat = "49.8|00'00''N.";
		// String lrLon = "99.8|00'00''W.";
		/*****************************/
		double ulLat = 50;
		double ulLon = -100;
		double urLat = 50;
		double urLon = -95;
		double llLat = 45;
		double llLon = -100;
		double lrLat = 45;
		double lrLon = -95;
		/*****************************/
		Location upperLeft = new Location(ulLat, ulLon);
		Location upperRight = new Location(urLat, urLon);
		Location lowerLeft = new Location(llLat, llLon);
		Location lowerRight = new Location(lrLat, lrLon);

		// initialize a gridmap
		GridMap map = new GridMap(upperLeft, upperRight, lowerLeft, lowerRight, cellDegree);
		/* debug information */
		System.out.println("Map length: " + map.getLength() + " km, Map height: " + map.getHeight() + " km");
		System.out.println("map rows: " + map.getRows() + ", map cols: " + map.getCols());
		map.showBoundary();
		System.out.println("Average distance between each cell is: " + map.getAverageDistance() + " km");

		// Random instance used to generate random location
		Random rand = new Random();

	    // initialize a server with parameters from initial settings
		Server server = new Server(map);

		/* add a PU to the server's grid map, speficify the PU's location
		 * The location of PU is: 47°30'00.0"N, 97°30'00.0"W
		 */
		PU pu0 = new PU(0, 49, -99);
		server.addPU(pu0);

		PU pu1 = new PU(1, 49, -96);
		server.addPU(pu1);

		PU pu2 = new PU(2, 46, -99);
		server.addPU(pu2);

		PU pu3 = new PU(3, 46, -96);
		server.addPU(pu3);

		// /* 
  //        * Use multiple PU, specified by Number_Of_Channels;
		//  */
		// for (int i = 0; i < Number_Of_Channels; i++) {
		// 	double rLat = 50 - (50 - 45) * rand.nextDouble();
		// 	double rLon = -100 + (-95 - (-100)) * rand.nextDouble();
		// 	PU pu = new PU(i, rLat, rLon);
		// 	server.addPU(pu);
		// }

		/* debug information */
		System.out.println("Number of PU on the map is: " + server.getNumberOfPUs());
		
		// initiliza a client, then change its location and make a query for N times;
		Client client = new Client(46.1, -96.1, map);
		// client.query(server);
		for (int i = 0; i < number_of_Queries; i++) {
			double rLat = 50 - (50 - 45) * rand.nextDouble();
			double rLon = -100 + (-95 - (-100)) * rand.nextDouble();
			client.setLocation(rLat, rLon);
			client.query(server);
		}
		
		client.updateWhich();
		/*
		Client client = new Client(map);
		Random rand = new Random();
		double rLat = 50 - (50 - 45) * rand.nextDouble();
		double rLon = -100 + (-95 - (-100)) * rand.nextDouble();
		client.setLocation(rLat, rLon);
		// SU sends a query to server, updates its inference results
		client.query(server);
		*/
		/*** these functions should be update! ***/
		for (int i = 0; i < Number_Of_Channels; i++) {
			client.printInferMap(i);
			client.printFormattedMatrix(i);
			client.printFormattedTable(i);
		}
		// client.printInferMap(0);
		// client.printInferMap(1);
		// client.printInferMap(2);
		// client.printInferMap(3);
		// client.printFormattedMatrix(0);
		// client.printFormattedTable(0);
	}
}