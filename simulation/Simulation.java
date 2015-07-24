package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import javaEmail.SendEmail;
import javaPlot.CmpPlot;
import javaPlot.MatPlot;
import client.Client;
import server.Server;
import utility.GridMap;
import utility.Location;
import utility.MTP;
import utility.PU;
import boot.BootParams;
import boot.LatLng;

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
	boolean icq;           // whether to include ic vs q, if query is greater than 100, icq is set to true
	Map<Integer, double[]> icMap; // associate ic to number of queries
	StringBuilder content; // for email

	public Simulation(BootParams bp, double cs, double scale, int inter, String dir) {
		this.bootParams = bp;
		this.cellsize = cs;
		this.mtpScale = scale;
		this.interval = inter;
		this.directory = dir;

		/* initialize map */
		Location upperLeft = new Location(bootParams.getNorthLat(), bootParams.getWestLng());
		Location upperRight = new Location(bootParams.getNorthLat(), bootParams.getEastLng());
		Location lowerLeft = new Location(bootParams.getSouthLat(), bootParams.getWestLng());
		Location lowerRight = new Location(bootParams.getSouthLat(), bootParams.getEastLng());
		this.map = new GridMap(upperLeft, upperRight, lowerLeft, lowerRight, cellsize);

		/* debug 
		 * See number of rows & cols in the map
		 */
//		map.showBoundary();
//		System.out.println("Map rows: " + map.getRows());
//		System.out.println("Map cols: " + map.getCols());
//		System.out.println("Average size per cell: " + map.getAverageDistance() + " km");
//		System.out.println("Total cells: " + map.getNumberOfCells());

		/* initialize number of channels */
		noc = bootParams.getNumberOfChannels();

		/* initialize MTP scale */
		MTP.ChangeMult(mtpScale);
		
		/* initialize server */
		server = new Server(map, noc);

		/* Add PU to the map */
		int PUid = 0;
		for (int k = 0; k < noc; k++) {
			List<LatLng> LatLngList = bootParams.getPUOnChannel(k);
			for (LatLng ll : LatLngList) {
				PU pu = new PU(PUid++, ll.getLat(), ll.getLng(), map);
//				System.out.println("PU: " + pu.getRowIndex() + ", " + pu.getColIndex());
				server.addPU(pu, k);
			}
		}

		/* initialize query locations */
		noq = bootParams.getNumberOfQueries();
		
		/* initialize number of repetition */
		repeat = 10;

		/* initialize ic */
		IC = null;

		/* whether to plot ic vs q */
		icq = false;

		/* associate ic to number of queries */
		icMap = new HashMap<Integer, double[]>();

		/* initialize email content */
		content = new StringBuilder(bootParams.paramsToString());
	}

	public void singleSimulation() {
		System.out.println("Start querying...");
		/* initialize a client */
		Client client = new Client(server);
		// do not print ic vs q
		icq = false;
		/* run simulation for one time */
		for (int i = 1; i <= noq; i++) {
			client.randomLocation();
			/**
			 * Debug: See client's new location
			 */
//			System.out.println("***Query***");
//			System.out.println("Query location: " + client.getRowIndex() + ", " + client.getColIndex());
			client.query(server);
		}
		IC = client.computeIC();
		
		/* debug */
//		for (List<PU> puList : server.getChannelsList()) {
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
		
		printSingle(server, client, directory);
	}

	public void multipleSimulation() {
		System.out.println("Start computing average IC...");
		// update icq
		if (noq < 50) {
			icq = false;
			return;
		}
		icq = true;

		// create a new client instead of using the old one, which is saved for email to use
		Client multclient = new Client(server);
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
			multclient.reset(); // set infer matrix to 0.5
		}
		printMultiple(qlist, icMap, directory, "averageIC_NoCountermeasure.txt");
	}

	/**
	 * Print text file
	 * Probability matrix and location of pu on each channel
	 * @param server provides information about location of primary users
	 * @param client provide probability matrix
	 * @param dir output path of text file
	 */
	public void printSingle(Server server, Client client, String dir) {
		if (client == null || server == null || dir == null || dir.length() == 0) {
			throw new NullPointerException();
		}
		
		System.out.println("Start printing probability...");
		client.printProbability(dir);
		
		System.out.println("Start printing location of primary users...");
		server.printPUAllChannel(dir);
	}

	/**
	 * Print text file
	 * IC vs Query
	 * @param qlist points of queries
	 * @param icMap ic values at each point of query
	 * @param dir output directory
	 * @param fileName name of the text file
	 */
	public void printMultiple(List<Integer> qlist, Map<Integer, double[]> icMap, String dir, String fileName) {
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

	private String buildMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("<h3>Simlation_result</h3>");
		sb.append("<p>Distribution_of_primary_users:<br>");
		sb.append("Number_of_PUs:_" + server.getNumberOfPUs() + "<br>");
		sb.append(server.puOnChannelToString()); // which pu is on which channel
		sb.append("</p>");
		sb.append("<p>Querying_information:<br>");
//		sb.append(client.countChannelUpdateToString()); // channel is updated how many times
		sb.append("</p>");
		if (IC != null) {
			sb.append("<p>Inaccuracy_for_each_channel:<br>");
			for (int i = 0; i < IC.length; i++) {
				sb.append("Channel_" + i + ":_" + IC[i] + "<br>");
			}
		}
		sb.append("</p>");
		sb.append("<p>See_probability_plots_in_the_attachments._Location_of_primary_users_are_marked_as_yellow_stars.</p>");
		return sb.toString();
	}

	public String getEmailContent() {
		return content.append(buildMessage()).toString();
	}

	/**
	 * Plot function that helps to plot data on google map and ic vs query
	 * @param plotProbOnMap   whether to plot data on google map
	 * @param iCvsQ           whether to plot ic vs query on google map
	 * @param countermeasure  name of the countermeasure
	 */
	public void plot(boolean plotProbOnMap, boolean iCvsQ, String countermeasure) {
    	System.out.println("Plotting probability distribution on Google Map...");
    	if (plotProbOnMap) {
        	for (int i = 0; i < noc; i++) {
        		if (!MatPlot.plot(i, map.getRows(), map.getCols(), bootParams.getNorthLat(), bootParams.getSouthLat(), bootParams.getWestLng(), bootParams.getEastLng())) {
        			System.out.println("Plotting failed");
        		}
        	}
    	}
		if (iCvsQ) { // this is set to true if noq is greater than 100
			System.out.println("Plotting average inacurracy...");
			if (!CmpPlot.plot(countermeasure)) {
				System.out.println("Plotting failed");
			}
		}
	}
	
	/**
	 * Send email to users attached with google map plots and ic vs query plots,
	 * @param googleMap  whether to include plot of probability data
	 * @param iCvsQ      whether to include plot of ic vs query
	 */
	public void sendEmail(boolean googleMap, boolean iCvsQ) {
		System.out.println("Sending email...");
		// call send email api
		if (!SendEmail.send("ningli@vt.edu", bootParams.getEmail(), getEmailContent(), noc, googleMap, iCvsQ)) {
			System.out.print("Email failed!");
		}
	}
	
	/**
	 * Whether to plot data on google map
	 * @return true if query number is greater than 50 times
	 */
	public boolean plotICvsQuery() {
		return icq;
	}
}