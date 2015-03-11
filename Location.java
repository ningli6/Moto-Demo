/*
 * This class represent coordinate location for a certain cell in the grid
 */
public class Location {
	private int row;
	private int col;
	// constructor
	public Location(int r, int c) {
		if (r < 0) throw new IllegalArgumentException();
		if (c < 0) throw new IllegalArgumentException();
		row = r;
		col = c;
	}

	public Location() {
		row = 0;
		col = 0;
	}

	public void setRowCol(int r, int c) {
		row = r;
		col = c;
	}

	public int getRowIndex() {
		return row;
	}

	public int getColIndex() {
		return col;
	}
	// print
	public void printLocation() {
		System.out.println("( " + row + ", " + col + " )");
	}
	// compute distance between two cells
	// returns number of cells as distance
	public double distTo(Location location) {
		if (location == null) return 0;
		if (location == this) return 0;
		int diffR = Math.abs(this.row - location.getRowIndex());
		int diffC = Math.abs(this.col - location.getColIndex());
		return Math.sqrt((double)(diffR * diffR + diffC * diffC));
	}
}