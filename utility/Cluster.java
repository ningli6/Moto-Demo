package utility;

import java.util.List;
import java.util.LinkedList;


/*
 * Cluster represents a cluster of PUs used by k-clustering method
 */
public class Cluster {
	private List<PU> puSet;

	public Cluster() {
		puSet = new LinkedList<PU>();
	}
	
	/* initialize a cluster with single pu */
	public Cluster(PU pu) {
		puSet = new LinkedList<PU>();
		puSet.add(pu);
		pu.putInCluster(this);   // put pu in this cluster
	}
	
	/* initialize a cluster with a list of pus */
	public Cluster(List<PU> list) {
		puSet = new LinkedList<PU>(list);
	}

	public int getNumbersOfPU() {
		return puSet.size();
	}

	public boolean isEmpty() {
		return puSet.isEmpty();
	}

	/**
	 * Get a list of primary users of this cluster
	 * @return  List<PU>
	 */
	public List<PU> getMembers() {
		return puSet;
	}

	public void putInThisCluster() {
		for (PU pu : puSet) {
			pu.putInCluster(this);
		}
	}

	public void clear() {
		puSet.clear();
	}

	/**
	 * Merge members of another cluster.
	 * The cluster that is about to be merge will be cleared after merging
	 * @param c   another cluster
	 */
	public void merge(Cluster c) {
		if (c == null || c.getNumbersOfPU() == 0) return;
		if (this == c) return;           // don't merge with yourself
		puSet.addAll(c.getMembers());    // add all members of the other cluster
		c.clear();                       // empty the other cluster
		putInThisCluster();              // redeclare that all pus belongs to this cluster
	}

	public void printCluster() {
		System.out.print("{ ");
		for (PU p: puSet) {
			System.out.print(p.getID() + " ");
		}
		System.out.println("}");
	}
}