package utility.geometry;

public class Line {
	// ax + by + c = 0
	public double a, b, c;
	public double k;

	public Line(Point a, Point b) {
		if (a.x == b.x && a.y == b.y) throw new IllegalArgumentException();
		else if (a.x == b.x) {
			this.a = 1;
			this.c = -1 * a.x;
			this.b = 0;
			this.k = Double.POSITIVE_INFINITY;
		}
		else {
			this.k = (b.y - a.y) / (b.x - a.x);
			double intersect = a.y - k * a.x;
			this.a = k;
			this.b = -1;
			this.c = intersect;
		}
	}

	public boolean online(Point a) {
		return this.a * a.x + this.b * a.y + this.c == 0;
	}

	public boolean sameSide(Point a, Point b) {
		if (a.x == b.x && a.y == b.y) return true;
		// if some point is on the line, return true
		return (this.a * a.x + this.b * a.y + this.c) * (this.a * b.x + this.b * b.y + this.c) >= 0;
	}

	public void print() {
		System.out.println("a: " + a + " b: " + b + " c: " + c + " k: " + k);
	}

	public static void main(String[] args) {
		Point a = new Point(2, 2);
		Point b = new Point(6, 6);
		Line l = new Line(a, b);
		System.out.println(l.online(new Point(1, 5)));
	}
}