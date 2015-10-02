package utility;
/*
 * This class provides parameters that MTP function is going to use
 */
public class MTP {
//	public static double times = 1;
	public static double d0 = 0;
	public static double d1 = 8;
	public static double d2 = 14;
	public static double d3 = 25;

	public static double P_100 = 1;
	public static double P_75 = 0.75;
	public static double P_50 = 0.5;
	public static double P_0 = 0;
	
	public static double pid1d1 = 201; // km ^ 2
	public static double pid2d2 = 616;
	public static double pid3d3 = 1963;

	public static void ChangeMult(double time) {
		d0 = 0 * time;
		d1 = 8 * time;
		d2 = 14 * time;
		d3 = 25 * time;
		pid1d1 = 201 * time * time; // km ^ 2
		pid2d2 = 616 * time * time;
		pid3d3 = 1963 * time * time;
	}
}