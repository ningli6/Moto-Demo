package boot;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Parser {
	public static Map<String, String> cmswitch;

	public static BootParams parse(String[] args) {
		if (args == null) return null;
		if (cmswitch == null) {
			cmswitch = new HashMap<String, String>();
			cmswitch.put("-an", "ADDITIVENOISE"); cmswitch.put("-tf", "TRANSFIGURATION");
			cmswitch.put("-ka", "KANONYMITY"); cmswitch.put("-kc", "KCLUSTERING");
		}
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
			System.out.println("NorthLat: " + args[1]);
			System.out.println("parse NorthLat: " + Double.parseDouble(args[1]));
			System.out.println("SouthLat: " + args[3]);
			System.out.println("parse SouthLat: " + Double.parseDouble(args[3]));
			System.out.println("WestLng: " + args[2]);
			System.out.println("parse WestLng: " + Double.parseDouble(args[2]));
			System.out.println("EastLng: " + args[4]);
			System.out.println("parse EastLng: " + Double.parseDouble(args[4]));
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
					if (args[i + 1].equals("-C")) {
						i++;
						continue;
					}
					init[putchannel].add(new LatLng(Double.parseDouble(args[++i]), Double.parseDouble(args[++i])));
					i++;
				}
				else if (!args[i].equals("-C") && !args[i].equals("-q") && !args[i].equals("-f") && !cmswitch.containsKey(args[i])) {
					init[putchannel].add(new LatLng(Double.parseDouble(args[i]), Double.parseDouble(args[++i])));
					i++;
				}
				else break;
			}
			bootParams.setPUonChannels(init);
			if (i == args.length) throw new IllegalArgumentException("Invalid ending");
			if (cmswitch.containsKey(args[i])) {
				bootParams.setCountermeasure(cmswitch.get(args[i++]));
				bootParams.setCMParam(Double.parseDouble(args[i++]));
			}
			if (i == args.length) throw new IllegalArgumentException("Invalid ending");
			if (args[i].equals("-f")) {
				bootParams.useFile(args[++i]);
			}
			else if (args[i].equals("-q")){
				bootParams.setNumberOfQueries(Integer.parseInt(args[++i]));
			}
			else {
				throw new IllegalArgumentException("Invalid ending");
			}
			if (i++ == args.length) throw new IllegalArgumentException("Invalid ending");
			if (args[i].equals("-e")) {
				bootParams.setEmail(args[++i]);
			}
			return bootParams;
		}
		catch (Exception e) {
			System.out.println("Exception thrown:" + e);
			System.out.println("Usage: ");
			System.out.println("java Boot -a NorthLat WestLng SouthLat EastLng -c number_of_channels (-C (lat lon)*){number_of_channels} -q number_of_queries");
			return null;
		}
	}

	public static void main(String[] args) {
		BootParams bp = Parser.parse(args);
		bp.printParams();
	}
}