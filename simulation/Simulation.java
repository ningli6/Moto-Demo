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
	BootParams bootParams; // params
	double cellsize;
	double mtpScale;
	int interval;         
	StringBuilder content; // for email
	GridMap map;
	int noc;               // number of channels
	int noq;               // number of queries
	Server server;
	Client client;
	double[] IC;           // ic for single simulation
	boolean icq;           // whether to include ic vs q, if query is greater than 100, icq is set to true
	boolean gMap;          // whether to include google map in email
	List<Integer> qlist;   // queries for multiple simulation
	List<Double>[] rlist;  // ic for multiple simulation
	String directory;      // output dir

	@SuppressWarnings("unchecked")
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
		map.showBoundary();
		System.out.println("Map rows: " + map.getRows());
		System.out.println("Map cols: " + map.getCols());


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
				/**
				 * Debug: See pu's row & col index
				 */
				System.out.println("PU: " + pu.getRowIndex() + ", " + pu.getColIndex());
				server.addPU(pu, k);
			}
		}

		/* initialize a client */
		client = new Client(0, 0, map, noc);

		/* initialize query locations */
		noq = bootParams.getNumberOfQueries();

		/* initialize ic */
		IC = null;

		/* whether to plot ic vs q */
		icq = false;
		
		/* whether to plot google map */
		gMap = true;

		/* initialize qlist & rlist */
		qlist = new ArrayList<Integer>();

		rlist = (ArrayList<Double>[]) new ArrayList[noc];
		for (int i = 0; i < rlist.length; i++) {
			rlist[i] = new ArrayList<Double>();
		}

		/* initialize email content */
		content = new StringBuilder(bootParams.paramsToString());
	}

	public void singleSimulation() {
		System.out.println("Start querying...");

		/* run simulation for once */
		for (int i = 0; i < noq; i++) {
			client.randomLocation();
			/**
			 * Debug: See client's new location
			 */
			System.out.println("Query location: " + client.getRowIndex() + ", " + client.getColIndex());
			client.query(server);
		}

		/* compute IC */
		IC = client.computeIC();
	}

	public void multipleSimulation() {
		System.out.println("Start computing average IC...");
		if (noq < 100) {
			icq = false;
			return;
		}
		icq = true;
		Client mclient = new Client(0, 0, map, noc);
		double[] mIC = new double[noc];
		int gap = noq / interval;
		// compute number of integer
		int base = 1;
		int tmp = gap;
		while(tmp / base > 0) {
			base *= 10;
		}
		gap = (gap / (base / 10)) * (base / 10);
		qlist.add(0);
		for (int i = 1; i <= interval + 1; i++) qlist.add(gap * i);
		int repeat = 10;
		/* start query, each is done for 'repeat' times and compute average */
		for (int q : qlist) {
			System.out.println("Number of queries: " + q);
			double[] sumIC = new double[noc];
			// make queries for certain times
			for (int i = 0; i < repeat; i++) {
				mclient.reset();
				server.reset();
				for (int j = 0; j < q; j++) {
					mclient.randomLocation();
					mclient.query(server);
				}
				mIC = mclient.computeIC();
				int k = 0;
				for (double ic : mIC) {
					sumIC[k] += ic;
					k++;
				}
			}
			// compute average
			int cid = 0;
			for (double ic : sumIC) {
				rlist[cid].add(ic / repeat);
				cid++;
			}
		}
	}

	public void printSingle() {
		System.out.println("Start printing probability...");
		client.printProbability(directory);

		System.out.println("Start printing number of rows and cols...");
		client.printRC(directory);

		System.out.println("Start printing boundaries...");
		client.printBounds(directory);
		
		System.out.println("Start printing location of primary users...");
		server.printPUAllChannel(directory);
	}

	public void printMultiple() {
		System.out.println("Start printing average IC...");
		File file = new File(directory + "averageIC_NoCountermeasure.txt");
		try {
			PrintWriter out = new PrintWriter(file);
			for (Integer q : qlist) {
				out.print(q + " ");
			}
			out.println();	
			for (List<Double> listOnChannel : rlist) {
				for (double d : listOnChannel) {
					out.print((int) d + " ");
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
		sb.append(client.countChannelUpdateToString()); // channel is updated how many times
		sb.append("</p>");
		if (IC != null) {
			sb.append("<p>Inaccuracy_for_each_channel:<br>");
			for (int i = 0; i < IC.length; i++) {
				sb.append("Channel_" + i + ":_" + IC[i] + "<br>");
			}
		}
		sb.append("</p>");
		sb.append("<p>See_probability_plots_in_the_attachments._Location_of_primary_users_are_marked_as_red_stars.</p>");
		return sb.toString();
	}

	public String getEmailContent() {
		return content.append(buildMessage()).toString();
	}

	public void plot() {
    	System.out.println("Plotting probability distribution on Google Map...");
		if (!MatPlot.plot(noc)) {
			System.out.println("Plotting failed");
		}
		if (icq) { // this is set to true if noq is greater than 100
			System.out.println("Plotting average inacurracy...");
			if (!CmpPlot.plot("NOCOUNTERMEASURE")) {
				System.out.println("Plotting failed");
			}
		}
	}
	
	public void sendEmail() {
		System.out.println("Sending email...");
		// call send email api
		if (!SendEmail.send("ningli@vt.edu", bootParams.getEmail(), getEmailContent(), noc, icq, gMap)) {
			System.out.print("Email failed!");
		}
	}
}