package server;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import utility.*;
import client.Client;

/* K-Anonymity server */
public class ServerKAnonymity extends Server {
	private int k = 3;                    // k for k anonymity
	private List<PU>[] copyChannelsList;  // a copy of channel list of actual locations of pu on each channel, is going to be cleared
	private List<PU>[] virtualList;      // channel list for virtual primary users
	private int numberOfVPUs;    // number of virtual primary users

	@SuppressWarnings("unchecked")
	public ServerKAnonymity(GridMap map, int noc, int k) {
		super(map, noc);
		this.k = k;
		this.virtualList = (List<PU>[]) new List[numberOfChannels];
		this.copyChannelsList = (List<PU>[]) new List[numberOfChannels];
		for (int i = 0; i < numberOfChannels; i++) {
			virtualList[i] = new LinkedList<PU>();
			copyChannelsList[i] = new LinkedList<PU>();
		}
		numberOfVPUs = 0;
	}

	/**
	 * Group primary users into several groups that all have k members.
	 * Each group contains k closest primary users.
	 * Algorithm for K anonymity:
	 * Inputs: set of primary users U
	 * Output: groups of k closest pus
	 * while U is not empty:
	 * 		if size(U) <= k:
	 * 			put all members of U in group G
	 * 		else:
	 * 			choose one member from U randomly, add it to group G
	 * 			compute its distance from all other members in U
	 * 			add first k - 1 members to group G
	 * 		remove members of G from U
	 */
	public void groupKPUs() {
		// copy the channel list
		for (int i = 0; i < numberOfChannels; i++) {
			copyChannelsList[i].addAll(channelsList[i]);    // virtual list is actually not different with actual list
		}
		if (getNumberOfPUs() == 0 || this.k == 1) {         // special case, if no primary user or k is 1 (itself is a group)
			System.out.println("No need to convert to virtual pu");
			for (int i = 0; i < numberOfChannels; i++) {
				virtualList[i].addAll(copyChannelsList[i]);    // virtual list is actually not different with actual list
				copyChannelsList[i].clear();                    // clear channel list
			}
			updateNumbersOfVirtualPUs();  // after grouping, number of virtual pus is N / k where N is the original number of primary users
			return;
		}
		for (int i = 0; i < numberOfChannels; i++) {        // for each channel
			while (!copyChannelsList[i].isEmpty()) {            // channel is not empty
				if (copyChannelsList[i].size() <= this.k) {     // if remaining pus is just enough for the last group
					virtualList[i].add(findVirtualPU(copyChannelsList[i], i));
					copyChannelsList[i].clear();
				}
				else {
					Collections.shuffle(copyChannelsList[i]);   // randomly choose a pu from that channel
					PU axle = ((LinkedList<PU>) copyChannelsList[i]).pop();        // remove first element
					Collections.sort(copyChannelsList[i], axle.DIST_ORDER);  // sort all other pus according to distance to this pu
					List<PU> tmpGroup = new LinkedList<PU>();// a temporary group that holds candidate pu
					tmpGroup.add(axle);
					for (int j = 0; j < this.k - 1; j++)
						tmpGroup.add(((LinkedList<PU>)copyChannelsList[i]).pop());// remove first
					virtualList[i].add(findVirtualPU(tmpGroup, i));
				}
			}
		}
		updateNumbersOfVirtualPUs();      // after grouping, number of virtual pus is N / k where N is the original number of primary users

		/* debug */
//		 System.out.println();
//		 System.out.println("*****Virtual list*****");
//		 for (List<PU> list : virtualList) {
//		 	if (list.isEmpty()) {
//		 		System.out.println("No virtual pu in this channel");
//		 		continue;
//		 	}
//		 	for (PU pu : list) {
//		 		pu.printLocation();
//		 		System.out.println(pu.getChannelID() + ", R: " + pu.getRadius());
//		 	}
//		 }
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
		if (list.size() > this.k) throw new IllegalArgumentException();
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
			Collections.shuffle(list); // for each list, minimum responses can have more than one, this guarantee randomness
			PU minPU = null;
			double minPower = Double.MAX_VALUE;
			for (PU pu : list) {
				double resPower = virtualMTP(pu.getLocation().distTo(client.getLocation()), pu.getRadius());
				if (resPower <= minPower) {  // find the minimum for each channel
					minPU = pu;
					minPower = resPower;
				}
			}
			if (minPU != null) responseList.add(new Response(minPU, minPower)); // if one of channels is empty, don't add it
		}
		Collections.shuffle(responseList);     // shuffle guarantees randomness choosing from ties
		return Collections.max(responseList);  // return max response over different channels
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
	 * Get virtual channel list instead of actual channel list
	 * @return virtual pu list
	 */
	public List<PU>[] getVirtualChannelList() {
		return virtualList;
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

}