package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.Client;
import client.SmartAttacker;
import server.ServerKClustering;
import utility.Location;
import utility.PU;
import boot.BootParams;

public class SimKClustering extends Simulation {
	private String counterMeasure;            // name of countermeasure
	private int k;                            // k for clustering
	private ServerKClustering cmServer;        // instance of countermeasure server
	private boolean feasible;                 // is k for anonymity a valid parameter
	private Map<Integer, double[]> icCMMap;;  // ic for multiple simulation with countermeasure
	
	public SimKClustering(BootParams bootParams, double mtpScale,
			int interval, String directory) {
		/* parent instructor */
		super(bootParams, mtpScale, interval, directory);
		
		/* initialize countermeasure */
		this.counterMeasure = "KCLUSTERING";
		
		/* initialize k for clustering */
		this.k = (int) bootParams.getCMParam(this.counterMeasure);
		
		/* initialize server */
		cmServer = new ServerKClustering(map, noc, k);
		int PUid = 0;
		for (int k = 0; k < noc; k++) {
			List<Location> LatLngList = bootParams.getPUOnChannel(k);
			for (Location ll : LatLngList) {
				PU pu = new PU(PUid++, ll.getLatitude(), ll.getLongitude(), map);
				cmServer.addPU(pu, k);
			}
		}
		if (k > 0) {
			cmServer.clustering();
		}
		
		/* initialize hashmap for query-ic with countermeasure */
		icCMMap = new HashMap<Integer, double[]>();
		
		/* initialize feasible */
		if (this.k > 0) feasible = true;
		else feasible = false;
	}
	
	@Override
	public void singleRandomSimulation() {
		if (this.k > 0) {
			feasible = true;
		}
		else {
			feasible = false;
			return;
		}
		System.out.println("Start random query with k clustering for once...");

		/* initialize a client */
		Client client = new Client(cmServer);
		/* run simulation for once */
		for (int i = 0; i < noq; i++) {
			client.randomLocation();
			client.query(cmServer);
		}
		
		/* compute IC */
		IC = client.computeIC();
		
		printInfercenMatrix(cmServer, client, directory, "K_Clustering");
	}
	
	@Override
	public void randomSimulation() {
		if (this.k > 0) {
			feasible = true;
		}
		else {
			feasible = false;
			return;
		}
		
		// multiple simulation with k anonymity
		Client multclient = new Client(cmServer); 
		System.out.println("Start computing average IC with k clustering...");
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
		printICvsQ(qlist, icCMMap, directory, "cmp_KClustering.txt");
	}
	
	public void smartSimulation() {
		if (this.k > 0) {
			feasible = true;
		}
		else {
			feasible = false;
			return;
		}
		// multiple simulation with k anonymity
		SmartAttacker multclient = new SmartAttacker(cmServer); 
		System.out.println("Start computing average IC with k clustering using smart queries...");
		// compute query points
		int gap = noq / interval;
		// start query from 0 times
		List<Integer> qlist = new ArrayList<Integer>(10);
		for (int i = 0; i <= interval; i++) {
			qlist.add(gap * i);
			icSmartMap.put(gap * i, new double[noc]);
		}
		int maxQ = qlist.get(qlist.size() - 1);
		int repetition = 1;
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
		printInfercenMatrix(cmServer, multclient, directory, "smart_KClustering");
		printICvsQ(qlist, icSmartMap, directory, "cmp_smart_KClustering.txt");		}

	public void randomTradeOffBar() {
		System.out.println("Start computing trade off bar for K Clustering with random queries...");
		int repeat = 10;
		Client trClient = new Client(cmServer);
		// find value for k
		int k = 0;
		for (int i = 0; i < noc; i++) {
			k = Math.max(k, bootParams.getPUOnChannel(i).size());
		}
		// initialize value for k
		int[] cmVal = new int[k];
		int[] icVal = new int[k];
		for (int i = 1; i <= k; i++) {
			cmVal[i - 1] = i;
		}
		for (int i : cmVal) { // for different k
			cmServer.setK(i); // set new k and regroup
			for (int r = 0; r < repeat; r++) {
				trClient.reset();// reset k
				for (int q = 0; q < noq; q++) {
					trClient.randomLocation();
					trClient.query(cmServer);
				}
				icVal[i - 1] += (int) average(trClient.computeIC()) / repeat;
			}
		}
		printTradeOff(cmVal, icVal, directory, "traddOff_KClustering.txt");
	}
	
	private void printTradeOff(int[] cmString, int[] icVal,
			String directory, String string) {
		System.out.println("Start printing trade-off value...");
		File file = new File(directory + string);
		try {
			PrintWriter out = new PrintWriter(file);
			for (int k : cmString) {
				out.print(k + " ");
			}
			out.println();	
			for (int ic : icVal) {
				out.print(ic + " ");
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
	
	public String getCountermeasure() {
		return counterMeasure;
	}

	public boolean isFeasible() {
		return feasible;
	}

	public void smartTradeOffBar() {
		// TODO Auto-generated method stub
		
	}
}
