package utility;

import java.util.Comparator;

import server.*;
/*
 * PU represents a primary user, server keeps track of pus that were added to it. 
 * Right now it just has location information.
 */

public class PU {
	/* may not need id for pu in future versioin */
	public final Comparator<PU> DIST_ORDER = new distOrder(); // can this be static
	private int id = -1;
	private int channelID = -1;
	private Location location = null;
	private int indexOfRow = 0;
	private int indexOfCol = 0;
	/* debugging purpose */
	private int number_of_response = 0;
	/* maybe implemented in future version */
	// private double power; potenital? power that pu is transmitting
	private Server server = null;
	private double baseRadius = 0;

	private class distOrder implements Comparator<PU> { // can this be static class
		public int compare(PU pu1, PU pu2) {
			if (pu1 == null || pu2 == null) throw new NullPointerException();
			// if (pu1 == this || pu2 == this) throw new IllegalArgumentException();
			double dist1 = distTo(pu1);
			double dist2 = distTo(pu2);
			if (dist1 == dist2) return 0;
			if (dist1 < dist2) return -1;
			return 1;
		}
	}

	public PU() {

	}

	public PU(int id, Location location) {
		if (location == null) throw new NullPointerException();
		// defalut
		this.id = id;
		this.location = location;
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

	public PU(int id, int r, int c) {
		this.id = id;
		indexOfRow = r;
		indexOfCol = c;
	}

	public void attachToServer(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
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

	// clear number of response
	public void reset() {
		number_of_response = 0;
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

	public void setIndices(int r, int c) {
		indexOfRow = r;
		indexOfCol = c;
	}

	public int getRowIndex() {
		return indexOfRow;
	}

	public int getColIndex() {
		return indexOfCol;
	}

	public Location getLocation() {
		if (location == null) throw new NullPointerException("Location for PU has not yet been initialized");
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

	private double distTo(PU pu) {
		if (pu == null) throw new NullPointerException();
		if (this.location == null) throw new NullPointerException("Location for PU has not yet been initialized");
		return this.location.distTo(pu.getLocation());
	}

	public Location indexToLocation() {
		// if (location != null) return location;
		if (this.server == null) throw new NullPointerException("Attach to server first");
		this.location = this.server.getMap().getLocation(this.indexOfRow, this.indexOfCol);
		return location;
	}

	public void updateRadius(double base) {
		if (base < 0) throw new IllegalArgumentException();
		this.baseRadius = base;
	}

	public double getRadius() {
		return baseRadius;
	}

	public void printIndices() {
		System.out.println("[ " + indexOfRow + ", " + indexOfCol + " ]");
	}

	public void printLocation() {
		if (location == null) {
			System.out.println("PU has no location information");
			return;
		}
		location.printLocation();
		// location.printLocationIndex();
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