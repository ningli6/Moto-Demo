package utility;

/* A helper class that bind a pair of PUs */
public class Pair implements Comparable<Pair> {
	private PU pu1;
	private PU pu2;
	private double dist;
	
	public int compareTo(Pair pair) {
		if (this.dist == pair.getDist()) return 0;
		if (this.dist < pair.getDist()) return -1;
		else return 1;
	}

	/* initialize a pair with two pus */
	public Pair(PU pu1, PU pu2) {
		this.pu1 = pu1;
		this.pu2 = pu2;
		dist = pu1.distTo(pu2);
	}

	public PU getPU1() {
		return pu1;
	}

	public PU getPU2() {
		return pu2;
	}

	/**
	 * Is this pair the same pair
	 * @param p    the pair that is compared to
	 * @return     true if same pair
	 */
	public boolean samePair(Pair p) {
		return (this.pu1 == p.getPU1() && this.pu2 == p.getPU2()) ||
					(this.pu2 == p.getPU1() && this.pu1 == p.getPU2());
	}

	/**
	 * Does this pair contains this primary user
	 * @param pu
	 * @return   true if this pair contains primary user pu
	 */
	public boolean contains(PU pu) {
		return this.pu1 == pu || this.pu2 == pu;
	}

	/**
	 * Get the distance of this pair
	 * @return   distance between two primary users in this pair
	 */
	public double getDist() {
		return dist;
	}

	public void printPair() {
		System.out.println("[" + pu1.getID() + "], " + "[" + pu2.getID() +"], " + dist + "km");
	}
}