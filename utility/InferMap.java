package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import client.Client;

/*
 * InferMap is the grid map that attackers use to infer PU's location
 * It inherents GridMap class, with additional matrix that corresponding
 * to each cell representing the probability of existence of PU
 */
public class InferMap extends GridMap {
	private int id;                      // channel id
	private double[][] p;                // probability matrix

	/**
	 * Construct a probability map with 0.5 for each cell
	 * @param id    channel id
	 * @param map   grid map that inherents from parent class
	 */
	public InferMap(int id, GridMap map) {
		super(map);
		this.id = id;
		p = new double[getRows()][getCols()];
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				p[i][j] = 0.5;
			}
		}
	}

	/**
	 * Update probabilities for infer map
	 * @param client    current location for client
	 * @param d1        update range, area with d1 is set to 0
	 * @param d2        update range, probability of area in between d1 and d2 will increase
	 */
	public void update(Client client, double d1, double d2) {
		Location clientLocation = client.getLocation();
		if (clientLocation == null) return;
		if (!withInBoundary(clientLocation)) {
			System.out.println("Invalid location");
			return;
		}
		if (d1 < 0 || d2 < 0 || d1 > d2) {
			System.out.println("Invalid parameters");
			return;
		}
		// parameter used in updating formula
		int G = 0;
		// index for client's position
		int rowIndex = client.getRowIndex();
		int colIndex = client.getColIndex();
		int updateLength = (int) Math.round(MTP.d3 * 4 / getAverageDistance());

		int startRow = rowIndex - (int) Math.round(updateLength / 2.0);
		if (startRow < 0) startRow = 0;
		int startCol = colIndex - (int) Math.round(updateLength / 2.0);
		if (startCol < 0) startCol = 0;

		// temporary location for each cell
		Location tmpCell = new Location();
		for (int i = startRow; i <= startRow + updateLength && i < getRows(); i++) {
			for (int j = startCol; j <= startCol + updateLength && j < getCols(); j++) {
				// assume that PU is located at the center of cell
				tmpCell.setLocation(rowToLat(i), colToLng(j));
				double distance = tmpCell.distTo(clientLocation);
				if (distance < d1) {
					p[i][j] = 0;
				}
				if (distance >= d1 && distance < d2) G++;
			}
		}
		if (G != 0) {
			for (int i = startRow; i <= startRow + updateLength && i < getRows(); i++)
				for (int j = startCol; j <= startCol + updateLength && j < getCols(); j++) {
					// assume that PU is located at the center of cell
					tmpCell.setLocation(rowToLat(i), colToLng(j));
					double distance = tmpCell.distTo(clientLocation);
					if (distance >= d1 && distance < d2) {
						p[i][j] = p[i][j] / (1 - (1 - p[i][j]) / (G));
					}
				}
		}
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
				p[i][j] = 0.5;
			}
		}
	}

	/**
	 * Print infer map's probability map
	 * @param dir     output path
	 * @param fileName file name of text file that contains probability
	 */
	public void printProbability(String dir, String fileName) {
		if (dir == null || dir.length() == 0) return;
		File file = new File(dir + fileName + "_" + this.id + "_pMatrix.txt");
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
}