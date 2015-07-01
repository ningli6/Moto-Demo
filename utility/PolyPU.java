package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.swing.JFrame;

import utility.geometry.Point;
import utility.geometry.Polygon;

/*
 * A pu class that has polygon contours
 * It include a hashmap that maps location to transmit power
 */
public class PolyPU {
	private PU pu;
	private int sides;
	private Location location;
	private int center_indexRow;
	private int center_indexCol;
	private GridMap map;
	private Polygon polygon;

	private HashMap<Integer, Double> hashmap;
	public static String directory = "/Users/ningli/Desktop/Project/output/";
	public PolyPU(PU pu, int sides) {
		if (sides < 3) throw new IllegalArgumentException("Number of sides must be at least 3"); 
		this.pu = pu;
		this.sides = sides;
		center_indexRow = pu.getRowIndex();
		center_indexCol = pu.getColIndex();
		location = pu.getLocation();
		map = pu.getServer().getMap();
		hashmap = new HashMap<Integer, Double>();
		transfigure(MTP.d1, MTP.P_0);
		transfigure(MTP.d2, MTP.P_50);
		transfigure(MTP.d3, MTP.P_75);
	}

	private void transfigure(double r, double power) {
		if (r < 0) throw new IllegalArgumentException();
		double cake = 360 / sides;
		Point[] vertexes = new Point[sides];
		double R = r / Math.cos(Math.toRadians(cake / 2));
		// System.out.println("R: " + R);
		for (int i = 0; i < sides; i++) {
			double angle = Math.toRadians(cake * i + cake / 2);
			double x = R * Math.sin(angle);
			double y = R * Math.cos(angle);
			vertexes[i] = new Point(x, y);
		}
		polygon = new Polygon(vertexes);
		// polygon.printPoly();
		// System.out.println();
		int updateRadius = (int) Math.round(r / getAveDist());
		int startRow = center_indexRow - 3 * updateRadius;
		if (startRow < 0) startRow = 0;
		int startCol = center_indexCol - 3 * updateRadius;
		if (startCol < 0) startCol = 0;
		int endRow = center_indexRow + 3 * updateRadius;
		if (endRow > map.getRows()) endRow = map.getRows();
		int endCol = center_indexCol + 3 * updateRadius;
		if (endCol > map.getCols()) endCol = map.getCols();
		for (int i = startRow; i < endRow; i++) {
			for (int j = startCol; j < endCol; j++) {
				// System.out.println("center: " + "[" + center_indexRow + ", " + center_indexCol + "]");
				// System.out.println("compute: " + "[" + i + ", " + j + "]");
				// indexTodist(i, j).printPoint();
				int code = hashcode(i, j);
				if (polygon.inPolygon(indexTodist(i, j)) && !hashmap.containsKey(code))
					hashmap.put(code, power);
			}
		}
	}

	private Point indexTodist(int i, int j) {
		Location loc = map.getLocation(i, j);
		double dist = loc.distTo(this.location);
		// System.out.println("center location: ");
		// this.location.printLocation();
		// System.out.println("compute location: ");
		// loc.printLocation();
		// compute bearing, then return coordinate
		double bear = Math.toRadians(bearing(this.location, loc));
		// System.out.println("bear(degrees): " + Math.toDegrees(bear) + " bear(radians): " + bear + " distance: " + dist);
		return new Point(dist * Math.sin(bear), dist * Math.cos(bear));
	}

