package utility;

import java.util.Comparator;

/* A helper class that bind a pair of PUs */
public class Pair {
	private PU pu1;
	private PU pu2;
	private double dist;
	public static Comparator<Pair> PAIR_ORDER = new pairOrder(); // can this be static

	private static class pairOrder implements Comparator<Pair> { // can this be static class
		public int compare(Pair p1, Pair p2) {
			// if (pu1 == this || pu2 == this) throw new IllegalArgumentException();
			Double dist1 = p1.getDist();
			Double dist2 = p2.getDist();
			// ascending order
			return dist1.compareTo(dist2);
		}
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

	public boolean samePair(Pair p) {
		return (this.pu1 == p.getPU1() && this.pu2 == p.getPU2()) ||
					(this.pu2 == p.getPU1() && this.pu1 == p.getPU2());
	}

	public boolean contains(PU pu) {
		return this.pu1 == pu || this.pu2 == pu;
	}

	public double getDist() {
		return dist;
	}

	public void printPair() {
		System.out.println("[" + pu1.getID() + "], " + "[" + pu2.getID() +"], " + dist + "km");
	}
}