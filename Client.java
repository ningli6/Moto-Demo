import java.util.List;
import java.util.LinkedList;
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
	// cheat
	private List<PU>[] channels_List;
	// public int[] check = new int[4];

	public class NumberOfChannelsMismatchException extends RuntimeException {
		public NumberOfChannelsMismatchException(String message) {
			super(message);
		}
		public NumberOfChannelsMismatchException() {
			super();
		}
	}

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

	public Location getLocation() {
		return location;
	}

	// send a query to server
	public void query(Server server) {
		if (inferMap == null) {
			System.out.println("Initialize infermap first");
			return;
		}
		if (server == null) return;
		Response res = server.response(this);
		double power = res.getPower();
		int channelID = res.getChannelID();
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
		/* debug information */
		res.getPU().sendResponse();
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

	public double[] computeIC(Server server) {
		if (server == null) return null;
		channels_List = server.getChannelsList();
		if (channels_List == null) return null;
		if (Number_Of_Channels != channels_List.length)
			throw new NumberOfChannelsMismatchException();
		double[] IC = new double[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++) {
			if (channels_List[i].size() == 0) {
				System.out.println("channel " + i + " has no PU");
				IC[i] = Double.POSITIVE_INFINITY;
				continue;
			}
			double sum = 0;
			double[][] p = inferMap[i].getProbabilityMatrix();
			int row = p.length;
			int col = p[0].length;
			for (int r = 0; r < row; r++) {
				for (int c = 0; c < col; c++) {
					sum += p[r][c] * distanceToClosestPU(i, r, c);
				}
			}
			if (sum == 0) IC[i] = Double.POSITIVE_INFINITY;
			else IC[i] = sum;
		}
		return IC;
	}

	private double distanceToClosestPU(int channel, int r, int c) {
		if (channel < 0 || channel >= Number_Of_Channels) throw new IllegalArgumentException("Bad channel number");
		double minDist = Double.MAX_VALUE;
		PU minPU = null;
		for (PU pu : channels_List[channel]) {
			double dist = inferMap[channel].getLocation(r, c).distTo(pu.getLocation());
			if (dist < minDist) {
				minDist = dist;
				minPU = pu;
			}
		}
		/* debug info */
		// check[minPU.getID()]++;
		return minDist;
	}

	/* debug information */
	public void updateWhich() {
		for (int i = 0; i < Number_Of_Channels; i++) {
			System.out.println("Channel [" + i + "] is updated " + count[i] + " times");
		}
	}

	public void reset() {
		for (int i = 0; i < Number_Of_Channels; i++) {
			count[i] = 0;
			inferMap[i].resetMap();
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