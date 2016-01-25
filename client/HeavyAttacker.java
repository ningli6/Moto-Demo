package client;

import server.Server;
import utility.InferMap;
import utility.Location;
import utility.MTP;
import utility.Response;

public class HeavyAttacker extends Client {
	private static double[] resSet = {0, 0.5, 0.75, 1}; // response set
	private InferMap inferenceMap;    // inference map just for 1 channel
	private int[] prevQueryLoc;       // previous query location of smart query
	
	public HeavyAttacker(Server server) {
		super(server);
		inferenceMap = new InferMap(0, map);
		this.prevQueryLoc = new int[2];
		prevQueryLoc[0] = prevQueryLoc[1] = -1;
	}
	
	public void query(Server server) {
		if (server == null) return;
		Response res = server.response(this);
		if (res == null) return;
		double power = res.getPower();      // response power
		inferenceMap.update(indexOfRow, indexOfCol, updateInnerRadius(power), updateOutterRadius(power));
	}
	
	public void heavyLocation() {
		int[] nextQueryLoc = {-1, -1};
		long minExp = Long.MAX_VALUE;
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfCols; j++) { // for each query location
				if (i == prevQueryLoc[0] && j == prevQueryLoc[1]) continue; // skip last query location
				long exp = 0;
				for (int r = 0; r < numOfRows; r++) {
					for (int c = 0; c < numOfCols; c++) { // for each possible pu location
						if (inferenceMap.getProbability(r, c) == 0) continue;
						for (double res : resSet) {
							InferMap tmp = new InferMap(inferenceMap);
//							System.out.println("Before update: " + computeIC(r, c, tmp));
							tmp.update(i, j, updateInnerRadius(res), updateOutterRadius(res));
							long tmpIC = computeIC(r, c, tmp);
							long tmpVal = (long) (tmpIC * 0.25 * inferenceMap.getProbability(r, c));
							exp += tmpVal;
						}
					}
				}
//				System.out.println("i, j: [" + i + ", " + j + "], exp: " + exp);
				if (exp < minExp) {
					minExp = exp;
					nextQueryLoc[0] = i;
					nextQueryLoc[1] = j;
//					System.out.println("Minimum: " + minExp + ", location: " + nextQueryLoc[0] + ", " + nextQueryLoc[1]);
				}
			}
		}
		setLocation(nextQueryLoc[0], nextQueryLoc[1]);
		prevQueryLoc[0] = nextQueryLoc[0];
		prevQueryLoc[1] = nextQueryLoc[1];
	}
	
	public double[] computeIC() {
		double[] IC = new double[1];
		double sum = 0;
		for (int r = 0; r < numOfRows; r++) {
			for (int c = 0; c < numOfCols; c++) {
				sum += inferenceMap.getProbability(r, c) * distanceToClosestPU(0, r, c);
			}
		}
		IC[0] = sum;
		return IC;
	}
	
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
	
	protected double updateInnerRadius(double power) {
		if (power < 0 || power > 1) throw new IllegalArgumentException();
		if (power == MTP.P_0) return MTP.d0;  //  0 km
		if (power == MTP.P_50) return MTP.d1;  //  8 km
		if (power == MTP.P_75) return MTP.d2;  // 14 km
		return MTP.d3;  // 25 km
	}
	
	protected double updateOutterRadius(double power) {
		if (power < 0 || power > 1) throw new IllegalArgumentException();
		if (power == MTP.P_0) return MTP.d1;  //  8 km
		if (power == MTP.P_50) return MTP.d2;  //  14 km
		if (power == MTP.P_75) return MTP.d3;  // 25 km
		return MTP.d3;  // 25 km
	}
}
