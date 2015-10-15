package javaPlot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmpPlot {
	/**
	 * Call a python script that plot ic vs q
	 * @param dataDir TODO
	 * @param plotDir TODO
	 * @param noCM plot no countermeasure
	 * @param ad   plot additive noise
	 * @param tf   plot transfiguration
	 * @param ka   plot k anonymity
	 * @param kc   plot k clustering
	 * @return true if it works all right
	 */
	public static boolean plotRandom(String dataDir, String plotDir, boolean noCM, boolean ad, boolean tf, boolean ka, boolean kc) {
		System.out.println("Start plotting ic vs queries...");
		try {
//			String cmd = "python C:\\Users\\Administrator\\Desktop\\motoDemo\\python\\plotICvsQwChannels.py " + dataDir + " "+ plotDir;
			String cmd = "python C:\\Users\\Administrator\\Desktop\\motoDemo\\python\\plotICvsQ.py " + dataDir + " "+ plotDir;
			if (noCM) {
				cmd += " averageIC_NoCountermeasure.txt";
			}
			if (ad) {
				cmd += " cmp_AdditiveNoise.txt";
			}
			if (tf) {
				cmd += " cmp_Transfiguration.txt";
			}
			if (ka) {
				cmd += " cmp_kAnonymity.txt";
			}
			if (kc) {
				cmd += " cmp_kClustering.txt";
			}
			System.out.println(cmd);
			Process p = Runtime.getRuntime().exec(cmd);
			int r = p.waitFor();

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

			return r == 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean plotSmart(String dataDir, String plotDir,
			boolean containsCM, boolean containsCM2, boolean containsCM3,
			boolean containsCM4, boolean containsCM5) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean plotRandomSmart(String dataDir, String plotDir,
			boolean containsCM, boolean containsCM2, boolean containsCM3,
			boolean containsCM4, boolean containsCM5) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean plotRandomSmartBar(String dataDir, String plotDir,
			boolean containsCM, boolean containsCM2, boolean containsCM3,
			boolean containsCM4, boolean containsCM5) {
		// TODO Auto-generated method stub
		return false;
	}

}