package utility;

import java.text.DecimalFormat;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;

/*
 * InferMap is the grid map that attackers use to infer PU's location
 * It inherents GridMap class, with additional matrix that corresponding
 * to each cell representing the probability of existence of PU
 */
public class InferMap extends GridMap {
	// probability map
	private int id;
	private double[][] p;
	public static String directory;

	public InferMap(int id, GridMap map) {
		super(map);
		this.id = id;
		p = new double[getRows()][getCols()];
		for (int i = 0; i < getRows(); i++) 
			for (int j = 0; j < getCols(); j++)
				p[i][j] = 0.5;
	}

	// initialize each entry to be 0.5
	public InferMap(int id, Location ul, Location ur, Location ll, Location lr, double cd) {
		super(ul, ur, ll, lr, cd);
		this.id = id;
		p = new double[getRows()][getCols()];
		for (int i = 0; i < getRows(); i++) 
			for (int j = 0; j < getCols(); j++)
				p[i][j] = 0.5;
	}

	// updates the matrix based on response from server
	public void update(Location location, double d1, double d2) {
		if (location == null) return;
		if (!withInBoundary(location)) {
			System.out.println("Invalid location");
			return;
		}
		if (d1 < 0 || d2 < 0) {
			System.out.println("Invalid parameters");
			return;
		}
		int G = 0;
		double cd = getCellDegree();
		if (cd <= 0) return;
		double upperBoundary = getUpperBoundary();
		double leftBoundary = getLeftBoundary();
		int rowIndex = LatToRow(location.getLatitude());
		int colIndex = LonToCol(location.getLongitude());
		
		/* debug information */
		// System.out.println("***Update****");
		// System.out.println("d1: " + d1 + " d2: " + d2);
		// System.out.println("center: ");
		// location.printLocation();
		// System.out.println("[" + rowIndex + "][" + colIndex + "]");
		// int updateLength = (int) Math.round(30 * 2 / getAverageDistance());
		// for testing
		int updateLength = (int) Math.round(MTP.d3 * 4 / getAverageDistance());
		/* debug information */
		// System.out.println("updateLength: " + updateLength);

		int startRow = rowIndex - (int) Math.round(updateLength / 2.0);
		if (startRow < 0) startRow = 0;
		int startCol = colIndex - (int) Math.round(updateLength / 2.0);
		if (startCol < 0) startCol = 0;
		/* debug information */
		// System.out.println("startRow: " + startRow);
		// System.out.println("startCol: " + startCol);

		Location loc = new Location();
		for (int i = startRow; i <= startRow + updateLength && i < getRows(); i++)
			for (int j = startCol; j <= startCol + updateLength && j < getCols(); j++) {
				// assume that PU is located at the center of cell
				loc.setLocation(upperBoundary - 0.5 * cd - i * cd, leftBoundary + 0.5 * cd + j * cd);
				double distance = loc.distTo(location);
				if (distance < d1) {
					p[i][j] = 0;
				}
				if (distance >= d1 && distance < d2) G++;
			}
		/* debug information */
		// System.out.println("Number of G is: " + G);
		if (G != 0) {
			for (int i = startRow; i <= startRow + updateLength && i < getRows(); i++)
				for (int j = startCol; j <= startCol + updateLength && j < getCols(); j++) {
					// assume that PU is located at the center of cell
					loc.setLocation(upperBoundary - 0.5 * cd - i * cd, leftBoundary + 0.5 * cd + j * cd);
					double distance = loc.distTo(location);
					if (distance >= d1 && distance < d2) {
						p[i][j] = p[i][j] / (1 - (1 - p[i][j]) / G);
						/* debug information */
						// System.out.println("p" + "[" + i + "]" + "[" + j + "] = " + p[i][j]);
					}
				}
		}
		// System.out.println("***Update over****");
		// System.out.println("");
	}

	public double[][] getProbabilityMatrix() {
		return p;
	}

	public Location getLocation(int r, int c) {
		return super.getLocation(r, c);
	}

	public void resetMap() {
		for (int i = 0; i < getRows(); i++) 
			for (int j = 0; j < getCols(); j++)
				p[i][j] = 0.5;
	}

	// print the probability matrix
	// obsolete
	public void print() {
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				System.out.print(new DecimalFormat(".000").format(p[i][j]) + " ");
			}
			System.out.println();
		}
	}

	// visualize results
	public void visualize() {
		// boolean greater = false;
		int rows = getRows();
		int cols = getCols();
		int[] data = new int[rows * cols];
		int red = -1;
		int green = -1;
		int blue = -1;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (p[i][j] == 0.5) { // gray
					red = 128;
					green = 128;
					blue = 128;
				}
				else if (p[i][j] == 0) { // white
					red = 255;
					green = 255;
					blue = 255;
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
		JFrame frame = new JFrame("ColorPan " + id);
		frame.getContentPane().add(new ColorPan(data, cols, rows));
		frame.setSize(cols, rows);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		// System.out.println("Greater than 0.5:" + greater);
	}

	// the output will be matrix
	public void printoutMatrix(int id) {
		if (directory == null || directory.length() == 0) return;
		// String text = "Output";
		File file = new File(directory + "demoMatrix_" + id + ".txt");
		double max = 0;
		try {
			PrintWriter out = new PrintWriter(file);
			System.out.println("Start printing... ");
			for (int i = 0; i < getRows(); i++) {
				for (int j = 0; j < getCols(); j++) {
					out.print(p[i][j] + " ");
					if (p[i][j] > max) max = p[i][j];
				}
				out.println();
			}
			out.close (); // this is necessary
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		} finally {
			System.out.println("Greatest value: " + max);
			System.out.println("Printing ends");
		}
	}

	// the output will be tables that specified in the sample data
	public void printInRequiredFormat(int id) {
		if (directory == null || directory.length() == 0) return;
		File file = new File(directory + "demoTable_" + id + ".txt");
		try {
			PrintWriter out = new PrintWriter(file);
			System.out.println("Start printing... ");
			out.println("LAT LON P");
			for (int i = 0; i < getRows(); i++) {
				for (int j = 0; j < getCols(); j++) {
					out.println(RowToLat(i) + " " + LonToCol(j) + " " + p[i][j]);
				}
			}
			out.close (); // this is necessary
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		} finally {
			System.out.println("Printing ends");
		}
	}
}