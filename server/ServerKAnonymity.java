package server;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import utility.*;
import client.Client;

public class ServerKAnonymity extends Server {
	public static int K = 3;
	private List<PU>[] virtual_List;

	public ServerKAnonymity(GridMap map) {
		super(map);
		this.K = 3;
		this.virtual_List = (List<PU>[]) new List[Number_Of_Channels];
		for (List<PU> list : virtual_List)
			list = new LinkedList<PU>();
	}

	public ServerKAnonymity(GridMap map, int k) {
		super(map);
		if (k <= 0) throw new IllegalArgumentException();
		this.K = k;
		this.virtual_List = (List<PU>[]) new List[Number_Of_Channels];
		for (List<PU> list : virtual_List)
			list = new LinkedList<PU>();
	}

	public void kAnonymity() {
		if (getNumberOfPUs() == 0 || this.K == 1) {
			virtual_List = channels_List;
			return;
		}
		List<PU> tmpGroup;
		for (int i = 0; i < Number_Of_Channels; i++) {
			while (!channels_List[i].isEmpty()) {
				if (channels_List[i].size() <= this.K) {
					virtual_List[i].add(findVirtualPU(channels_List[i]));
					channels_List[i].clear();
				}
				else {
					Collections.shuffle(channels_List[i]);
					PU axle = channels_List[i].pop();
					Collections.sort(channels_List[i], axle.DIST_ORDER);
					tmpGroup = new LinkedList<PU>();
					tmpGroup.add(axle);
					for (int j = 0; j < this.K - 1; j++)
						tmpGroup.add(channels_List[i].pop());
					virtual_List[i].add(findVirtualPU(tmpGroup));
				}
			}
		}
	}

	/* Minimal Enclosing Circle Problem */
	/* It's not a easy to implement linear time algorithm, use brute force here */
	private PU findVirtualPU(List<PU> list) {
		if (list == null || list.size() != this.K) throw new IllegalArgumentException();
		double min_max_radius = Double.POSITIVE_INFINITY;
		PU virtualPU = new PU();
		for (int i = 0; i < map.getRows(); i++) {
			for (int j = 0; j < map.getCols(); j++) {
				Location loc = map.getLocation(i, j);
				double max_radius = 0;
				for (PU pu: list) {
					double r = pu.getLocation().distTo(loc);
					if (r > max_radius) max_radius = r; 
				}
				if (max_radius < min_max_radius) {
					min_max_radius = max_radius;
					/* specific MTP function requried */
					virtualPU.setIndices(i, j);
				}
			}
		}
		return virtualPU;
	}
}