package client;

import server.Server;
import utility.InferMap;
import utility.Location;
import utility.Response;

public class SmartAttacker extends Client {
	private static double[] resSet = {0, 0.5, 0.75, 1}; // response set
	private long[][][] icMat;           // inaccuracy of each cell assuming that's the pu
	private boolean[][][] isVisited;  // channel i responses when querying at (i, j)
	
	/**
	 * Initialize a smart attacker, 
	 * only handles one channel, use a new inference map for that.
	 * Also keep track of inaccuracy value of each cell assuming pu is located at that cell
	 * @param server
	 */
	public SmartAttacker(Server server) {
		super(server);
		icMat = new long[numberOfChannels][numOfRows][numOfCols];
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfCols; j++) {
				icMat[0][i][j] = computeIC(i, j, inferMap[0]);
			}
		}
		for (int k = 1; k < numberOfChannels; k++) {
			for (int i = 0; i < numOfRows; i++) {
				for (int j = 0; j < numOfCols; j++) {
					icMat[k][i][j] = icMat[0][i][j];
				}
			}
		}
		isVisited = new boolean[numberOfChannels][numOfRows][numOfCols];
	}
	
	/**
	 * Launch a query to server, server is responsible for determining available transmit power.
	 * Update the inference map and adjust ic for each cell accordingly.
	 */
	public void query(Server server) {
		if (server == null) return;
		Response res = server.response(this);
		if (res == null) return;
		double power = res.getPower();
		int cID = res.getChannelID();
		System.out.println("Response power: " + power);
		isVisited[cID][indexOfRow][indexOfCol] = true;
		System.out.println("Set isVisited[" + cID + "][" + indexOfRow + "][" + indexOfCol + "] to true");
		inferMap[cID].updateIcMat(indexOfRow, indexOfCol, power, icMat[cID]);
	}

	/**
	 * Using sophisticated algorithm to find the next query location.
	 * Skip last query location.
	 */
	public void smartLocation() {
		int qi = -1;
		int qj = -1;
		long minExp = Long.MAX_VALUE;
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfCols; j++) { // for each query location
				long exp = 0;
				for (int k = 0; k < numberOfChannels; k++) { // for each channel
					for (int r = 0; r < numOfRows; r++) {
						for (int c = 0; c < numOfCols; c++) { // for each possible pu location
							if (isVisited[k][i][j]) {  // visited before, set exp to max value
								exp += Integer.MAX_VALUE;
							} else {  // not visited, compute exp
								if (inferMap[k].getProbability(r, c) == 0) continue;
								for (double res : resSet) {
									long tmpIC = inferMap[k].tmpUpdate(i, j, r, c, res, icMat[k][r][c]);
									long tmpVal = (long) (tmpIC * 0.25 * inferMap[k].getProbability(r, c));
									exp += tmpVal;
								}
							}
						}
					}
				}
				if (exp < minExp) {
					minExp = exp;
					qi = i;
					qj = j;
				}
			}
		}
		setLocation(qi, qj);
		System.out.println("Choose: (" + qi + ", " + qj + ")");
	}
	
	/**
	 * Compute inaccuracy of certain inference map, with assumed location of primary user
	 * @param puR      assumed row of pu
	 * @param puC      assumed col of pu
	 * @param inferMap inference map on which ic will be computed
	 * @return         inaccuracy
	 */
	private long computeIC(int puR, int puC, InferMap inferMap) {
		if (inferMap == null) return 0;
		Location puLoc = inferMap.getLocation(puR, puC);
		long ic = 0;
		for (int r = 0; r < numOfRows; r++) {
			for (int c = 0; c < numOfCols; c++) {
				ic += (long) (inferMap.getProbability(r, c) * puLoc.distTo(inferMap.getLocation(r, c)));
			}
		}
		return ic;
	}
	
	/**
	 * Reset attacker, reset its inference map, recalculate its ic for each map entry
	 * reset previous query location
	 */
	@Override
	public void reset() {
		super.reset();
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfCols; j++) {
				icMat[0][i][j] = computeIC(i, j, inferMap[0]);
				isVisited[0][i][j] = false;
			}
		}
		for (int k = 1; k < numberOfChannels; k++) {
			for (int i = 0; i < numOfRows; i++) {
				for (int j = 0; j < numOfCols; j++) {
					icMat[k][i][j] = icMat[0][i][j];
					isVisited[k][i][j] = false;
				}
			}
		}
	}
}
