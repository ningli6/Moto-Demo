package boot;

import java.util.List;
import java.util.LinkedList;

public class Parser {
	// private String params;
	// private BootParams bootParams;

	// public Parser(String s) {
	// 	this.params = s;
	// 	bootParams = parse(params);
	// }

	// public String getParams() {
	// 	return params;
	// }

	// public BootParams getBootParams() {
	// 	return bootParams;
	// }

	public static BootParams parse(String[] args) {
		if (args == null) return null;
		BootParams bootParams = new BootParams();
		try {
			if (!args[0].equals("-a")) {
				System.out.println(args[0]);
				throw new IllegalArgumentException("-a");
			}
			bootParams.setNorthLat(Double.parseDouble(args[1]));
			bootParams.setSouthLat(Double.parseDouble(args[3]));
			bootParams.setWestLng(Double.parseDouble(args[2]));
			bootParams.setEastLng(Double.parseDouble(args[4]));
			if (!args[5].equals("-c")) {
				System.out.println(args[5]);
				throw new IllegalArgumentException("-c");
			}
			bootParams.setNumberOfChannels(Integer.parseInt(args[6]));
			if (!args[7].equals("-C") && bootParams.getNumberOfChannels() > 0) throw new IllegalArgumentException("Must specify location of PU");
			if (!args[7].equals("-C")) throw new IllegalArgumentException("-C");
			int count = 0;
			int i = 7;
			while(i < args.length) {
				if (args[i].equals("-C")) count++;
				i++;
			}
			if (count != bootParams.getNumberOfChannels()) throw new IllegalArgumentException("Number of channels don't match");
			List<LatLng>[] init = (List<LatLng>[]) new List[count];
			for (int j = 0; j < init.length; j++) {
				init[j] = new LinkedList<LatLng>();
			}
			i = 7;
			int putchannel = -1;
			while(i < args.length) {
				if (args[i].equals("-C")) {
					putchannel++;
					init[putchannel].add(new LatLng(Double.parseDouble(args[++i]), Double.parseDouble(args[++i])));
					i++;
				}
				else if (!args[i].equals("-C") && !args[i].equals("-q")) {
					init[putchannel].add(new LatLng(Double.parseDouble(args[i]), Double.parseDouble(args[++i])));
					i++;
				}
				else break;
			}
			bootParams.setPUonChannels(init);
			if (i == args.length - 1) throw new IllegalArgumentException("Must speficify number of queries");
			bootParams.setNumberOfQueries(Integer.parseInt(args[i + 1]));
			return bootParams;
		}
		catch (Exception e) {
			System.out.println("Exception thrown:" + e);
			System.out.println("Usage: ");
			System.out.println("java Main -a lat lon lat lon -c number_of_channels (-C (lat lon)*){number_of_channels} -q number_of_queries");
			return null;
		}
	}
}