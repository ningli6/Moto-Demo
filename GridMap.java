/*
 * GridMap class represent a grid map, with lenght, height and cell size
 * Number of rows and columns are computed then
 * Note that the origin is the upper left cell
 */
public class GridMap {
	public static double epsilon = 0.05; // in degree
	// location of four coordinates
	private Location upperLeft;
	private Location upperRight;
	private Location lowerLeft;
	private Location lowerRight;
	// length of the grid
	private double length = -1;
	// height of the grid
	private double height = -1;
	// number of rows
	private int rows = -1;
	// number of columns
	private int cols = -1;
	// size of cells in degrees
	private double cellDegree;
	// average distance between each cell
	private double averDist;

	// initialize map using four coordinates and cell degree
	public GridMap(Location ul, Location ur, Location ll, Location lr, double cd) {
		// expect user's input is a rectangle area
		if (Math.abs(ul.getLatitude() - ur.getLatitude()) > epsilon) throw new IllegalArgumentException();
		if (Math.abs(ll.getLatitude() - lr.getLatitude()) > epsilon) throw new IllegalArgumentException();
		if (Math.abs(ul.getLongitude() - ll.getLongitude()) > epsilon) throw new IllegalArgumentException();
		if (Math.abs(ur.getLongitude() - lr.getLongitude()) > epsilon) throw new IllegalArgumentException();
		upperLeft = ul;
		upperRight = ur;
		lowerLeft = ll;
		lowerRight = lr;
		this.cellDegree = cd;
		rows = (int) (Math.abs(upperLeft.getLatitude() - lowerLeft.getLatitude()) / cellDegree);
		cols = (int) (Math.abs(upperLeft.getLongitude() - upperRight.getLongitude()) / cellDegree);
		if (rows <= 0 || cols <= 0) throw new IllegalArgumentException();
		// works for north hemisphere
		upperRight.setLocation(upperLeft.getLatitude(), upperLeft.getLongitude() + cellDegree * cols);
		lowerLeft.setLocation(upperLeft.getLatitude() - cellDegree * rows, upperLeft.getLongitude());
		lowerRight.setLocation(upperLeft.getLatitude() - cellDegree * rows, upperLeft.getLongitude() + cellDegree * cols);
		length = upperLeft.distTo(upperRight);
		height = upperLeft.distTo(lowerLeft);
		averDist = (length + height) / (rows + cols);
	}

	public double getLength() {
		return length;
	}

	public double getHeight() {
		return height;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
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
		double lat = location.getLatitude();
		double lon = location.getLongitude();
		if (lat < getLowerBounday() || lat > getUpperBoundary()) return false;
		if (lon < getLeftBoundary() || lat > getRightBoundary()) return false;
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

	public void showBoundary() {
		System.out.println("Map boundary: ");
		upperLeft.printLocation();
		upperRight.printLocation();
		lowerLeft.printLocation();
		lowerRight.printLocation();
	}
}