package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javaPlot.CmpPlot;
import javaPlot.MatPlot;
import server.Server;
import server.ServerTransfiguration;
import utility.PU;
import boot.BootParams;
import boot.LatLng;
import client.Client;



public class SimTransfiguration extends Simulation {
	private String counterMeasure;
	private int sides;
	private ServerTransfiguration cmServer;
	private List<Double>[] cmList;  // ic for multiple simulation with countermeasure
	private int repeat = 10;        // repeat for each number of query
	private boolean feasible;       // if number of sides is valid
	
	/**
	 * Construct a transfiguration simulator
	 * @param bp        boot parameters
	 * @param cs        cell size
	 * @param scale     mtp scale
	 * @param inter     internal number of queries for multiple simulations
	 * @param dir       directory
	 */
	@SuppressWarnings("unchecked")
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
			List<LatLng> LatLngList = bootParams.getPUOnChannel(k);
			for (LatLng ll : LatLngList) {
				PU pu = new PU(PUid++, ll.getLat(), ll.getLng(), map);
				cmServer.addPU(pu, k);
			}
		}
		// after adding pu, wrap pu as polygon 
		if (sides > 2) cmServer.transfigure();

		/* initialize cm list */
		cmList = (ArrayList<Double>[]) new ArrayList[noc];
		for (int i = 0; i < cmList.length; i++) {
			cmList[i] = new ArrayList<Double>();
		}

		/* initialize feasible */
		feasible = (sides > 2) ? true : false;
		if (!feasible) {
			icq = false;   // don't include ic vs q in the email
			gMap = false;  // don't include google map in the email
		}
	}

	@Override
	public void singleSimulation() {
		if (!feasible) return;
		System.out.println("Start querying...");

		/* run simulation for once */
		for (int i = 0; i < noq; i++) {
			client.randomLocation();
			client.query(cmServer);
		}

		/* compute IC */
		IC = client.computeIC();
	}

	@Override
	public void multipleSimulation() {
		if (!feasible) return;
		super.multipleSimulation();
		if (!icq) return; // number of query is less than 100 times
		/**
		 * use a new client for multiple simulation, 
		 * the old one should be saved for printing probability
		 * however we can reuse this client since we don't need its infer result 
		 */
		Client mclient = new Client(0, 0, map, noc); 
		System.out.println("Start computing average IC with transfiguration...");
		/* start query, each is done for 'repeat' times then compute average */
		for (int q : qlist) {
			System.out.println("Number of queries: " + q);
			double[] sumIC = new double[noc];
			// make queries for certain times
			for (int i = 0; i < repeat; i++) {
				mclient.reset();                  // reset infermap to 0.5
				cmServer.reset();
				for (int j = 0; j < q; j++) {
					mclient.randomLocation();
					mclient.query(cmServer);
				}
				double[] mIC = mclient.computeIC();
				int k = 0;
				for (double ic : mIC) {
					sumIC[k] += ic;
					k++;
				}
			}
			// compute average
			int cid = 0;
			for (double ic : sumIC) {
				cmList[cid].add(ic / repeat);
				cid++;
			}
		}
	}

	@Override
	public void printSingle(Server server, Client client, String dir) {
		if (!feasible) return;
		super.printSingle(server, client, dir);
	}

	@Override
	public void printMultiple(List<Integer> qlist, Map<Integer, double[]> icMap, String dir, String fileName) {
		if (!feasible) return;
		super.printMultiple(qlist, icMap, dir, fileName);
		File file = new File(directory + "cmp_Transfiguration.txt");
		try {
			PrintWriter out = new PrintWriter(file);
			for (Integer q : qlist) {
				out.print(q + " ");
			}
			out.println();	
			for (List<Double> listOnChannel : cmList) {
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
		if (!feasible) {
			sb.append("<p>Number_of_sides_for_polygon_must_be_greater_than_2.</p>");
			return sb.toString();
		}
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
	
	@Override
	public String getEmailContent() {
		return content.append(buildMessage()).toString();
	}

	@Override
	public void plot(boolean plotGoogleMap, boolean iCvsQ, String countermeasure) {
		if (!feasible) return;
    	System.out.println("Plotting probability distribution on Google Map...");
		if (!MatPlot.plot(noc, map.getRows(), map.getCols(), bootParams.getNorthLat(), bootParams.getSouthLat(), bootParams.getWestLng(), bootParams.getEastLng())) {
			System.out.println("Plotting failed");
		}
		if (icq) { // this is set to true if noq is greater than 100
			System.out.println("Plotting average inacurracy...");
			if (!CmpPlot.plot("TRANSFIGURATION")) {
				System.out.println("Plotting failed");
			}
		}
	}

	public boolean isFeasible() {
		return feasible;
	}

	public String getCountermeasure() {
		return counterMeasure;
	}
}