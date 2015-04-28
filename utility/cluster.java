package utility;

import java.util.List;
import java.util.LinkedList;

public class cluster {
	private List<PU> pus;

	public cluster() {
		pus = new LinkedList<PU>();
	}

	public cluster(PU pu) {
		pus = new LinkedList<PU>();
		pus.add(pu);
	}

	public cluster(List<PU> list) {
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

	public void merge(cluster c) {
		if (c == null || c.getNumbersOfPU() == 0) return;
		pus.addAll(c.getMembers());
		c.clear();
	}
}