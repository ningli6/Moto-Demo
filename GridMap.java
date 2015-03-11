/*
 * GridMap class represent a grid map, with lenght, height and cell size
 * Number of rows and columns are computed then
 * Note that the origin is the upper left cell
 */
public class GridMap {
	// length of the grid
	private double length;
	// height of the grid
	private double height;
	// number of rows
	private int rows;
	// number of columns
	private int cols;
	// size of cells
	private double cellLength;

	public GridMap(double length, double height, double cellLength) {
		this.length = length;
		this.height = height;
		this.cellLength = cellLength;
		rows = (int) (this.height / this.cellLength);
		cols = (int) (this.length / this.cellLength);
	}

	public double getLength() {
		return length;
	}

	public double getHeight() {
		return height;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public double getCellSize() {
		return cellLength;
	}
}