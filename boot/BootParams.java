package boot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utility.Location;

/* class that holds information about input parameters */
public class BootParams {
	private double NorthLat;           // coordinates
	private double SouthLat;
	private double EastLng;
	private double WestLng;
	private List<Location>[] puList;   // location list
	private Map<String, Double> cmMap; // countermeasures and their values
	private boolean gMapNO;            // plot google map for no countermeasure
	private boolean gMapAD;            // plot google map for additive noise
	private boolean gMapTF;            // plot google map for transfiguration
	private boolean gMapKA;            // plot google map for k anonymity
	private boolean gMapKC;            // plot google map for k clustering
	private boolean tradeOffAD;        // whether to plot trade off curve for tf
	private boolean tradeOffTF;        // whether to plot trade off curve for tf
	private int numberOfChannels;      // number of channels
	private int numberOfQueries;       // number of queries
	private String fileName;           // upload file name
	private String email;              // email address
	private boolean inputParams;       // whether to include input parameters in the email

	public BootParams() {
		this.cmMap = new HashMap<String, Double>();
	}

	public double getNorthLat() {
		return NorthLat;
	}

	public void setNorthLat(double lat) {
		this.NorthLat = lat;
	}

	public double getSouthLat() {
		return SouthLat;
	}

	public void setSouthLat(double lat) {
		this.SouthLat = lat;
	}

	public double getEastLng() {
		return EastLng;
	}

	public void setEastLng(double lng) {
		this.EastLng = lng;
	}

	public double getWestLng() {
		return WestLng;
	}

	public void setWestLng(double lng) {
		this.WestLng = lng;
	}

	public void setPUonChannels(List<Location>[] list) {
		this.puList = list;
	}

	/**
	 * Get a list of coordinate locations on one channels
	 * @param c   channel
	 * @return    a list of Location class
	 */
	public List<Location> getPUOnChannel(int c) {
		if (c < 0 || c >= numberOfChannels) throw new IllegalArgumentException();
		return puList[c];
	}

	/**
	 * Associate countermeasure with its value
	 * @param cm  name of countermeasure
	 * @param val parameters for countermeasure
	 */
	public void putCountermeasure(String cm, double val) {
		if (cmMap.containsKey(cm)) {
			throw new IllegalArgumentException("This countermeasure was selected!");
		}
		cmMap.put(cm, val);
	}

	/**
	 * Whether this countermeasure is selected
	 * @param  cm name of countermeasure
	 * @return    true if user have decided to plot this countermeasure
	 */
	public boolean containsCM(String cm) {
		return cmMap.containsKey(cm);
	}

	/**
	 * Return the value for the countermeasure
	 * @param  cm name of the countermeasure
	 * @return    value of that countermeasrue
	 */
	public double getCMParam(String cm) {
		if (!containsCM(cm)) {
			throw new IllegalArgumentException("User didn't select this countermeasure");
		}
		return cmMap.get(cm);
	}

	// setter and getter for google map options
	public boolean plotGooglMapNO() {
		return this.gMapNO;
	}

	public void setGoogleMapNO(boolean f) {
		this.gMapNO = f;
	}

	public boolean plotGooglMapAD() {
		return this.gMapAD;
	}

	public void setGoogleMapAD(boolean f) {
		this.gMapAD = f;
	}

	public boolean plotGooglMapTF() {
		return this.gMapTF;
	}

	public void setGoogleMapTF(boolean f) {
		this.gMapTF = f;
	}

	public boolean plotGooglMapKA() {
		return this.gMapKA;
	}

	public void setGoogleMapKA(boolean f) {
		this.gMapKA = f;
	}

	public boolean plotGooglMapKC() {
		return this.gMapKC;
	}

	public void setGoogleMapKC(boolean f) {
		this.gMapKC = f;
	}

	/**
	 * Whether to plot trade off curve for additive noise
	 * @return true if user decided to plot trade off curve
	 */
	public boolean tradeOffAD() {
		return this.tradeOffAD;
	}

	/**
	 * Whether to plot trade off curve for transfiguration
	 * @return true if user decided to plot trade off curve
	 */
	public boolean tradeOffTF() {
		return this.tradeOffTF;
	}

	/**
	 * Plot trade off curve for additive noise
	 */
	public void setTradeOffAD(boolean f) {
		this.tradeOffAD = f;
	}

	/**
	 * Plot trade off curve for transfiguration
	 */
	public void setTradeOffTF(boolean f) {
		this.tradeOffTF = f;
	}

	public int getNumberOfChannels() {
		return numberOfChannels;
	}

	public void setNumberOfChannels(int c) {
		if (c <= 0) throw new IllegalArgumentException();
		this.numberOfChannels = c;
	}

	public int getNumberOfQueries() {
		return numberOfQueries;
	}

	public void setNumberOfQueries(int q) {
		if (q < 0) throw new IllegalArgumentException();
		this.numberOfQueries = q;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void useFile(String file) {
		if (file == null) throw new IllegalArgumentException();
		this.fileName = file;
	}

	public void setEmail(String e) {
		this.email = e;
	}

	public String getEmail() {
		return email;
	}

	/**
	 * Whether to include input parameters in the email
	 * @return true if user decide to include input parameters in the email in text format
	 */
	public boolean getInputParams() {
		return inputParams;
	}

	/**
	 * Include input parameters in the email
	 */
	public void setInputParams(boolean f) {
		this.inputParams = f;
	}

	/**
	 * Construct email content
	 * @return  a string that describing input parameters
	 */
	public String paramsToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<h3>Simulation result</h3>");
		sb.append("<p>Number of channels: " + numberOfChannels + "</p>");
		sb.append("<p>Analysis region: <br>");
		sb.append("North latitude :" + NorthLat + "<br>");
		sb.append("South latitude :" + SouthLat + "<br>");
		sb.append("West longitude :" + WestLng + "<br>");
		sb.append("East longitude :" + EastLng + "<br>");
		sb.append("</p>");
		sb.append("<p>Location of Primary users: <br>");
		int len = puList.length;
		for (int i = 0; i < len; i++) {
			sb.append("Channel " + i + ": <br>");
			for (Location ll : puList[i]) {
				sb.append("[" + ll.getLatitude() + ", " + ll.getLongitude() + "] <br>");
			}
			sb.append("</p>");
		}
//		if (!isCountermeasure) {
//			sb.append("<p>Countermeasure: No countermeausre</p>");
//		}
//		else {
//			String cmName = null;
//			String cmParam = null;
//			switch (countermeasure) {
//			case "ADDITIVENOISE":
//				cmName = countermeasure.toLowerCase();
//				cmParam = "noise level: " + getCMParam();
//				break;
//			case "TRANSFIGURATION":
//				cmName = countermeasure.toLowerCase();
//				cmParam = "sides of polygon: " + (int) getCMParam();
//				break;
//			case "KANONYMITY":
//				cmName = "k anonymity";
//				cmParam = "size of group: " + (int) getCMParam();
//				break;
//			case "KCLUSTERING":
//				cmName = "k clustering";
//				cmParam = "number of clusters: " + (int) getCMParam();
//				break;
//			}
//			sb.append("<p>Countermeasure: " + cmName + ", " + cmParam + "</p>");
//		}
		if (numberOfQueries < 0) {
			sb.append("<p>Upload_file:_" + fileName + "</p>");
		}
		else {
			sb.append("<p>Number of queries: " + numberOfQueries + "</p>");
		}
		return sb.toString();
	}
}