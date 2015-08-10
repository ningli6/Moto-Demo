package utility.geometry;

public class Point {
	public double x, y;

	/**
	 * Construct Point
	 * @param x   coordinate of x
	 * @param y   coordinate of y
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public boolean SamePoint(Point p) {
		if (this.x == p.x && this.y == p.y) return true;
		return false;
	}

	public void printPoint() {
		System.out.println("[" + x + ", " + y + "]");
	}
}