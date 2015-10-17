package javaPlot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TradeOffPlot {
	public static boolean plotRandom(String dataDir, String plotDir, boolean ad, boolean tr, boolean ka, boolean kc) {
		if (!ad && !tr && !ka && !kc) {
			System.out.println("No trade-off curve need to be plotted.");
			return true;
		}
		try {
			System.out.println("Start plotting trade off curves for random queries...");
			String cmd = "python C:\\Users\\Administrator\\Desktop\\motoDemo\\python\\plotTradeOff.py " + dataDir + " " + plotDir;
			if (ad) {
				cmd += " traddOff_AdditiveNoise.txt";
			}
			if (tr) {
				cmd += " traddOff_Transfiguration.txt";
			}
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
			
			System.out.println("Start plotting trade off bars for random queries...");
			cmd = "python C:\\Users\\Administrator\\Desktop\\motoDemo\\python\\plotTradeOffBar.py " + dataDir + " " + plotDir;
			if (ka) {
				cmd += " traddOff_KAnonymity.txt";
			}
			if (kc) {
				cmd += " traddOff_KClustering.txt";
			}
			p = Runtime.getRuntime().exec(cmd);
			r = p.waitFor();

			stdInput = new BufferedReader(new
				InputStreamReader(p.getInputStream()));

			stdError = new BufferedReader(new
				InputStreamReader(p.getErrorStream()));

			// read the output from the command
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

	        // read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
			return r == 0;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean plotSmart(String dataDir, String plotDir,
			boolean tradeOffAD, boolean tradeOffTF, boolean tradeOffKA,
			boolean tradeOffKC) {
		// TODO Auto-generated method stub
		return false;
	}
}
