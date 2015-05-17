package tests;

import java.util.Scanner;
import java.io.File;
import java.util.Random;
import java.util.List;
import java.util.LinkedList;

import client.Client;
import server.*;
import utility.*;

/*
 * Test class for location preservation without countermeasure.
 */
public class Main {
	public static void main(String[] args) {
		// for (int n = 0; n < args.length; n++) {
		// 	System.out.println(args[n]);
		// }
		/* usage
		 * Java Main -a lat lon lat lon -c number_of_channels (-C (lat lon)*){number_of_channels} -q number_of_queries
		 */
		GridMap map;
		// cell size
		double cellDegree = 0.05;
		// multiple times for MTP
		double mult = 5;
		List<PU>[] init;
		int Number_Of_Channels;
		int Number_of_Queries;
		System.out.println("Entry");
		try {
			if (!args[0].equals("-a")) {
				System.out.println(args[0]);
				throw new IllegalArgumentException("-a");
			}
			System.out.println("Initialize map...");
			double upperleftlat = Double.parseDouble(args[1]);
			double upperleftlng = Double.parseDouble(args[2]);
			double lowerrightlat = Double.parseDouble(args[3]);
			double lowerrightlng = Double.parseDouble(args[4]);
			double ulLat = upperleftlat;
			double ulLon = upperleftlng;
			double urLat = upperleftlat;
			double urLon = lowerrightlng;
			double llLat = lowerrightlat;
			double llLon = upperleftlng;
			double lrLat = lowerrightlat;
			double lrLon = lowerrightlng;
			Location upperLeft = new Location(ulLat, ulLon);
			Location upperRight = new Location(urLat, urLon);
			Location lowerLeft = new Location(llLat, llLon);
			Location lowerRight = new Location(lrLat, lrLon);
			map = new GridMap(upperLeft, upperRight, lowerLeft, lowerRight, cellDegree);
			System.out.println("Initialize number of channels...");
			if (!args[5].equals("-c")) {
				System.out.println(args[5]);
				throw new IllegalArgumentException("-c");
			}
			Number_Of_Channels = Integer.parseInt(args[6]);
			System.out.println("Initialize location of primary users...");
			if (!args[7].equals("-C") && Number_Of_Channels > 0) throw new IllegalArgumentException("Must specify location of PU");
			if (!args[7].equals("-C")) throw new IllegalArgumentException("-C");
			int count = 0;
			int i = 7;
			while(i < args.length) {
				if (args[i].equals("-C")) count++;
				i++;
			}
			if (count != Number_Of_Channels) throw new IllegalArgumentException("Number of channels don't match");
			init = new List[count];
			for (int j = 0; j < init.length; j++) {
				init[j] = new LinkedList<PU>();
			}
			i = 7;
			int putchannel = -1;
			int PUnumber = 0;
			while(i < args.length) {
				if (args[i].equals("-C")) {
					putchannel++;
					init[putchannel].add(new PU(PUnumber++, Double.parseDouble(args[++i]), Double.parseDouble(args[++i]), map));
					i++;
				}
				else if (!args[i].equals("-C") && !args[i].equals("-q")) {
					init[putchannel].add(new PU(PUnumber++, Double.parseDouble(args[i]), Double.parseDouble(args[++i]), map));
					i++;
				}
				else break;
			}
			System.out.println("Initialize number of queries...");
			if (i == args.length - 1) throw new IllegalArgumentException("Must speficify number of queries");
			Number_of_Queries = Integer.parseInt(args[i + 1]);
		}
		catch (Exception e) {
			System.out.println("Exception thrown:" + e);
			System.out.println("Usage: ");
			System.out.println("java Main -a lat lon lat lon -c number_of_channels (-C (lat lon)*){number_of_channels} -q number_of_queries");
			return;
		}

		// // number of PUs/Channels
		// int Number_Of_Channels = 1;
		// // query times
		// int Number_of_Queries = 50;

		// System.out.println("Cell size in degree: ");
		// cellDegree = sc.nextDouble();
		// System.out.println("Multiple times on default MTP function: ");
		// mult = sc.nextDouble();
		// try {
		// 	Number_Of_Channels = Integer.parseInt(args[0]);
		// 	Number_of_Queries = Integer.parseInt(args[1]);
		// 	System.out.println("Number of channels: " + Number_Of_Channels);
		// 	System.out.println("Number of queries: " + Number_of_Queries);

		// } catch (Exception e) {
		// 	 System.err.println("Caught Exception: " + e.getMessage());
		// 	 return;
		// }

		// double ulLat = 38;
		// double ulLon = -82;
		// double urLat = 38;
		// double urLon = -79;
		// double llLat = 36;
		// double llLon = -82;
		// double lrLat = 36;
		// double lrLon = -79;
		/*****************************/
		// Location upperLeft = new Location(ulLat, ulLon);
		// Location upperRight = new Location(urLat, urLon);
		// Location lowerLeft = new Location(llLat, llLon);
		// Location lowerRight = new Location(lrLat, lrLon);

		// initialization
		// GridMap map = new GridMap(upperLeft, upperRight, lowerLeft, lowerRight, cellDegree);
		MTP.ChangeMult(mult);
		Server.Number_Of_Channels = Number_Of_Channels;
		Client.Number_Of_Channels = Number_Of_Channels;
		// InferMap.directory = directory;
		/* debug information */
		System.out.println("Map length: " + map.getLength() + " km, Map height: " + map.getHeight() + " km");
		System.out.println("map rows: " + map.getRows() + ", map cols: " + map.getCols());
		map.showBoundary();
		// System.out.println("Average distance between each cell is: " + map.getAverageDistance() + " km");

	    // initialize a server with parameters from initial settings
		Server server = new Server(map);

		/* 
		 * Add a PU to the server's grid map, speficify the PU's location
		 * The location of PU is: 47°30'00.0"N, 97°30'00.0"W
		 */
		for (int k = 0; k < Number_Of_Channels; k++)
			for (PU pu : init[k]) 
				server.addPU(pu, k);

		// PU pu0 = new PU(0, 10, 10);
		// server.addPU(pu0, 0);

		// PU pu1 = new PU(1, 10, 50);
		// server.addPU(pu1, 1);

		// PU pu2 = new PU(2, 30, 10);
		// server.addPU(pu2, 0);

		// PU pu3 = new PU(3, 30, 50);
		// server.addPU(pu3, 1);

		/* debug information */
		System.out.println("Number of PUs: " + server.getNumberOfPUs());
		// server.printInfoPU();
		server.printInfoChannel();
		System.out.println();

		// initiliza a client, then change its location and make a query for N times;
		Client client = new Client(10, 49, map);
		Random randr = new Random();
		Random randc = new Random();
		// for (int i = 0; i < Number_of_Queries; i++) client.query(server);
		for (int i = 0; i < Number_of_Queries; i++) {
			// int newR = randr.nextInt(map.getRows());
			// int newC = randc.nextInt(map.getCols());
			int newR = randr.nextInt(40);
			int newC = randc.nextInt(60);
			client.setLocation(newR, newC);
			// client.randomLocation();
			client.query(server);
		}
		/* debug information */
		client.updateWhich();
		server.printInfoPU();
		// System.out.println("PU's location index: [" + rowPUIndex + "], [" + colPUIndex + "]");
		/*** these functions should be update! ***/
		for (int i = 0; i < Number_Of_Channels; i++) {
			client.plotInferMap(i);
			// client.printFormattedMatrix(i);
			// client.printFormattedTable(i);
		}

		double[] IC = client.computeIC(server);
		for (int i = 0; i < IC.length; i++) {
			System.out.println("Channel " + i + " : " + IC[i]);
		}
	}
}