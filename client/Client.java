package client;

import java.util.List;
import java.util.Random;

import server.Server;
/*
 * Client represents an attacker. It has its own location and a inference map
 * It uses results from queries to update inference map
 */
import utility.GridMap;
import utility.InferMap;
import utility.Location;
import utility.MTP;
import utility.PU;
import utility.Response;

public class Client {
	public static final double PMAX = 1;
	int Number_Of_Channels = 1;
	// location of the SU
	private Location location;
	private int indexOfRow;
	private int indexOfCol;
	// inferMap of the SU for each channel
	private InferMap[] inferMap;
	// count the number for each channel for updating
	private int[] count;
	private List<PU>[] channels_List;
	private Random rand;

	public class NumberOfChannelsMismatchException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public NumberOfChannelsMismatchException(String message) {
			super(message);
		}
		public NumberOfChannelsMismatchException() {
			super();
		}
	}

	public Client(int r, int c, GridMap map, int noc) {
		if (r < 0 || r >= map.getRows()) throw new IllegalArgumentException("SU's location is out out index");
		if (c < 0 || c >= map.getCols()) throw new IllegalArgumentException("SU's location is out out index");
		this.location = new Location(map.RowToLat(r), map.ColToLon(c));
		this.Number_Of_Channels = noc;
		this.count = new int[Number_Of_Channels];
		this.inferMap = new InferMap[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++) inferMap[i] = new InferMap(i, map);
	}

	public void setLocation(int r, int c) {
		if (r < 0 || r >= inferMap[0].getRows()) throw new IllegalArgumentException("SU's location is out out index");
		if (c < 0 || c >= inferMap[0].getCols()) throw new IllegalArgumentException("SU's location is out out index");
		location.setLocation(inferMap[0].RowToLat(r), inferMap[0].ColToLon(c));
	}

	public void randomLocation() {
		rand = new Random();
		int newR = rand.nextInt(inferMap[0].getRows());
		int newC = rand.nextInt(inferMap[0].getCols());
		setLocation(newR, newC);
		indexOfRow = newR;
		indexOfCol = newC;
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
		if (inferMap == null) {
			System.out.println("Initialize infermap first");
			return;
		}
		if (server == null) return;
		Response res = server.response(this);
		if (res == null) return;
		double power = res.getPower();
		int channelID = res.getChannelID();
		/* debug */
		// System.out.println("Server response: " + power + ", dist to pu: " + res.getPU().getLocation().distTo(this.location));
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
		else if (power == 0.5 * PMAX) {
			d1 = MTP.d1;
			d2 = MTP.d2;
		}
		else if (power == 0.75 * PMAX) {
			d1 = MTP.d2;
			d2 = MTP.d3;
		}
		else if (power == PMAX) {
			d1 = MTP.d3;
			d2 = d1;
		}
		else {
			throw new IllegalArgumentException();
		}
		count[channelID]++;
		inferMap[channelID].update(this, d1, d2);
	}

	/**
	 * Compute inaccuracy of inferred matrix
	 * @return i th element are ic value for i th channel
	 */
	public double[] computeIC(Server server) {
		double[] IC = new double[Number_Of_Channels];
		this.channels_List = server.getChannelsList();
		for (int i = 0; i < Number_Of_Channels; i++) {
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
		if (channel < 0 || channel >= Number_Of_Channels) throw new IllegalArgumentException("Bad channel number");
		double minDist = Double.MAX_VALUE;
		for (PU pu : channels_List[channel]) {
			double dist = inferMap[channel].getLocation(r, c).distTo(pu.getLocation());
			if (dist < minDist) {
				minDist = dist;
			}
		}
		/* debug info */
		// check[minPU.getID()]++;
		return minDist;
	}

	/**
	 * Part of message in the email
	 * Record number of times each channels is updated
	 * @return string that include information about number of time each channel is updated
	 */
	public String countChannelUpdateToString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Number_Of_Channels; i++) {
			sb.append("Channel_[" + i + "]_is_updated_" + count[i] + "_times<br>");
		}
		return sb.toString();
	}

	/**
	 * Reset infer matrix back to 0.5
	 */
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

	/**
	 * Print probability matrix of ALL channels
	 * @param dir   output path
	 */
	public void printProbability(String dir) {
		for (int i = 0; i < Number_Of_Channels; i++) {
			inferMap[i].printProbability(dir);
		}
	}

	/**
	 * Print number of rows & cols
	 * @param dir   output path
	 */
	public void printRC(String dir) {
		for (int i = 0; i < Number_Of_Channels; i++) {
			inferMap[i].printRC(dir);
		}
	}

	/**
	 * Print map boundaries
	 * @param dir   output path
	 */
	public void printBounds(String dir) {
		for (int i = 0; i < Number_Of_Channels; i++) {
			inferMap[i].printBounds(dir);
		}
	}

}