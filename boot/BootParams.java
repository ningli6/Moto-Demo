package boot;

import java.util.List;

public class BootParams {
	private int numberOfChannels;
	private double NorthLat;
	private double SouthLat;
	private double EastLng;
	private double WestLng;
	private List<LatLng>[] puList;
	private boolean isCountermeasure;
	private String countermeasure = "NOCOUNTERMEASURE";
	private double counterParamD = -1;
	private int counterParamI = -1;
	private int numberOfQueries = -1;
	private String fileName;
	private String email;

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

	public void setPUonChannels(List<LatLng>[] list) {
		this.puList = list;
	}

	public List<LatLng> getPUOnChannel(int c) {
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
		if (!cm.equals("ADDITIVENOISE") &&  !cm.equals("TRANSFIGURATION") &&
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
			for (LatLng ll : puList[i]) {
				sb.append("[" + ll.getLat() + ", " + ll.getLng() + "] <br>");
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