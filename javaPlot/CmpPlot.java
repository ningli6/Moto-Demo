package javaPlot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmpPlot {
	/**
	 * Call a python script that plot ic vs q
	 * @param cm Name of countermeasure
	 * @return true if it works all right
	 */
	public static boolean plot(String cm) {
		try {
			String fileName = "averageIC_NoCountermeasure.txt";
			String cmpName = null;
			switch(cm) {
			case "NOCOUNTERMEASURE": 
				break;
			case "ADDITIVENOISE": 
				cmpName = "cmp_AdditiveNoise.txt";
				break;
			case "TRANSFIGURATION":
				cmpName = "cmp_Transfiguration.txt";
			case "KANONYMITY":
				cmpName = "cmp_kAnonymity.txt";
			case "KCLUSTERING":
				cmpName = "cmp_kClustering.txt";
			}

			String cmd;
			if (cm == "NOCOUNTERMEASURE") 
				cmd = "python C:\\Users\\Administrator\\Desktop\\motoDemo\\python\\plotICvsQ.py " + fileName;
			else if (cm =="TRANSFIGURATION")
				cmd = "python C:\\Users\\Administrator\\Desktop\\motoDemo\\python\\plotICvsQwithCmpChopHead.py " + fileName + " " + cmpName; // chop query at 0
			else 
				cmd = "python C:\\Users\\Administrator\\Desktop\\motoDemo\\python\\plotICvsQwithCmp.py " + fileName + " " + cmpName;
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

}