package client;

import java.util.List;
import java.util.Random;

import server.Server;
import utility.GridMap;
import utility.InferMap;
import utility.Location;
import utility.MTP;
import utility.PU;
import utility.Response;
/*
 * Client represents an attacker. It has its own location and a inference map
 * It uses results from queries to update inference map
 */
public class Client {
	public static final double PMAX = 1;
	protected int numberOfChannels = 1; // number of channels
	protected Location location;	      // location of the attacker
	protected int indexOfRow = -1;      // row index
	protected int indexOfCol = -1;      // column index
	protected GridMap map;            // grid map instance
	protected InferMap[] inferMap;      // inference map for each channel
	protected List<PU>[] channelsList;  // channel list of primary user, used to compute distance from each cell to the nearest primary user
	public int[] count;               // count the number for each channel for updating (for debugging)
	
	/**
	 * Use server to initialize client, to get number of channels, reference to map and channel list
	 * but client does not keep server instance
	 * @param server
	 */
	public Client(Server server) {
		this.numberOfChannels = server.getNumberOfChannels();
		this.location = new Location();
		this.count = new int[numberOfChannels];
		this.inferMap = new InferMap[numberOfChannels];
		this.map = server.getMap();
		for (int i = 0; i < numberOfChannels; i++) inferMap[i] = new InferMap(i, map);
		this.channelsList = server.getChannelsList();
	}

	public int getNumberOfChannels() {
		return numberOfChannels;
	}

	public void setNumberOfChannels(int numberOfChannels) {
		this.numberOfChannels = numberOfChannels;
	}

	/**
	 * Set the location of attacker at (r, c) with respect of grid map
	 * @param r    Number of rows
	 * @param c    Number of columns
	 */
	public void setLocation(int r, int c) {
		if (r < 0 || r >= map.getRows()) throw new IllegalArgumentException("SU's location is out out index");
		if (c < 0 || c >= map.getCols()) throw new IllegalArgumentException("SU's location is out out index");
		location.setLocation(map.rowToLat(r), map.colToLng(c));
		indexOfRow = r;
		indexOfCol = c;
	}

	/**
	 * Generate random location for client
	 */
	public void randomLocation() {
		Random rand = new Random();
		int newR = rand.nextInt(map.getRows());
		int newC = rand.nextInt(map.getCols());
		setLocation(newR, newC);
	}

	public Location getLocation() {
		return location;
	}

	public int getRowIndex() {
		return indexOfRow;
	}

	public int getColIndex() {
		return indexOfCol;
	}
	
	// send a query to server
	public void query(Server server) {
		if (server == null) return;
		Response res = server.response(this);
		if (res == null) return;
		double power = res.getPower();      // response power
		int channelID = res.getChannelID(); // channel id
		// client will know that no one is responding
		if (channelID < 0) {
			System.out.println("No PU responses within the map");
			return;
		}
		// pu records how many times it has been chosen to response
		res.getPU().sendResponse();
		double d1 = -1; double d2 = -1;
		if (power == 0) {
			d1 = MTP.d0;  //  0 km
			d2 = MTP.d1;  //  8 km
		}
		else if (power == 0.5 * PMAX) {
			d1 = MTP.d1;  //  8 km
			d2 = MTP.d2;  // 14 km
		}
		else if (power == 0.75 * PMAX) {
			d1 = MTP.d2;  // 14 km
			d2 = MTP.d3;  // 25 km
		}
		else if (power == PMAX) {
			d1 = MTP.d3;  // 25 km
			d2 = d1;      // 25 km
		}
		else {
			throw new IllegalArgumentException();
		}
		// client records how many times a channel is updated
		count[channelID]++;
		inferMap[channelID].update(indexOfRow, indexOfCol, d1, d2);
	}

	/**
	 * Compute inaccuracy of inferred matrix
	 * @return i th element are ic value for i th channel
	 */
	public double[] computeIC() {
		double[] IC = new double[numberOfChannels];
		for (int i = 0; i < numberOfChannels; i++) {
			/* debug 
			 * check server has returned correct list of pu to client */
			// System.out.println("CLient=> list size: " + channels_List[i].size());
			double sum = 0;
			double[][] p = inferMap[i].getProbabilityMatrix();
			int row = p.length;
			int col = p[0].length;
			for (int r = 0; r < row; r++) {
				for (int c = 0; c < col; c++) {
					// if (p[r][c] > 0) System.out.println("(r, c): " + r + ", " + c);
					sum += p[r][c] * distanceToClosestPU(i, r, c);
				}
			}
			IC[i] = sum;
		}
		return IC;
	}

	private double distanceToClosestPU(int channel, int r, int c) {
		if (channel < 0 || channel >= numberOfChannels) throw new IllegalArgumentException("Bad channel number");
		double minDist = Double.MAX_VALUE; // if channel is empty, return max int
		for (PU pu : channelsList[channel]) {
			double dist = inferMap[channel].getLocation(r, c).distTo(pu.getLocation());
			if (dist < minDist) {
				minDist = dist;
			}
		}
		return minDist;
	}

	/**
	 * Reset infer matrix back to 0.5
	 */
	public void reset() {
		for (int i = 0; i < numberOfChannels; i++) {
			count[i] = 0;
			inferMap[i].resetMap();
		}
	}

	/**
	 * Print probability matrix of ALL channels
	 * @param dir   output path
	 * @param fileName file name of text file that holds probability information
	 */
	public void printProbability(String dir, String fileName) {
		for (int i = 0; i < numberOfChannels; i++) {
			inferMap[i].printProbability(dir, fileName);
		}
	}
}