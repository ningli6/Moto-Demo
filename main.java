/*
 * Main class is the entrace of the program
 */

public class main {

	// initial settings
	static double length = 20; // km
    static double height = 20; // km
    static double cellLength = 1; // km/cell

	public static void main(String[] args) {
	    // initialize a server with parameters from initial settings
		Server server = new Server(length, height, cellLength);
		// add a PU to the server's grid map, speficify the PU's location
		server.addPU(0, 0);
		// initialize a SU, speficify its location
		Client client = new Client(10, 0, server.getMap());
		// SU sends a query to server, updates its inference results
		client.query(server);
		client.printInferMap();
	}
}