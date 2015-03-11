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

	// constructor
	public Server(double length, double height, double cellLength) {
		map = new GridMap(length, height, cellLength);
		set = new HashSet<Location>();
	}

	// add PU
	public void addPU(int r, int c) {
		// error checking
		if (map == null) throw new NullPointerException("Initialize map first");
		if (r < 0 || r >= map.getRows()) {
			System.out.println("Rows must be with in " + 0 + " and " + map.getRows());
			return;
		}
		if (c < 0 || c >= map.getCols()) {
			System.out.println("Columns must be with in " + 0 + " and " + map.getCols());
			return;
		}
		Location location = new Location(r, c);
		// assume no duplicate locations
		set.add(location);
	}
	// print the location of pus
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
    // return the GridMap instance
	public GridMap getMap() {
		return map;
	}
	// resonse to the query from client
	public double response(Location location) {
		if (location == null) return -1;
		if (set.isEmpty()) return PMAX;
		Iterator<Location> iter = set.iterator();
		Location pu = new Location();
		// Note that right now set only has one PU
		if (iter.hasNext()) {
			pu = iter.next();
		}
		return MTP(pu.distTo(location) * map.getCellSize());
	}
    // MTP function
	private double MTP(double distance) {
		if (distance < 8) return 0;
		if (distance >= 8 && distance < 14) return 0.5 * PMAX;
		if (distance >= 14 && distance < 25) return 0.75 * PMAX;
		return PMAX; // >= 25
	}
}