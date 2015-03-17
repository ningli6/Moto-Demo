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
	private double[][] p;

	public InferMap(GridMap map) {
		super(map);
		p = new double[getRows()][getCols()];
		for (int i = 0; i < getRows(); i++) 
			for (int j = 0; j < getCols(); j++)
				p[i][j] = 0.5;
	}

	// initialize each entry to be 0.5
	public InferMap(Location ul, Location ur, Location ll, Location lr, double cd) {
		super(ul, ur, ll, lr, cd);
		p = new double[getRows()][getCols()];
		for (int i = 0; i < getRows(); i++) 
			for (int j = 0; j < getCols(); j++)
				p[i][j] = 0.5;
	}

	// updates the matrix based on response from server
	public void update(Location location, int d1, int d2) {
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
		int rowIndex = (int) (Math.abs((upperBoundary - location.getLatitude())) / cd);
		int colIndex = (int) (Math.abs((leftBoundary - location.getLongitude())) / cd);
		
		/* debug information */
		System.out.println("***Update****");
		System.out.println("center: ");
		location.printLocation();
		System.out.println("[" + rowIndex + "][" + colIndex + "]");
		System.out.println("***Update****");

		int updateLength = (int) Math.round(30 * 2 / getAverageDistance());
		/* debug information */
		System.out.println("updateLength: " + updateLength);

		int startRow = rowIndex - (int) Math.round(updateLength / 2.0);
		if (startRow < 0) startRow = 0;
		int startCol = colIndex - (int) Math.round(updateLength / 2.0);
		if (startCol < 0) startCol = 0;
		/* debug information */
		System.out.println("startRow: " + startRow);
		System.out.println("startCol: " + startCol);

		Location loc = new Location();
		for (int i = startRow; i < startRow + updateLength && i < getRows(); i++)
			for (int j = startCol; j < startCol + updateLength && j < getCols(); j++) {
				// assume that PU is located at the center of cell
				loc.setLocation(upperBoundary - 0.5 * cd - i * cd, leftBoundary + 0.5 * cd + j * cd);
				double distance = loc.distTo(location);
				if (distance < d1) {
					p[i][j] = 0;
					/* debug information */
					// System.out.println("p" + "[" + i + "]" + "[" + j + "] = 0");
				}
				if (distance >= d1 && distance < d2) G++;
			}
		/* debug information */
		System.out.println("Number of G is: " + G);
		if (G != 0) {
			for (int i = startRow; i < startRow + updateLength && i < getRows(); i++)
				for (int j = startCol; j < startCol + updateLength && j < getCols(); j++) {
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
	}

	// print the probability matrix
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
				else if (p[i][j] == 0) { // black
					red = 255;
					green = 255;
					blue = 255;
				}
				else { // > 0.5, white
					red = 0;
					green = 0;
					blue = 0;
				}
				data[j + rows * i] = (red << 16) | (green << 8) | blue;
			}
		}
		JFrame frame = new JFrame("ColorPan");
		frame.getContentPane().add(new ColorPan(data, cols, rows));
		frame.setSize(rows, cols);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	// the output will be matrix
	public void printout() {
		// String text = "Output";
		File file = new File("/Users/ningli/Desktop/javaout.txt");
		try {
			PrintWriter out = new PrintWriter(file);
			System.out.println("Start printing... ");
			for (int i = 0; i < getRows(); i++) {
				for (int j = 0; j < getCols(); j++) {
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
	// the output will be tables that specified in the sample data
	public void printInRequiredFormat() {
		File file = new File("/Users/ningli/Desktop/demodata.txt");
		try {
			PrintWriter out = new PrintWriter(file);
			System.out.println("Start printing... ");
			out.println("LAT LON P");
			for (int i = 0; i < getRows(); i++) {
				for (int j = 0; j < getCols(); j++) {
					out.println(getLatitude(i) + " " + getLongitude(j) + " " + p[i][j]);
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