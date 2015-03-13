import java.text.DecimalFormat;

/*
 * InferMap is the grid map that attackers use to infer PU's location
 * It inherents GridMap class, with additional matrix that corresponding
 * to each cell representing the probability of existence of PU
 */

public class InferMap extends GridMap {
	private double[][] p;

	public InferMap(GridMap map) {
		super(map.getUpperLeft(), map.getUpperRight(), map.getLowerLeft(), map.getLowerRight(), map.getCellDegree());
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
		if (!withInBoundary(location)) {
			System.out.println("Invalid location");
			return;
		}
		if (d1 < 0 || d2 < 0) {
			System.out.println("Invalid parameters");
			return;
		}
		int G = 0;
		int rowIndex = (int) (Math.abs((getUpperBoundary() - location.getLatitude())) / getCellDegree());
		int colIndex = (int) (Math.abs((getLeftBoundary() - location.getLongitude())) / getCellDegree());
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
		// assume that PU is located at the center of cell
		for (int i = startRow; i < startRow + updateLength && i < getRows(); i++)
			for (int j = startCol; j < startCol + updateLength && j < getCols(); j++) {
				loc.setLocation(getUpperBoundary() - 0.5 * getCellDegree() - i * getCellDegree(), getLeftBoundary() + 0.5 * getCellDegree() + j * getCellDegree());
				double distance = loc.distTo(location);
				if (distance < d1) {
					p[i][j] = 0;
					/* debug information */
					System.out.println("Set to 0: " + i + ", " + j);
				}
				if (distance >= d1 && distance < d2) G++;
			}
		/* debug information */
		System.out.println("Number of G is: " + G);
		if (G != 0) {
			for (int i = startRow; i < startRow + updateLength && i < getRows(); i++)
				for (int j = startCol; j < startCol + updateLength && j < getCols(); j++) {
					loc.setLocation(getUpperBoundary() - 0.5 * getCellDegree() - i * getCellDegree(), getLeftBoundary() + 0.5 * getCellDegree() + j * getCellDegree());
					double distance = loc.distTo(location);
					if (distance >= d1 && distance < d2) {
						p[i][j] = p[i][j] / (1 - (1 - p[i][j]) / G);
						/* debug information */
						System.out.println("p" + "[" + i + "]" + "[" + j + "] = " + p[i][j]);
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