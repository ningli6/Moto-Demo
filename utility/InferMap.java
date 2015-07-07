package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JFrame;

import client.Client;

/*
 * InferMap is the grid map that attackers use to infer PU's location
 * It inherents GridMap class, with additional matrix that corresponding
 * to each cell representing the probability of existence of PU
 */
public class InferMap extends GridMap {
	private int id;                      // channel id
	private double[][] p;                // probability matrix
	private double totalP;
	public static String directory;      // output path

	/**
	 * Construct a probability map with 0.5 for each cell
	 * @param id    channel id
	 * @param map   grid map that inherents from parent class
	 */
	public InferMap(int id, GridMap map) {
		super(map);
		this.id = id;
		p = new double[getRows()][getCols()];
		for (int i = 0; i < getRows(); i++) 
			for (int j = 0; j < getCols(); j++)
//				p[i][j] = 0.5;
				p[i][j] = 1.0 / getNumberOfCells();
		totalP = getTotalP();
		if (!checkTotalP()) throw new IllegalArgumentException("Probability doesn't add up to 1");
	}

	/**
	 * Update probabilities for infer map
	 * @param client    current location for client
	 * @param d1        update range, area with d1 is set to 0
	 * @param d2        update range, probability of area in between d1 and d2 will increase
	 */
	public void update(Client client, double d1, double d2) {
		// probability over the map should sum to 1
		Location clientLocation = client.getLocation();
		if (clientLocation == null) return;
		if (!withInBoundary(clientLocation)) {
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

		// index for client's position
		int rowIndex = client.getRowIndex();
		int colIndex = client.getColIndex();
		
		/* debug information */
		System.out.println("***Update****");
		// see d1 and d2
		System.out.println("d1: " + d1 + " d2: " + d2);
		// see channel
		System.out.println("on channel: " + id);

		int updateLength = (int) Math.round(MTP.d3 * 4 / getAverageDistance());
		/* debug information */
		System.out.println("updateLength: " + updateLength);

		int startRow = rowIndex - (int) Math.round(updateLength / 2.0);
		if (startRow < 0) startRow = 0;
		int startCol = colIndex - (int) Math.round(updateLength / 2.0);
		if (startCol < 0) startCol = 0;
		/* debug information */
		// System.out.println("startRow: " + startRow);
		// System.out.println("startCol: " + startCol);

		// temporary location for each cell
		Location tmpCell = new Location();
		for (int i = startRow; i <= startRow + updateLength && i < getRows(); i++) {
			for (int j = startCol; j <= startCol + updateLength && j < getCols(); j++) {
				// assume that PU is located at the center of cell
				tmpCell.setLocation(RowToLat(i), ColToLon(j));
				double distance = tmpCell.distTo(clientLocation);
				if (distance < d1) {
					totalP -= p[i][j];
					p[i][j] = 0;
				}
				if (distance >= d1 && distance < d2) G++;
			}
		}
		/* debug information */
		 System.out.println("Number of G: " + G);
		if (G != 0) {
			for (int i = startRow; i <= startRow + updateLength && i < getRows(); i++)
				for (int j = startCol; j <= startCol + updateLength && j < getCols(); j++) {
					// assume that PU is located at the center of cell
					tmpCell.setLocation(RowToLat(i), ColToLon(j));
					double distance = tmpCell.distTo(clientLocation);
					if (distance >= d1 && distance < d2) {
						p[i][j] = p[i][j] / (1 - (1 - p[i][j]) / G);
						/* debug information */
						// System.out.println("p" + "[" + i + "]" + "[" + j + "] = " + p[i][j]);
					}
				}
		}
		// adjust p so that they add up to 1
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				p[i][j] /= totalP;
			}
		}
		totalP = getTotalP();
		System.out.println("Total p: " + totalP);
		/**
		 * Debug: check if the total probability add up to 1
		 */
		if (!checkTotalP()) throw new IllegalArgumentException("Probability doesn't add up to 1");
	}

	private boolean checkTotalP() {
		double sum = 0.0;
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				sum += p[i][j];
			}
		}
		double epsilon = 0.01;
		return Math.abs(sum - 1) < epsilon;
	}
	
	private double getTotalP() {
		double sum = 0.0;
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				sum += p[i][j];
			}
		}
		return sum;
	}
	
	public double[][] getProbabilityMatrix() {
		return p;
	}

	/**
	 * Rest infer matrix back to 0.5
	 */
	public void resetMap() {
		for (int i = 0; i < getRows(); i++)  {
			for (int j = 0; j < getCols(); j++) {
				p[i][j] = 1.0 / getNumberOfCells();
//				p[i][j] = 0.5;
			}
		}
		totalP = getTotalP();
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

	/**
	 * Print infer map's probability map
	 * @param dir     output path
	 */
	public void printProbability(String dir) {
		if (dir == null || dir.length() == 0) return;
		File file = new File(dir + "demoTable_" + this.id + ".txt");
		try {
			PrintWriter out = new PrintWriter(file);
			out.println("ROW COL P");
			for (int i = 0; i < getRows(); i++) {
				for (int j = 0; j < getCols(); j++) {
					out.println(i + " " + j + " " + p[i][j]);
				}
			}
			out.close (); // this is necessary
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print number of rows & cols
	 * @param dir  output path
	 */
	public void printRC(String dir) {
		File file = new File(dir + "demoTable_" + id + "_rowcol.txt");
		try {
			PrintWriter out = new PrintWriter(file);
			out.println("ROWS COLS");
			out.println(getRows() + " " + getCols());
			out.close (); // this is necessary
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Print map boundaries
	 * @param dir  output path
	 */ 
	public void printBounds(String dir) {
		File file = new File(dir + "demoTable_" + id + "_bounds.txt");
		try {
			PrintWriter out = new PrintWriter(file);
			out.println("NLAT SLAT WLNG ELNG");
			out.println(getUpperBoundary() + " " + getLowerBounday() + " " + getLeftBoundary() + " " + getRightBoundary());
			out.close (); // this is necessary
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}