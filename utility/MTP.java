package utility;
/*
 * This class provides parameters that MTP function is going to use
 */
public class MTP {
	public static double d0 = 0;
	public static double d1 = 8;
	public static double d2 = 14;
	public static double d3 = 25;
	
	public static double pid1d1 = 201; // km ^ 2
	public static double pid2d2 = 616;
	public static double pid3d3 = 1963;
	
	public static double P_100 = 1;
	public static double P_75 = 0.75;
	public static double P_50 = 0.5;
	public static double P_0 = 0;
	
	public static void ModifyMTP(double d0, double d1, double d2) {
		MTP.d1 = d0;
		MTP.d2 = d1;
		MTP.d3 = d2;
		MTP.pid1d1 = Math.PI * MTP.d1 * MTP.d1;
		MTP.pid2d2 = Math.PI * MTP.d2 * MTP.d2;
		MTP.pid3d3 = Math.PI * MTP.d3 * MTP.d3;
	}
}
