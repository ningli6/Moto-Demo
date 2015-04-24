package server;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import utility.*;
import client.Client;

public class ServerKAnonymity extends Server {
	public static int K = 3;
	private List<PU>[] virtual_List;

	public class NumberOfChannelsMismatchException extends RuntimeException {
		public NumberOfChannelsMismatchException() {
			super();
		}

		public NumberOfChannelsMismatchException(String message) {
			super(message);
		}
	}

	public ServerKAnonymity(GridMap map) {
		super(map);
		this.K = 3;
		this.virtual_List = (List<PU>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++)
			virtual_List[i] = new LinkedList<PU>();
	}

	public ServerKAnonymity(GridMap map, int k) {
		super(map);
		if (k <= 0) throw new IllegalArgumentException();
		this.K = k;
		this.virtual_List = (List<PU>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++)
			virtual_List[i] = new LinkedList<PU>();
	}

	public void kAnonymity() {
		if (getNumberOfPUs() == 0 || this.K == 1) {
			System.out.println("No need to convert to virtual pu");
			virtual_List = channels_List;
			return;
		}
		List<PU> tmpGroup;
		for (int i = 0; i < Number_Of_Channels; i++) {
			while (!channels_List[i].isEmpty()) {
				/* debug */
				System.out.println("Before k-anonymity: ");
				System.out.print("channel [" + i + "]: ");
				for (PU pu : channels_List[i]) {
					System.out.print(pu.getID() + " ");
					if (i != pu.getChannelID()) throw new IllegalArgumentException("Channel id mismatch");
				}
				System.out.println();
				if (channels_List[i].size() <= this.K) {
					/* debug */
					System.out.println("list size is less than K");
					virtual_List[i].add(findVirtualPU(channels_List[i], i));
					channels_List[i].clear();
				}
				else {
					Collections.shuffle(channels_List[i]);
					PU axle = channels_List[i].pop();
					Collections.sort(channels_List[i], axle.DIST_ORDER);
					/* debug */
					System.out.println("After sort: " + "take " + axle.getID());
					System.out.print("channel [" + i + "]: ");
					for (PU pu : channels_List[i]) {
						System.out.print(pu.getID() + " ");
						if (i != pu.getChannelID()) throw new IllegalArgumentException("Channel id mismatch");
					}
					System.out.println();
					tmpGroup = new LinkedList<PU>();
					tmpGroup.add(axle);
					for (int j = 0; j < this.K - 1; j++)
						tmpGroup.add(channels_List[i].pop()); // remove first
					/* debug */
					System.out.println("In tmpGroup: ");
					System.out.print("channel [" + i + "]: ");
					for (PU pu : tmpGroup) {
						System.out.print(pu.getID() + " ");
						if (i != pu.getChannelID()) throw new IllegalArgumentException("Channel id mismatch");
					}
					System.out.println();
					virtual_List[i].add(findVirtualPU(tmpGroup, i));
					System.out.println();
				}
			}
		}
		/* debug */
		int i = 0;
		System.out.println();
		System.out.println("*****Virtual list*****");
		for (List<PU> list : virtual_List) {
			for (PU pu : list) {
				System.out.println("Channel: [" + i + "]");
				System.out.println(pu.getChannelID() + ", R: " + pu.getRadius());
				pu.printLocation();
				if (i != pu.getChannelID()) throw new IllegalArgumentException("Channel id mismatch");
			}
			i++;
		}
	}

	/* Minimal Enclosing Circle Problem */
	/* It's not a easy to implement linear time algorithm, use brute force here */
	private PU findVirtualPU(List<PU> list, int channel_id) {
		if (list == null) throw new NullPointerException();
		if (list.size() > this.K) throw new IllegalArgumentException();
		if (channel_id < 0 || channel_id >= Number_Of_Channels) throw new IllegalArgumentException();
		double min_max_radius = Double.POSITIVE_INFINITY;
		/* debug */
		System.out.println("Input list: ");
		for (PU pu : list) {
			System.out.print(pu.getID() + " ");
			if (channel_id != pu.getChannelID()) throw new IllegalArgumentException("Channel id mismatch");
		}
		System.out.println();
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
					virtualPU.setIndices(i, j);
					virtualPU.updateRadius(min_max_radius);
				}
			}
		}
		/* debug */
		virtualPU.printVirtualPUInfo();
		/* debug */
		for (PU pu: list) {
			System.out.println("distance to " + pu.getID() + " = " + pu.distTo(virtualPU));
		}
		return virtualPU;
	}

	//@ override
	public Response response(Client client) {
		if (client == null) throw new NullPointerException("Querying client does not exist");
		if (!map.withInBoundary(client.getLocation())) throw new ClientOutOfMapException("Client location is not in the range of map");
		if (getNumberOfPUs() == 0) return new Response(-1, PMAX); // no pu responses, have max transmit power
		List<Response> response_list = new LinkedList<Response>();
		double final_res_power = -1;
		int final_res_id = -1;
		int channel_id = 0;
		for (List<PU> list : virtual_List) {
			Collections.shuffle(list);
			PU minPU = null;
			double minPower = Double.MAX_VALUE;
			for (PU pu : list) {
				// System.out.println("Distance between SU and PU [" + pu.getID() + "] is: " + pu.getLocation().distTo(client.getLocation()) + " km");
				double resPower = virtualMTP(pu.getLocation().distTo(client.getLocation()), pu.getRadius());
				// System.out.println("Server compute dist: [" + pu.getID() + "] " + resPower);
				if (resPower <= minPower) {
					// take care of channel id for virtual pu, which was undefined
					pu.setChannelID(channel_id);
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

	public static double virtualMTP(double dist, double base) {
		double PMAX = 1;
		if (dist < MTP.d1 + base) return 0;
		if (dist >= MTP.d1 + base && dist < MTP.d2 + base) return 0.5 * PMAX;
		if (dist >= MTP.d2 + base && dist < MTP.d3 + base) return 0.75 * PMAX;
		return PMAX;
	}


}