	/*
	 *	Formula: θ = atan2( sin Δλ ⋅ cos φ2 , cos φ1 ⋅ sin φ2 − sin φ1 ⋅ cos φ2 ⋅ cos Δλ )
	 *  φ is latitude, λ is longitude, R is earth’s radius
	 *	JavaScript: (all angles in radians)
	 *	var y = Math.sin(λ2-λ1) * Math.cos(φ2);
	 *	var x = Math.cos(φ1)*Math.sin(φ2) - Math.sin(φ1)*Math.cos(φ2)*Math.cos(λ2-λ1);
	 *	var brng = Math.atan2(y, x).toDegrees();
	 */
	public static double bearing(Location s, Location d) {
		if (s == null || d == null) throw new NullPointerException();
		double lat1 = s.getLatitude();
		double long1 = s.getLongitude();
		double lat2 = d.getLatitude();
		double long2 = d.getLongitude();
		double y = Math.sin(Math.toRadians(long2 - long1)) * Math.cos(Math.toRadians(lat2));
		double x = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) - Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(long2-long1));
		return Math.toDegrees(Math.atan2(y, x));
	}

	private double getAveDist() {
		if (map == null) return 0;
		return map.getAverageDistance();
	}

	public double response(int i, int j) {
		if (i < 0 || j < 0 || i >= map.getRows() || j >= map.getCols()) throw new IndexOutOfBoundsException();
		int code = hashcode(i, j);
		if (hashmap.containsKey(code)) {
			/* debug */
//			System.out.println("Find match!");
			return hashmap.get(code);
		}
		return MTP.P_100;
	}

	public PU getPU() {
		return pu;
	}

	/* This hash function works as long as j is smaller than 100000 */
	public static int hashcode(int i, int j) {
		return 100000 * i + j;
	}

	/* Following three methods are used for visualize/plot protection contours */
	/* call only after transfiguration */
	public void visualPreCompute() {
		int rows = map.getRows();
		int cols = map.getCols();
		double[][] p = new double[rows][cols];
		int[] data = new int[rows * cols];
		int red = -1;
		int green = -1;
		int blue = -1;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				@SuppressWarnings("unused")
				int code = hashcode(i, j);
				p[i][j] = response(i, j);
				if (p[i][j] == MTP.P_0) { // gray
					red = 255;
					green = 255;
					blue = 255;
				}
				else if (p[i][j] == MTP.P_50) { // white
					red = 180;
					green = 180;
					blue = 180;
				}
				else if (p[i][j] == MTP.P_75) { // white
					red = 100;
					green = 100;
					blue = 100;
				}
				else { // > 0.5, black
					red = 0;
					green = 0;
					blue = 0;
					// greater = true;
					// throw new IllegalArgumentException();
				}
				data[j + cols * i] = (red << 16) | (green << 8) | blue;
			}
		}
		JFrame frame = new JFrame("Pre compute " + pu.getID());
		frame.getContentPane().add(new ColorPan(data, cols, rows));
		frame.setSize(cols, rows);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void printPreCompute() {
		File file = new File(directory + "precompute" + pu.getID() + ".txt");
		double[][] p = new double[map.getRows()][map.getCols()];
		try {
			PrintWriter out = new PrintWriter(file);
			System.out.println("Start printing... ");
			for (int i = 0; i < map.getRows(); i++) {
				for (int j = 0; j < map.getCols(); j++) {
					p[i][j] = response(i, j);
					out.print(p[i][j] + " ");
				}
				out.println();
			}
			out.close (); // this is necessary
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		} finally {
			System.out.println("Printing ends");
		}
	}

	public void printOriginCompute() {
		File file = new File(directory + "original" + pu.getID() + ".txt");
		double[][] p = new double[map.getRows()][map.getCols()];
		try {
			PrintWriter out = new PrintWriter(file);
			System.out.println("Start printing... ");
			for (int i = 0; i < map.getRows(); i++) {
				for (int j = 0; j < map.getCols(); j++) {
					p[i][j] = MTP(map.getLocation(i, j).distTo(this.location));
					out.print(p[i][j] + " ");
				}
				out.println();
			}
			out.close (); // this is necessary
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		} finally {
			System.out.println("Printing ends");
		}
	}

    // MTP function
	public static double MTP(double distance) {
		double PMAX = 1;
		// System.out.println("Distance between PU and SU is: " + distance + " km");
		if (distance < MTP.d1) return 0;
		if (distance >= MTP.d1 && distance < MTP.d2) return 0.5 * PMAX;
		if (distance >= MTP.d2 && distance < MTP.d3) return 0.75 * PMAX;
		return PMAX;
	}

	public static void main(String[] args) {
		Location l1 = new Location(38, -82);
		Location l2 = new Location(38, -80);
		System.out.println(bearing(l1, l2));
	}
}
