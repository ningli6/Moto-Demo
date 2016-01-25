package utility.geometry;

/* This class represents the polygon contour for a PU 
 * The polygon must be a convex polygon and centerd with point(0, 0)
 */

public class Polygon {
	private Point[] vertexes;   // vertices
	private Line[] borders;     // borders
	private int sides;          // number of borders

	public Point[] getVertexes() {
		return vertexes;
	}

	public Line[] getBorders() {
		return borders;
	}

	public int getSides() {
		return sides;
	}

	public Polygon(Point[] p) {
		if (p == null || p.length == 0) throw new NullPointerException();
		sides = p.length;
		if (sides < 3) throw new IllegalArgumentException();
		borders = new Line[sides];
		vertexes = new Point[sides];
		for (int i = 0; i < sides; i++) vertexes[i] = p[i];
		for (int i = 0; i < sides; i++) borders[i] = new Line(vertexes[i], vertexes[(i + 1) % sides]);
	}

	/**
	 * Check if the given point is within the polygon
	 * Vertices are considered within polygon
	 * @param p   point
	 * @return    true if the given point is within the polygon
	 */
	public boolean inPolygon(Point p) {
		if (p == null) return false;
		for (Point v : vertexes)
			if (p.SamePoint(v)) return true;  // true if given point is the vertex
		Point ref = new Point(0, 0);          // (0, 0) must be center
		if (p.SamePoint(ref)) return true;    // true if given point is the center of polygon
		for (Line l : borders)                // true if given point is on the same side with center point
			if (!l.sameSide(ref, p)) return false;
		return true;
	}

	public void printPoly() {
		for (Point p : vertexes)
			System.out.println("[" + p.x + ", " + p.y + "]");
	}

//	public static void main(String[] args) {
//		int sides = 4;
//		double r = 10; // km
//		double cake = 360 / sides;
//		Point[] vertexes = new Point[sides];
//		double R = r / Math.cos(Math.toRadians(cake / 2));
//		// System.out.println("R: " + R);
//		// incorrect
//		for (int i = 0; i < sides; i++) {
//			double angle = Math.toRadians(cake * i + cake / 2);
//			double x = R * Math.sin(angle);
//			double y = R * Math.cos(angle);
//			vertexes[i] = new Point(x, y);
//			// System.out.print(i + ": ");
//			// vertexes[i].printPoint();
//		}
//		Polygon poly = new Polygon(vertexes);
//		poly.printPoly();
//		Scanner sc = new Scanner(System.in);
//		double x = 0, y = 0;
//		int count = 0;
//		while(sc.hasNextDouble()) {
//			if (count == 0) {
//				x = sc.nextDouble();
//				count++;
//				continue;
//			}
//			y = sc.nextDouble();
//			System.out.println(poly.inPolygon(new Point(x, y)));
//			count = 0;
//		}
//		sc.close();
//	}
}