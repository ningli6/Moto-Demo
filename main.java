/*
 * Main class is the entrace of the program
 */

public class main {

	public static void main(String[] args) {
		// cell size, when cellDegree equals to 0.1, distance between each cell is about 10 km
		final double cellDegree = 0.1; // in degree
		/* allow user to define a rectangle area with four lat-lon coordinates
		 * Input is String
			String ulLat;
			String ulLon;
			String urLat;
			String urLon;
			String llLat;
			String llLon;
			String lrLat;
			String lrLon;
		*****************************/
		/* Input is lat & long coordination */
		double ulLat = 50;
		double ulLon = 100;
		double urLat = 50;
		double urLon = 102;
		double llLat = 48;
		double llLon = 100;
		double lrLat = 48;
		double lrLon = 102;
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

	    // initialize a server with parameters from initial settings
		Server server = new Server(map);
		// add a PU to the server's grid map, speficify the PU's location
		server.addPU(49.05, 101.05);
		/* debug information */
		System.out.println("Number of PU on the map is: " + server.getNumberOfPUs());

		// initialize a SU, speficify its location
		Client client = new Client(49.35, 101.05, map);
		// SU sends a query to server, updates its inference results
		client.query(server);
		client.printInferMap();
	}
}