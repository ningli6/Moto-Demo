package boot;

import java.util.List;

public class BootParams {
	private int number_of_channels;
	private double NorthLat;
	private double SouthLat;
	private double EastLng;
	private double WestLng;
	private List<LatLng>[] PUList;
	private boolean isCountermeasure;
	private String countermeasure = "NOCOUNTERMEASURE";
	private double counterParamD = -1;
	private int counterParamI = -1;
	private int number_of_queries = -1;
	private String fileName;
	private String email;

	public int getNumberOfChannels() {
		return number_of_channels;
	}

	public void setNumberOfChannels(int c) {
		if (c <= 0) throw new IllegalArgumentException();
		this.number_of_channels = c;
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
		this.PUList = list;
	}

	public List<LatLng> getPUOnChannel(int c) {
		if (c < 0 || c >= number_of_channels) throw new IllegalArgumentException();
		return PUList[c];
	}

	public boolean isCountermeasure() {
		return isCountermeasure;
	}

	public String countermeasure() {
		return countermeasure;
	}

	public void setCountermeasure(String cm) {
		if (!cm.equals("ADDITIVENOISE") &&  !cm.equals("TRANSFIGURATION") &&
			!cm.equals("KANONYMITY") && !cm.equals("KCLUSTERING"))
			throw new IllegalArgumentException();
		isCountermeasure = true;
		countermeasure = cm;
	}

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
		return number_of_queries;
	}

	public void setNumberOfQueries(int q) {
		if (q < 0) throw new IllegalArgumentException();
		this.number_of_queries = q;
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

	public void printParams() {
		System.out.println("Print parameters");
		System.out.println("Number of channels: " + number_of_channels);
		System.out.println("Analysis region: ");
		System.out.println("North latitude: " + NorthLat);
		System.out.println("South latitude: " + SouthLat);
		System.out.println("West longitude: " + WestLng);
		System.out.println("East longitude: " + EastLng);
		System.out.println("Location of PU: ");
		int len = PUList.length;
		for (int i = 0; i < len; i++) {
			System.out.println("Channel " + i + ": ");
			for (LatLng ll : PUList[i]) {
				System.out.print("[" + ll.getLat() + ", " + ll.getLng() + "] ");
			}
			System.out.println();
		}
		if (!isCountermeasure) {
			System.out.println("Countermeasure: " + "No countermeausre");
		}
		else {
			System.out.println("Countermeasure: " + countermeasure + ", Param: " + getCMParam());
		}
		if (number_of_queries < 0) {
			System.out.println("Upload file: " + fileName);
		}
		else {
			System.out.println("Number of Queries: " + number_of_queries);
		}
		System.out.println("Email to: " + email);
		System.out.println();
	}

	public String paramsToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("*****Print_parameters*****#");
		sb.append("#");
		sb.append("Number_of_channels:_" + number_of_channels + "#");
		sb.append("#");
		sb.append("Analysis_region:#");
		sb.append("North_latitude:_" + NorthLat + "#");
		sb.append("South_latitude:_" + SouthLat + "#");
		sb.append("West_longitude:_" + WestLng + "#");
		sb.append("East_longitude:_" + EastLng + "#");
		sb.append("#");
		sb.append("Location_of_PU:#");
		int len = PUList.length;
		for (int i = 0; i < len; i++) {
			sb.append("Channel_" + i + ":#");
			for (LatLng ll : PUList[i]) {
				sb.append("[" + ll.getLat() + ",_" + ll.getLng() + "]#");
			}
			sb.append("#");
		}
		if (!isCountermeasure) {
			sb.append("Countermeasure:_" + "No_countermeausre#");
		}
		else {
			sb.append("Countermeasure:_" + countermeasure + ",_Param:_" + getCMParam() + "#");
		}
		sb.append("#");
		if (number_of_queries < 0) {
			sb.append("Upload_file:_" + fileName + "#");
		}
		else {
			sb.append("Number_of_Queries:_" + number_of_queries + "#");
		}
		sb.append("#");
		sb.append("Email_to:_" + email + "#");
		sb.append("#");
		return sb.toString();
	}
}