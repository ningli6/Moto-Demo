package javaPlot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TradeOffPlot {
	public static boolean plot(boolean ad, boolean tr) {
		if (!ad && !tr) {
			System.out.println("No trade-off curve need to be plotted.");
			return true;
		}
		System.out.println("Start plotting trade off curves...");
		try {
			String cmd = "python C:\\Users\\Administrator\\Desktop\\motoDemo\\python\\plotTradeOff.py";
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
			return r == 0;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
