/*
 * PU represents a primary user, server keeps track of pus that were added to it. 
 * Right now it just has location information.
 */

public class PU {
	private int id;
	private Location location;

	public PU(int id, Location location) {
		// defalut
		this.id = id;
		if (location == null) this.location = new Location();
		else this.location = location;
	}

	public PU(int id, double lat, double lon) {
		this.id = id;
		this.location = new Location(lat, lon);
	}

	public PU(int id, String lat, String lon) {
		if (lat == null || lon == null) throw new NullPointerException();
		this.id = id;
		this.location = new Location(lat, lon);
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		if (this == null) {
			System.out.println("Initialize PU first");
			return;
		}
		this.id = id;
	}

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
		if (location == null) return;
		location.printLocation();
	}
}