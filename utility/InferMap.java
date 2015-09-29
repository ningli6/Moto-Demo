package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/*
 * InferMap is the grid map that attackers use to infer PU's location
 * It inherents GridMap class, with additional matrix that corresponding
 * to each cell representing the probability of existence of PU
 */
public class InferMap extends GridMap {
	private int id;                      // channel id
	private double[][] p;                // probability matrix
	private int updateLength;            // update diameter
	private int updateRadius;            // update radius
	private double cellArea;

	/**
	 * Construct a probability map with 0.5 for each cell
	 * @param id    channel id
	 * @param map   grid map that inherents from parent class
	 */
	public InferMap(int id, GridMap map) {
		super(map);
		this.id = id;
		p = new double[getNumOfRows()][getNumOfCols()];
		for (int i = 0; i < getNumOfRows(); i++) {
			for (int j = 0; j < getNumOfCols(); j++) {
				p[i][j] = 0.5;
			}
		}
		updateLength = (int) (MTP.d3 * 2.5 / averDist);
		updateRadius = updateLength / 2;
		cellArea = averDist * averDist;
		System.out.println("Update length: " + updateLength);
	}
	
	/**
	 * Deep copy of another inference map
	 * @param map
	 */
	public InferMap(InferMap infMap) {
		super(infMap);
		this.id = infMap.getID();;
		p = new double[getNumOfRows()][getNumOfCols()];
		for (int i = 0; i < getNumOfRows(); i++) {
			for (int j = 0; j < getNumOfCols(); j++) {
				p[i][j] = infMap.getProbability(i, j);
			}
		}
		updateLength = infMap.getUpdateLenght();
		updateRadius = infMap.getUpdateRadius();
		cellArea = infMap.getCellArea();
	}

	/**
	 * Update probabilities for infer map
	 * @param rIndex    row index location of client
	 * @param cIndex    column index location of client
	 * @param d1        update range, area with d1 is set to 0
	 * @param d2        update range, probability of area in between d1 and d2 will increase
	 */
	public void update(int rIndex, int cIndex, double d1, double d2) {
		if (d1 < 0 || d2 < 0 || d1 > d2) throw new IllegalArgumentException();
		if (rIndex < 0 || rIndex >= numberOfRows || cIndex < 0 || cIndex >= numberOfCols) throw new IllegalArgumentException();
		Location clientLocation = getLocation(rIndex, cIndex);
		// parameter used in updating formula
		int G = 0;
		int startRow = Math.max(rIndex - updateRadius, 0);
		int startCol = Math.max(cIndex - updateRadius, 0);

		// temporary location for each cell
		Location tmpCell = new Location();
		for (int i = startRow; i <= startRow + updateLength && i < getNumOfRows(); i++) {
			for (int j = startCol; j <= startCol + updateLength && j < getNumOfCols(); j++) {
				// assume that PU is located at the center of cell
				tmpCell.setLocation(rowToLat(i), colToLng(j));
				double distance = tmpCell.distTo(clientLocation);
				if (distance < d1) {
					p[i][j] = 0;
				}
				if (distance >= d1 && distance < d2) G++;
			}
		}
//		System.out.println("G: " + G);
		if (G != 0) {
			for (int i = startRow; i <= startRow + updateLength && i < getNumOfRows(); i++)
				for (int j = startCol; j <= startCol + updateLength && j < getNumOfCols(); j++) {
					// assume that PU is located at the center of cell
					tmpCell.setLocation(rowToLat(i), colToLng(j));
					double distance = tmpCell.distTo(clientLocation);
					if (distance >= d1 && distance < d2) {
						p[i][j] = p[i][j] / (1 - (1 - p[i][j]) / (G));
					}
				}
		}
	}
	
	/**
	 * Temporarily update inaccuracy value at location (clientR, clientC), with radius (d1, d2).
	 * Previous inaccuracy is ic assuming pu is at (puR, puC).
	 * Return updated inaccuracy without changing the map.
	 * @param attackerR   row index of updating location
	 * @param attackerC   column index of updating location
	 * @param puR   row index of assumed pu
	 * @param puC   column index of assumed pu
	 * @param res  response power
	 * @param ic   previous inaccuracy
	 * @return    updated inaccuracy
	 */
	public long tmpUpdate(int attackerR, int attackerC, int puR, int puC, double res, long ic) {		
		if (attackerR < 0 || attackerR >= numberOfRows || attackerC < 0 || attackerC >= numberOfCols) throw new IllegalArgumentException();
		if (puR < 0 || puR >= numberOfRows || puC < 0 || puC >= numberOfCols) throw new IllegalArgumentException();
//		if (ic < 0) throw new IllegalArgumentException();
		Location clientLocation = getLocation(attackerR, attackerC);
		Location puLocation = getLocation(puR, puC);
//		// parameter used in updating formula
		int G, d0, d1;
		if (res == MTP.P_0) {
			d0 = 0; d1 = 8;
			G = (int) (MTP.pid1d1 / cellArea);
		}
		else if (res == MTP.P_50) {
			d0 = 8; d1 = 14;
			G = (int) ((MTP.pid2d2 - MTP.pid1d1) / cellArea);
		}
		else if (res == MTP.P_75) {
			d0 = 14; d1 = 25;
			G = (int) ((MTP.pid3d3 - MTP.pid2d2) / cellArea);
		}
		else {
			d0 = 25; d1 = 25;
			G = 0;
		}
		int startRow = Math.max(attackerR - updateRadius, 0);
		int startCol = Math.max(attackerC - updateRadius, 0);
//
//		// temporary location for each cell
		Location tmpCell = new Location();
		for (int i = startRow; i <= startRow + updateLength && i < numberOfRows; i++) {
			for (int j = startCol; j <= startCol + updateLength && j < numberOfCols; j++) {
				// assume that PU is located at the center of cell
				tmpCell.setLocation(rowToLat(i), colToLng(j));
				double distance = tmpCell.distTo(clientLocation);
				if (distance < d0) {
					ic -= p[i][j] * tmpCell.distTo(puLocation);
				}
				if (distance >= d0 && distance < d1 && G > 0) {
					double oldP = p[i][j];
					double newP = p[i][j] / (1 - (1 - p[i][j]) / G);
					ic += (newP - oldP) * tmpCell.distTo(puLocation);
				}
			}
		}
		return ic;
	}
	
