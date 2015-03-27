/*
 * Client represents an attacker. It has its own location and a inference map
 * It uses results from queries to update inference map
 */

public class Client {
	public static final double PMAX = 1;
	public static int Number_Of_Channels = 1;
	// location of the SU
	private Location location;
	// inferMap of the SU for each channel
	private InferMap[] inferMap;
	// count the number for each channel for updating
	private int[] count;

	/*
	 * This function should be called to set number of channels before consturct clients
	 */
	public static void setNumberOfChannels(int n) {
		if (n < 0) throw new IllegalArgumentException("Number of channels must be greater than 1");
		Number_Of_Channels = n;
	}

	// constructor
	public Client(double lat, double lon, GridMap map) {
		// System.out.println("Initialize client...");
		location = new Location(lat, lon);
		count = new int[Number_Of_Channels];
		inferMap = new InferMap[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++) inferMap[i] = new InferMap(i, map);
		for (int i = 0; i < Number_Of_Channels; i++)
			if (!inferMap[i].withInBoundary(location)) 
				System.out.println("SU's location for channel" + i + "is not in the range of map area");
		// System.out.println("Initialize complete!");
	}

	public Client(GridMap map) {
		location = new Location(); // use defalut settings lat = 0 & lon = 0;
		inferMap = new InferMap[Number_Of_Channels];
		count = new int[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++) inferMap[i] = new InferMap(i, map);
		for (int i = 0; i < Number_Of_Channels; i++)
			if (!inferMap[i].withInBoundary(location)) 
				System.out.println("SU's location for channel" + i + "is not in the range of map area");
	}

	public void setLocation(double lat, double lon) {
		if (location == null) System.out.println("Initialize client first");
		else location.setLocation(lat, lon);
	}

	public void setLocation(String lat, String lon) {
		if (location == null) System.out.println("Initialize client first");
		else location.setLocation(lat, lon);
	}

	// send a query to server
	public void query(Server server) {
		if (inferMap == null) {
			System.out.println("Initialize infermaps first");
			return;
		}
		if (server == null) return;
		Response res = server.response(this);
		double power = res.getPower();
		int channelID = res.getChannelID();
		/* debug information */
		res.getPU().sendResponse();
		System.out.println("Server response with: " + "[" + res.getPUID() + "] with power " + power + " on channel [" + channelID + "]");
		if (power < 0) {
			System.out.println("Channel unavailable");
			return;
		}
		// client will know that no one is responding
		if (channelID < 0) {
			System.out.println("No PU responses within the map");
			return;
		}
		double d1 = -1; double d2 = -1;
		if (power == 0) {
			d1 = MTP.d0;
			d2 = MTP.d1;
		}
		if (power == 0.5 * PMAX) {
			d1 = MTP.d1;
			d2 = MTP.d2;
		}
		if (power == 0.75 * PMAX) {
			d1 = MTP.d2;
			d2 = MTP.d3;
		}
		if (power == PMAX) {
			d1 = MTP.d3;
			d2 = d1;
		}
		System.out.println("d1: " + d1 + ", d2: " + d2);
		count[channelID]++;
		inferMap[channelID].update(this.location, d1, d2);
	}

	public Location getLocation() {
		return location;
	}

	/* debug information */
	public void updateWhich() {
		for (int i = 0; i < Number_Of_Channels; i++) {
			System.out.println("Channel [" + i + "] is updated " + count[i] + " times");
		}
	}

	/*
	 * This function plot the result in a colorpan for visualization
	 */
	public void plotInferMap(int i) {
		if (i < 0 || i > Number_Of_Channels) 
			throw new IllegalArgumentException("Query channels must be positive but less than the number of channels in the system");
		// inferMap.print();
		inferMap[i].visualize();
	}

	/*
	 * This function output the probability matrix in matrix form
	 */
	public void printFormattedMatrix(int i) {
		if (i < 0 || i > Number_Of_Channels) 
			throw new IllegalArgumentException("Query channels must be positive but less than the number of channels in the system");
		inferMap[i].printoutMatrix(i);
	}

	/*
	 * This function output the probability matrix as a column in the table
	 */
	public void printFormattedTable(int i) {
		if (i < 0 || i > Number_Of_Channels) 
			throw new IllegalArgumentException("Query channels must be positive but less than the number of channels in the system");
		inferMap[i].printInRequiredFormat(i);
	}
}