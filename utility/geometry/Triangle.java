package utility.geometry;

public class Triangle {
	private Point[] vertexes;
	private Line[] boders;

	public Triangle(Point a, Point b, Point c) {
		if (a == null || a == null || c == null) throw new NullPointerException();
		if (a == b || b == c || a ==c) throw new IllegalArgumentException("Two points are identical");
		if (a.SamePoint(b) || a.SamePoint(c) || b.SamePoint(c)) throw new IllegalArgumentException("Two points are identical");
		Line l0 = new Line(a, b);
		if (l0.online(c)) throw new IllegalArgumentException("Three points are on the same line");
		Line l1 = new Line(a, c);
		Line l2 = new Line(b, c);
		boders = new Line[3];
		vertexes = new Point[3];
		boders[0] = l0; boders[1] = l1; boders[2] = l2;
		vertexes[0] = a; vertexes[1] = b; vertexes[2] = c;
	}

	public boolean inTriangle(Point p) {
		if (p == null) return false;
		if (p.SamePoint(vertexes[0]) || p.SamePoint(vertexes[1]) || p.SamePoint(vertexes[2])) return true;
		Point ref = new Point(0, 0);
		if (p.SamePoint(ref)) return true;
		return boders[0].sameSide(ref, p) && boders[1].sameSide(ref, p) && boders[2].sameSide(ref, p);
	}

	public static void main(String[] args) {
		Triangle t = new Triangle(new Point(-5, -5), new Point(5, -5), new Point(0, 8));
		System.out.println(t.inTriangle(new Point(0, -5)));
		System.out.println(t.inTriangle(new Point(10, -5)));
	}
}