package utility;

import java.util.List;
import java.util.LinkedList;

public class Cluster {
	private List<PU> pus;

	public Cluster() {
		pus = new LinkedList<PU>();
	}

	public Cluster(PU pu) {
		pus = new LinkedList<PU>();
		pus.add(pu);
	}

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

	public void clear() {
		pus.clear();
	}

	public void merge(Cluster c) {
		if (c == null || c.getNumbersOfPU() == 0) return;
		pus.addAll(c.getMembers());
		c.clear();
	}
}