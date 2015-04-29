package server;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import utility.*;
import client.Client;

public class ServerKClustering extends Server {
	public static int K = 3; /* number of Cluster */
	private List<PU>[] virtual_List; /* list for virtual pus on that channel */
	private List<Cluster>[] cluster_list;

	public ServerKClustering(GridMap map) {
		super(map);
		this.virtual_List = (List<PU>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++)
			virtual_List[i] = new LinkedList<PU>();
		this.cluster_list = (List<Cluster>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++)
			cluster_list[i] = new LinkedList<Cluster>();
	}

	public ServerKClustering(GridMap map, int k) {
		super(map);
		if (k <= 0) throw new IllegalArgumentException();
		this.K = k;
		this.virtual_List = (List<PU>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++)
			virtual_List[i] = new LinkedList<PU>();
		this.cluster_list = (List<Cluster>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++)
			cluster_list[i] = new LinkedList<Cluster>();
	}

	/* Cluster and find virtual pus */
	public void kClustering() {
		if (getNumberOfPUs() == 0) {
			System.out.println("No need to convert to virtual pu");
			virtual_List = channels_List;
			return;
		}
		LinkedList<Pair> compare = new LinkedList<Pair>();
		for (int i = 0; i < Number_Of_Channels; i++) {
			while (!channels_List[i].isEmpty()) {
				int size = channels_List[i].size();
				/* do nothing if number of pus is smaller than K */
				if (size <= this.K) {
					virtual_List[i] = channels_List[i];
					return;
				}
				/* Put each ui into a single cluster. */
				for (PU pu : channels_List[i]) cluster_list[i].add(new Cluster(pu));
				for (int k = 0; k < size; k++) {
					for (int j = i + 1; j < size; j++) {
						/* Compute the distance dij between all pairs of PUs ui and uj. */
						compare.add(new Pair(channels_List[i].get(k), channels_List[i].get(j)));
					}
				}
				/* Put dij values into a sorted array D. */
				Collections.sort(compare);
				/* while (number of clusters) > k do */
				while(cluster_list[i].size() > this.K) {
					
				}
			}
		}
	}
}