/*
 * PU represents a primary user, server keeps track of pus that were added to it. 
 * Right now it just has location information.
 */

public class PU {
	/* may not need id for pu in future versioin */
	private int id = -1;
	private Location location = null;
	private int channelID = -1;
	/* debugging purpose */
	private int number_of_response;
	/* maybe implemented in future version */
	// private double power; potenital? power that pu is transmitting

	public PU(int id, Location location) {
		// defalut
		this.id = id;
		if (location == null) this.location = new Location();
		else this.location = location;
		this.number_of_response = 0;
	}

	public PU(int id, double lat, double lon) {
		this.id = id;
		this.location = new Location(lat, lon);
		this.number_of_response = 0;
	}

	public PU(int id, String lat, String lon) {
		if (lat == null || lon == null) throw new NullPointerException();
		this.id = id;
		this.location = new Location(lat, lon);
		this.number_of_response = 0;
	}

	public void setID(int id) {
		if (this == null) {
			System.out.println("Initialize PU first");
			return;
		}
		this.id = id;
	}

	public int getID() {
		if (this == null) {
			System.out.println("Initialize PU first");
			return -1;
		}
		return id;
	}

	public void setChannelID(int cid) {
		if (this == null) {
			System.out.println("Initialize PU first");
			return;
		}
		if (cid < 0) {
			System.out.println("Channel ID must be positive");
			return;
		}
		this.channelID = cid;
	}

	public int getChannelID() {
		if (this == null) {
			System.out.println("Initialize PU first");
			return -1;
		}
		if (channelID == -1) {
			System.out.println("Set channel ID first");
			return -1;
		}
		return channelID;
	}

	/* client will call this method if that pu is updated
	 * for debugging purpose
	 */
	public void sendResponse() {
		number_of_response++;
	}
	
	/* main will call this method to check which pu has responsed for how many times
	 * again for debugging purpose
	 *
	public int getNumberOfResponse() {
		return number_of_response;
	} */

	public void setLocation(Location location) {
		if (location == null) {
			System.out.println("PU's location cannot be set to null");
			return;
		}
		this.location = location;
	}

	public void setLocation(double lat, double lon) {
		if (location == null) location = new Location(lat, lon);
		else location.setLocation(lat, lon);
	}

	public void setLocation(String lat, String lon) {
		if (location == null) location = new Location(lat, lon);
		else location.setLocation(lat, lon);
	}

	public Location getLocation() {
		return location;
	}

	public double getLatitude() {
		if (location == null) return 0;
		return location.getLatitude();
	}

	public double getLongitude() {
		if (location == null) return 0;
		return location.getLongitude();
	}

	public void printLocation() {
		if (location == null) {
			System.out.println("PU has no location information");
			return;
		}
		location.printLocation();
	}

	public void printInfo() {
		System.out.println("***PU***");
		System.out.println("id: " + this.id);
		System.out.println("working channel: " + this.channelID);
		printLocation();
		System.out.println("updated " + this.number_of_response + " times");
		System.out.println();
	}
}