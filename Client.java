/*
 * Client represents an attacker. It has its own location and a inference map
 * It uses results from queries to update inference map
 */

public class Client {
	public static final double PMAX = 1;
	// location of the SU
	private Location location;
	// inferMap of the SU
	private InferMap inferMap;
	// constructor
	public Client(double lat, double lon, GridMap map) {
		location = new Location(lat, lon);
		inferMap = new InferMap(map);
		if (!inferMap.withInBoundary(location)) System.out.println("SU's location is not in the range of map area");
	}

	// send a query to server
	public void query(Server server) {
		if (server == null) return;
		double power = server.response(this);
		System.out.println("Server response with: " + power);
		if (power < 0) {
			System.out.println("Channel unavailable");
			return;
		}
		int d1 = -1; int d2 = -1;
		if (power == 0) {
			d1 = 0;
			d2 = 8;
		}
		if (power == 0.5 * PMAX) {
			d1 = 8;
			d2 = 14;
		}
		if (power == 0.75 * PMAX) {
			d1 = 14;
			d2 = 25;
		}
		if (power == PMAX) {
			d1 = 25;
			d2 = d1;
		}
		System.out.println("d1: " + d1 + ", d2: " + d2);
		inferMap.update(this.location, d1, d2);
	}

	public Location getLocation() {
		return location;
	}

	public void printInferMap() {
		// inferMap.print();
		inferMap.visualize();
	}
}