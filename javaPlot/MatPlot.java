package javaPlot;

public class MatPlot {
	/**
	 * Call Matlab routine that plot data on google map
	 * @param noc number of channels
	 * @return return true if it works
	 */
	public static boolean plot(int noc) {
		try {
	        // using the Runtime exec method:
	        String cmd = "java -cp \"C:\\Users\\Administrator\\Desktop\\plotMap\";\"C:\\Program Files\\MATLAB\\MATLAB Compiler Runtime\\v83\\toolbox\\javabuilder\\jar\\win64\\javabuilder.jar\";\"C:\\Users\\Administrator\\Desktop\\plotMap\\MatPlot.jar\" getmagic " + Integer.toString(noc);

	        Process p = Runtime.getRuntime().exec(cmd);
	        int r = p.waitFor();

//	        BufferedReader stdInput = new BufferedReader(new
//	           InputStreamReader(p.getInputStream()));
//
//	        BufferedReader stdError = new BufferedReader(new
//	           InputStreamReader(p.getErrorStream()));
//
//	        String s;
//			// read the output from the command
//	        while ((s = stdInput.readLine()) != null) {
//	            System.out.println(s);
//	        }
//
//	        // read any errors from the attempted command
//	        while ((s = stdError.readLine()) != null) {
//	            System.out.println(s);
//	        }
	        return r == 0;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
}