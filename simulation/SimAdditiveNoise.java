package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.ServerAdditiveNoise;
import utility.Location;
import utility.PU;
import boot.BootParams;
import client.Client;
import client.SmartAttacker;

public class SimAdditiveNoise extends Simulation {
	private String countermeasure;        // name of countermeasure
	private double noiseLevel;            // noise level, [0, 1]
	private ServerAdditiveNoise cmServer; // instance of countermeasure server
	private int maxIteration;             // max attempts to reach noise level
	private boolean feasible;             // whether this noise level is feasible
	private Map<Integer, double[]> icCMMap;;      // ic for multiple simulation with countermeasure
	
	public SimAdditiveNoise(BootParams bootParams, double mtpScale, int interval, String directory) {
		/* call parent constructor */
		super(bootParams, mtpScale, interval, directory);

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

	public double getNoiseLevel() {
		return noiseLevel;
	}

	public ServerAdditiveNoise getCmServer() {
		return cmServer;
	}

	@Override
	public void singleRandomSimulation() {
		if (noiseLevel < 0 || noiseLevel > 1) {
			System.out.println("Noise level must be in range 0 to 1.");
			feasible = false;   // do not execute following simulations
			return;
		}
		if (!feasible) {  // multiple simulation was performed first, so if it's not feasible, return
			return;
		}
		System.out.println("Start random query with additive noise for once...");  
		Client client = new Client(cmServer);		// initialize a client
		int attempts = maxIteration;		        // define max iteration
		while(attempts > 0) {
			client.reset();  // clear client's probability map to 0.5
			cmServer.reset();// set actual lies back to 0
			for (int i = 0; i < noq; i++) {
				client.randomLocation();
				client.query(cmServer);
			}
			if (cmServer.reachNoiseLevel()) { // if noise level satisfied
				System.out.println("Noise level satisfied!");
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
		IC = client.computeIC();
		
		printInfercenMatrix(cmServer, client, directory, "Additive_Noise");
	}

	@Override
	public void randomSimulation() {
		if (this.noiseLevel > 1 || this.noiseLevel < 0) {
			feasible = false;
			System.out.println("Noise level is not feasible.");
			return;
		}
		System.out.println("Start computing average IC with additive noise...");
		Client multclient = new Client(cmServer); // get a new client
		int gap = noq / interval;  		          // compute query points, number of query must be a mulitple of 10
		List<Integer> qlist = new ArrayList<Integer>();
		for (int i = 0; i <= interval; i++) {     // start query from 0 times
			qlist.add(gap * i);
			icCMMap.put(gap * i, new double[noc]);
		}
		for (int q : qlist) {             // for each query number
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
		feasible = true;         // noise level is feasible, proceed
		printICvsQ(qlist, icCMMap, directory, "cmp_AdditiveNoise.txt");
	}
	
	@Override
	public void smartSimulation() {
		if (this.noiseLevel > 1 || this.noiseLevel < 0) {
			feasible = false;
			System.out.println("Noise level is not feasible.");
			return;
		}
		System.out.println("Start smart query with additive noise...");
		SmartAttacker attacker = new SmartAttacker(cmServer); // get a new client
		int gap = noq / interval;  		          // compute query points, number of query must be a mulitple of 10
		int iteration = 5;
		List<Integer> qlist = new ArrayList<Integer>();
		for (int i = 0; i <= interval; i++) {     // start query from 0 times
			qlist.add(gap * i);
			icSmartMap.put(gap * i, new double[noc]);
		}
		for (int q : qlist) {             // for each query number
			System.out.println("For query: " + q);
			cmServer.updateLiesNeeded(q); // update expected number of lies
			int attempts = iteration;     // within 5 attempts, must succeed once
			int succeed = 0;              // number of successful attempts
			while (attempts > 0 && succeed < 1) {
				attacker.reset();         // reset matrix to 0.5
				cmServer.reset();         // rest actual lies to 0
				for (int j = 0; j < q; j++) {
					System.out.println("Q: " + j);
					attacker.smartLocation();
					attacker.query(cmServer);
				}
				if (!cmServer.reachNoiseLevel()) {
					System.out.println("Noise condition is not satisfied, try again");
					attempts--; // noise level not reached, bad attempt
				}
				else {
					double[] newIC = attacker.computeIC();
					double[] sum = icSmartMap.get(q);
					for (int k = 0; k < noc; k++) {
						sum[k] += newIC[k]; // avoid overflow
					}
					icSmartMap.put(q, sum);
					succeed++; // succeed
					attempts = iteration;  // have another [maxIteration] times for next success
				}
			}
			if (attempts == 0) { // can't reach noise level in [maxIteration] attempts
				feasible = false;
				return;
			}
		}
		feasible = true;         // noise level is feasible, proceed
		printInfercenMatrix(cmServer, attacker, directory, "smart_AddtiveNoise");
		printICvsQ(qlist, icSmartMap, directory, "cmp_smart_AddtiveNoise.txt");
	}
	
	public void testSmartSimulation(int noq) {
		System.out.println("Start additive noise simulation wiht " + noq + " number of queries");
		SmartAttacker attacker = new SmartAttacker(cmServer); // get a new client
		cmServer.updateLiesNeeded(noq); // update expected number of lies
		for (int j = 0; j < noq; j++) {
			System.out.println("Q: " + j);
			attacker.smartLocation();
			attacker.query(cmServer);
		}
		if (!cmServer.reachNoiseLevel()) {
			System.out.println("Noise condition is not satisfied.");
		}
		double[] newIC = attacker.computeIC();
		for (int i = 0; i < newIC.length; i++) {
			System.out.println("IC of channel " + i + ": " + newIC[i]);
		}
	}
	
	/**
	 * Plot trade-of curve, output data in a file named traddOff_AdditiveNoise.txt
	 */
	public void randomTradeOffCurve() {
		double[] cmString = {0, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8};
		double[] trdIC = new double[8];
		int repeat = 20;
		System.out.println("Start computing trade off curve for additive noise with random queries...");
		Client trdOfClient = new Client(cmServer);  // create a new client
		for (int k = 0; k < cmString.length; k++) { // for each noise level
			cmServer.setNoiseLevel(cmString[k]);    // set new noise level
			System.out.println("Noise: " + cmString[k]);
			for (int r = 0; r < repeat; r++) {
				trdOfClient.reset();                // set matrix back to 0.5
				cmServer.reset();                   // set actual lies back to 0
				for (int i = 0; i < noq; i++) {
					trdOfClient.randomLocation();
					trdOfClient.query(cmServer);
				}
				trdIC[k] += average(trdOfClient.computeIC()) / repeat;
			}
		}
		printTradeOff(cmString, trdIC, directory, "traddOff_AdditiveNoise.txt");
	}
	
	private void printTradeOff(double[] cm, double[] ic, String path, String fileName) {
		System.out.println("Start printing trade-off value...");
		File file = new File(path + fileName);
		try {
			PrintWriter out = new PrintWriter(file);
			for (double q : cm) {
				out.print(q + " ");
			}
			out.println();	
			for (double q : ic) {
				out.print(q + " ");
			}
			out.println();
			out.close (); // this is necessary	
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Find the average ic of all channels
	 * @param nums   array that contains ic values for all channels
	 * @return       average ic of all channels
	 */
	public double average(double[] nums) {
		double ans = 0;
		int len = nums.length;
		for (double d : nums) {
			ans += d / len;
		}
		return ans;
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

	public void smartTradeOffCurve() {
		// TODO Auto-generated method stub
		
	}
}