	/**
	 * Update inference map and update ic value for each entry
	 * @param res          response power
	 * @param icMat        icMat
	 * @param indexOfRow   query row
	 * @param indexOfCol   query columns
	 */
	public void updateIcMat(int attackerR, int attackerC, double res, long[][] icMat) {
		if (attackerR < 0 || attackerR >= numberOfRows || attackerC < 0 || attackerC >= numberOfCols) throw new IllegalArgumentException();
		if (icMat.length != numberOfRows || icMat[0].length != numberOfCols) throw new IllegalArgumentException();
		Location clientLocation = getLocation(attackerR, attackerC);
		// parameter used in updating formula
		int G, d0, d1;
		if (res == MTP.P_0) {
			d0 = 0; d1 = 8;
			G = (int) (MTP.pid1d1 / cellArea);
		}
		else if (res == MTP.P_50) {
			d0 = 8; d1 = 14;
			G = (int) ((MTP.pid2d2 - MTP.pid1d1) / cellArea);
		}
		else if (res == MTP.P_75) {
			d0 = 14; d1 = 25;
			G = (int) ((MTP.pid3d3 - MTP.pid2d2) / cellArea);
		}
		else {
			d0 = 25; d1 = 25;
			G = 0;
		}
		int startRow = Math.max(attackerR - updateRadius, 0);
		int startCol = Math.max(attackerC - updateRadius, 0);

		// temporary location for each cell
		Location tmpCell = new Location();
		for (int i = startRow; i <= startRow + updateLength && i < getNumOfRows(); i++) {
			for (int j = startCol; j <= startCol + updateLength && j < getNumOfCols(); j++) {
				// assume that PU is located at the center of cell
				tmpCell.setLocation(rowToLat(i), colToLng(j));
				double distance = tmpCell.distTo(clientLocation);
				if (distance < d0) {
					for (int r = 0; r < numberOfRows; r++) {
						for (int c = 0; c < numberOfCols; c++) {
							if (p[r][c] > 0) {
								double dist = getLocation(r, c).distTo(tmpCell);
								icMat[r][c] -= dist * p[i][j];
							}
						}
					}
					p[i][j] = 0;
				}
				if (distance >= d0 && distance < d1) {
					double oldP = p[i][j];
					double newP = p[i][j] / (1 - (1 - p[i][j]) / (G));
					for (int r = 0; r < numberOfRows; r++) {
						for (int c = 0; c < numberOfCols; c++) {
							if (p[r][c] > 0) {
								double dist = getLocation(r, c).distTo(tmpCell);
								icMat[r][c] += dist * (newP - oldP);
							}
						}
					}
					p[i][j] = newP;
				}
			}
		}
	}
	
	/**
	 * Get probability at location (i, j)
	 * @param i    row index
	 * @param j    column index
	 * @return     probability at that location
	 */
	public double getProbability(int i, int j) {
		if (i < 0 || i >= getNumOfRows()) throw new IllegalArgumentException();
		if (j < 0 || j >= getNumOfCols()) throw new IllegalArgumentException();
		return p[i][j];
	}
	
	/**
	 * Get the whole probability matrix
	 * @return  double[][]
	 */
	public double[][] getProbabilityMatrix() {
		return p;
	}
	
	/**
	 * Get the id of the inference map
	 * @return  id
	 */
	public int getID() {
		return id;
	}
	
	private double getCellArea() {
		return cellArea;
	}

	private int getUpdateRadius() {
		return updateRadius;
	}

	private int getUpdateLenght() {
		return updateLength;
	}

	/**
	 * Rest infer matrix back to 0.5
	 */
	public void resetMap() {
		for (int i = 0; i < getNumOfRows(); i++)  {
			for (int j = 0; j < getNumOfCols(); j++) {
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
			for (int i = 0; i < getNumOfRows(); i++) {
				for (int j = 0; j < getNumOfCols(); j++) {
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