package utility.geometry;

public class Line {
	// ax + by + c = 0
	public double a, b, c;
	public double k;

	/**
	 * Construct a line from two points, they can't be the same point
	 * @param a   point a
	 * @param b   point b
	 */
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

	/**
	 * Determine if a point is on the line
	 * @param a  point
	 * @return   true if point is on the line
	 */
	public boolean online(Point a) {
		double delta = 0.001;
		return Math.abs(this.a * a.x + this.b * a.y + this.c) < delta;
	}

	/**
	 * Determine if point a and point b are on the same side of line
	 * @param a  point a
	 * @param b  point b
	 * @return   true if point a, b are on the same side
	 */
	public boolean sameSide(Point a, Point b) {
		if (a.SamePoint(b)) return true;
		// if some point is on the line, return true
		return (this.a * a.x + this.b * a.y + this.c) * (this.a * b.x + this.b * b.y + this.c) >= 0;
	}

	public void print() {
		System.out.println("a: " + a + " b: " + b + " c: " + c + " k: " + k);
	}
}