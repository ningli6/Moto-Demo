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
	private String countermeasure;
	private double counterParam;
	private int number_of_queries;
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


}