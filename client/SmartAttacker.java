package client;

import server.Server;
import utility.InferMap;
import utility.Location;
import utility.MTP;
import utility.Response;

public class SmartAttacker extends Client {

	private static double[] resSet = {0, 0.5, 0.75, 1};
	private InferMap inferenceMap;    // inference map just for 1 channel
	
	public SmartAttacker(Server server) {
		super(server);
		inferenceMap = new InferMap(0, map);
	}
	
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
		double[] radius = updateRegion(power);
		// client records how many times a channel is updated
		count[channelID]++;
		inferenceMap.update(indexOfRow, indexOfCol, radius[0], radius[1]);
	}

	public void smartLocation() {
		int[] nextQueryLoc = {-1, -1};
//		long grids = map.getCols() * map.getCols();
		long minExp = Long.MAX_VALUE;
		int rows = map.getRows();   // number of rows on map
		int cols = map.getCols();   // number of columns on map
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.println("i, j: " + i + ", " + j);
				// for all location
				long exp = 0;  // initialize expected ic for location i, j
				for (double res : resSet) {
					System.out.println("Res: " + res);
					// for each possible response
					for (int r = 0; r < rows; r++) {
						for (int c = 0; c < cols; c++) {
							// for each probable pu location
//							System.out.println("r, c: " + r + ", " + c);
							if (inferenceMap.getProbability(r, c) == 0) continue;
							double[] radius = updateRegion(res);
							InferMap tmpMap = new InferMap(inferenceMap);
							tmpMap.update(r, c, radius[0], radius[1]);
							long tmpVal = (long) (computeIC(r, c, tmpMap) * 0.25 * inferenceMap.getProbability(r, c));
							exp += tmpVal;
						}
					}
				}
				System.out.println("Exp: " + exp);
				if (exp < minExp) {
					minExp = exp;
					nextQueryLoc[0] = i;
					nextQueryLoc[1] = j;
				}
			}
		}
		setLocation(nextQueryLoc[0], nextQueryLoc[1]);
		System.out.println("Location: " + nextQueryLoc[0] + ", " + nextQueryLoc[1]);
	}
	
	private double[] updateRegion(double power) {
		if (power < 0 || power > 1) throw new IllegalArgumentException();
		double[] radius = new double[2];
		if (power == 0) {
			radius[0] = MTP.d0;  //  0 km
			radius[1] = MTP.d1;  //  8 km
		}
		else if (power == 0.5 * PMAX) {
			radius[0] = MTP.d1;  //  8 km
			radius[1] = MTP.d2;  // 14 km
		}
		else if (power == 0.75 * PMAX) {
			radius[0] = MTP.d2;  // 14 km
			radius[1] = MTP.d3;  // 25 km
		}
		else if (power == PMAX) {
			radius[0] = MTP.d3;  // 25 km
			radius[1] = radius[0];      // 25 km
		}
		else {
			throw new IllegalArgumentException();
		}
		return radius;
	}
	
	private long computeIC(int puR, int puC, InferMap inferMap) {
		if (inferMap == null) return 0;
		Location puLoc = inferMap.getLocation(puR, puC);
		long ic = 0;
		int rows = inferMap.getRows();
		int cols = inferMap.getCols();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				ic += (long) (inferMap.getProbability(r, c) * puLoc.distTo(inferMap.getLocation(r, c)));
			}
		}
		return ic;
	}
	
	@Override
	public void reset() {
		this.inferenceMap.resetMap();
	}
}
