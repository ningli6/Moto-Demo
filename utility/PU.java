package utility;

import java.util.Comparator;
/*
 * PU represents a primary user, server keeps track of pus that were added to it. 
 * Right now it just has location information.
 */
public class PU {
	public final Comparator<PU> DIST_ORDER = new distOrder();
	private int id = -1;                      // pu id
	private int channelID = -1;               // channel id
	private Location cellLocation;            // location of pu, centered in the cell
	private int indexOfRow = 0;               // row index on the map
	private int indexOfCol = 0;               // col index on the map
	public int number_of_response = 0;       // record times being chosen to response
	private GridMap map;                      // map instance 
	private double baseRadius = 0;            // used by k-anonymity/k-clustering, protection contour for the group
	private Cluster cluster = null;           // used by k-clustering

	private class distOrder implements Comparator<PU> {
		public int compare(PU pu1, PU pu2) {
			double dist1 = distTo(pu1);
			double dist2 = distTo(pu2);
			if (dist1 == dist2) return 0;
			if (dist1 < dist2) return -1;
			return 1;
		}
	}
	
	/**
	 * Empty constructor
	 */
	public PU() {}
	
	/**
	 * Initialize primary user
	 * @param  id  [pu's id]
	 * @param  lat [lat]
	 * @param  lng [lng]
	 * @param  map [map that pu is working on]
	 */
	public PU(int id, double lat, double lng, GridMap map) {
		this.id = id;
		this.map = map;
		this.indexOfRow = map.latToRow(lat); // round down, 
		this.indexOfCol = map.lngToCol(lng); // round down, 
		this.cellLocation = map.getLocation(indexOfRow, indexOfCol);
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

	/**
	 * Get working channel id
	 * @return -1 if channel id is not specified
	 */
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
	
	/**
	 * Get the pu's reference of map
	 * @return pu's reference of map
	 */
	public GridMap getMap() {
		return this.map;
	}
	
	/**
	 * attach a map instance to pu
	 * @param map
	 */
	public void setMap(GridMap map) {
		this.map = map;
	}

	/**
	 * Set location of primary users given indices on the map
	 * Used to update virtual pu's location
	 * @param r  row index
	 * @param c  col index
	 */
	public void setLocation(int r, int c) {
		indexOfRow = r;
		indexOfCol = c;
		this.cellLocation = map.getLocation(indexOfRow, indexOfCol);
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

	/**
	 * used by k-anonymity and k-clustering to update protection radius for the group
	 * @param base protection radius for the group
	 */
	public void updateRadius(double base) {
		if (base < 0) throw new IllegalArgumentException();
		this.baseRadius = base;
	}
	
	/**
	 * Used by k-anonymity and k-clustering.
	 * Get the protection contour of group/cluster in meters
	 * @return protection radius of group/cluster
	 */
	public double getRadius() {
		return baseRadius;
	}

	/**
	 * Attach pu to cluster c
	 * @param c    cluster that pu belongs to
	 */
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

	public void printInfo() {
		System.out.println("***PU***");
		System.out.println("id: " + this.id);
		System.out.println("working channel: " + this.channelID);
		printIndices();
		System.out.println("updated " + this.number_of_response + " times");
		System.out.println();
	}

	/* used by k-anonymity and k-clustering */
	public void printVirtualPUInfo() {
		System.out.println("***Virtual PU***");
		System.out.println("Radius: " + getRadius());
		System.out.println("Working channel: " + this.channelID);
		printIndices();
		System.out.println("updated " + this.number_of_response + " times");
	}
}