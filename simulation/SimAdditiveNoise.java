package simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.ServerAdditiveNoise;
import utility.Location;
import utility.PU;
import boot.BootParams;
import client.Client;

public class SimAdditiveNoise extends Simulation {
	private String countermeasure;        // name of countermeasure
	private double noiseLevel;            // noise level, [0, 1]
	private ServerAdditiveNoise cmServer; // instance of countermeasure server
	private int maxIteration;             // max attempts to reach noise level
	private boolean feasible;             // whether this noise level is feasible
	private Map<Integer, double[]> icCMMap;;      // ic for multiple simulation with countermeasure

	public SimAdditiveNoise(BootParams bootParams, double cellSize, double mtpScale, int interval, String directory) {
		/* call parent constructor */
		super(bootParams, cellSize, mtpScale, interval, directory);

		/* initialize countermeasure */
		this.countermeasure = "ADDITIVENOISE";

		/* initialize noise level */
		this.noiseLevel = bootParams.getCMParam(this.countermeasure);

		/* initialize server with additive noise */
		cmServer = new ServerAdditiveNoise(map, noc, noq, noiseLevel);
		/* add pu to cmServer */
		int PUid = 0;
		for (int k = 0; k < noc; k++) {
			List<Location> LatLngList = bootParams.getPUOnChannel(k);
			for (Location ll : LatLngList) {
				PU pu = new PU(PUid++, ll.getLatitude(), ll.getLongitude(), map);
				cmServer.addPU(pu, k);
			}
		}

		/* initialize max number of attempts */
		maxIteration = 20;

		/* initialize hashmap for query-ic with countermeasure */
		icCMMap = new HashMap<Integer, double[]>();

		/* initialize feasibility */
		feasible = false;
	}

	@Override
	public void singleSimulation() {
		if (noiseLevel < 0 || noiseLevel > 1) {
			System.out.println("Noise level must be in range 0 to 1.");
			feasible = false;   // do not execute following simulations
			return;
		}
		System.out.println("Start querying...");
		
		/* initialize a client */
		Client client = new Client(cmServer);

		/* run simulation for once */
		int attempts = maxIteration;
		while(attempts > 0) {
			// clear client's probability map to 0.5
			client.reset();
			// set actual lies back to 0
			cmServer.reset();
			for (int i = 0; i < noq; i++) {
				client.randomLocation();
				client.query(cmServer);
			}
			if (cmServer.reachNoiseLevel()) {
				System.out.println("Noise level satisfied!");
//				System.out.println("Actual lies: " + cmServer.getNumberOfLies() + " Expected lies: " + cmServer.getExpectedLies());
				break;
			}
			attempts--;
			System.out.println("Noise level not satisfied, try again");
		}
		/* if can't reach noise requirement within 20 attempts, return */
		if (attempts == 0) {
			feasible = false;   // do not execute following simulations
			System.out.println("Noise level is set too high. Requirement can't be reached within 20 attempts.");
			return;
		}
		feasible = true;        // noise level is feasible, proceed
		IC = client.computeIC();
		
//		/* debug */
//		for (List<PU> puList : cmServer.getChannelsList()) {
//			for (PU pu : puList){
//				pu.printInfo();
//			}
//		}
//		client.countChannel();
//		System.out.println("IC: ");
//		for (double d : IC){
//			System.out.print((int)d + " ");
//		}
//		System.out.println();
		
		printSingle(cmServer, client, directory);
	}

	@Override
	public void multipleSimulation() {
		if (this.noiseLevel > 1 || this.noiseLevel < 0) {
			feasible = false;
			System.out.println("Noise level is not feasible.");
			return;
		}

		System.out.println("Start computing average IC with additive noise...");
		Client multclient = new Client(cmServer);
		// compute query points
		int gap = noq / interval;
		// start query from 0 times
		List<Integer> qlist = new ArrayList<Integer>();
		for (int i = 0; i <= interval; i++) {
			qlist.add(gap * i);
			icCMMap.put(gap * i, new double[noc]);
		}
		/* run simulation for multiple times */
		for (int q : qlist) {             // for each query number
			System.out.println("Number of queries: " + q);
			cmServer.updateLiesNeeded(q); // update expected number of lies
			int attempts = maxIteration;  // with in maxIteration, must succeed once
			int succeed = 0;              // number of successful attempts
			while (attempts > 0 && succeed < repeat) {
				multclient.reset();       // reset matrix to 0.5
				cmServer.reset();         // rest actual lies to 0
				for (int j = 0; j < q; j++) {
					multclient.randomLocation();
					multclient.query(cmServer);
				}
				if (!cmServer.reachNoiseLevel()) {
					System.out.println("Noise condition is not satisfied, try again");
					attempts--; // noise level not reached, bad attempt
				}
				else {
					double[] newIC = multclient.computeIC();
					double[] sum = icCMMap.get(q);
					for (int k = 0; k < noc; k++) {
						sum[k] += newIC[k] / repeat; // avoid overflow
					}
					icCMMap.put(q, sum);
					succeed++; // succeed
					attempts = maxIteration;  // have another [maxIteration] times for next success
				}
			}
			if (attempts == 0) { // can't reach noise level in [maxIteration] attempts
				feasible = false;
				return;
			}
		}
		printMultiple(qlist, icCMMap, directory, "cmp_AdditiveNoise.txt");
	}

	/**
	 * Construct email content
	 * @return   a string that describing simulation results
	 */
	@Override
	protected String buildMessage() {
		StringBuilder sb = new StringBuilder();
		if (!isFeasible()) {
			sb.append("<p>Noise level is too high. Noise requirement can't be reached.</p>");
		}
		else {
			sb.append("<p>Simulation results are plotted and attached to this email. "
					+ "Maps indecate attacker's speculation of primary users' whereabout for each channel. ");
			sb.append("Inaccuracy-query plot shows tendency of inaccuracy when number of queries increases.");
			sb.append("</p>");
		}
		return sb.toString();
	}

	/**
	 * Is the given noise level a practical one for this simulation
	 * @return true if noise requirement can be reached 
	 */
	public boolean isFeasible() {
		return feasible;
	}

	public String getCountermeasure() {
		return countermeasure;
	}

	public void tradeOffCurve() {
		// TODO Auto-generated method stub
		
	}
}