package geometry;
/* This class represents the polygon contour for a PU */

public class Polygon {
	private Point[] vertexes;
	private Line[] boders;
	private int sides;

	public Polygon(Point[] p) {
		if (p == null || p.length == 0) throw new NullPointerException();
		sides = p.length;
		if (sides < 3) throw new IllegalArgumentException();
		boders = new Line[sides];
		vertexes = new Point[sides];
		for (int i = 0; i < sides; i++) vertexes[i] = p[i];
		for (int i = 0; i < sides; i++) boders[i] = new Line(vertexes[i], vertexes[(i + 1) % sides]);
	}

	public boolean inPolygon(Point p) {
		if (p == null) return false;
		for (Point v : vertexes)
			if (p.SamePoint(v)) return true;
		Point ref = new Point(0, 0);
		if (p.SamePoint(ref)) return true;
		for (Line l : boders)
			if (!l.sameSide(ref, p)) return false;
		return true;
	}

	public static void main(String[] args) {
		Point[] p = new Point[4];
		p[0] = new Point(-5, -5);
		p[1] = new Point(5, -5);
		p[2] = new Point(5, 5);
		p[3] = new Point(-5, 5);
		Polygon t = new Polygon(p);
		System.out.println(t.inPolygon(new Point(5, 3)));
	}
}