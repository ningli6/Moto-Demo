package boot;

import java.util.LinkedList;
import java.util.List;

import utility.Location;

/**
 * Parser class that takes in input string from web interface,
 * then parse it and return a BootParams instance
 * @author Ning Li <ningli@vt.edu>
 */
public class Parser {
	/**
	 * Parse an input string from web interface
	 * The arguments from web interface should be formatted as:
	 * -cd cellSize -a NorthLat WestLng SouthLat EastLng -c noc (-C (lat lon)+){noc} -cm [(no -1) (an val) (tf val) (ka val) (kc val)]+ -gm no? ad? tf? ka? kc? -tr ad? tf? ka? kc? -q noq -sq noq -e email -opt pa?
	 * @param     string from web page
	 * @return    BootParams that holds all these information
	 */
	public static BootParams parse(String[] args) {
		if (args == null || args.length == 0) 
			return null;
		BootParams bootParams = new BootParams();
		try {
			int i = 0; // index
			// get cell size
			if (!args[i++].equals("-cd")) {
				throw new IllegalArgumentException("-cd");
			}
			bootParams.setCellSize(Double.parseDouble(args[i++]));
			// get boundary
			if (!args[i++].equals("-a")) {
				throw new IllegalArgumentException("-a");
			}
			bootParams.setNorthLat(Double.parseDouble(args[i++]));
			bootParams.setSouthLat(Double.parseDouble(args[i++]));
			bootParams.setWestLng(Double.parseDouble(args[i++]));
			bootParams.setEastLng(Double.parseDouble(args[i++]));
			// get number of channels
			if (!args[i++].equals("-c")) {
				throw new IllegalArgumentException("-c");
			}
			int noc = Integer.parseInt(args[i++]);
			if (noc < 1 || noc > 3) {
				throw new IllegalArgumentException("Number of channels must be 1, 2 or 3");
			}
			bootParams.setNumberOfChannels(noc);
			// specify location for all pu on each channel
			@SuppressWarnings("unchecked")
			List<Location>[] puLocations = (List<Location>[]) new List[noc];
			for (int j = 0; j < puLocations.length; j++) {
				puLocations[j] = new LinkedList<Location>();
			}
			int ch = 0;
			while(ch < noc) {
				if (args[i].equals("-C")) {
					i++;
					while (!args[i].equals("-C") && !args[i].equals("-cm")) {
						puLocations[ch].add(new Location(Double.parseDouble(args[i++]), Double.parseDouble(args[i++])));
					}
					ch++;
				}
			}
			for (int j = 0; j < puLocations.length; j++) {
				if (puLocations[j].size() == 0) {
					throw new IllegalArgumentException("Some channel is empty!");
				}
			}
			if (ch != noc || !args[i].equals("-cm")) {
				throw new IllegalArgumentException();
			}
			i += 1;
			bootParams.setPUonChannels(puLocations);
			// countermeasure
			if (!args[i].equals("no") && !args[i].equals("an") && !args[i].equals("tf") && !args[i].equals("ka") && !args[i].equals("kc")) {
				throw new IllegalArgumentException("Must specify countermeasure!");
			}
			if (args[i].equals("no")) {
				bootParams.putCountermeasure("NOCOUNTERMEASURE", Double.valueOf(args[i + 1]));
				if (bootParams.getCMParam("NOCOUNTERMEASURE") == 1) { // -1 for no smart query, 1 for smart query
					bootParams.setSmartQuery(true);
				}
				i += 2;
			}
			if (args[i].equals("an")) {
				bootParams.putCountermeasure("ADDITIVENOISE", Double.valueOf(args[i + 1]));
				i += 2;
			}
			if (args[i].equals("tf")) {
				bootParams.putCountermeasure("TRANSFIGURATION", Double.valueOf(args[i + 1]));
				i += 2;
			}
			if (args[i].equals("ka")) {
				bootParams.putCountermeasure("KANONYMITY", Double.valueOf(args[i + 1]));
				i += 2;
			}
			if (args[i].equals("kc")) {
				bootParams.putCountermeasure("KCLUSTERING", Double.valueOf(args[i + 1]));
				i += 2;
			}
			// google maps
			if (!args[i].equals("-gm")) {
				throw new IllegalArgumentException("Need to specify trade off information");
			}
			i += 1;
			if (args[i].equals("no")) {
				bootParams.setGoogleMapNO(true);
				i += 1;
			}
			if (args[i].equals("ad")) {
				bootParams.setGoogleMapAD(true);
				i += 1;
			}
			if (args[i].equals("tf")) {
				bootParams.setGoogleMapTF(true);
				i += 1;
			}
			if (args[i].equals("ka")) {
				bootParams.setGoogleMapKA(true);
				i += 1;
			}
			if (args[i].equals("kc")) {
				bootParams.setGoogleMapKC(true);
				i += 1;
			}
			// trade off curve
			if (!args[i].equals("-tr")) {
				throw new IllegalArgumentException("Need to specify trade off information");
			}
			i += 1;
			if (args[i].equals("ad")) {
				bootParams.setTradeOffAD(true);
				i += 1;
			}
			if (args[i].equals("tf")) {
				bootParams.setTradeOffTF(true);
				i += 1;
			}
			if (args[i].equals("ka")) {
				bootParams.setTradeOffKA(true);
				i += 1;
			}
			if (args[i].equals("kc")) {
				bootParams.setTradeOffKC(true);
				i += 1;
			}
			// queries
			if (!args[i].equals("-q")) {
				throw new IllegalArgumentException("-q");
			}
			i++;
			if (!args[i].equals("-sq")) {
				bootParams.setNumberOfQueries(Integer.parseInt(args[i]));
				bootParams.setRandomQuery(true);
				i++;
			}
			if (!args[i].equals("-sq")) {
				throw new IllegalArgumentException("-sq");
			}
			i++;
			if (!args[i].equals("-e")) {
				bootParams.setNumberOfQueries(Integer.parseInt(args[i]));
				bootParams.setSmartQuery(true);
				i++;
			}
			if (!args[i].equals("-e")) {
				throw new IllegalArgumentException("No email address!");
			}
			bootParams.setEmail(args[i + 1]);
			i += 2;
			if (!args[i].equals("-opt")) {
				throw new IllegalArgumentException("No email option!");
			}
			i += 1;
			if (i < args.length && args[i].equals("pa")) {
				bootParams.setInputParams(true);
				i += 1;
			}
			return bootParams;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Usage: ");
			System.out.println("java Boot -cd cellSize -a NorthLat WestLng SouthLat EastLng -c noc (-C (lat lon)+){noc} -cm [(-no -1) (-an -val) (-tf -val) (-ka -val) (-kc -val)]+ -gm no? ad? tf? ka? kc? -tr ad? tf? ka? kc? ((-q number_of_queries)|(-f filename)) -e email -opt pa?");
			return null;
		}
	}
}