package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.ServerTransfiguration;
import utility.GridMap;
import utility.Location;
import utility.PU;
import boot.BootParams;
import boot.Parser;
import client.Client;
import client.SmartAttacker;

public class SimTransfiguration extends Simulation {
	private String counterMeasure;            // name of countermeasure
	private int sides;                        // number of sides for polygon
	private ServerTransfiguration cmServer;   // instance of countermeasure server
	private boolean feasible;                 // if number of sides is valid
	private Map<Integer, double[]> icCMMap;   // ic for multiple simulation with countermeasure
	
	/**
	 * Construct a transfiguration simulator
	 * @param bootParams        boot parameters
	 * @param mtpScale     mtp scale
	 * @param interval     internal number of queries for multiple simulations
	 * @param directory       directory
	 */
	public SimTransfiguration(BootParams bootParams, double mtpScale, int interval, String directory) {
		/* call parent instructor */
		super(bootParams, mtpScale, interval, directory);

		/* initialize countermeasure */
		this.counterMeasure = "TRANSFIGURATION";

		/* initialize number of sides for transfiguration */
		this.sides = (int) bootParams.getCMParam(this.counterMeasure);

		/* initialize server with transfiguration */
		cmServer = new ServerTransfiguration(map, noc, sides);
		int PUid = 0;
		for (int k = 0; k < noc; k++) {
			List<Location> LatLngList = bootParams.getPUOnChannel(k);
			for (Location ll : LatLngList) {
				PU pu = new PU(PUid++, ll.getLatitude(), ll.getLongitude(), map);
				cmServer.addPU(pu, k);
			}
		}
		// after adding pu, wrap pu as polygon 
		if (sides > 2) cmServer.transfigure();

		/* initialize hashmap for query-ic with countermeasure */
		icCMMap = new HashMap<Integer, double[]>();
		
		/* initialize feasible */
		if (sides > 2) feasible = true;
		else feasible = false;
	}

	@Override
	public void singleRandomSimulation() {
		if (sides > 2) {
			feasible = true;
		}
		else {
			feasible = false;
			return;
		}
		System.out.println("Start random query with transfiguration for once...");

		/* initialize a client */
		Client client = new Client(cmServer);
		/* run simulation for once */
		for (int i = 0; i < noq; i++) {
			client.randomLocation();
			client.query(cmServer);
		}
		
		/* compute IC */
		IC = client.computeIC();
		
		printInfercenMatrix(cmServer, client, directory, "Transfiguration");
	}

	@Override
	public void randomSimulation() {
		if (sides > 2) {
			feasible = true;
		}
		else {
			feasible = false;
			return;
		}
		/**
		 * use a new client for multiple simulation, 
		 */
		Client multclient = new Client(cmServer); 
		System.out.println("Start computing average IC with transfiguration...");
		// compute query points
		int gap = noq / interval;
		// start query from 0 times
		List<Integer> qlist = new ArrayList<Integer>(10);
		for (int i = 0; i <= interval; i++) {
			qlist.add(gap * i);
			icCMMap.put(gap * i, new double[noc]);
		}
		int maxQ = qlist.get(qlist.size() - 1);
		/* run simulation for multiple times */
		icCMMap.put(0, multclient.computeIC()); // ic at query 0 is constant
		for (int rep = 0; rep < repeat; rep++){
			for (int i = 1; i <= maxQ; i++) {
				multclient.randomLocation();
				multclient.query(cmServer);
				if (icCMMap.containsKey(i)){
					double[] newIC = multclient.computeIC();
					double[] sum = icCMMap.get(i);
					for (int k = 0; k < noc; k++) {
						sum[k] += newIC[k] / repeat; // avoid overflow
					}
					icCMMap.put(i, sum);
				}
			}
			multclient.reset(); // set infer matrix to 0.5
		}
		printICvsQ(qlist, icCMMap, directory, "cmp_Transfiguration.txt");
	}
	
	/**
	 * Simulation using smart query algorithm
	 */
	@Override
	public void smartSimulation() {
		if (sides > 2) {
			feasible = true;
		}
		else {
			feasible = false;
			return;
		}
		SmartAttacker multclient = new SmartAttacker(cmServer); 
		System.out.println("Start computing average IC with transfiguration usin smart querying...");
		// compute query points
		int gap = noq / interval;
		// start query from 0 times
		List<Integer> qlist = new ArrayList<Integer>(10);
		for (int i = 0; i <= interval; i++) {
			qlist.add(gap * i);
			icSmartMap.put(gap * i, new double[noc]);
		}
		int maxQ = qlist.get(qlist.size() - 1);
		int repetition = 1; // just repeat for once
		/* run simulation for multiple times */
		icSmartMap.put(0, multclient.computeIC()); // ic at query 0 is constant
		for (int rep = 0; rep < repetition; rep++){
			for (int i = 1; i <= maxQ; i++) {
				System.out.println("Q: " + i);
				multclient.smartLocation();
				multclient.query(cmServer);
				if (icSmartMap.containsKey(i)){
					double[] newIC = multclient.computeIC();
					double[] sum = icSmartMap.get(i);
					for (int k = 0; k < noc; k++) {
						sum[k] += newIC[k] / repetition; // avoid overflow
					}
					icSmartMap.put(i, sum);
				}
			}
			multclient.reset(); // set infer matrix to 0.5
		}
		printInfercenMatrix(cmServer, multclient, directory, "smart_Transfiguration");
		printICvsQ(qlist, icSmartMap, directory, "cmp_smart_Transfiguration.txt");	
	}

