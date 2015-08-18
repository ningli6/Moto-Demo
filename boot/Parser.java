package boot;

import java.util.LinkedList;
import java.util.List;

import utility.Location;

/**
 * Parser class that takes in input string from web interface,
 * then parse and wrap it as a BootParams instance
 * @author Ning Li
 */
public class Parser {
	/**
	 * Parse an input string from web interface
	 * The arguments from web interface should be formatted as:
	 * -cd cellSize -a NorthLat WestLng SouthLat EastLng -c noc (-C (lat lon)+){noc} -cm [(-no -1) (-an -val) (-tf -val) (-ka -val) (-kc -val)]+ -gm no? ad? tf? ka? kc? -tr ad? tf? ka? kc? ((-q number_of_queries)|(-f filename)) -e email -opt pa?
	 * @param     string from web page
	 * @return    BootParams that holds all these information
	 */
	public static BootParams parse(String[] args) {
		if (args == null || args.length == 0) 
			return null;
		BootParams bootParams = new BootParams();
		try {
			// get cell size
			if (!args[0].equals("-cd")) {
				throw new IllegalArgumentException("-cd");
			}
			bootParams.setCellSize(Double.parseDouble(args[1]));
			// get boundary
			if (!args[2].equals("-a")) {
				throw new IllegalArgumentException("-a");
			}
			bootParams.setNorthLat(Double.parseDouble(args[3]));
			bootParams.setSouthLat(Double.parseDouble(args[5]));
			bootParams.setWestLng(Double.parseDouble(args[4]));
			bootParams.setEastLng(Double.parseDouble(args[6]));
			// get number of channels
			if (!args[7].equals("-c")) {
				throw new IllegalArgumentException("-c");
			}
			int noc = Integer.parseInt(args[8]);
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
			int i = 9;
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
			if (ch != noc || !args[i].equals("-cm")) {
				throw new IllegalArgumentException();
			}
			i += 1;
			bootParams.setPUonChannels(puLocations);
			// countermeasure
			if (!args[i].equals("-no") && !args[i].equals("-an") && !args[i].equals("-tf") && !args[i].equals("-ka") && !args[i].equals("-kc")) {
				throw new IllegalArgumentException("Must specify countermeasure!");
			}
			if (args[i].equals("-no")) {
				bootParams.putCountermeasure("NOCOUNTERMEASURE", -1);
				i += 2;
			}
			if (args[i].equals("-an")) {
				bootParams.putCountermeasure("ADDITIVENOISE", Double.valueOf(args[i + 1]));
				i += 2;
			}
			if (args[i].equals("-tf")) {
				bootParams.putCountermeasure("TRANSFIGURATION", Double.valueOf(args[i + 1]));
				i += 2;
			}
			if (args[i].equals("-ka")) {
				bootParams.putCountermeasure("KANONYMITY", Double.valueOf(args[i + 1]));
				i += 2;
			}
			if (args[i].equals("-kc")) {
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
			if (args[i].equals("-f")) {
				bootParams.useFile(args[i + 1]);
				i += 2;
			}
			else if (args[i].equals("-q")){
				bootParams.setNumberOfQueries(Integer.parseInt(args[i + 1]));
				i += 2;
			}
			else {
				throw new IllegalArgumentException("No query information!");
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