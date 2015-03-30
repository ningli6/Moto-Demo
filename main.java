import java.util.Random;
import java.util.Scanner;
import java.io.File;
/*
 * Main class is the entrace of the program
 */

public class Main {
	public static String directory = "/Users/ningli/Desktop/Project/output/";
	/* 
	 * allow user to define a rectangle area with four lat-lon coordinates
	 * user can input location as string that matches to
	 * ([0-9])+(\.[0-9]+){0,1}\|([0-9])+(\.[0-9]+){0,1}'([0-9])+(\.[0-9]+){0,1}''(N|S|E|W)\.
	 */
	public static void main(String[] args) {
		// clear file
		File dir = new File(directory);
		for(File file: dir.listFiles()) file.delete();

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
		System.out.println("Number of channels: ");
		Number_Of_Channels = sc.nextInt();
		System.out.println("Number of queries: ");
		number_of_Queries = sc.nextInt();

		// Cell size, when cellDegree equals to 0.1, distance between each cell is about 10 km.
		// When cellDegree equals to 0.01, distance between each cell is about 1 km.
		final double cellDegree = cd; // in degree
		MTP.ChangeMult(mult);
		Server.setNumberOfChannels(Number_Of_Channels);
		Client.setNumberOfChannels(Number_Of_Channels);
		// define directory for the output file
		InferMap.getDirectory(directory);

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
		PU pu0 = new PU(0, 37.5, -81);
		server.addPU(pu0, 0);

		PU pu1 = new PU(1, 37.5, -80);
		server.addPU(pu1, 0);

		PU pu2 = new PU(2, 36.5, -81);
		server.addPU(pu2, 1);

		PU pu3 = new PU(3, 36.5, -80);
		server.addPU(pu3, 2);

		/* 
         * Use multiple PU, specified by Number_Of_Channels;
		 *
		for (int i = 0; i < Number_Of_Channels; i++) {
			double rLat = 50 - (50 - 45) * rand.nextDouble();
			double rLon = -100 + (-95 - (-100)) * rand.nextDouble();
			PU pu = new PU(i, rLat, rLon);
			server.addPU(pu);
		}
		*/

		/* debug information */
		System.out.println("Number of PUs: " + server.getNumberOfPUs());
		server.printInfoPU();
		server.printInfoChannel();
		System.out.println();
		// initiliza a client, then change its location and make a query for N times;
		Client client = new Client(37.4, -80.9, map);
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
		/*** these functions should be update! ***/
		for (int i = 0; i < Number_Of_Channels; i++) {
			client.plotInferMap(i);
			client.printFormattedMatrix(i);
			client.printFormattedTable(i);
		}

		double[] IC = client.computeIC(server);
		for (int i = 0; i < IC.length; i++) {
			System.out.println("Channel " + i + " : " + IC[i]);
		}
		// System.out.println(client.check[0]);
		// System.out.println(client.check[1]);
		// System.out.println(client.check[2]);
		// System.out.println(client.check[3]);
	}
}