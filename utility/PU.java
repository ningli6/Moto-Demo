package utility;

import java.util.Comparator;

import server.*;
/*
 * PU represents a primary user, server keeps track of pus that were added to it. 
 * Right now it just has location information.
 */
public class PU {
	public final Comparator<PU> DIST_ORDER = new distOrder(); // can this be static
	private int id = -1;                      // pu id
	private int channelID = -1;               // channel id
	// location
	private Location cellLocation;
	private int indexOfRow = 0;
	private int indexOfCol = 0;
	private int number_of_response = 0;       // record times being chosen to response
	private Server server;                    // server instance 
	/* used by k-anonymity */
	private double baseRadius = 0;
	/* used by k-clustering */
	private Cluster cluster = null;

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

	/**
	 * Initialize primary user
	 * @param  id  [pu's id]
	 * @param  lat [lat]
	 * @param  lng [lng]
	 * @param  map [map that pu is working on]
	 */
	public PU(int id, double lat, double lng, GridMap map) {
		this.id = id;
		this.indexOfRow = map.latToRow(lat);
		this.indexOfCol = map.lngToCol(lng);
		this.cellLocation = map.getLocation(indexOfRow, indexOfCol);
	}

	public void attachToServer(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public void setChannelID(int cid) {
		if (cid < 0) {
			System.out.println("Channel ID must be positive");
			return;
		}
		this.channelID = cid;
	}

	public int getChannelID() {
		if (channelID == -1) {
			System.out.println("Set channel ID first");
			return -1;
		}
		return channelID;
	}

	/**
	 * client will call this method if that pu is chosn to updated
	 */
	public void sendResponse() {
		number_of_response++;
	}

	/**
	 * clear number of response 
	 * used by additive noise 
	 */
	public void reset() {
		number_of_response = 0;
	}

	public void setIndices(int r, int c) {
		indexOfRow = r;
		indexOfCol = c;
		indexToLocation();
	}

	public int getRowIndex() {
		return indexOfRow;
	}

	public int getColIndex() {
		return indexOfCol;
	}

	public Location getLocation() {
		if (cellLocation == null) throw new NullPointerException("Location for PU has not yet been initialized");
		return cellLocation;
	}

	public double getLatitude() {
		return cellLocation.getLatitude();
	}

	public double getLongitude() {
		return cellLocation.getLongitude();
	}

	public double distTo(PU pu) {
		if (pu == null) throw new NullPointerException();
		if (pu == this) return 0;
		if (this.cellLocation == null) throw new NullPointerException("Location for PU has not yet been initialized");
		return this.cellLocation.distTo(pu.getLocation());
	}

	private void indexToLocation() {
		if (this.server == null) throw new NullPointerException("Attach to server first");
		this.cellLocation = this.server.getMap().getLocation(this.indexOfRow, this.indexOfCol);
	}

	/* used by k-anonymity and k-clustering */
	public void updateRadius(double base) {
		if (base < 0) throw new IllegalArgumentException();
		this.baseRadius = base;
	}
	/* used by k-anonymity and k-clustering */
	public double getRadius() {
		return baseRadius;
	}

	/* used by k-clustering */
	public void putInCluster(Cluster c) {
		this.cluster = c;
	}
	/* used by k-clustering */
	public boolean isInCluster() {
		return this.cluster != null;
	}
	/* used by k-clustering */
	public Cluster getCluster() {
		return this.cluster;
	}

	public void printIndices() {
		System.out.println("[ " + indexOfRow + ", " + indexOfCol + " ]");
	}

	public void printLocation() {
		if (cellLocation == null) {
			System.out.println("PU has no location information");
			return;
		}
		printIndexLocation();
		cellLocation.printLocation();
	}

	public void printIndexLocation() {
		System.out.println("[ " + getRowIndex() + " " + getColIndex() + " ]");
	}

	public void printInfo() {
		System.out.println("***PU***");
		System.out.println("id: " + this.id);
		System.out.println("working channel: " + this.channelID);
		printLocation();
		System.out.println("updated " + this.number_of_response + " times");
		System.out.println();
	}

	/* used by k-anonymity and k-clustering */
	public void printVirtualPUInfo() {
		System.out.println("***Virtual PU***");
		System.out.println("Radius: " + getRadius());
		System.out.println("Working channel: " + this.channelID);
		printLocation();
		System.out.println("updated " + this.number_of_response + " times");
	}
}