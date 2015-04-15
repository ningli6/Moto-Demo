package utility;

import java.util.HashMap;

/*
 * This class represents the polygon contour for a PU
 * It include a hashmap that maps location to transmit power
 */
public class Polygon {
	private PU pu;
	private int sides;
	private Location location;
	private int center_indexRow;
	private int center_indexCol;
	// a random offset used to change orientations of polygon
	private double offset;
	private Point[] vertex;
	private Line[] borders;

	private HashMap map<Location, double>();

	public Polygon(PU pu, int sides) {
		this.pu = pu;
		this.sides = sides;
		center_indexRow = pu.getRowIndex();
		center_indexCol = pu.getColIndex();
		location = pu.getLocation();
		offset = 0;
		transfigure(MTP.d1);
		transfigure(MTP.d2);
		transfigure(MTP.d3);
	}

	private void transfigure(double r) {

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