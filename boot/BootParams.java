package boot;

import java.util.List;

import utility.Location;

/* class that holds information about input parameters */
public class BootParams {
	private double NorthLat;         // coordinates
	private double SouthLat;
	private double EastLng;
	private double WestLng;
	private List<Location>[] puList;   // location list
	private boolean isCountermeasure;// whether use countermeasure or not
	private String countermeasure = "NOCOUNTERMEASURE"; // countermeasure
	private double counterParamD = -1;  // countermeasure value (float)
	private int counterParamI = -1;     // countermeasure value (integer)
	private int numberOfChannels;    // number of channels
	private int numberOfQueries = -1;   // number of queries
	private String fileName;            // upload file name
	private String email;               // email address

	public int getNumberOfChannels() {
		return numberOfChannels;
	}

	public void setNumberOfChannels(int c) {
		if (c <= 0) throw new IllegalArgumentException();
		this.numberOfChannels = c;
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

	public boolean isCountermeasure() {
		return isCountermeasure;
	}

	/**
	 * Get name of countermeasure
	 * @return String of the name of countermeasure
	 */
	public String getCountermeasure() {
		return countermeasure;
	}

	public void setCountermeasure(String cm) {
		if (!cm.equals("ADDITIVENOISE") && !cm.equals("TRANSFIGURATION") &&
			!cm.equals("KANONYMITY") && !cm.equals("KCLUSTERING"))
			throw new IllegalArgumentException();
		isCountermeasure = true;
		countermeasure = cm;
	}

	/**
	 * Get value for the countermeasure
	 * @return
	 */
	public double getCMParam() {
		if (counterParamI != -1) return (double) counterParamI;
		return counterParamD;
	}

	/**
	 * Set countermeasure value
	 * @param p    float for additive noise, cast to integer for other countermeasures
	 */
	public void setCMParam(double p) {
		if (countermeasure.equals("ADDITIVENOISE")) {
			this.counterParamD = p;
		}
		else this.counterParamI = (int) p;
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
		if (!isCountermeasure) {
			sb.append("<p>Countermeasure: No countermeausre</p>");
		}
		else {
			String cmName = null;
			String cmParam = null;
			switch (countermeasure) {
			case "ADDITIVENOISE":
				cmName = countermeasure.toLowerCase();
				cmParam = "noise level: " + getCMParam();
				break;
			case "TRANSFIGURATION":
				cmName = countermeasure.toLowerCase();
				cmParam = "sides of polygon: " + (int) getCMParam();
				break;
			case "KANONYMITY":
				cmName = "k anonymity";
				cmParam = "size of group: " + (int) getCMParam();
				break;
			case "KCLUSTERING":
				cmName = "k clustering";
				cmParam = "number of clusters: " + (int) getCMParam();
				break;
			}
			sb.append("<p>Countermeasure: " + cmName + ", " + cmParam + "</p>");
		}
		if (numberOfQueries < 0) {
			sb.append("<p>Upload_file:_" + fileName + "</p>");
		}
		else {
			sb.append("<p>Number of queries: " + numberOfQueries + "</p>");
		}
		return sb.toString();
	}
}