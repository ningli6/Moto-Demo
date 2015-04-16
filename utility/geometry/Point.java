package utility.geometry;

public class Point {
	public double x, y;

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

	public static void main(String[] args) {
		Point a = new Point(3, 5);
		Point b = new Point(3, 5);
		System.out.println(a.SamePoint(b));
	}
}