	public void randomTradeOffCurve() {
		int[] cmString = {3, 4, 5, 6};
		double[] trdIC = new double[cmString.length];
		String args = "-cd 0.005 -a 38.05890484918669 -79.70169067382812 37.46831856835604 -78.88320922851562 -c 1 -C 37.779398571318765 -79.29519653320312 -cm tf 3 -gm -tr tf -q 50 -sq -e ningli@vt.edu -opt pa ";
		BootParams tfBp = Parser.parse(args.split(" "));
		Location upperLeft = new Location(tfBp.getNorthLat(), tfBp.getWestLng());
		Location lowerRight = new Location(tfBp.getSouthLat(), tfBp.getEastLng());
		GridMap tfMap = new GridMap(upperLeft, lowerRight, tfBp.getCellSize());
		ServerTransfiguration tfServer = new ServerTransfiguration(tfMap, tfBp.getNumberOfChannels(), (int) tfBp.getCMParam("TRANSFIGURATION"));
		int PUid = 0;
		for (int k = 0; k < tfBp.getNumberOfChannels(); k++) {
			List<Location> LatLngList = tfBp.getPUOnChannel(k);
			for (Location ll : LatLngList) {
				PU pu = new PU(PUid++, ll.getLatitude(), ll.getLongitude(), tfMap);
				tfServer.addPU(pu, k);
			}
		}
		int repeat = 30;
		System.out.println("Start computing trade off curve for transfiguration withe random queries...");
		Client trdOfClient = new Client(tfServer);     // get a new client
		for (int k = 0; k < cmString.length; k++) {// for each sides
			tfServer.transfigure(cmString[k]);         // set new number of sides
			System.out.println("Sides: " + cmString[k]);
			int count = 0;
			while (true) {
				for (int r = 0; r < repeat; r++) {
					trdOfClient.reset(); // set client maps back to 0.5
					for (int i = 0; i < tfBp.getNumberOfQueries(); i++) {
						trdOfClient.randomLocation();
						trdOfClient.query(tfServer);
					}
					double[] ic = trdOfClient.computeIC();
					trdIC[k] += average(ic) / repeat;
				}
				System.out.println(cmString[k] + ": " + trdIC[k]);
				count++;
				if (count == 5) {   // if after 5 attempts, we can't get smaller value of ic, make it 0.7 of its predecessor
					trdIC[k] = 07 * trdIC[k - 1];
				}
				if (k == 0 || trdIC[k] < trdIC[k - 1]) break;  // try to get a smaller ic for larger sides
				trdIC[k] = 0;
			}
		}
		
		printTradeOff(cmString, trdIC, directory, "traddOff_Transfiguration.txt");
	}
	
	public void smartTradeOffCurve() {
		int[] cmString = {3, 4, 5, 6};
		double[] trdIC = new double[cmString.length];
		int repeat = 1;
		System.out.println("Start computing trade off curve for transfiguration withe smart queries...");
		SmartAttacker trdOfClient = new SmartAttacker(cmServer);     // get a new client
		for (int k = 0; k < cmString.length; k++) {// for each sides
			cmServer.transfigure(cmString[k]);         // set new number of sides
			System.out.println("Sides: " + cmString[k]);
			for (int r = 0; r < repeat; r++) {
				trdOfClient.reset(); // set client maps back to 0.5
				for (int i = 0; i < noq; i++) {
					System.out.println("Q: " + i);
					trdOfClient.smartLocation();
					trdOfClient.query(cmServer);
				}
				double[] ic = trdOfClient.computeIC();
				trdIC[k] += average(ic) / repeat;
			}
		}
		printTradeOff(cmString, trdIC, directory, "traddOff_smart_Transfiguration.txt");		
	}
	
	private void printTradeOff(int[] cmString, double[] trdIC,
			String directory, String string) {
		System.out.println("Start printing trade-off value...");
		File file = new File(directory + string);
		try {
			PrintWriter out = new PrintWriter(file);
			for (int q : cmString) {
				out.print(q + " ");
			}
			out.println();	
			for (double q : trdIC) {
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

	private double average(double[] computeIC) {
		double ans = 0;
		int len = computeIC.length;
		for (double d : computeIC) {
			ans += d / len;
		}
		return ans;
	}

	public boolean isFeasible() {
		return feasible;
	}

	public String getCountermeasure() {
		return counterMeasure;
	}
}