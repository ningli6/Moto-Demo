import java.util.HashSet;
import java.util.Iterator;

/*
 * Server has an instance of GridMap, and uses HashSet to record the location of PUs
 */

public class Server {
	public static double PMAX = 1;
	public static int Number_Of_Channels = 1;
	// Server has an instance of GridMap
	private GridMap map;
	// use a hashSet to store the location of PU
	private HashSet<PU> set;

	/*
	 * This function should be called to set number of channels before consturct server
	 */
	public static void setNumberOfChannels(int n) {
		if (n < 0) throw new IllegalArgumentException("Number of channels must be greater than 1");
		Number_Of_Channels = n;
	}

	public Server(GridMap map) {
		this.map = map;
		set = new HashSet<PU>();
	}

	public void addPU(PU pu) {
		// error checking
		if (map == null) {
			System.out.println("Initialize map first");
			return;
		}
		if (pu == null) return;
		// check if location is in the rectangle area
		// for now let's say we allow pu to have the same location
		if (map.withInBoundary(pu.getLocation())) set.add(pu);
		else System.out.println("PU's location out of range");
	}

	// print the location of PUs
	public void printLocationOfPUs() {
		if (set == null) return;
		for (PU pu : set) {
			pu.printLocation();
		}
	}

	// return numbers of PUs
	public int getNumberOfPUs() {
		if (set == null) return 0;
		return set.size();
	}

	// resonse to the query from client
	public Response response(Client client) {
		if (client == null) return new Response(-1, -1);
		if (!map.withInBoundary(client.getLocation())) return new Response(-1, -1);
		if (set.isEmpty()) return new Response(-1, PMAX);
		double minDis = Double.MAX_VALUE;
		int minID = -1;
		Iterator<PU> iter = set.iterator();
		PU pu = null;
		// Note that right now set only has one PU
		while (iter.hasNext()) {
			pu = iter.next();
			double dist = pu.getLocation().distTo(client.getLocation());
			System.out.println("Server compute dist: [" + pu.getID() + "] " + dist);
			if (dist < minDis) {
				minID = pu.getID();
				minDis = dist;
			}
		}
		System.out.println("Server chooses dist: [" + minID + "] " + minDis);
		// return MTP(pu.getLocation().distTo(client.getLocation()));
		// for testing
		return new Response(minID, MTP(minDis));
	}

 //    // MTP function
	// private double MTP(double distance) {
	// 	System.out.println("Distance between PU and SU is: " + distance + " km");
	// 	if (distance < 8) return 0;
	// 	if (distance >= 8 && distance < 14) return 0.5 * PMAX;
	// 	if (distance >= 14 && distance < 25) return 0.75 * PMAX;
	// 	return PMAX; // >= 25
	// }

	// in test, change MTP function
	// private double MTPtest(double distance, double multi) {
	// 	if (multi < 1) System.out.println("Required multiple para greater than 1");
	// 	System.out.println("Distance between PU and SU is: " + distance + " km");
	// 	if (distance < 8 * multi) return 0;
	// 	if (distance >= 8 * multi && distance < 14 * multi) return 0.5 * PMAX;
	// 	if (distance >= 14 * multi && distance < 25 * multi) return 0.75 * PMAX;
	// 	return PMAX; // >= 25
	// }
	private double MTP(double distance) {
		System.out.println("Distance between PU and SU is: " + distance + " km");
		if (distance < MTP.d1) return 0;
		if (distance >= MTP.d1 && distance < MTP.d2) return 0.5 * PMAX;
		if (distance >= MTP.d2 && distance < MTP.d3) return 0.75 * PMAX;
		return PMAX; // >= 25
	}
}