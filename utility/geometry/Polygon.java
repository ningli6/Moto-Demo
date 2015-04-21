package utility.geometry;

import java.util.Scanner;
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

	public void printPoly() {
		for (Point p : vertexes)
			System.out.println("[" + p.x + ", " + p.y + "]");
	}

	public static void main(String[] args) {
		int sides = 4;
		double r = 10; // km
		double cake = 360 / sides;
		Point[] vertexes = new Point[sides];
		double R = r / Math.cos(Math.toRadians(cake / 2));
		// System.out.println("R: " + R);
		// incorrect
		for (int i = 0; i < sides; i++) {
			double angle = Math.toRadians(cake * i + cake / 2);
			double x = R * Math.sin(angle);
			double y = R * Math.cos(angle);
			vertexes[i] = new Point(x, y);
			// System.out.print(i + ": ");
			// vertexes[i].printPoint();
		}
		Polygon poly = new Polygon(vertexes);
		poly.printPoly();
		Scanner sc = new Scanner(System.in);
		double x = 0, y = 0;
		int count = 0;
		while(sc.hasNextDouble()) {
			if (count == 0) {
				x = sc.nextDouble();
				count++;
				continue;
			}
			y = sc.nextDouble();
			System.out.println(poly.inPolygon(new Point(x, y)));
			count = 0;
		}
	}
}