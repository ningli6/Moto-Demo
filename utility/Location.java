package utility;

/*
 * This class represent coordinate location for a certain cell in the grid
 */
public class Location {
	private double latitude;
	private double longitude;

	public Location() {
		latitude = 0;
		longitude = 0;
	}

	public Location(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}

	public void setLocation(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	// compute actual distance between two location
	public double distTo(Location location) {
		if (location == null) return 0;
		if (location == this) return 0;
		double ans = 0;
		//convert the position into decimal degrees fromat
		double lat1 = this.latitude;
		double lat2 = location.getLatitude();
		double lon1 = this.longitude;
		double lon2 = location.getLongitude();
		double dlon = lon2 - lon1;
		double dlat = lat2 - lat1;
		dlon = Math.toRadians(dlon);
		dlat = Math.toRadians(dlat);
		double a = Math.pow(Math.sin(dlat/2), 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(dlon/2), 2);
		// Math.atan2(); ??
		double c = 2 * Math.asin(Math.sqrt(a));
		double R = 6371.0;//Radius of E in Km
		ans = R * c;
		return ans;
	}

	public void printLocation() {
		System.out.println("( " + latitude + ", " + longitude + " )");
	}
}