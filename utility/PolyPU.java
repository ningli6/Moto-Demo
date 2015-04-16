package utility;

import java.util.HashMap;

/*
 * It include a hashmap that maps location to transmit power
 */
public class PolyPU {
	private PU pu;
	private int sides;
	private Location location;
	private int center_indexRow;
	private int center_indexCol;
	private GridMap map;
	private Polygon polygon;

	private HashMap hashmap<Location, double>();

	public PolyPU(PU pu, int sides) {
		if (sides < 3) throw new IllegalArgumentException("Number of sides must be at least 3"); 
		this.pu = pu;
		this.sides = sides;
		center_indexRow = pu.getRowIndex();
		center_indexCol = pu.getColIndex();
		location = pu.getLocation();
		map = pu.getServer().getMap();
		hashmap = new HashMap<Location, double>();
		transfigure(MTP.d1, MTP.P_0);
		transfigure(MTP.d2, MTP.P_50);
		transfigure(MTP.d3, MTP.P_75);
	}

	private void transfigure(double r, double power) {
		if (r < 0) throw new IllegalArgumentException();
		double cake = 360 / sides;
		Point[] vertexes = new Point[sides];
		double R = r / Math.cos(Math.toRadians(cake / 2));
		for (int i = 0; i < sides; i++) {
			double angle = Math.toRadians(cake * i + cake / 2);
			double x = R * Math.sin(angle);
			double y = R * Math.cos(angle);
			vertexes[i] = new Point(x, y);
		}
		polygon = new Polygon(vertexes);
		int updateRadius = (int) Math.round(r / getAveDist());
		int startRow = center_indexRow - 3 * updateRadius;
		if (startRow < 0) startRow = 0;
		int startCol = center_indexCol - 3 * updateRadius;
		if (startCol < 0) startCol = 0;
		int endRow = center_indexRow + 3 * updateRadius;
		if (endRow >= map.getRows()) endRow = map.getRows();
		int endCol = center_indexCol + 3 * updateRadius;
		if (endCol >= map.getCols()) endCol = map.getCols();
		for (int i = startRow; i < endRow; i++) {
			for (int j = startCol; j < endCol; j++) {
				if (polygon.inPolygon(indexTodist(i, j)) && !hashmap.containsKey())
			}
		}
	}

	private Point indexTodist(int i, int j) {
		Location loc = map.getLocation(i, j);
		double dist = loc.distTo(this.location);
		// compute bearing, then return coordinate
		double bear = Math.toRadians(bearing(this.location, loc));
		return new Point(dist * Math.sin(bear), dist * Math.cos(bear));
	}

	/*
	 *	Formula: θ = atan2( sin Δλ ⋅ cos φ2 , cos φ1 ⋅ sin φ2 − sin φ1 ⋅ cos φ2 ⋅ cos Δλ )
	 *  φ is latitude, λ is longitude, R is earth’s radius
	 *	JavaScript: (all angles in radians)
	 *	var y = Math.sin(λ2-λ1) * Math.cos(φ2);
	 *	var x = Math.cos(φ1)*Math.sin(φ2) - Math.sin(φ1)*Math.cos(φ2)*Math.cos(λ2-λ1);
	 *	var brng = Math.atan2(y, x).toDegrees();
	 */
	public static double bearing(Location s, Location d) {
		if (s == null || d == null) throw new NullPointerException();
		double lat1 = s.getLatitude();
		double long1 = s.getLongitude();
		double lat2 = d.getLatitude();
		double long2 = d.getLongitude();
		double y = Math.sin(Math.toRadians(long2 - long1)) * Math.cos(Math.toRadians(lat2));
		double x = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) - Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(long2-long1));
		return Math.toDegrees(Math.atan2(y, x));
	}

	private double getAveDist() {
		if (map == null) return 0;
		return map.getAverageDistance();
	}

	public double response(Location loc) {
		// this may not work!
		if (map.containsKey(loc)) return hashmap.get(loc);
		return MTP.P_100;
	}

	public PU getPU() {
		return pu;
	}
}