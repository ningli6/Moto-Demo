package boot;

import demo.*;

/**
 * Entrance of java program
 * Parse parameters from web interface then pass it as a new data structure to a runnable class
 * @author Administrator
 *
 */
public class Boot {
	/* initial settings, these are not included in BootParams */
	public static double cellsize = 0.005;    // cell size in degree, about 0.5 km
	public static double mtpScale = 1;        // MTP function
	public static int interval = 5;           // number of internal points of querying
	public static String directory = "C:\\Users\\Administrator\\Desktop\\motoData\\";
//	public static String directory = "/Users/ningli/Desktop/motoData/";

	public static void main(String[] args) {
		BootParams bp = Parser.parse(args);
		RunnableInterface R;
		if (bp == null) {
			System.out.println("FAILED");
			return;
		}
		if (bp.isCountermeasure() && bp.getCountermeasure().equals("NOCOUNTERMEASURE")) {
			System.out.println("FAILED");
			return;
		}

		R = new RunnableInterface(bp, cellsize, mtpScale, interval, directory);

		/* start program, return ok */
		System.out.println("OK");
	    R.start();
	}
}