import java.text.DecimalFormat;

/*
 * InferMap is the grid map that attackers use to infer PU's location
 * It inherents GridMap class, with additional matrix that corresponding
 * to each cell representing the probability of existence of PU
 */

public class InferMap extends GridMap {
	private double[][] p;
	// constructor
	// initialize each entry to be 0.5
	public InferMap(double length, double height, double cellLength) {
		super(length, height, cellLength);
		p = new double[getRows()][getCols()];
		for (int i = 0; i < getRows(); i++) 
			for (int j = 0; j < getCols(); j++)
				p[i][j] = 0.5;
	}
	// updates the matrix based on response from server
	public void update(Location location, int d1, int d2) {
		int G = 0;
		int updateLength = (int) 25 * 2 / (int) getCellSize();
		int startRow = location.getRowIndex() - updateLength / 2;
		if (startRow < 0) startRow = 0;
		int startCol = location.getColIndex() - updateLength / 2;
		if (startCol < 0) startCol = 0;
		Location loc = new Location();
		for (int i = startRow; i < updateLength && i < getRows(); i++)
			for (int j = startCol; j < updateLength && j < getCols(); j++) {
				loc.setRowCol(i, j);
				int distance = (int) getCellSize() * (int) loc.distTo(location);
				if (distance < d1) p[loc.getRowIndex()][loc.getColIndex()] = 0;
				if (distance >= d1 && distance < d2) G++;
			}
		System.out.println("Number of G is: " + G);
		if (G != 0) {
			for (int i = startRow; i < updateLength && i < getRows(); i++)
				for (int j = startCol; j < updateLength && j < getCols(); j++) {
					loc.setRowCol(i, j);
					int distance = (int) getCellSize() * (int) loc.distTo(location);
					if (distance >= d1 && distance < d2) {
						p[loc.getRowIndex()][loc.getColIndex()] = p[loc.getRowIndex()][loc.getColIndex()] / (1 - (1 - p[loc.getRowIndex()][loc.getColIndex()]) / G);
					}
				}
		}
	}
	// print the matrix
	public void print() {
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				System.out.print(new DecimalFormat(".000").format(p[i][j]) + " ");
			}
			System.out.println();
		}
	}
}