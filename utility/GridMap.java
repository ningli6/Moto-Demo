package utility;
/*
 * GridMap class represent a grid map, with lenght, height and cell size
 * Number of number_of_rows and columns are computed then
 * Note that the origin is the upper left cell
 */
public class GridMap {
	public static double epsilon = 0.05; // in degree
	// size of cells in degrees
	public static double cellDegree;
	// location of four coordinates
	private Location upperLeft;
	private Location upperRight;
	private Location lowerLeft;
	private Location lowerRight;
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
	private int number_of_rows = -1;
	// number of columns
	private int number_of_cols = -1;
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
		upperLeft = ul;
		upperRight = ur;
		lowerLeft = ll;
		lowerRight = lr;
		cellDegree = cd;
		number_of_rows = (int) (Math.abs(upperLeft.getLatitude() - lowerLeft.getLatitude()) / cellDegree);
		number_of_cols = (int) (Math.abs(upperLeft.getLongitude() - upperRight.getLongitude()) / cellDegree);
		if (number_of_rows <= 0 || number_of_cols <= 0) throw new IllegalArgumentException();
		// works for north hemisphere
		upperRight.setLocation(upperLeft.getLatitude(), upperLeft.getLongitude() + cellDegree * number_of_cols);
		lowerLeft.setLocation(upperLeft.getLatitude() - cellDegree * number_of_rows, upperLeft.getLongitude());
		lowerRight.setLocation(upperLeft.getLatitude() - cellDegree * number_of_rows, upperLeft.getLongitude() + cellDegree * number_of_cols);
		length = upperLeft.distTo(upperRight);
		height = upperLeft.distTo(lowerLeft);
		averDist = (length + height) / (number_of_rows + number_of_cols);
		upperLat = getUpperBoundary();
		lowerLat = getLowerBounday();
		leftLon = getLeftBoundary();
		rightLon = getRightBoundary();
	}

	public GridMap(GridMap map) {
		if (map == null) throw new NullPointerException();
		upperLeft = map.getUpperLeft();
		upperRight = map.getUpperRight();
		lowerLeft = map.getLowerLeft();
		lowerRight = map.getLowerRight();
		cellDegree = map.getCellDegree();
		number_of_rows = map.getRows();
		number_of_cols = map.getCols();
		length = map.getLength();
		height = map.getHeight();
		averDist = map.getAverageDistance();
		upperLat = map.getUpperBoundary();
		lowerLat = map.getLowerBounday();
		leftLon = map.getLeftBoundary();
		rightLon = map.getRightBoundary();
	}

	public double getLength() {
		return length;
	}

	public double getHeight() {
		return height;
	}
	
	// number of number_of_rows for the map
	public int getRows() {
		return number_of_rows;
	}
	
	// number of number_of_cols for the map
	public int getCols() {
		return number_of_cols;
	}
	
	/**
	 * Return total number of cells
	 */
	public long getNumberOfCells() {
		return number_of_rows * number_of_cols;
	}

	public double getCellDegree() {
		return cellDegree;
	}

	public double getAverageDistance() {
		return averDist;
	}

	public Location getUpperLeft() {
		return upperLeft;
	}

	public Location getUpperRight() {
		return upperRight;
	}

	public Location getLowerLeft() {
		return lowerLeft;
	}

	public Location getLowerRight() {
		return lowerRight;
	}
	// check if the location is in the range of grid map
	public boolean withInBoundary(Location location) {
		if (location == null) return false;
		double lat = location.getLatitude();
		double lon = location.getLongitude();
		if (lat < getLowerBounday() || lat > getUpperBoundary()) return false;
		if (lon < getLeftBoundary() || lon > getRightBoundary()) return false;
		return true;
	}

	// check if the location is in the range of grid map
	public boolean withInBoundary(double lat, double lon) {
		if (lat < getLowerBounday() || lat > getUpperBoundary()) return false;
		if (lon < getLeftBoundary() || lon > getRightBoundary()) return false;
		return true;
	}

	public double getLeftBoundary() {
		return upperLeft.getLongitude();
	}

	public double getRightBoundary() {
		return upperRight.getLongitude();
	}

	public double getUpperBoundary() {
		return upperLeft.getLatitude();
	}

	public double getLowerBounday() {
		return lowerLeft.getLatitude();
	}
	// print four corners of location
	public void showBoundary() {
		System.out.println("Map boundary: ");
		upperLeft.printLocation();
		upperRight.printLocation();
		lowerLeft.printLocation();
		lowerRight.printLocation();
	}
	
	// get lat & lon according to given number_of_rows & number_of_cols index
	public double RowToLat(int r) {
		if (r < 0 || r >= number_of_rows) throw new IllegalArgumentException();
		double newLat = upperLat - 0.5 * cellDegree - r * cellDegree;
		if (newLat < getLowerBounday() || newLat > getUpperBoundary()) throw new coordinatesOutOfBoundsException();
		return newLat;
	}

	public double ColToLon(int c) {
		if (c < 0 || c >= number_of_cols) throw new IndexOutOfBoundsException();
		double newLon = leftLon + 0.5 * cellDegree + c * cellDegree;
		if (newLon < getLeftBoundary() || newLon > getRightBoundary()) throw new coordinatesOutOfBoundsException();
		return newLon;
	}

	// convert latitude to the index of row where the coordinate belongs 
	public int LatToRow(double lat) {
		if (lat < getLowerBounday() || lat > getUpperBoundary()) throw new IllegalArgumentException("Latitude is out of map range");
		int rowIndex = (int) (Math.abs(upperLeft.getLatitude() - lat) / cellDegree);
		if (rowIndex >= number_of_rows) throw new IndexOutOfBoundsException();
		return rowIndex;
	}

	// convert longitude to the index of col where the coordinate belongs 
	public int LonToCol(double lon) {
		if (lon < getLeftBoundary() || lon > getRightBoundary()) throw new IllegalArgumentException("Longitude is out of map range");
		int colIndex = (int) (Math.abs(upperLeft.getLongitude() - lon) / cellDegree);
		if (colIndex >= number_of_cols) throw new IndexOutOfBoundsException();
		return colIndex;
	}

	public Location getLocation(int r, int c) {
		if (r < 0 || r >= number_of_rows || c < 0 || c >= number_of_cols) throw new IndexOutOfBoundsException();
		return new Location(upperLat - 0.5 * cellDegree - r * cellDegree, 
			leftLon + 0.5 * cellDegree + c * cellDegree);
	}
}