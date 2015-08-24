package boot;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Entrance of java program
 * Parse parameters from web interface then pass it as a new data structure to a runnable class
 * @author Administrator
 *
 */
public class Boot {
	/* initial settings, these are not included in BootParams */
	public static double mtpScale = 1;        // MTP function
	public static int interval = 5;           // number of internal points of querying
	public static String dataRootDir = "C:\\Users\\Administrator\\Desktop\\motoData\\";
	public static String plotRootDir = "C:\\Users\\Administrator\\Desktop\\motoPlot\\";;

	public static void main(String[] args) {
		BootParams bp = Parser.parse(args);
		if (bp == null) {
			System.out.println("FAILED");
			return;
		}
		Long time = System.currentTimeMillis();
		String dataDir = dataRootDir + time + "\\";
		String plotDir = plotRootDir + time + "\\";
		System.out.println("Start creating folders...");
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
		System.out.println(dataDir);
		System.out.println(plotDir);
		Demo R = new Demo(bp, mtpScale, interval, dataDir, plotDir);
		/* start program, return ok */
		System.out.println("OK");
	    R.start();
	}
}