package server;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import utility.*;
import client.Client;

/* K-Clustering server */
public class ServerKClustering extends Server {
	private int k = 3;                      // k clusters
	private List<PU>[] virtualList;         // channel list for virtual primary users
	private int numberOfVPUs;               // number of primary users

	/* initialize server */
	@SuppressWarnings("unchecked")
	public ServerKClustering(GridMap map, int noc, int k) {
		super(map, noc);
		this.k = k;
		this.virtualList = (List<PU>[]) new List[numberOfChannels];
		for (int i = 0; i < numberOfChannels; i++) {
			virtualList[i] = new LinkedList<PU>();
		}
		numberOfVPUs = 0;
	}

	/**
	 * K clustering algorithm for preserving location privacy
	 * Inputs: set of primary users U
	 * Output: k clusters
	 * compute distance d(i, j) for all unique pairs of primary users ui and uj
	 * sort all distance and put each primary user as a single cluster
	 * while number of clusters > k:
	 * 		choose smallest d(i, j)
	 * 		combine clusters ui and uj
	 * for each cluster:
	 * 		find virtual center and protection contour
	 */
	public void Clustering() {
		if (getNumberOfPUs() == 0 || k <= 0) { // if no pu on the map or invalid k, do nothing
			System.out.println("No need to cluster");
			for (int i = 0; i < numberOfChannels; i++) {
				virtualList[i] = channelsList[i];    // virtual list is actually not different with actual list
			}
			updateNumbersOfVirtualPUs();  // after grouping, number of virtual pus is N / k where N is the original number of primary users
			return;
		}
		@SuppressWarnings("unchecked")
		List<Cluster>[] clusterList = (List<Cluster>[]) new List[numberOfChannels];    // list for clusters on each channel
		for (int i = 0; i < numberOfChannels; i++) {
			clusterList[i] = new LinkedList<Cluster>();
		}
		for (int i = 0; i < numberOfChannels; i++) { // for each channel
			if (channelsList[i].isEmpty()) continue; // if that channel is empty, do nothing     
			if (channelsList[i].size() <= this.k) {  // do nothing if number of pus is smaller than K
				System.out.println("K is greater than or equal to the number of pus in that channel");
				virtualList[i] = channelsList[i];
				continue;
			}
			LinkedList<Pair> distPair = new LinkedList<Pair>(); // list of unique pairs
			for (int m = 0; m < channelsList[i].size(); m++) {   // compute distance bewteen any pair of primary users
				for (int n = m + 1; n < channelsList[i].size(); n++) {
					distPair.add(new Pair(channelsList[i].get(m), channelsList[i].get(n)));
				}
			}
			Collections.sort(distPair);
			for (PU pu : channelsList[i]) {
				clusterList[i].add(new Cluster(pu)); // each pu itself is a cluster
			}
			while(clusterList[i].size() > this.k) { // while number of clusters > k
//				System.out.println("Size: " + clusterList[i].size());
				Pair min = distPair.pop();          // Choose the smallest pair
//				min.printPair();
				Cluster keepCluster = min.getPU1().getCluster();
				Cluster rmCluster = min.getPU2().getCluster();
				keepCluster.merge(rmCluster); // Combine clusters of ui and uj
				if (rmCluster.isEmpty()) clusterList[i].remove(rmCluster);
			}
			for (Cluster c : clusterList[i]) {      // Find virtual point and protection radius
				if (!c.isEmpty()) virtualList[i].add(findVirtualPU(c.getMembers(), i));
			}
		}
		updateNumbersOfVirtualPUs();
		/* debug */
//		 System.out.println();
//		 System.out.println("*****Virtual list*****");
//		 for (List<PU> list : virtualList) {
//		 	if (list.isEmpty()) {
//		 		System.out.println("No virtual pu in this channel");
//		 		continue;
//		 	}
//		 	for (PU pu : list) {
//		 		System.out.println("channel " + pu.getChannelID() + ", with R " + pu.getRadius() + "km");
//		 		pu.printLocation();
//		 	}
//		 }
//		 System.out.println();
	}

	/**
	 * Minimal Enclosing Circle Problem
	 * It's not easy to implement linear time algorithm, use brute force here
	 * @param list           a list of primary users
	 * @param channelID      channel id
	 * @return a virtual primary user with correct channel id and radius of group
	 */
	private PU findVirtualPU(List<PU> list, int channelID) {
		if (list == null) throw new NullPointerException();
		if (channelID < 0 || channelID >= numberOfChannels) throw new IllegalArgumentException();
		double min_max_radius = Double.POSITIVE_INFINITY;
		PU virtualPU = new PU();
		virtualPU.setMap(map);
		virtualPU.setChannelID(channelID); // set up working channel
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
					virtualPU.setIndices(i, j);  // set index position and actual location for pu
					virtualPU.updateRadius(min_max_radius);  // set group radius
				}
			}
		}
		return virtualPU;
	}

	/**
	 * Server responses to client with virtual primary users
	 * For each channel, it chooses minimum response
	 * Finally it returns max response among all channels
	 * @param client
	 * @return (-1, PMAX) if no PU on the map
	 */
	@Override
	public Response response(Client client) {
		if (client == null) throw new NullPointerException("Querying client does not exist");
		if (!map.withInBoundary(client.getLocation())) throw new IllegalArgumentException("Client location is not in the range of map");
		// response with (-1, PMAX) means that no PU responses, but allow max transmit power
		if (numberOfVPUs == 0) return new Response(-1, PMAX);
		List<Response> responseList = new LinkedList<Response>();// linkedlist that saves responses
		for (List<PU> list : virtualList) {
			Collections.shuffle(list);
			PU minPU = null;
			double minPower = Double.MAX_VALUE;
			for (PU pu : list) {
				double resPower = virtualMTP(pu.getLocation().distTo(client.getLocation()), pu.getRadius());
				if (resPower <= minPower) {
					minPU = pu;
					minPower = resPower;
				}
			}
			if (minPU != null) responseList.add(new Response(minPU, minPower));
		}
		Collections.shuffle(responseList);
		return Collections.max(responseList);
	}
	
	/**
	 * Update number of virtual primary users on all channels
	 */
	private void updateNumbersOfVirtualPUs() {
		int sum = 0;
		for (int i = 0; i < numberOfChannels; i++) {
			sum += virtualList[i].size();
		}
		numberOfVPUs = sum;
	}

	/**
	 * Compute transmit power for virtual pu's
	 * @param dist     distance between client to virtual primary user
	 * @param base     base radius for the group/cluster
	 * @return         transmit power available
	 */
	private double virtualMTP(double dist, double base) {
		double PMAX = 1;
		if (dist < MTP.d1 + base) return 0;
		if (dist >= MTP.d1 + base && dist < MTP.d2 + base) return 0.5 * PMAX;
		if (dist >= MTP.d2 + base && dist < MTP.d3 + base) return 0.75 * PMAX;
		return PMAX;
	}

	public void printInfoVirtualPU() {
		if (virtualList == null) return;
		for (int i = 0; i < numberOfChannels; i++) {
			for (PU pu : virtualList[i]) {
				pu.printVirtualPUInfo();
			}
		}
	}

	/**
	 * Get virtual channel list instead of actual channel list
	 * @return virtual pu list
	 */
	public List<PU>[] getVirtualChannelList() {
		return virtualList;
	}
}