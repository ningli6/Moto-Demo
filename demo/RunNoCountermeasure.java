package demo;

import boot.*;
import utility.*;
import client.*;
import server.*;

import java.util.List;

public class RunNoCountermeasure {
	private BootParams bootParams;
	// cell size
	public static double cellsize = 0.05; // in degree
	// multiple times for MTP
	public static double multitimes = 5;

	public RunNoCountermeasure(BootParams bp) {
		this.bootParams = bp;
	}

	public void run() {
		try {
			bootParams.printParams();
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

			message.append("*****Program_output*****#");
			message.append("#Map_length:_" + map.getLength() + "_km,_Map_height:_" + map.getHeight() + "_km#");
			/* debug information */
			System.out.println("map length: " + map.getLength() + " km, map height: " + map.getHeight() + " km");
			System.out.println("map rows: " + map.getRows() + ", map cols: " + map.getCols());
			map.showBoundary();

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

			message.append("#PU_information:#");
			message.append("Number_of_PUs:_" + server.getNumberOfPUs() + "#");
			/* debug information */
			System.out.println("Number of PUs: " + server.getNumberOfPUs());
			message.append(server.infoChannelToString());
			server.printInfoChannel();
			message.append("#");
			System.out.println();

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
			for (int i = 0; i < Number_of_Queries; i++) {
				client.randomLocation();
				client.query(server);
			}

			/* debug information */
			client.updateWhich();
			server.printInfoPU();

			message.append("Querying_information:#");
			message.append(client.updateWhichToString());


			/*** these functions should be update! ***/
			// InferMap.directory = "/var/www/html/Project/output/";
			InferMap.directory = "/Users/ningli/Desktop/Project/output/";
			Server.directory = "/Users/ningli/Desktop/Project/output/";
			server.printPUAllChannel();
			for (int i = 0; i < Number_Of_Channels; i++) {
				client.plotInferMap(i);
				client.printFormattedTable(i);
			}

			/* compute IC */
			double[] IC = client.computeIC(server);
			message.append("IC_for_each_channel:#");
			for (int i = 0; i < IC.length; i++) {
				message.append("Channel_" + i + "_:_" + IC[i] + "#");
				System.out.println("Channel " + i + " : " + IC[i]);
			}

			/* if everything works all right, send email */
			/* comment out to run offline */
			// JavaRunCommand.sendEmail(bootParams.getEmail(), message.toString());
		} catch (Exception e) {
			String err = "Error! Program throws an exception!";
			/* comment out to run offline */
			// JavaRunCommand.sendEmail(bootParams.getEmail(), err);
		}
	}
}