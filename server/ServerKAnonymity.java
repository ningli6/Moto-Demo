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
						tmpGroup.add(channels_List[i].pop()); // remove first
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
		virtualPU.attachToServer(this);
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
					virtualPU.indexToLocation(); // update virtual pu's location
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
		if (getNumberOfPUs() == 0) return new Response(-1, PMAX); // no pu responses, have max transmit power
		List<Response> response_list = new LinkedList<Response>();
		double final_res_power = -1;
		int final_res_id = -1;
		for (List<PU> list : virtual_List) {
			Collections.shuffle(list);
			PU minPU = null;
			double minPower = Double.MAX_VALUE;
			for (PU pu : list) {
				// System.out.println("Distance between SU and PU [" + pu.getID() + "] is: " + pu.getLocation().distTo(client.getLocation()) + " km");
				double resPower = virtualMTP(pu.getLocation().distTo(client.getLocation()), pu.getRadius());
				// System.out.println("Server compute dist: [" + pu.getID() + "] " + resPower);
				if (resPower <= minPower) {
					minPU = pu;
					minPower = resPower;
				}
			}
			// if one of channels is empty, then minPU would be null
			if (minPU != null) response_list.add(new Response(minPU, minPower));
		}
		// shuffle the list to make sure server choose randomly over tied items. This method runs in linear time.
		Collections.shuffle(response_list);
		// This method iterates over the entire collection, hence it requires time proportional to the size of the collection
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