package boot;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Entrance of java program
 * Parse parameters from web interface then pass it as a new data structure to a runnable class
 * @author Administrator
 *
 */
public class Boot {
	/* initial settings for working directory */
	public static String dataRootDir = "C:\\Users\\Administrator\\Desktop\\motoData\\";
	public static String plotRootDir = "C:\\Users\\Administrator\\Desktop\\motoPlot\\";;

	public static void main(String[] args) {
		try {
			String cmd = "";
			for (String arg : args) {
				cmd += arg + " ";
			}
			Long time = System.currentTimeMillis();
			String dataDir = dataRootDir + time + "\\";
			String plotDir = plotRootDir + time + "\\";
//			System.out.println("Start creating folders...");
			File dataFile = new File(dataDir);
			File plotFile = new File(plotDir);
			if (dataFile.exists()) {
				SecureRandom random = new SecureRandom();
				dataDir = dataRootDir + new BigInteger(130, random).toString(32) + "\\";
				if (!new File(dataDir).mkdirs()) {
					System.out.println("FAIL");
					return;
				}
			}
			else if (!dataFile.mkdirs()) {
				System.out.println("FAIL");
				return;
			}
			if (plotFile.exists()) {
				SecureRandom random1 = new SecureRandom();
				plotDir = plotRootDir + new BigInteger(130, random1).toString(32) + "\\";
				if (!new File(plotDir).mkdirs()) {
					System.out.println("FAIL");
					return;
				}
			}
			else if (!plotFile.mkdirs()) {
				System.out.println("FAIL");
				return;
			}
			/* start program, return ok */
			System.out.println("OK");
			Runtime.getRuntime().exec("java -jar C:\\Users\\Administrator\\Desktop\\motoDemo\\demoRun.jar " + cmd + dataDir + " " + plotDir);
//			BufferedReader stdInput = new BufferedReader(new
//						InputStreamReader(p.getInputStream()));
//	
//			BufferedReader stdError = new BufferedReader(new
//					InputStreamReader(p.getErrorStream()));
//
//			String s;
//			// read the output from the command
//			while ((s = stdInput.readLine()) != null) {
//				System.out.println(s);
//			}
//
//			// read any errors from the attempted command
//			while ((s = stdError.readLine()) != null) {
//				System.out.println(s);
//			}
		} catch (IOException e) {
			System.out.println("FAIL");
			e.printStackTrace();
		}
	    System.out.println("Finish");
	}
}