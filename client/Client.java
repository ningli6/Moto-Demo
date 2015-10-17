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
	protected int numberOfChannels; // number of channels
	protected Location location;	    // location of the attacker
	protected int indexOfRow;           // row location
	protected int indexOfCol;           // column location
	protected int numOfRows;            // number of rows of map
	protected int numOfCols;            // number of cols of map
	protected GridMap map;              // grid map instance
	protected InferMap[] inferMap;      // inference map for each channel
	protected List<PU>[] channelsList;  // channel list of primary user, used to compute distance from each cell to the nearest primary user
	public int[] count;                 // count the number for each channel of updating (for debugging)
	
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
		this.numOfRows = map.getNumOfRows();
		this.numOfCols = map.getNumOfCols();
		this.indexOfRow = -1;
		this.indexOfCol = -1;
		for (int i = 0; i < numberOfChannels; i++) inferMap[i] = new InferMap(i, map);
		this.channelsList = server.getChannelsList();
	}

	/**
	 * Set the location of attacker at (r, c) with respect of grid map
	 * @param r    Number of rows
	 * @param c    Number of columns
	 */
	public void setLocation(int r, int c) {
		if (r < 0 || r >= map.getNumOfRows()) throw new IllegalArgumentException("SU's location is out out index");
		if (c < 0 || c >= map.getNumOfCols()) throw new IllegalArgumentException("SU's location is out out index");
		location.setLocation(map.rowToLat(r), map.colToLng(c));
		indexOfRow = r;
		indexOfCol = c;
	}

	/**
	 * Generate random location for client
	 */
	public void randomLocation() {
		Random rand = new Random();
		int newR = rand.nextInt(map.getNumOfRows());
		int newC = rand.nextInt(map.getNumOfCols());
		setLocation(newR, newC);
	}
	
	/**
	 * Query the server at client's location
	 * @param server   server of the map
	 */
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
		// client records how many times a channel is updated
		count[channelID]++;
		inferMap[channelID].update(indexOfRow, indexOfCol, updateInnerRadius(power), updateOutterRadius(power));
	}
	
	/**
	 * Updating radius of inner circle
	 * @param power   available transmit power
	 * @return        radius
	 */
	protected double updateInnerRadius(double power) {
		if (power < 0 || power > 1) throw new IllegalArgumentException();
		if (power == MTP.P_0) return MTP.d0;  //  0 km
		if (power == MTP.P_50) return MTP.d1;  //  8 km
		if (power == MTP.P_75) return MTP.d2;  // 14 km
		return MTP.d3;  // 25 km
	}
	
	/**
	 * Updating radius of outer circle
	 * @param power   available transmit power
	 * @return        radius
	 */
	protected double updateOutterRadius(double power) {
		if (power < 0 || power > 1) throw new IllegalArgumentException();
		if (power == MTP.P_0) return MTP.d1;  //  8 km
		if (power == MTP.P_50) return MTP.d2;  //  14 km
		if (power == MTP.P_75) return MTP.d3;  // 25 km
		return MTP.d3;  // 25 km
	}

	/**
	 * Compute inaccuracy of inferred matrix
	 * @return i th element are ic value for i th channel
	 */
	public double[] computeIC() {
		double[] IC = new double[numberOfChannels];
		for (int i = 0; i < numberOfChannels; i++) {
			double sum = 0;
			for (int r = 0; r < numOfRows; r++) {
				for (int c = 0; c < numOfCols; c++) {
					sum += inferMap[i].getProbability(r, c) * distanceToClosestPU(i, r, c);
				}
			}
			IC[i] = sum;
		}
		return IC;
	}

	/**
	 * Find the closet distance from (R, C) to any pu on channel CHANNEL
	 * @param channel   channel id
	 * @param r         row index
	 * @param c         col index
	 * @return          closest distance
	 */
	protected double distanceToClosestPU(int channel, int r, int c) {
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
	
	public Location getLocation() {
		return location;
	}

	public int getRowIndex() {
		return indexOfRow;
	}

	public int getColIndex() {
		return indexOfCol;
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