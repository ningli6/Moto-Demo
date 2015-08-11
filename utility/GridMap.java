package utility;
/*
 * GridMap class represent a grid map, with length, height and cell size
 * Number of number_of_rows and columns are computed then
 * Note that the origin is the upper left cell
 */
public class GridMap {
	public static double epsilon = 0.05; // in degree
	// size of cells in degrees
	public static double cellDegree;
	// location of four coordinates
	private Location topLeft;
	private Location topRight;
	private Location botLeft;
	private Location botRight;
	// lat & lon coordinate boundary
	private double upperLat;
	@SuppressWarnings("unused")
	private double lowerLat;
	private double leftLon;
	@SuppressWarnings("unused")
	private double rightLon;
	// length of the map
	private double length = -1;
	// height of the map
	private double height = -1;
	// number of rows
	private int numberOfRows = -1;
	// number of columns
	private int numberOfCols = -1;
	// average distance between each cell
	private double averDist;

	public class coordinatesOutOfBoundsException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public coordinatesOutOfBoundsException(String message) {
			super(message);
		}
		public coordinatesOutOfBoundsException() {
			super();
		}
	}

	// initialize map using four coordinates and cell degree
	public GridMap(Location ul, Location ur, Location ll, Location lr, double cd) {
		if (cd <= 0) throw new IllegalArgumentException("Cell size defined in degree must be positive");
		// expect user's input is a rectangle area
		if (Math.abs(ul.getLatitude() - ur.getLatitude()) > epsilon) throw new IllegalArgumentException("Rectangular area expected");
		if (Math.abs(ll.getLatitude() - lr.getLatitude()) > epsilon) throw new IllegalArgumentException("Rectangular area expected");
		if (Math.abs(ul.getLongitude() - ll.getLongitude()) > epsilon) throw new IllegalArgumentException("Rectangular area expected");
		if (Math.abs(ur.getLongitude() - lr.getLongitude()) > epsilon) throw new IllegalArgumentException("Rectangular area expected");
		topLeft = ul;
		topRight = ur;
		botLeft = ll;
		botRight = lr;
		cellDegree = cd;
		numberOfRows = (int) (Math.abs(topLeft.getLatitude() - botLeft.getLatitude()) / cellDegree);
		numberOfCols = (int) (Math.abs(topLeft.getLongitude() - topRight.getLongitude()) / cellDegree);
		if (numberOfRows <= 0 || numberOfCols <= 0) throw new IllegalArgumentException();
		// works for north hemisphere
		topRight.setLocation(topLeft.getLatitude(), topLeft.getLongitude() + cellDegree * numberOfCols);
		botLeft.setLocation(topLeft.getLatitude() - cellDegree * numberOfRows, topLeft.getLongitude());
		botRight.setLocation(topLeft.getLatitude() - cellDegree * numberOfRows, topLeft.getLongitude() + cellDegree * numberOfCols);
		length = topLeft.distTo(topRight);
		height = topLeft.distTo(botLeft);
		averDist = (length + height) / (numberOfRows + numberOfCols);
		upperLat = getTopBound();
		lowerLat = getBotBound();
		leftLon = getWestBound();
		rightLon = getEastBound();
	}

	public GridMap(GridMap map) {
		if (map == null) throw new NullPointerException();
		topLeft = map.getTopLeftCorner();
		topRight = map.getTopRightCorner();
		botLeft = map.getBotLeftCorner();
		botRight = map.getBotRightCorner();
		cellDegree = map.getCellDegree();
		numberOfRows = map.getRows();
		numberOfCols = map.getCols();
		length = map.getLength();
		height = map.getHeight();
		averDist = map.getAverageDistance();
		upperLat = map.getTopBound();
		lowerLat = map.getBotBound();
		leftLon = map.getWestBound();
		rightLon = map.getEastBound();
	}

	public double getLength() {
		return length;
	}

	public double getHeight() {
		return height;
	}
	
	// number of number_of_rows for the map
	public int getRows() {
		return numberOfRows;
	}
	
	/**
	 * Get number of columns
	 */
	public int getCols() {
		return numberOfCols;
	}

	public double getCellDegree() {
		return cellDegree;
	}

	/**
	 * Get average distance of a single cell in meters
	 * @return average cell size in meters
	 */
	public double getAverageDistance() {
		return averDist;
	}

	public Location getTopLeftCorner() {
		return topLeft;
	}

	public Location getTopRightCorner() {
		return topRight;
	}

	public Location getBotLeftCorner() {
		return botLeft;
	}

	public Location getBotRightCorner() {
		return botRight;
	}
	// check if the location is in the range of grid map
	public boolean withInBoundary(Location location) {
		if (location == null) return false;
		double lat = location.getLatitude();
		double lon = location.getLongitude();
		if (lat < getBotBound() || lat > getTopBound()) return false;
		if (lon < getWestBound() || lon > getEastBound()) return false;
		return true;
	}

	public double getWestBound() {
		return topLeft.getLongitude();
	}

	public double getEastBound() {
		return topRight.getLongitude();
	}

	public double getTopBound() {
		return topLeft.getLatitude();
	}

	public double getBotBound() {
		return botLeft.getLatitude();
	}
	
	// print four corners of location
	public void showBoundary() {
		System.out.println("Map boundary: ");
		topLeft.printLocation();
		topRight.printLocation();
		botLeft.printLocation();
		botRight.printLocation();
	}
	
	// get lat & lon according to given number_of_rows & number_of_cols index
	public double rowToLat(int r) {
		if (r < 0 || r >= numberOfRows) throw new IllegalArgumentException();
		double newLat = upperLat - 0.5 * cellDegree - r * cellDegree;
		if (newLat < getBotBound() || newLat > getTopBound()) throw new coordinatesOutOfBoundsException();
		return newLat;
	}

	public double colToLng(int c) {
		if (c < 0 || c >= numberOfCols) throw new IndexOutOfBoundsException();
		double newLon = leftLon + 0.5 * cellDegree + c * cellDegree;
		if (newLon < getWestBound() || newLon > getEastBound()) throw new coordinatesOutOfBoundsException();
		return newLon;
	}

	// convert latitude to the index of row where the coordinate belongs 
	public int latToRow(double lat) {
		if (lat < getBotBound() || lat > getTopBound()) throw new IllegalArgumentException("Latitude is out of map range");
		int rowIndex = (int) (Math.abs(topLeft.getLatitude() - lat) / cellDegree);
		if (rowIndex >= numberOfRows) throw new IndexOutOfBoundsException();
		return rowIndex;
	}

	// convert longitude to the index of col where the coordinate belongs 
	public int lngToCol(double lon) {
		if (lon < getWestBound() || lon > getEastBound()) throw new IllegalArgumentException("Longitude is out of map range");
		int colIndex = (int) (Math.abs(topLeft.getLongitude() - lon) / cellDegree);
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