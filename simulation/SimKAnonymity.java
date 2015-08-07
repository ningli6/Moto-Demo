package simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.ServerKAnonymity;
import utility.Location;
import utility.PU;
import boot.BootParams;
import client.Client;

public class SimKAnonymity extends Simulation {
	private String counterMeasure;            // name of countermeasure
	private int k;                            // k for anonymity
	private ServerKAnonymity cmServer;        // instance of countermeasure server
	private boolean feasible;                 // is k for anonymity a valid parameter
	private Map<Integer, double[]> icCMMap;;  // ic for multiple simulation with countermeasure

	public SimKAnonymity(BootParams bootParams, double cellsize,
			double mtpScale, int interval, String directory) {
		
		/* parent instructor */
		super(bootParams, cellsize, mtpScale, interval, directory);
		
		/* initialize countermeasure */
		this.counterMeasure = "KANONYMITY";
		
		/* initialize k for anonymity */
		this.k = (int) bootParams.getCMParam(this.counterMeasure);
		
		/* initialize server */
		cmServer = new ServerKAnonymity(map, noc, k);
		int PUid = 0;
		for (int k = 0; k < noc; k++) {
			List<Location> LatLngList = bootParams.getPUOnChannel(k);
			for (Location ll : LatLngList) {
				PU pu = new PU(PUid++, ll.getLatitude(), ll.getLongitude(), map);
				cmServer.addPU(pu, k);
				/* debug */
//				pu.printInfo();
			}
		}
		if (k > 0) {
			cmServer.groupKPUs();
		}
		
		/* initialize hashmap for query-ic with countermeasure */
		icCMMap = new HashMap<Integer, double[]>();
		
		/* initialize feasible */
		feasible = false;
	}

	@Override
	public void singleSimulation() {
		if (this.k > 0) {
			feasible = true;
		}
		if (!feasible) {
			System.out.println("K for anonymity must be a postitive integer");
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
//		for (List<PU> puList : cmServer.getVirtualChannelList()) {
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
		
		printSingle(cmServer, client, directory, "K_Anonymity");
	}
	
	@Override
	public void multipleSimulation() {
		if (this.k < 1) {
			this.feasible = false;
			System.out.println("K must be a positive integer.");
			return;
		}

		// multiple simulation with k anonymity
		Client multclient = new Client(cmServer); 
		System.out.println("Start computing average IC with k anonymity...");
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
		printMultiple(qlist, icCMMap, directory, "cmp_kAnonymity.txt");
	}
	
	public boolean isFeasible() {
		return feasible;
	}

	public String getCountermeasure() {
		return counterMeasure;
	}
	
	/**
	 * Construct email content
	 * @return   a string that describing simulation results
	 */
	@Override
	protected String buildMessage() {
		StringBuilder sb = new StringBuilder();
		if (!isFeasible()) {
			sb.append("<p>K for anonymity must be a positive integer.</p>");
		}
		else {
			sb.append("<p>Simulation results are plotted and attached to this email. "
					+ "Maps indecate attacker's speculation of primary users' whereabout for each channel. ");
			sb.append("Inaccuracy-query plot shows tendency of inaccuracy when number of queries increases.");
			sb.append("</p>");
		}
		return sb.toString();
	}
}
