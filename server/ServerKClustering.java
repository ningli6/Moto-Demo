package server;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import utility.*;
import client.Client;

public class ServerKClustering extends Server {
	public static int K = 3;                /* default number of cluster */
	private List<PU>[] virtual_List;        /* list for virtual pus on that channel */
	private List<Cluster>[] cluster_list;   /* list for clusters on each channel */

	public class UnitTestException extends RuntimeException {
		public UnitTestException() {
			super();
		}
		public UnitTestException(String message) {
			super(message);
		}
	}
	/* initialize server */
	public ServerKClustering(GridMap map) {
		super(map);
		this.virtual_List = (List<PU>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++)
			virtual_List[i] = new LinkedList<PU>();
		this.cluster_list = (List<Cluster>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++)
			cluster_list[i] = new LinkedList<Cluster>();
	}

	/* initialize server with number of k specified*/
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

	/* construct clusters and find virtual pus */
	public void kClustering() {
		if (getNumberOfPUs() == 0) { // if no pu on the map, do nothing
			System.out.println("No pu on the map");
			return;
		}
		/* list for pairs of pus */
		LinkedList<Pair> compare = new LinkedList<Pair>();
		for (int i = 0; i < Number_Of_Channels; i++) { // for each channel
			while (!channels_List[i].isEmpty()) { // if that channel is empty, do nothing
				int size = channels_List[i].size();
				/* do nothing if number of pus is smaller than K */
				if (size <= this.K) {
					virtual_List[i] = channels_List[i];
					return;
				}
				/* each pu is a cluster. */
				for (PU pu : channels_List[i]) cluster_list[i].add(new Cluster(pu));
				for (int m = 0; m < size; m++) {
					for (int n = m + 1; n < size; n++) {
						/* Compute the distance dij between all pairs of PUs ui and uj. */
						compare.add(new Pair(channels_List[i].get(m), channels_List[i].get(n)));
					}
				}
				/* Put dij values into a sorted array D. */
				Collections.sort(compare);
				/* debug 
				 * see the result of sorting
				 */
				for (Pair p : compare) p.printPair();
				/* while (number of clusters) > k do */
				while(cluster_list[i].size() > this.K) {
					/* Choose the smallest value dij from array D. */
					Pair min = compare.pop();
					/* debug 
					 * see which one is poped out */
					min.printPair();
					PU pu1 = min.getPU1();
					PU pu2 = min.getPU2();
					/* Combine clusters of ui and uj. */
					pu1.getCluster().merge(pu2.getCluster());
					for (Cluster c : cluster_list[i]) {
						if (c == null || c.getNumbersOfPU() == 0)
							cluster_list[i].remove(c);
					}
					/* debug
					 * see result of merging
					 */
					for (Cluster c : cluster_list[i]) c.printCluster();
				}
				/* Find point Q that minimizes Equation 3. */
				for (Cluster c : cluster_list[i]) 
					virtual_List[i].add(findVirtualPU(c.getMembers(), i));
			}
		}
		/* debug 
		 * see result of virtual pu
		 */
		int i = 0;
		System.out.println();
		System.out.println("*****Virtual list*****");
		for (List<PU> list : virtual_List) {
			if (cluster_list[i].size() != virtual_List[i].size()) throw new UnitTestException("Cluster size is not equal to virtual pu number");
			if (list.isEmpty()) {
				System.out.println("No virtual pu in this channel");
				continue;
			}
			for (PU pu : list) {
				System.out.println("Channel: [" + i + "]");
				System.out.println(pu.getChannelID() + ", R: " + pu.getRadius());
				pu.printLocation();
				if (i != pu.getChannelID()) throw new UnitTestException("Channel id mismatches");
			}
			i++;
		}
	}

	private PU findVirtualPU(List<PU> list, int channel_id) {
		if (list == null) throw new NullPointerException();
		if (channel_id < 0 || channel_id >= Number_Of_Channels) throw new IllegalArgumentException();
		double min_max_radius = Double.POSITIVE_INFINITY;
		PU virtualPU = new PU();
		virtualPU.attachToServer(this);               // attach to server
		virtualPU.setChannelID(channel_id);           // set up working channel
		for (int i = 0; i < map.getRows(); i++) {
			for (int j = 0; j < map.getCols(); j++) { // search thru the whole map
				Location loc = map.getLocation(i, j);
				double max_radius = 0;
				for (PU pu: list) {
					double r = pu.getLocation().distTo(loc);
					if (r > max_radius) max_radius = r; 
				}
				if (max_radius < min_max_radius) {
					min_max_radius = max_radius;
					/* specific MTP function requried */
					virtualPU.setIndices(i, j);       // set index position and actual location for pu
					virtualPU.updateRadius(min_max_radius);
				}
			}
		}
		return virtualPU;
	}

	//@ override
	public Response response(Client client) {
		if (client == null) throw new NullPointerException("Querying client does not exist");
		if (!map.withInBoundary(client.getLocation())) throw new ClientOutOfMapException("Client location is not in the range of map");
		if (getNumbersOfVirtualPUs() == 0) return new Response(-1, PMAX); // no pu responses, have max transmit power
		List<Response> response_list = new LinkedList<Response>();// linkedlist that saves responses
		double final_res_power = -1;
		int final_res_id = -1;
		int channel_id = 0;
		for (List<PU> list : virtual_List) {
			Collections.shuffle(list);
			PU minPU = null;
			double minPower = Double.MAX_VALUE;
			for (PU pu : list) {
				if (channel_id != pu.getChannelID()) throw new UnitTestException("Channel id does not match");
				double resPower = virtualMTP(pu.getLocation().distTo(client.getLocation()), pu.getRadius());
				if (resPower <= minPower) {
					minPU = pu;
					minPower = resPower;
				}
			}
			if (minPU != null) response_list.add(new Response(minPU, minPower));
			channel_id++;
		}
		if (channel_id != Number_Of_Channels) throw new UnitTestException();
		Collections.shuffle(response_list);
		return Collections.max(response_list);
	}

	private int getNumbersOfVirtualPUs() {
		int sum = 0;
		for (int i = 0; i < Number_Of_Channels; i++) {
			sum += virtual_List[i].size();
		}
		return sum;
	}

	private double virtualMTP(double dist, double base) {
		double PMAX = 1;
		if (dist < MTP.d1 + base) return 0;
		if (dist >= MTP.d1 + base && dist < MTP.d2 + base) return 0.5 * PMAX;
		if (dist >= MTP.d2 + base && dist < MTP.d3 + base) return 0.75 * PMAX;
		return PMAX;
	}

	public void printInfoVirtualPU() {
		if (virtual_List == null) return;
		for (int i = 0; i < Number_Of_Channels; i++) {
			for (PU pu : virtual_List[i]) {
				pu.printVirtualPUInfo();
			}
		}
	}
}