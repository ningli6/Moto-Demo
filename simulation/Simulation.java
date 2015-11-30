package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.Server;
import utility.GridMap;
import utility.Location;
import utility.MTP;
import utility.PU;
import boot.BootParams;
import client.Client;
import client.SmartAttacker;

public class Simulation {
	String directory;      // output dir
	BootParams bootParams; // params
	double cellsize;       // cell size in degree
	double mtpScale;       // control MTP function's protection zoo
	int interval;          // how many querying points from 0 to noq
	GridMap map;           // instance of grid map
	Server server;         // instance of base server
	int noc;               // number of channels
	int noq;               // number of queries
	int repeat;            // number of repetition for multiple simulations
	double[] IC;           // ic for single simulation, may include this information in email
	Map<Integer, double[]> icMap;       // associate ic to number of queries
	Map<Integer, double[]> icSmartMap;  // associate ic to number of queries under smart query
	
	public Simulation(BootParams bootParams, double mtpScale, int interval, String directory) {
		this.bootParams = bootParams;
		this.cellsize = bootParams.getCellSize();
		this.mtpScale = mtpScale;
		this.interval = interval;
		this.directory = directory;

		/* initialize map */
		Location nwLoc = new Location(bootParams.getNorthLat(), bootParams.getWestLng());
		Location seLoc = new Location(bootParams.getSouthLat(), bootParams.getEastLng());
		this.map = new GridMap(nwLoc, seLoc, cellsize);
		/* initialize number of channels */
		noc = bootParams.getNumberOfChannels();

		/* initialize MTP scale */
		MTP.ChangeMult(mtpScale);
		
		/* initialize server */
		server = new Server(map, noc);
		
		/* Add PU to the map */
		int PUid = 0;
		for (int k = 0; k < noc; k++) {
			List<Location> LatLngList = bootParams.getPUOnChannel(k);
			for (Location ll : LatLngList) {
				PU pu = new PU(PUid++, ll.getLatitude(), ll.getLongitude(), map);
				server.addPU(pu, k);
			}
		}

		/* initialize query locations */
		noq = bootParams.getNumberOfQueries();
		
		/* initialize number of repetition */
		repeat = 20;

		/* initialize ic */
		IC = null;

		/* associate ic to number of queries */
		icMap = new HashMap<Integer, double[]>();
		icSmartMap = new HashMap<Integer, double[]>();
	}

	public void singleRandomSimulation() {
		System.out.println("Start random query withouth countermeasure for once...");
		/* initialize a client */
		Client client = new Client(server);
		/* run simulation for one time */
		for (int i = 1; i <= noq; i++) {
			client.randomLocation();
			client.query(server);
		}
		IC = client.computeIC();
		
		printInfercenMatrix(server, client, directory, "No_Countermeasure");
	}

	public void randomSimulation() {
		System.out.println("Start computing average IC...");
		// create a new client instead of using the old one, which is saved for email to use
		Client multclient = new Client(server);
		// compute query points
		int gap = noq / interval;
		// start query from 0 times
		List<Integer> qlist = new ArrayList<Integer>(10);
		for (int i = 0; i <= interval; i++) {
			qlist.add(gap * i);
			icMap.put(gap * i, new double[noc]);
		}
		int maxQ = qlist.get(qlist.size() - 1);
		/* run simulation for multiple times */
		icMap.put(0, multclient.computeIC()); // ic at query 0 is constant
		for (int rep = 0; rep < repeat; rep++){
			for (int i = 1; i <= maxQ; i++) {
				multclient.randomLocation();
				multclient.query(server);
				if (icMap.containsKey(i)){
					double[] newIC = multclient.computeIC();
					double[] sum = icMap.get(i);
					for (int k = 0; k < noc; k++) {
						sum[k] += newIC[k] / repeat; // avoid overflow
					}
					icMap.put(i, sum);
				}
			}
			multclient.reset();
		}
		printICvsQ(qlist, icMap, directory, "cmp_NoCountermeasure.txt");
	}
	
	public void smartSimulation() {
		System.out.println("Start smart quering...");
		/* initialize a client */
		SmartAttacker attacker = new SmartAttacker(server);
		// find record point, works if number of queries is multiple of 10
		int gap = noq / interval;
		// start query from 0 times
		List<Integer> qlist = new ArrayList<Integer>(10);
		for (int i = 0; i <= interval; i++) {
			qlist.add(gap * i);
			icSmartMap.put(gap * i, new double[noc]);
		}
		int maxQ = qlist.get(qlist.size() - 1);
		icSmartMap.put(0, attacker.computeIC());    // ic at query 0 is constant
		int repetition = 1;
		for (int rep = 0; rep < repetition; rep++){ // repetition
			attacker.reset();
			for (int i = 1; i <= maxQ; i++) {       // number of queries
				/* Debug */
				System.out.println("Q: " + i);
				attacker.smartLocation();           // find next query location
				attacker.query(server);
				if (icSmartMap.containsKey(i)){
					double[] newIC = attacker.computeIC();
					double[] sum = icSmartMap.get(i);
					for (int k = 0; k < noc; k++) {
						sum[k] += newIC[k] / repetition; // avoid overflow
					}
					icSmartMap.put(i, sum);
				}
			}
		}
		printInfercenMatrix(server, attacker, directory, "smart_No_Countermeasure");
		printICvsQ(qlist, icSmartMap, directory, "cmp_smart_NoCountermeasure.txt");
	}

	/**
	 * Print probability matrix and location of pu on each channel
	 * @param server       provides information about location of primary users
	 * @param client       provide probability matrix
	 * @param dir          output path of text file
	 * @param fileName     file that holds information about probability and locations
	 */
	public void printInfercenMatrix(Server server, Client client, String dir, String fileName) {
		if (client == null || server == null || dir == null || dir.length() == 0) {
			throw new NullPointerException();
		}
		System.out.println("Start printing location of primary users...");
		server.printPUAllChannel(dir, fileName);
		System.out.println("Start printing probability...");
		client.printProbability(dir, fileName);
	}

	/**
	 * Print IC vs Query
	 * @param qlist points of queries
	 * @param icMap ic values at each point of query
	 * @param dir output directory
	 * @param fileName name of the text file
	 */
	public void printICvsQ(List<Integer> qlist, Map<Integer, double[]> icMap, String dir, String fileName) {
		System.out.println("Start printing average IC...");
		File file = new File(dir + fileName);
		try {
			PrintWriter out = new PrintWriter(file);
			for (Integer q : qlist) {
				out.print(q + " ");
			}
			out.println();	
			for (int i = 0; i < noc; i++) {
				for (int q : qlist) {
					out.print((int) icMap.get(q)[i] + " ");
				}
				out.println();
			}
			out.close (); // this is necessary	
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}