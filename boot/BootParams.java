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
	
	public void delCountermeasure(String cm) {
		if (cmMap.containsKey(cm)) {
			cmMap.remove(cm);
		}
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
	
	public String paramsToTextFile() {
		StringBuilder sb = new StringBuilder();
		sb.append("Simulation parameters:\n\n");
		sb.append("Number of channels: " + numberOfChannels + "\n");
		sb.append("Analysis region: \n");
		sb.append("North latitude :" + NorthLat + "\n");
		sb.append("South latitude :" + SouthLat + "\n");
		sb.append("West longitude :" + WestLng + "\n");
		sb.append("East longitude :" + EastLng + "\n\n");
		sb.append("Location of Primary users: \n");
		int len = puList.length;
		for (int i = 0; i < len; i++) {
			sb.append("Channel " + i + ": \n");
			for (Location ll : puList[i]) {
				sb.append("[" + ll.getLatitude() + ", " + ll.getLongitude() + "]\n");
			}
			sb.append("\n");
		}
		sb.append("Countermeasure: \n");
		if (containsCM("NOCOUNTERMEASURE")) {
			sb.append("No countermeasure.");
			if (plotGooglMapNO()) {
				sb.append(" Plot inferred location of primary users on Google Maps.");
			}
			sb.append("\n");
		}
		if (containsCM("ADDITIVENOISE")) {
			sb.append("Additive noise. Noise level: " + getCMParam("ADDITIVENOISE") + ".");
			if (plotGooglMapAD()) {
				sb.append(" Plot inferred location of primary users on Google Maps.");
			}
			if (tradeOffAD()) {
				sb.append(" Plot trade-off curve.");
			}
			sb.append("\n");
		}
		if (containsCM("TRANSFIGURATION")) {
			sb.append("Transfiguration. Number of sids: " + (int) getCMParam("TRANSFIGURATION") + ".");
			if (plotGooglMapTF()) {
				sb.append(" Plot inferred location of primary users on Google Maps.");
			}
			if (tradeOffTF()) {
				sb.append(" Plot trade-off curve.");
			}
			sb.append("\n");
		}
		if (containsCM("KANONYMITY")) {
			sb.append("K anonymity. K: " + (int) getCMParam("KANONYMITY") + ".");
			if (plotGooglMapKA()) {
				sb.append(" Plot inferred location of primary users on Google Maps.");
			}
			sb.append("\n");
		}
		if (containsCM("KCLUSTERING")) {
			sb.append("K clustering. K: " + (int) getCMParam("KCLUSTERING") + ".");
			if (plotGooglMapAD()) {
				sb.append(" Plot inferred location of primary users on Google Maps.");
			}
			sb.append("\n");
		}
		sb.append("\n");
		if (numberOfQueries < 0) {
			sb.append("Upload_file: " + fileName + "\n");
		}
		else {
			sb.append("Number of queries: " + numberOfQueries + "\n");
		}
		return sb.toString();
	}

}