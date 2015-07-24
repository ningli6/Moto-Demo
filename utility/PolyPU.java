package utility;

import java.util.HashMap;

import utility.geometry.Point;
import utility.geometry.Polygon;

/*
 * A pu class that has polygon contours
 * It include a hashmap that maps location to transmit power
 */
public class PolyPU {
	private PU pu;                  // instance of primary user
	private int sides;              // number of sides
	private Location location;      // reference of location of pu
	private int indexOfRow;         // row index
	private int indexOfCol;         // col index
	private GridMap map;            // reference of map
	private HashMap<Long, Double> hashmap;    // associate map coordinates with transmit power at that location

	public PolyPU(PU pu, int sides) {
		if (sides < 3) throw new IllegalArgumentException("Number of sides must be at least 3"); 
		this.pu = pu;
		this.sides = sides;
		indexOfRow = pu.getRowIndex();
		indexOfCol = pu.getColIndex();
		location = pu.getLocation();
		map = pu.getServer().getMap();
		hashmap = new HashMap<Long, Double>();
		transfigure(MTP.d1, MTP.P_0);   // ( 8 km,    0)
		transfigure(MTP.d2, MTP.P_50);  // (14 km,  0.5)
		transfigure(MTP.d3, MTP.P_75);  // (25 km, 0.75)
	}

	/**
	 * Precompute transmit power within radius of r
	 * The protection zone will be transformed from circle to polygon
	 * @param r          radius of protection zone
	 * @param power      transmit power that will be returned within that protection zone
	 */
	private void transfigure(double r, double power) {
		if (r < 0) throw new IllegalArgumentException();
		double cake = 360 / sides;  // use a regular polygon, each side has 360/sides in degree
		Point[] vertexes = new Point[sides];
		double R = r / Math.cos(Math.toRadians(cake / 2)); // distance from center of polygon to the vertex
		for (int i = 0; i < sides; i++) {  // find vertices
			double angle = Math.toRadians(cake * i + cake / 2);
			double x = R * Math.sin(angle);
			double y = R * Math.cos(angle);
			vertexes[i] = new Point(x, y);
		}
		Polygon polygon = new Polygon(vertexes); // construct a polygon with vertexes, center is the location of pu
		int updateRadius = (int) Math.round(r / getAveDist());
		int startRow = indexOfRow - 3 * updateRadius;
		if (startRow < 0) startRow = 0;
		int startCol = indexOfCol - 3 * updateRadius;
		if (startCol < 0) startCol = 0;
		int endRow = indexOfRow + 3 * updateRadius;
		if (endRow > map.getRows()) endRow = map.getRows();
		int endCol = indexOfCol + 3 * updateRadius;
		if (endCol > map.getCols()) endCol = map.getCols();
		for (int i = startRow; i < endRow; i++) {
			for (int j = startCol; j < endCol; j++) {
				long code = hashcode(i, j);
				if (polygon.inPolygon(indexTodist(i, j)) && !hashmap.containsKey(code))
					hashmap.put(code, power);
			}
		}
	}

	/**
	 * Construct a point with the reference of pu's location
	 * @param i  row index
	 * @param j  col index
	 * @return  a point object with the center of pu's location
	 */
	private Point indexTodist(int i, int j) {
		Location loc = map.getLocation(i, j);    // get location of cell(i, j)
		double dist = loc.distTo(this.location); // distance between cell(i, j) to the cell where pu stands
		// compute bearing, then return coordinate
		double bear = Math.toRadians(bearing(this.location, loc));
		return new Point(dist * Math.sin(bear), dist * Math.cos(bear));
	}


	/**
	 * Compute initialize bearing between from location s to location d
	 * @param s     start location
	 * @param d     destination location
	 * @return      bearing in degrees
	 */
	private double bearing(Location s, Location d) {
		if (s == null || d == null) throw new NullPointerException();
		double lat1 = s.getLatitude();
		double long1 = s.getLongitude();
		double lat2 = d.getLatitude();
		double long2 = d.getLongitude();
		double y = Math.sin(Math.toRadians(long2 - long1)) * Math.cos(Math.toRadians(lat2));
		double x = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) - Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(long2-long1));
		return Math.toDegrees(Math.atan2(y, x));
	}

	/**
	 * Get average cell size in meters from map instance
	 * @return  average cell size in meters, 0 if referenced map is null
	 */
	private double getAveDist() {
		if (map == null) return 0;
		return map.getAverageDistance();
	}

	/**
	 * Transmit power available at location (i, j) on map
	 * @param i  row index
	 * @param j  col index
	 * @return   transmit power
	 */
	public double response(int i, int j) {
		if (i < 0 || j < 0 || i >= map.getRows() || j >= map.getCols()) throw new IndexOutOfBoundsException();
		long code = hashcode(i, j);
		if (hashmap.containsKey(code)) {
			return hashmap.get(code);
		}
		return MTP.P_100;
	}

	public PU getPU() {
		return pu;
	}

	/**
	 * Return a distinct int value from index i and j
	 * @param i    row index
	 * @param j    col index
	 * @return     hash value
	 */
	public long hashcode(int i, int j) {
		return map.getCols() * i + j;
	}
}
