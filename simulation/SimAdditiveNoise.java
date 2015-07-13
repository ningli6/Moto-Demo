package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javaPlot.CmpPlot;
import javaPlot.MatPlot;
import server.ServerAdditiveNoise;
import utility.PU;
import boot.BootParams;
import boot.LatLng;
import client.Client;

public class SimAdditiveNoise extends Simulation {
	private String counterMeasure;
	private double noiseLevel;
	private ServerAdditiveNoise cmServer;
	private int maxIteration;
	private List<Double>[] cmList;  // ic for multiple simulation with countermeasure
	private int repeat = 10;        // repeat for each number of query
	private boolean feasible;

	@SuppressWarnings("unchecked")
	public SimAdditiveNoise(BootParams bp, double cs, double scale, int inter, String dir) {
		/* call parent instructor */
		super(bp, cs, scale, inter, dir);

		/* initialize countermeasure */
		this.counterMeasure = bootParams.getCountermeasure();

		/* initialize noise level */
		this.noiseLevel = bootParams.getCMParam();

		/* initialize server with additive noise */
		cmServer = new ServerAdditiveNoise(map, noc, noq, noiseLevel);
		int PUid = 0;
		for (int k = 0; k < noc; k++) {
			List<LatLng> LatLngList = bootParams.getPUOnChannel(k);
			for (LatLng ll : LatLngList) {
				PU pu = new PU(PUid++, ll.getLat(), ll.getLng(), map);
				cmServer.addPU(pu, k);
			}
		}

		/* initialize max iter */
		maxIteration = 20;

		/* initialize cm list */
		cmList = (ArrayList<Double>[]) new ArrayList[noc];
		for (int i = 0; i < cmList.length; i++) {
			cmList[i] = new ArrayList<Double>();
		}

		/* initialize feasibility */
		feasible = false;
	}

	@Override
	public void singleSimulation() {
		if (noiseLevel < 0 || noiseLevel > 1) {
			System.out.println("Noise level must be in range 0 to 1.");
			feasible = false;   // do not print & multiple simulations
			gMap = false;       // do not include google map in the email
			icq = false;        // do not include ic vs q
			return;
		}
		System.out.println("Start querying...");

		/* run simulation for once */
		while(maxIteration > 0) {
			for (int i = 0; i < noq; i++) {
				client.randomLocation();
				client.query(cmServer);
			}
			if (cmServer.reachNoiseLevel()) {
				System.out.println("Noise level satisfied!");
				break;
			}
			// clear client's probability map to 0.5
			client.reset();
			// set actual lies back to 0
			cmServer.reset();
			maxIteration--;
		}
		/* if can't reach noise requirement within 20 attempts, return */
		if (maxIteration == 0) {
			gMap = false;       // do not include google map in the email
			feasible = false;   // do not print & multiple simulations
			System.out.println("Noise level is set too high. Requirement can't be reached within 20 attempts.");
			return;
		}
		feasible = true;
		gMap = true;
		IC = client.computeIC(server);
	}

	@Override
	public void multipleSimulation() {
		super.multipleSimulation();
		if (!icq) return; // number of query is less than 100 times
		/**
		 * use a new client for multiple simulation, 
		 * the old one should be saved for printing probability
		 * however we can reuse this client since we don't need its infer result 
		 */
		Client mclient = new Client(0, 0, map, noc); 
		System.out.println("Start computing average IC with additive noise...");
		/* start query, each is done for 'repeat' times then compute average */
		for (int q : qlist) {
			System.out.println("Number of queries: " + q);
			double[] sumIC = new double[noc];
			// update expected lies according to new query
			cmServer.updateLiesNeeded(q);
			// make queries for certain times
			int count = 40;
			for (int i = 0; i < repeat; i++) {
				count--;
				if (count == 0) {
					System.out.println("Noise requirement can't be satisfied within 40 attempts");
					feasible = false; // do not print
					gMap = false;     // do not include google map in the email
					icq = false;      // do not include ic vs q
					return;
				}
				// reset infermap to 0.5
				mclient.reset();
				// reset actual lies to 0
				cmServer.reset();
				for (int j = 0; j < q; j++) {
					mclient.randomLocation();
					mclient.query(cmServer);
				}
				if (!cmServer.reachNoiseLevel()) {
					System.out.println("Noise condition is not satisfied, try again");
					i--;
					continue;
				}
				double[] mIC = mclient.computeIC(server);
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
	public void printSingle() {
		if (!feasible) return;
		super.printSingle();
	}

	@Override
	public void printMultiple() {
		if (!feasible) return;
		super.printMultiple();
		File file = new File(directory + "cmp_AdditiveNoise.txt");
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
			if (noiseLevel > 1 || noiseLevel < 0) {
				sb.append("<p>Noise_level_should_be_in_range_from_0_to_1.</p>");
			}
			else sb.append("<p>Noise_requirement_can't_be_satisfied_because_noise_level_is_set_too_high.</p>");
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
	public void plot() {
    	System.out.println("Plotting probability distribution on Google Map...");
		if (!MatPlot.plot(noc, map.getRows(), map.getCols(), bootParams.getNorthLat(), bootParams.getSouthLat(), bootParams.getWestLng(), bootParams.getEastLng())) {
			System.out.println("Plotting failed");
		}
		if (icq) { // this is set to true if noq is greater than 100
			System.out.println("Plotting average inacurracy...");
			if (!CmpPlot.plot("ADDITIVENOISE")) {
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