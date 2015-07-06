package boot;

import demo.*;

/**
 * Entrance of java program
 * Parse parameters from web interface then pass it as a new data structure to a runnable class
 * @author Administrator
 *
 */
public class Boot {
	/* initial settings */
	public static double cellsize = 0.05;
	public static double mtpScale = 5;
	public static int interval = 5;
	// public static String directory = "C:\\Users\\Administrator\\Desktop\\motoData\\";
	public static String directory = "/Users/ningli/Desktop/motoData/";

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