package javaPlot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmpPlot {
	/**
	 * Call a python script that plot ic vs q for random queries
	 * @param dataDir folder that contains simulation data
	 * @param plotDir folder that plots are saved
	 * @param noCM plot no countermeasure
	 * @param ad   plot additive noise
	 * @param tf   plot transfiguration
	 * @param ka   plot k anonymity
	 * @param kc   plot k clustering
	 * @return true if it works all right
	 */
	public static boolean plotRandom(String dataDir, String plotDir, boolean noCM, boolean ad, boolean tf, boolean ka, boolean kc) {
		System.out.println("Start plotting ic vs queries for random queries...");
		try {
			String cmd = "python C:\\Users\\Administrator\\Desktop\\motoDemo\\python\\plotICvsQ.py " + dataDir + " "+ plotDir;
			if (noCM) {
				cmd += " cmp_NoCountermeasure.txt";
			}
			if (ad) {
				cmd += " cmp_AdditiveNoise.txt";
			}
			if (tf) {
				cmd += " cmp_Transfiguration.txt";
			}
			if (ka) {
				cmd += " cmp_KAnonymity.txt";
			}
			if (kc) {
				cmd += " cmp_KClustering.txt";
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

	/**
	 * Call a python script that plot ic vs q for smart queries
	 * @param dataDir folder that contains simulation data
	 * @param plotDir folder that plots are saved
	 * @param noCM plot no countermeasure
	 * @param ad   plot additive noise
	 * @param tf   plot transfiguration
	 * @param ka   plot k anonymity
	 * @param kc   plot k clustering
	 * @return true if it works all right
	 */
	public static boolean plotSmart(String dataDir, String plotDir,
			boolean noCM, boolean ad, boolean tf,
			boolean ka, boolean kc) {
		System.out.println("Start plotting ic vs queries for smart queries...");
		try {
			String cmd = "python C:\\Users\\Administrator\\Desktop\\motoDemo\\python\\plotICvsQ_Smart.py " + dataDir + " "+ plotDir;
			if (noCM) {
				cmd += " cmp_smart_NoCountermeasure.txt";
			}
			if (ad) {
				cmd += " cmp_smart_AdditiveNoise.txt";
			}
			if (tf) {
				cmd += " cmp_smart_Transfiguration.txt";
			}
			if (ka) {
				cmd += " cmp_smart_KAnonymity.txt";
			}
			if (kc) {
				cmd += " cmp_smart_KClustering.txt";
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

	/**
	 * Plot ic vs q of random queris and smart queris with no countermeasure
	 * @param dataDir
	 * @param plotDir
	 * @return
	 */
	public static boolean plotRandom_vs_Smart(String dataDir, String plotDir) {
		System.out.println("Start plotting ic vs queries for random and smart queries with no countermeasure...");
		try {
			String cmd = "python C:\\Users\\Administrator\\Desktop\\motoDemo\\python\\plotICvsQ_random_smart.py " + dataDir + " "+ plotDir;
			cmd += " cmp_NoCountermeasure.txt cmp_smart_NoCountermeasure.txt";
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

	public static boolean plotRandomSmartBar(String dataDir, String plotDir,
			boolean containsCM, boolean containsCM2, boolean containsCM3,
			boolean containsCM4, boolean containsCM5) {
		// TODO Auto-generated method stub
		return false;
	}

}