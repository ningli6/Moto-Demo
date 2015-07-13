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
		// if (!map.withInBoundary(lat, lon)) throw new IllegalArgumentException("Coordinate is not in the range of map");
		latitude = lat;
		longitude = lon;
	}

	// the position's format - Degrees|minutes'seconds''hemisphere
	// ([0-9])+(\.[0-9]+){0,1}\|([0-9])+(\.[0-9]+){0,1}'([0-9])+(\.[0-9]+){0,1}''(N|S|E|W)\.
	public Location(String lat, String lon) {
		if (lat == null || lon == null) throw new NullPointerException();
		// define a pattern
		String regexp = "([0-9])+(\\.[0-9]+){0,1}\\|([0-9])+(\\.[0-9]+){0,1}'([0-9])+(\\.[0-9]+){0,1}''(N|S|E|W)\\.";
		if (!lat.matches(regexp) || !lon.matches(regexp))
			throw new IllegalArgumentException("The regexp of input must be: ([0-9])+(\\.[0-9]+){0,1}\\|([0-9])+(\\.[0-9]+){0,1}'([0-9])+(\\.[0-9]+){0,1}''(N|S|E|W)\\.");
		latitude = convertToDecDeg(Degrees(lat), Minutes(lat), Seconds(lat), Hemisphere(lat));
		longitude = convertToDecDeg(Degrees(lon), Minutes(lon), Seconds(lon), Hemisphere(lon));
		// if (!map.withInBoundary(latitude, longitude)) throw new IllegalArgumentException("Coordinate is not in the range of map");
	}

	/********************************************************************************************
	 * Author: Sudeep Bhattarai
	 * purpose: To convert deg, min, sec to decimal degrees
	 * @param deg - the degree portion
	 * @param min - the minutes portion
	 * @param sec - the seconds portion
	 * @param hemisphere - the hemisphere portion
	 */	
	private double convertToDecDeg( double deg, double min, double sec, String hemisphere ){
		double decimal = 0;
		
		// the formula -> decimal = deg + (min / 60) + (sec / 3600)
		decimal += deg;
		decimal += min / 60.0;
		decimal += sec / 3600.0;
		
		// the decimal is negetive if the position is in the Western or Souther hemisphere
		if( hemisphere.equals("S") || hemisphere.equals("W") ){
			decimal = -1.0 * decimal;
		}		
		return decimal;
	}

	/********************************************************************************************
	* Author: Sudeep Bhattarai
	*/
    private double Degrees(String pos) {
		double deg = 0;
		// the delimiter signifying the end of the degrees
		int end = pos.indexOf("|");
		
		//isolate the degrees
		String sub = pos.substring(0, end);
		// convert the degrees to double
		deg = (double) Double.valueOf(sub);
		return deg;
	}
	
	/********************************************************************************************
	 * Author: Sudeep Bhattarai
	 * purpose: The Lat and Long have degrees, minutes, seconds. 
	 * 				This program deals the whole Lat or Long as a String
	 * 				They are formatted as such: "Degrees|Minutes'Seconds''Hemisphere."
	 * 				To get the minutes portion of the position
	 * @param pos - the string formatted in the aforementioned format
	 */	
	private double Minutes(String pos){
		double minutes = 0;
		
		// the delimiter signifying the beginning of the minute
		int begin = pos.indexOf("|") + 1;
		// the delimiter signifying the end of the minute
		int end = pos.indexOf("'");
		
		//isolate and convert to double the minute portion
		String sub = pos.substring(begin, end);
		minutes = (double) Double.valueOf(sub);
		return minutes;
	}
	
	/********************************************************************************************
	 * Author: Sudeep Bhattarai
	 * purpose: The Lat and Long have degrees, minutes, seconds. 
	 * 				This program deals the whole Lat or Long as a String
	 * 				They are formatted as such: "Degrees|Minutes'Seconds''Hemisphere."
	 * 				To get the seconds portion of the position
	 * @param pos - the string formatted in the aforementioned format
	 */	
	private double Seconds(String pos){
		double seconds = 0;
		
		// the delimiter signifying the beginning of the seconds
		int begin = pos.indexOf("'") + 1;
		// the delimiter signifying the end of the seconds
		int end = pos.indexOf("''");
		
		// isolate and convert to double the seconds
		String sub = pos.substring(begin, end);
		seconds = (double) Double.valueOf(sub);
		return seconds;
	}
	
	/********************************************************************************************
	 * purpose: The Lat and Long have degrees, minutes, seconds. 
	 * 				This program deals the whole Lat or Long as a String
	 * 				They are formatted as such: "Degrees|Minutes'Seconds''Hemisphere."
	 * 				To get which hemisphere the position is in
	 * @param pos - the string formatted in the aforementioned format
	 */	
	private String Hemisphere(String pos){
		String hemisphere = "";
		
		// the delimiter signifying the beginning of the hemisphere
		int begin = pos.indexOf("''") + 2;
		// the delimiter signifying the end of the hemisphere
		//int end = pos.indexOf(".");
		/*** update by Ning Li ***/
		/* use lastIndexOf to allow decimal fraction from input */
		int end = pos.lastIndexOf(".");
		//isolate the hemisphere
		hemisphere = pos.substring(begin, end);
		return hemisphere;
	}

	public void setLocation(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}

	public void setLocation(String lat, String lon) {
		// use regular expression to check formation of input string
		String regexp = "([0-9])+\\|([0-9])+'([0-9])+''(N|S|E|W)";
		if (!lat.matches(regexp) || !lon.matches(regexp)) {
			System.out.println("Input location format must be: ([0-9])+\\|([0-9])+'([0-9])+''(N|S|E|W)");
			return;
		}
		latitude = convertToDecDeg(Degrees(lat), Minutes(lat), Seconds(lat), Hemisphere(lat));
		longitude = convertToDecDeg(Degrees(lon), Minutes(lon), Seconds(lon), Hemisphere(lon));
		// if (!map.withInBoundary(latitude, longitude)) throw new IllegalArgumentException("Coordinate is not in the range of map");
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