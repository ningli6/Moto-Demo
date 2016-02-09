package boot;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public static String logRootDir = "C:\\Users\\Administrator\\Desktop\\motoDemo\\";
	
	public static void main(String[] args) {
		try {
			Long time = System.currentTimeMillis();
			String dataDir = dataRootDir + time + "\\";
			String plotDir = plotRootDir + time + "\\";
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
				SecureRandom random = new SecureRandom();
				plotDir = plotRootDir + new BigInteger(130, random).toString(32) + "\\";
				if (!new File(plotDir).mkdirs()) {
					System.out.println("FAIL");
					return;
				}
			}
			else if (!plotFile.mkdirs()) {
				System.out.println("FAIL");
				return;
			}
			/* Parse input string */
			BootParams bp = Parser.parse(args);
			if (bp == null) {
				System.out.println("FAILED");
				return;
			}
			
			/* Keep record */
			List<String> records = new ArrayList<String>();
			records.add(new Date().toString() + " " + bp.getEmail() + "\n");
			Path logTxt = Paths.get(logRootDir + "log.txt");
			Files.write(logTxt, records, Charset.forName("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

			/* start program, return ok */
			System.out.println("OK");
			Demo demo = new Demo(bp, dataDir, plotDir);
			demo.run();
		} catch (Exception e) {
			System.out.println("FAIL");
			e.printStackTrace();
		}
	}
}