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
	// a random offset used to change orientations of polygon
	private double offset;
	private Polygon polygon;

	private HashMap hashmap<Location, double>();

	public PolyPU(PU pu, int sides) {
		this.pu = pu;
		this.sides = sides;
		center_indexRow = pu.getRowIndex();
		center_indexCol = pu.getColIndex();
		location = pu.getLocation();
		map = pu.getServer().getMap();
		hashmap = new HashMap<Location, double>();
		offset = 0;
		transfigure(MTP.d1);
		transfigure(MTP.d2);
		transfigure(MTP.d3);
	}

	private void transfigure(double r) {
		double cake = 360 / r;
		Point[] vertexes = new Point[sides];
		for (int i = 0; i < sides; i++) {
			double rad = Math.toRadians(cake * i + offset);
			double x = r * Math.sin(rad);
			double y = r * Math.cos(rad);
			vertexes[i] = new Point(x, y);
		}
		polygon = new Polygon(vertexes);
	}

	private Point indexTodist(int i, int j) {
		Location loc = map.getLocation(i, j);
		double dist = loc.distTo(this.location);
		// compute bearing, then return coordinate
	}

	private double getAveDist() {
		if (map == null) return 0;
		return map.getAverageDistance();
	}

	public double response(Location loc) {
		// this may not work!
		if (map.containsKey(loc)) return map.get(loc);
		return MTP.P_100;
	}

	public PU getPU() {
		return pu;
	}
}