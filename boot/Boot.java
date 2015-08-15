package boot;

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
	public static String directory = "C:\\Users\\Administrator\\Desktop\\motoData\\";
//	public static String directory = "/Users/ningli/Desktop/motoData/";

	public static void main(String[] args) {
		BootParams bp = Parser.parse(args);
		if (bp == null) {
			System.out.println("FAILED");
			return;
		}
		Demo R = new Demo(bp, mtpScale, interval, directory);
		/* start program, return ok */
		System.out.println("OK");
	    R.start();
	}
}