package server;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import utility.*;
import client.Client;

/* K-Anonymity server */
public class ServerKAnonymity extends Server {
	public static int K = 3;
	private List<PU>[] virtual_List;
	private int Number_Of_Virtual_PUs;

	public class NumberOfChannelsMismatchException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public NumberOfChannelsMismatchException() {
			super();
		}

		public NumberOfChannelsMismatchException(String message) {
			super(message);
		}
	}

	@SuppressWarnings("unchecked")
	public ServerKAnonymity(GridMap map) {
		super(map, 1);
		ServerKAnonymity.K = 3;
		this.virtual_List = (List<PU>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++)
			virtual_List[i] = new LinkedList<PU>();
		Number_Of_Virtual_PUs = 0;
	}

	@SuppressWarnings("unchecked")
	public ServerKAnonymity(GridMap map, int k) {
		super(map, 1);
		if (k <= 0) throw new IllegalArgumentException();
		ServerKAnonymity.K = k;
		this.virtual_List = (List<PU>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++)
			virtual_List[i] = new LinkedList<PU>();
		Number_Of_Virtual_PUs = 0;
	}

	public void kAnonymity() {
		if (getNumberOfPUs() == 0 || ServerKAnonymity.K == 1) {
			System.out.println("No need to convert to virtual pu");
			virtual_List = channels_List;
			updateNumbersOfVirtualPUs();
			updateNumbersOfPUs();
			return;
		}
		List<PU> tmpGroup;
		for (int i = 0; i < Number_Of_Channels; i++) {
			while (!channels_List[i].isEmpty()) {
				if (channels_List[i].size() <= ServerKAnonymity.K) {
					virtual_List[i].add(findVirtualPU(channels_List[i], i));
					channels_List[i].clear();
				}
				else {
					Collections.shuffle(channels_List[i]);
					PU axle = channels_List[i].pop();
					Collections.sort(channels_List[i], axle.DIST_ORDER);
					tmpGroup = new LinkedList<PU>();
					tmpGroup.add(axle);
					for (int j = 0; j < ServerKAnonymity.K - 1; j++)
						tmpGroup.add(channels_List[i].pop()); // remove first
					virtual_List[i].add(findVirtualPU(tmpGroup, i));
					System.out.println();
				}
			}
		}
		// update number of pus
		updateNumbersOfPUs();
		// update number of virtual pus
		updateNumbersOfVirtualPUs();

		/* debug */
		// int i = 0;
		// System.out.println();
		// System.out.println("*****Virtual list*****");
		// for (List<PU> list : virtual_List) {
		// 	if (list.isEmpty()) {
		// 		System.out.println("No virtual pu in this channel");
		// 		continue;
		// 	}
		// 	for (PU pu : list) {
		// 		System.out.println("Channel: [" + i + "]");
		// 		System.out.println(pu.getChannelID() + ", R: " + pu.getRadius());
		// 		pu.printLocation();
		// 		if (i != pu.getChannelID()) throw new IllegalArgumentException("Channel id mismatch");
		// 	}
		// 	i++;
		// }
	}

	/* Minimal Enclosing Circle Problem */
	/* It's not a easy to implement linear time algorithm, use brute force here */
	private PU findVirtualPU(List<PU> list, int channel_id) {
		if (list == null) throw new NullPointerException();
		if (list.size() > ServerKAnonymity.K) throw new IllegalArgumentException();
		if (channel_id < 0 || channel_id >= Number_Of_Channels) throw new IllegalArgumentException();
		double min_max_radius = Double.POSITIVE_INFINITY;
		PU virtualPU = new PU();
		virtualPU.attachToServer(this);     // attach to server
		virtualPU.setChannelID(channel_id); // set up working channel
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
					virtualPU.setIndices(i, j);  // set index position and actual location for pu
					virtualPU.updateRadius(min_max_radius);
				}
			}
		}
		return virtualPU;
	}

	//@ override
	public Response response(Client client) {
		if (client == null) throw new NullPointerException("Querying client does not exist");
		if (!map.withInBoundary(client.getLocation())) throw new IllegalArgumentException("Client location is not in the range of map");
		if (getNumbersOfVirtualPUs() == 0) return new Response(-1, PMAX); // no pu responses, have max transmit power
		List<Response> response_list = new LinkedList<Response>();// linkedlist that saves responses
		@SuppressWarnings("unused")
		double final_res_power = -1;
		@SuppressWarnings("unused")
		int final_res_id = -1;
		int channel_id = 0;
		for (List<PU> list : virtual_List) {
			Collections.shuffle(list);
			PU minPU = null;
			double minPower = Double.MAX_VALUE;
			for (PU pu : list) {
				if (channel_id != pu.getChannelID()) throw new IllegalArgumentException("Channel id does not match");
				double resPower = virtualMTP(pu.getLocation().distTo(client.getLocation()), pu.getRadius());
				if (resPower <= minPower) {
					minPU = pu;
					minPower = resPower;
				}
			}
			if (minPU != null) response_list.add(new Response(minPU, minPower));
			channel_id++;
		}
		if (channel_id != Number_Of_Channels) throw new NumberOfChannelsMismatchException();
		Collections.shuffle(response_list);
		return Collections.max(response_list);
	}

	public void updateNumbersOfVirtualPUs() {
		int sum = 0;
		for (int i = 0; i < Number_Of_Channels; i++) {
			sum += virtual_List[i].size();
		}
		Number_Of_Virtual_PUs = sum;
	}

	public int getNumbersOfVirtualPUs() {
		int sum = 0;
		for (int i = 0; i < Number_Of_Channels; i++) {
			sum += virtual_List[i].size();
		}
		if (sum != Number_Of_Virtual_PUs) {
			System.out.println("sum = " + sum + ", Number_Of_Virtual_PUs = " + Number_Of_Virtual_PUs);
			throw new NumberOfPUsMismatchException("Number of virtual PUs doesn't match");
		}
		return sum;
	}

	//@override
	public List<PU>[] getChannelsList() {
		if (virtual_List == null) {
			System.out.println("Initialize Server first");
			return null;
		} 
		return virtual_List;
	}

	public static double virtualMTP(double dist, double base) {
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