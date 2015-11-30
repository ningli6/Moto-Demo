package utility;
/*
 * GridMap class represent a grid map, with length, height and cell size
 * Number of number_of_rows and columns are computed then
 * Note that the origin is the upper left cell
 */
public class GridMap {
	public static double epsilon = 0.05; // in degree
	// size of cells in degrees
	private final double cellDegree;
	// lat & lon coordinate boundary
	private double northLat;
	private double southLat;
	private double westLng;
	private double eastLng;
	// length of the map
	private double length;
	// height of the map
	private double height;
	// number of rows
	protected int numberOfRows;
	// number of columns
	protected int numberOfCols;
	// average distance between each cell
	protected double averDist;

	/**
	 * Initialize map using four coordinates and cell degree
	 * Works for north hemisphere
	 * @param nw north west location
	 * @param se south east location
	 * @param cd cell degree
	 */
	public GridMap(Location nw, Location se, double cd) {
		if (cd <= 0) throw new IllegalArgumentException("Cell size defined in degree must be positive");
		cellDegree = cd;
		northLat = nw.getLatitude();
		southLat = se.getLatitude();
		westLng = nw.getLongitude();
		eastLng = se.getLongitude();
		numberOfRows = (int) ((northLat - southLat) / cellDegree);
		numberOfCols = (int) ((eastLng - westLng) / cellDegree);
		if (numberOfRows <= 0 || numberOfCols <= 0) throw new IllegalArgumentException();
		southLat = northLat - cellDegree * numberOfRows;
		eastLng = westLng + cellDegree * numberOfCols;
		Location nw_loc = new Location(northLat, westLng);
		Location ne_loc = new Location(northLat, eastLng);
		length = nw_loc.distTo(ne_loc);
		Location sw_loc = new Location(northLat, westLng);
		Location se_loc = new Location(southLat, westLng);
		height = sw_loc.distTo(se_loc);
		averDist = (length + height) / (numberOfRows + numberOfCols);
	}

	/**
	 * Construct a GridMap from another map
	 */
	public GridMap(GridMap map) {
		if (map == null) throw new NullPointerException();
		cellDegree = map.getCellDegree();
		numberOfRows = map.getNumOfRows();
		numberOfCols = map.getNumOfCols();
		northLat = map.getNorthLat();
		southLat = map.getSouthLat();
		westLng = map.getWestLng();
		eastLng = map.getEastLng();
		length = map.getLength();
		height = map.getHeight();
		averDist = map.getAverageDistance();
	}
	
	/**
	 * Get north latitude of the map
	 */
	public double getNorthLat() {
		return northLat;
	}

	/**
	 * Get south latitude of the map
	 */
	public double getSouthLat() {
		return southLat;
	}

	/**
	 * Get west longitude of the map
	 */
	public double getWestLng() {
		return westLng;
	}

	/**
	 * Get east longitude of the map
	 */
	public double getEastLng() {
		return eastLng;
	}
	
	/**
	 * Get number of rows
	 */
	public int getNumOfRows() {
		return numberOfRows;
	}
	
	/**
	 * Get number of columns
	 */
	public int getNumOfCols() {
		return numberOfCols;
	}

	/**
	 * Get cell size
	 */
	public double getCellDegree() {
		return cellDegree;
	}
	
	/**
	 * Get length of the map in km
	 */
	public double getLength() {
		return length;
	}

	/**
	 * Get height of the map in km
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Get average distance of a single cell in meters
	 */
	public double getAverageDistance() {
		return averDist;
	}

	/** 
	 * Check if the location is in the range of grid map
	 */
	public boolean withInBoundary(Location location) {
		if (location == null) return false;
		double lat = location.getLatitude();
		double lon = location.getLongitude();
		if (lat < southLat || lat > northLat) return false;
		if (lon < westLng || lon > eastLng) return false;
		return true;
	}
	
	/**
	 * Get latitude from row index
	 */
	public double rowToLat(int r) {
		if (r < 0 || r >= numberOfRows) throw new IndexOutOfBoundsException();
		double newLat = northLat - 0.5 * cellDegree - r * cellDegree;
		if (newLat < southLat || newLat > northLat) throw new IllegalArgumentException();
		return newLat;
	}
	
	/**
	 * Get longitude from column index
	 */
	public double colToLng(int c) {
		if (c < 0 || c >= numberOfCols) throw new IndexOutOfBoundsException();
		double newLon = westLng + 0.5 * cellDegree + c * cellDegree;
		if (newLon < westLng || newLon > eastLng) throw new IllegalArgumentException();
		return newLon;
	}

	/**
	 * Get row index from latitude
	 */
	public int latToRow(double lat) {
		if (lat < southLat || lat > northLat) throw new IllegalArgumentException("Latitude is out of map range");
		int rowIndex = (int) ((northLat - lat) / cellDegree);
		if (rowIndex >= numberOfRows) throw new IndexOutOfBoundsException();
		return rowIndex;
	}

	/**
	 * Get column index from longitude
	 */
	public int lngToCol(double lon) {
		if (lon < westLng || lon > eastLng) throw new IllegalArgumentException("Longitude is out of map range");
		int colIndex = (int) ((lon - westLng) / cellDegree);
		if (colIndex >= numberOfCols) throw new IndexOutOfBoundsException();
		return colIndex;
	}

	/**
	 * Given map indices of rows and cols, return location of the center of that cell
	 * @param r   index of row
	 * @param c   index of col
	 * @return    location of the center of cell(i, j)
	 */
	public Location getLocation(int r, int c) {
		if (r < 0 || r >= numberOfRows || c < 0 || c >= numberOfCols) throw new IndexOutOfBoundsException();
		return new Location(rowToLat(r), colToLng(c));
	}
}