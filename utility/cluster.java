package utility;

import java.util.List;
import java.util.LinkedList;

public class Cluster {
	private List<PU> pus;

	public Cluster() {
		pus = new LinkedList<PU>();
	}
	/* initialize a cluster with single pu */
	public Cluster(PU pu) {
		pus = new LinkedList<PU>();
		pus.add(pu);
		pu.putInCluster(this);
	}
	/* initialize a cluster with a list of pus */
	public Cluster(List<PU> list) {
		pus = new LinkedList<PU>(list);
	}

	public int getNumbersOfPU() {
		return pus.size();
	}

	public boolean isEmpty() {
		return pus.isEmpty();
	}

	public List<PU> getMembers() {
		return pus;
	}

	public void putInThisCluster() {
		for (PU pu : pus) {
			pu.putInCluster(this);
		}
	}

	public void clear() {
		pus.clear();
	}

	public void merge(Cluster c) {
		if (c == null || c.getNumbersOfPU() == 0) return;
		pus.addAll(c.getMembers());
		c.clear();
		putInThisCluster();
	}

	public void printCluster() {
		System.out.print("{ ");
		for (PU p: pus) {
			System.out.print(p.getID() + " ");
		}
		System.out.println("}");
	}
}