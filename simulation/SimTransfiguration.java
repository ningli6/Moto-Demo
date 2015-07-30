package simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.ServerTransfiguration;
import utility.Location;
import utility.PU;
import boot.BootParams;
import client.Client;

public class SimTransfiguration extends Simulation {
	private String counterMeasure;            // name of countermeasure
	private int sides;                        // number of sides for polygon
	private ServerTransfiguration cmServer;   // instance of countermeasure server
	private boolean feasible;                 // if number of sides is valid
	private Map<Integer, double[]> icCMMap;   // ic for multiple simulation with countermeasure
	
	/**
	 * Construct a transfiguration simulator
	 * @param bp        boot parameters
	 * @param cs        cell size
	 * @param scale     mtp scale
	 * @param inter     internal number of queries for multiple simulations
	 * @param dir       directory
	 */
	public SimTransfiguration(BootParams bp, double cs, double scale, int inter, String dir) {
		/* call parent instructor */
		super(bp, cs, scale, inter, dir);

		/* initialize countermeasure */
		this.counterMeasure = bootParams.getCountermeasure();

		/* initialize number of sides for transfiguration */
		this.sides = (int) bootParams.getCMParam();

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
		feasible = false;
	}

	@Override
	public void singleSimulation() {
		icq = false;           // do not include ic vs q
		if (sides > 2) {
			feasible = true;
		}
		if (!feasible) {
			System.out.println("Number of sides for polygon must be an integer greater than 2");
			return;
		}
		System.out.println("Start querying...");

		/* initialize a client */
		Client client = new Client(cmServer);
		/* run simulation for once */
		for (int i = 0; i < noq; i++) {
			client.randomLocation();
			client.query(cmServer);
		}
		
		/* compute IC */
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
		if (!feasible) return;
		if (noq < 50) {
			icq = false;
			return;
		}
		icq = true;
		super.multipleSimulation();

		/**
		 * use a new client for multiple simulation, 
		 */
		Client multclient = new Client(cmServer); 
		System.out.println("Start computing average IC with transfiguration...");
		// compute query points
		int gap = noq / interval;
		int base = 1;
		int tmp = gap;
		while(tmp / base > 0) {
			base *= 10;
		}
		gap = (gap / (base / 10)) * (base / 10);
		// start query from 0 times
		List<Integer> qlist = new ArrayList<Integer>(10);
		for (int i = 0; i <= interval + 1; i++) {
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
		printMultiple(qlist, icCMMap, directory, "cmp_Transfiguration.txt");
	}

	
	/**
	 * Construct email content
	 * @return   a string that describing simulation results
	 */
	@Override
	protected String buildMessage() {
		StringBuilder sb = new StringBuilder();
		if (!isFeasible()) {
			sb.append("<p>Simulation failed! Sides for polygon must be no less than 3.</p>");
		}
		else {
			sb.append("<p>Simulation results are plotted and attached to this email. "
					+ "Maps indecate attacker's speculation of primary users whereabout for each channel. ");
			if (icq) {
				sb.append("Inaccuracy-query plot shows tendency of inaccuracy when number of queries increase.");
			}
			sb.append("</p>");
		}
		return sb.toString();
	}

	public boolean isFeasible() {
		return feasible;
	}

	public String getCountermeasure() {
		return counterMeasure;
	}
}