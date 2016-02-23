package javaPlot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import utility.GridMap;
import utility.Location;

public class MatPlot {
	/**
	 * Call Matlab function that plot data on google map
	 * @param dataDir data dir
	 * @param plotDir plot dir
	 * @param cellSize grid size
	 * @param noc      number of channels
	 * @param nlat     north lat
	 * @param slat     south lat
	 * @param wlng     west lng
	 * @param elng     east lng
	 * @param randomQuery  plot result of random query
	 * @param smartQuery   plot result of smart query
	 * @param noCM     plot google map for no countermeasure
	 * @param ad       plot additive noise
	 * @param tf       plot transfiguration
	 * @param ka       plot k anonymity
	 * @param kc       plot k clustering
	 * @return
	 */
	public static boolean plot(String dataDir, String plotDir, double cellSize, int noc, double nlat, 
			double slat, double wlng, double elng, 
			boolean randomQuery, boolean smartQuery, 
			boolean noCM, boolean ad, boolean tf, boolean ka, boolean kc) {
		if (!randomQuery && !smartQuery) {
			System.out.println("Must specify method of queries");
			return true;
		}
		if (!noCM && !ad && !tf && !ka && !kc) {
			System.out.println("No google map need to be plotted");
			return true;
		}
		System.out.println("Start plotting Google Maps...");
		/**
		 * Construct a map instance to get number of rows and cols
		 */
		Location nwLoc = new Location(nlat, wlng);
		Location seLoc = new Location(slat, elng);
		GridMap map = new GridMap(nwLoc, seLoc, cellSize);
		try {
	        // using the Runtime exec method:
	        String cmd = "java -cp \"C:\\Users\\Administrator\\Desktop\\plotMap\";\"C:\\Program Files\\MATLAB\\MATLAB Compiler Runtime\\v83\\toolbox\\javabuilder\\jar\\win64\\javabuilder.jar\";\"C:\\Users\\Administrator\\Desktop\\plotMap\\MatPlot.jar\" getmagic " 
	        							+ dataDir + " " + plotDir + " "
	        							+ Integer.toString(noc) + " " + Integer.toString(map.getNumOfRows()) + " " + Integer.toString(map.getNumOfCols()) + " "
	        							+ Double.toString(map.getNorthLat()) + " " + Double.toString(map.getSouthLat()) + " " + Double.toString(map.getWestLng()) + " " + Double.toString(map.getEastLng());
	        if (randomQuery) {
		        if (noCM) {
		        	cmd += " No_Countermeasure";
		        }
		        if (ad) {
		        	cmd += " Additive_Noise";
		        }
		        if (tf) {
		        	cmd += " Transfiguration";
		        }
		        if (ka) {
		        	cmd += " K_Anonymity";
		        }
		        if (kc) {
		        	cmd += " K_Clustering";
		        }
	        }
	        if (smartQuery) {
		        if (noCM) {
		        	cmd += " smart_No_Countermeasure";
		        }
		        if (ad) {
		        	cmd += " smart_Additive_Noise";
		        }
		        if (tf) {
		        	cmd += " smart_Transfiguration";
		        }
		        if (ka) {
		        	cmd += " smart_K_Anonymity";
		        }
		        if (kc) {
		        	cmd += " smart_K_Clustering";
		        }
	        }

	        System.out.println(cmd);
	        
	        Process p = Runtime.getRuntime().exec(cmd);
	        /* uncomment these code to see matlab output */
			BufferedReader stdInput = new BufferedReader(new
						InputStreamReader(p.getInputStream()));
	
			BufferedReader stdError = new BufferedReader(new
					InputStreamReader(p.getErrorStream()));

			String s;
			// read the output from the command
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
	        int r = p.waitFor(); // if you don't read information from the input stream, it will overflow and hang
	        return r == 0;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}