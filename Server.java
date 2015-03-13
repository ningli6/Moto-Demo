import java.util.HashSet;
import java.util.Iterator;

/*
 * Server has an instance of GridMap, and uses HashSet to record the location of PUs
 */

public class Server {
	public static final double PMAX = 1;

	// Server has an instance of GridMap
	private GridMap map;
	// use a hashSet to store the location of PU
	private HashSet<Location> set;

	public Server(GridMap map) {
		this.map = map;
		set = new HashSet<Location>();
	}

	public void addPU(double lat, double lon) {
		// error checking
		if (map == null) {
			System.out.println("Initialize map first");
			return;
		}
		// check if location is in the rectangle area
		Location location = new Location(lat, lon);
		if (map.withInBoundary(location)) set.add(location);
	}

	public void addPU(Location location) {
		// error checking
		if (map == null) {
			System.out.println("Initialize map first");
			return;
		}
		// check if location is in the rectangle area
		if (map.withInBoundary(location)) set.add(location);
	}

	// print the location of PUs
	public void printLocationOfPUs() {
		if (set == null) return;
		for (Location location : set) {
			location.printLocation();
		}
	}

	// return numbers of PUs
	public int getNumberOfPUs() {
		if (set == null) return 0;
		return set.size();
	}

	// resonse to the query from client
	public double response(Location location) {
		if (location == null) return -1;
		if (!map.withInBoundary(location)) return -1;
		if (set.isEmpty()) return PMAX;
		Iterator<Location> iter = set.iterator();
		Location pu = null;
		// Note that right now set only has one PU
		if (iter.hasNext()) {
			pu = iter.next();
		}
		return MTP(pu.distTo(location));
	}

    // MTP function
	private double MTP(double distance) {
		System.out.println("Distance between PU and SU is: " + distance);
		if (distance < 8) return 0;
		if (distance >= 8 && distance < 14) return 0.5 * PMAX;
		if (distance >= 14 && distance < 25) return 0.75 * PMAX;
		return PMAX; // >= 25
	}
}