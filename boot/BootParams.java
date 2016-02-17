package boot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utility.Location;

/* class that holds information about input parameters */
public class BootParams {
	private double mtpScale = 1;       // scale for mtp function
	private int interval = 5;          // intervals for recording points
	public static final double MaxCellSize = 0.05; // max grid size
	private double cellSize;           // grid size
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
	private boolean tradeOffAD;        // whether to plot trade off curve for additive noise
	private boolean tradeOffTF;        // whether to plot trade off curve for transfiguration
	private boolean tradeOffKA;        // whether to plot rade off bar for k anonymity
	private boolean tradeOffKC;        // whether to plot rade off bar for k clustering
	private int numberOfChannels;      // number of channels
	private boolean randomQuery;       // whether to do random queries
	private boolean smartQuery;        // whether to do smart queries
	private int numberOfQueries;       // number of queries
	private String email;              // email address
	private boolean inputParams;       // whether to include input parameters in the email

	public BootParams() {
		this.cmMap = new HashMap<String, Double>();
	}

	public double getMtpScale() {
		return mtpScale;
	}

	public void setMtpScale(double mtpScale) {
		this.mtpScale = mtpScale;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public double getCellSize() {
		return cellSize;
	}
	
	/**
	 * Get the estimated grid size in km instead of degree
	 * @param cellsize
	 * @return
	 */
	private double getGridSizeInKM(double cellsize) {
		if (cellsize == 0.005) return 0.5;
		if (cellsize == 0.01) return 1;
		return 5;
	}

	public void setCellSize(double cellSize) {
		this.cellSize = cellSize;
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
	public boolean isTradeOffAD() {
		return this.tradeOffAD;
	}
	
	/**
	 * Plot trade off curve for additive noise
	 */
	public void setTradeOffAD(boolean f) {
		this.tradeOffAD = f;
	}

	/**
	 * Whether to plot trade off curve for transfiguration
	 * @return true if user decided to plot trade off curve
	 */
	public boolean isTradeOffTF() {
		return this.tradeOffTF;
	}

	/**
	 * Plot trade off curve for transfiguration
	 */
	public void setTradeOffTF(boolean f) {
		this.tradeOffTF = f;
	}
	
	public boolean isTradeOffKA() {
		return this.tradeOffKA;
	}
	
	public void setTradeOffKA(boolean f) {
		this.tradeOffKA = f;
	}
	
	public boolean isTradeOffKC() {
		return tradeOffKC;
	}

	public void setTradeOffKC(boolean tradeOffKC) {
		this.tradeOffKC = tradeOffKC;
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
	
	public boolean isRandomQuery() {
		return randomQuery;
	}
	
	public void setRandomQuery(boolean randq) {
		this.randomQuery = randq;
	}

	public boolean isSmartQuery() {
		return smartQuery;
	}

	public void setSmartQuery(boolean smartQuery) {
		this.smartQuery = smartQuery;
	}

	public void setNumberOfQueries(int q) {
		if (q < 0) throw new IllegalArgumentException();
		this.numberOfQueries = q;
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
	 * Generate string that contains information about inputs
	 */
	public String paramsToTextFile() {
		StringBuilder sb = new StringBuilder();
		sb.append("Simulation parameters:\n\n");
		sb.append("Number of channels: " + numberOfChannels + "\n\n");
		sb.append("Grid szie: " + getGridSizeInKM(cellSize) + " km \n\n");
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
			if (isTradeOffAD()) {
				sb.append(" Plot trade-off curve.");
			}
			sb.append("\n");
		}
		if (containsCM("TRANSFIGURATION")) {
			sb.append("Transfiguration. Number of sides: " + (int) getCMParam("TRANSFIGURATION") + ".");
			if (plotGooglMapTF()) {
				sb.append(" Plot inferred location of primary users on Google Maps.");
			}
			if (isTradeOffTF()) {
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
		if (isRandomQuery() && isSmartQuery()) {
			sb.append("Use both random query and smart query algorithms.\n");
		}
		else if (isRandomQuery()) {
			sb.append("Only use random queries in the simulation.\n");
		}
		else {
			sb.append("Only use smart queries in the simulation.\n");
		}
		sb.append("Number of queries: " + numberOfQueries + "\n\n");
		sb.append("Receiver: " + email + "\n");
		return sb.toString();
	}

}