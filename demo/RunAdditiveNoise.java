package demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import server.*;
import utility.GridMap;
import utility.InferMap;
import utility.JavaRunCommand;
import utility.Location;
import utility.MTP;
import utility.PU;
import boot.BootParams;
import boot.LatLng;
import client.Client;

public class RunAdditiveNoise {
	private BootParams bootParams;
	// cell size
	public static double cellsize = 0.05; // in degree
	// multiple times for MTP
	public static double multitimes = 5;
	public static int points = 5;

	public RunAdditiveNoise(BootParams bp) {
		this.bootParams = bp;
	}

	public void run() {
		System.out.println("Simulation start wtih addtive noise!");
		try {
			StringBuilder message = new StringBuilder();
			message.append(bootParams.paramsToString());
			
			/* initialize number of channels */
			int Number_Of_Channels = bootParams.getNumberOfChannels();

			/* initialize map */
			Location upperLeft = new Location(bootParams.getNorthLat(), bootParams.getWestLng());
			Location upperRight = new Location(bootParams.getNorthLat(), bootParams.getEastLng());
			Location lowerLeft = new Location(bootParams.getSouthLat(), bootParams.getWestLng());
			Location lowerRight = new Location(bootParams.getSouthLat(), bootParams.getEastLng());
			GridMap map = new GridMap(upperLeft, upperRight, lowerLeft, lowerRight, cellsize);

			message.append("<h3>Simlation_result</h3>");

			/* change MTP scale */
			MTP.ChangeMult(multitimes);
			
			/* Assume that we use random location for queries now */
			int Number_of_Queries = bootParams.getNumberOfQueries();
			if (Number_of_Queries == -1) {
				// read from file
				Number_of_Queries = 500;
			}

			/* initialize server */
			double noise_level = bootParams.getCMParam();
			if (noise_level < 0 || noise_level > 1) {
				System.out.println("Noise level invalid");
				message.append("<p>Noise_level_must_be_in_range_0_and_1.</p>");
				if (!JavaRunCommand.sendEmail(bootParams.getEmail(), message.toString(), 0, false)) {
					throw new Exception("Unable to send email");
				}
				System.out.println("Sending email successfully!");
				return;
			}
			
			/* server without countermeasure as comparison */
			Server baseServer = new Server(map);
			/* Specify number of channels for server */
			baseServer.setNumberOfChannels(Number_Of_Channels);
			
			ServerAdditiveNoise server = new ServerAdditiveNoise(map, noise_level);
			/* Specify number of channels for server */
			server.setNumberOfChannels(Number_Of_Channels);
			server.setNoise(Number_of_Queries);
			
			/* Add a PU to the server's grid map */
			int PUid = 0;
			for (int k = 0; k < Number_Of_Channels; k++) {
				List<LatLng> LatLngList = bootParams.getPUOnChannel(k);
				for (LatLng ll : LatLngList) {
					PU pu = new PU(PUid++, ll.getLat(), ll.getLng(), map);
					// System.out.println("k: " + k);
					server.addPU(pu, k);
					baseServer.addPU(pu, k);
				}
			}

			message.append("<p>Distribution_of_primary_users:<br>");
			message.append("Number_of_PUs:_" + server.getNumberOfPUs() + "<br>");
			/* debug information */
			// System.out.println("Number of PUs: " + server.getNumberOfPUs());
			message.append(server.infoChannelToString());
			/* debug information */
			// server.printInfoChannel();
			message.append("</p>");
			/* debug information */
			// System.out.println();

			// initialize a client
			Client client = new Client(0, 0, map);
			/* Specify number of channels for client */
			client.setNumberOfChannels(Number_Of_Channels);

			System.out.println("Start querying util noise level satisfied...");
			int maxIteration = 20;
			while(maxIteration > 0) {
				for (int i = 0; i < Number_of_Queries; i++) {
					client.randomLocation();
					client.query(server);
				}
				// if noise level satisfied
//				System.out.println("actual lies: " + server.getNumberOfLies());
//				System.out.println("expected lies" + server.getExpectedLies());
				if (server.reachNoiseLevel()) {
					System.out.println("Noise level satisfied!");
					break;
				}
				// clear client's probability map to 0.5
				client.reset();
				// set actual lies back to 0
				server.reset();
				maxIteration--;
			}
			
			if (maxIteration == 0) {
				System.out.println("Noise level is set too high. Requirement can't be reached.");
				message.append("<p>Sorry!_Noise_level_is_set_too_high._Requirement_can't_be_reached.</p>");
				if (!JavaRunCommand.sendEmail(bootParams.getEmail(), message.toString(), Number_Of_Channels, false)) {
					throw new Exception("Unable to send email");
				}
				System.out.println("Sending email successfully!");
				return;
			}

			message.append("<p>Querying_information:<br>");
			message.append(client.updateWhichToString());
			message.append("</p>");
			message.append("<p>Countermeasure:<br>");
			message.append("Additive_noise<br>");
			message.append("Noise_level:_" + Double.toString(noise_level));
			message.append("</p>");

			InferMap.directory = "C:\\Users\\Administrator\\Desktop\\motoData\\";
			Server.directory = "C:\\Users\\Administrator\\Desktop\\motoData\\";
			
			/* PRINT pus' location
			   print location of pu of different channel in seperate file */
			server.printPUAllChannel();

			/* PRINT data, bounds, rows/cols
			   print ic matrix, bounds, rows/cols of different channel in seperate file */
			for (int i = 0; i < Number_Of_Channels; i++) {
				/* comment this out to run online */
				// client.plotInferMap(i);
				client.printFormattedTable(i);
			}

			/* compute IC */
			double[] IC = client.computeIC(server);
			message.append("<p>Inaccuracy_for_each_channel:<br>");
			for (int i = 0; i < IC.length; i++) {
				message.append("Channel_" + i + ":_" + IC[i] + "<br>");
			}
			message.append("</p>");
			message.append("<p>See_probability_plots_in_the_attachments._Location_of_primary_users_are_presented_as_red_markers.</p>");

			boolean ICvsQ = false;
			/* Compute IC vs Query at certain points and plot */
			if (Number_of_Queries >= 100) {
				ICvsQ = true;
				String dict = "C:\\Users\\Administrator\\Desktop\\motoData\\";
				File file = new File(dict + "averageIC_AdditiveNoise.txt");
				File cmp = new File(dict + "cmp_AdditiveNoise.txt");
				System.out.println("Start to compute average IC...");
				int gap = Number_of_Queries / points;
				// compute number of integer
				int base = 1;
				int tmp = gap;
				while(tmp / base > 0) {
					base *= 10;
				}
				gap = (gap / (base / 10)) * (base / 10);
				List<Integer> qlist = new ArrayList<Integer>();

				qlist.add(0);
				for (int i = 1; i <= points + 1; i++) qlist.add(gap * i);
				try {
					@SuppressWarnings("unchecked")
					List<Double>[] rlist = (ArrayList<Double>[]) new ArrayList[Number_Of_Channels];
					for (int i = 0; i < rlist.length; i++) {
						rlist[i] = new ArrayList<Double>();
					}
					PrintWriter out = new PrintWriter(file);
					for (Integer q : qlist) {
						out.print(q + " ");
					}
					out.println();					
					int repeat = 10;
					/* start query, each is done for 'repeat' times and compute average */
					for (int q : qlist) {
						System.out.println("Number of queries: " + q);
						double[] sumIC = new double[Number_Of_Channels];
						server.setNoise(q);
						// make queries for certain times
						for (int i = 0; i < repeat; i++) {
							client.reset();
							server.reset();
							for (int j = 0; j < q; j++) {
								client.randomLocation();
								client.query(server);
							}
							/* debug */
//							System.out.println("actual lies: " + server.getNumberOfLies());
//							System.out.println("expected lies" + server.getExpectedLies());
							if (!server.reachNoiseLevel()) {
								System.out.println("Noise condition is not satisfied, try again");
								i--;
								continue;
							}
							IC = client.computeIC(server);
							int k = 0;
							for (double ic : IC) {
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
					System.out.println("Printing average IC for each query...");
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
				finally {
					System.out.println("Printing ends");
				}
				try {
					@SuppressWarnings("unchecked")
					List<Double>[] rlist = (ArrayList<Double>[]) new ArrayList[Number_Of_Channels];
					for (int i = 0; i < rlist.length; i++) {
						rlist[i] = new ArrayList<Double>();
					}
					PrintWriter out = new PrintWriter(cmp);
					for (Integer q : qlist) {
						out.print(q + " ");
					}
					out.println();					
					int repeat = 10;
					/* start query, each is done for 'repeat' times and compute average */
					for (int q : qlist) {
						System.out.println("Number of queries: " + q);
						double[] sumIC = new double[Number_Of_Channels];
						// make queries for certain times
						for (int i = 0; i < repeat; i++) {
							client.reset();
							baseServer.reset();
							for (int j = 0; j < q; j++) {
								client.randomLocation();
								client.query(baseServer);
							}
							IC = client.computeIC(baseServer);
							int k = 0;
							for (double ic : IC) {
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
					System.out.println("Printing comparison IC for each query...");
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
				finally {
					System.out.println("Printing ends");
				}
			}

			/* if everything works all right, generate plots and then send email */
			if (!JavaRunCommand.generatePlot(Number_Of_Channels, ICvsQ, "ADDITIVENOISE")) {
				throw new Exception("Unable to generate plots");
			}
			System.out.println("Generating plots successfully!");
			if (!JavaRunCommand.sendEmail(bootParams.getEmail(), message.toString(), Number_Of_Channels, ICvsQ)) {
				throw new Exception("Unable to send email");
			}
			System.out.println("Sending email successfully!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
	}
}
