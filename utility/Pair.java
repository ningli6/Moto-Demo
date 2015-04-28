package utility;

public class Pair implements Comparable<Pair>{
	private PU pu1;
	private PU pu2;
	private double dist;

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

	public int compareTo(Pair p) {
		if (this.dist < p.getDist()) return -1;
		else if (this.dist == p.getDist()) return 0;
		else return 1;
	}
}