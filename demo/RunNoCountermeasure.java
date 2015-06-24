package demo;

import boot.*;
import utility.*;
import client.*;
import server.*;

import java.util.List;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;

public class RunNoCountermeasure {
	private BootParams bootParams;
	// cell size
	public static double cellsize = 0.05; // in degree
	// multiple times for MTP
	public static double multitimes = 5;
	public static int points = 5;

	public RunNoCountermeasure(BootParams bp) {
		this.bootParams = bp;
	}

	public void run() {
		System.out.println("Simulation start!");
		try {
			/* debug information */
			// bootParams.printParams();
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
			// message.append("#Map_length:_" + map.getLength() + "_km,_Map_height:_" + map.getHeight() + "_km#");
			/* debug information */
			// System.out.println("map length: " + map.getLength() + " km, map height: " + map.getHeight() + " km");
			// System.out.println("map rows: " + map.getRows() + ", map cols: " + map.getCols());
			// map.showBoundary();

			/* change MTP scale */
			MTP.ChangeMult(multitimes);

			/* initialize server */
			Server server = new Server(map);
			/* Specify number of channels for server */
			server.setNumberOfChannels(Number_Of_Channels);

			/* Add a PU to the server's grid map */
			int PUid = 0;
			for (int k = 0; k < Number_Of_Channels; k++) {
				List<LatLng> LatLngList = bootParams.getPUOnChannel(k);
				for (LatLng ll : LatLngList) {
					PU pu = new PU(PUid++, ll.getLat(), ll.getLng(), map);
					// System.out.println("k: " + k);
					server.addPU(pu, k);
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

			// initiliza a client
			Client client = new Client(0, 0, map);

			/* Specify number of channels for client */
			client.setNumberOfChannels(Number_Of_Channels);

			/* Assume that we use random location for queries now */
			int Number_of_Queries = bootParams.getNumberOfQueries();
			if (Number_of_Queries == -1) {
				// read from file
				Number_of_Queries = 500;
			}
			System.out.println("Start querying...");
			for (int i = 0; i < Number_of_Queries; i++) {
				client.randomLocation();
				client.query(server);
			}

			/* debug information */
			// client.updateWhich();
			// server.printInfoPU();

			message.append("<p>Querying_information:<br>");
			message.append(client.updateWhichToString());
			message.append("</p>");
			/*** these functions should be update! ***/
			// InferMap.directory = "/var/www/html/Project/output/";
			// Server.directory = "/var/www/html/Project/output/";
			// InferMap.directory = "/Users/ningli/Desktop/Project/output/";
			// Server.directory = "/Users/ningli/Desktop/Project/output/";
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
				// System.out.println("Channel " + i + " : " + IC[i]);
			}
			message.append("</p>");
			message.append("<p>See_probability_plots_in_the_attachments._Location_of_primary_users_are_presented_as_red_markers.</p>");

			boolean ICvsQ = false;
			/* Compute IC vs Query at certain points and plot */
			if (Number_of_Queries >= 100) {
				ICvsQ = true;
				String dict = "C:\\Users\\Administrator\\Desktop\\motoData\\";
				File file = new File(dict + "averageIC_NoCountermeasure.txt");
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
				@SuppressWarnings("unchecked")
				List<Double>[] rlist = (ArrayList<Double>[]) new ArrayList[Number_Of_Channels];
				for (int i = 0; i < rlist.length; i++) {
					rlist[i] = new ArrayList<Double>();
				}
				qlist.add(0);
				for (int i = 1; i <= points + 1; i++) qlist.add(gap * i);
				try {
					PrintWriter out = new PrintWriter(file);
					System.out.println("Printing number of queries...");
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
							server.reset();
							for (int j = 0; j < q; j++) {
								client.randomLocation();
								client.query(server);
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
			} 

			/* if everything works all right, generate plots and then send email */
			/* 
			 * Update this function :
			 * boolean generatePlot(int number_of_channels, boolean ICvsQ) 
			 */
			if (!JavaRunCommand.generatePlot(Number_Of_Channels, ICvsQ)) {
				throw new Exception("Unable to generate plots");
			}
			System.out.println("Generating plots successfully!");
			/* 
			 * Update this function :
			 * boolean sendEmail(String addr, String message, int number_of_channels) 
			 */
			if (!JavaRunCommand.sendEmail(bootParams.getEmail(), message.toString(), Number_Of_Channels, ICvsQ)) {
				throw new Exception("Unable to send email");
			}
			System.out.println("Sending email successfully!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
			/* comment out to run offline */
			// JavaRunCommand.sendEmail(bootParams.getEmail(), err);
			return;
		}
	}
}