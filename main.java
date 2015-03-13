/*
 * Main class is the entrace of the program
 */

public class Main {

	public static void main(String[] args) {
		// Cell size, when cellDegree equals to 0.1, distance between each cell is about 10 km.
		// When cellDegree equals to 0.01, distance between each cell is about 1 km.
		final double cellDegree = 0.01; // in degree
		// allow user to define a rectangle area with four lat-lon coordinates
		// ([0-9])+(\.[0-9]+){0,1}\|([0-9])+(\.[0-9]+){0,1}'([0-9])+(\.[0-9]+){0,1}''(N|S|E|W)\.
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

	    // initialize a server with parameters from initial settings
		Server server = new Server(map);
		// add a PU to the server's grid map, speficify the PU's location
		PU pu = new PU(47.5, -97.5);
		server.addPU(pu);
		/* debug information */
		System.out.println("Number of PU on the map is: " + server.getNumberOfPUs());

		// initialize a SU, speficify its location and grid map it uses
		Client client = new Client(47.7, -97.5, map);
		// SU sends a query to server, updates its inference results
		client.query(server);
		client.printInferMap();
	}
}