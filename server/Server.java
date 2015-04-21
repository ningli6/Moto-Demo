package server;

import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

import utility.*;
import client.Client;

/*
 * Server has an instance of GridMap, and uses HashSet to record the location of PUs
 */

public class Server {
	public static double PMAX = 1;
	public static int Number_Of_Channels = 1;
	// Server has an instance of GridMap
	protected GridMap map;
	protected List<PU>[] channels_List;
	private int Number_Of_PUs;

	public class NumberOfPUsMismatchException extends RuntimeException {
		public NumberOfPUsMismatchException(String message) {
			super(message);
		}
	}

	public class ClientOutOfMapException extends RuntimeException {
		public ClientOutOfMapException(String message) {
			super(message);
		}
	}

	public Server(GridMap map) {
		this.map = map;
		this.Number_Of_PUs = 0;
		// set = new LinkedList<PU>();
		channels_List = (List<PU>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++) {
			channels_List[i] = new LinkedList<PU>();
		}
	}

	// add pu to one of channels
	public void addPU(PU pu, int channel) {
		// error checking
		if (map == null) {
			System.out.println("Initialize map first");
			return;
		}
		if (pu == null) return;
		if (channel < 0 || channel >= Number_Of_Channels) {
			System.out.println("Avalible channels are from 0 to " + (Number_Of_Channels - 1) + ". Operation failed");
			return;
		}
		int pu_r = pu.getRowIndex();
		int pu_c = pu.getColIndex();
		if (pu_r < 0 || pu_r >= map.getRows()) {
			System.out.println("PU's location is out out index");
			return;
		}
		if (pu_c < 0 || pu_c >= map.getCols()) {
			System.out.println("PU's location is out out index");
			return;
		}
		pu.setLocation(map.RowToLat(pu_r), map.ColToLon(pu_c));
		// check if location is in the rectangle area
		// for now let's say we allow pu to have the same location
		if (map.withInBoundary(pu.getLocation())) {
			channels_List[channel].add(pu);
			pu.attachToServer(this);
			pu.setChannelID(channel);
			Number_Of_PUs++;
		}
		else System.out.println("PU's location out of range");
	}

	public GridMap getMap() {
		return map;
	}

	// resonse to the query
	public Response response(Client client) {
		// response with (-1, -1) means no transmit power available
		if (client == null) return new Response(-1, -1);
		if (!map.withInBoundary(client.getLocation())) throw new ClientOutOfMapException("Client location is not in the range of map");
		// response with (-1, PMAX) means that no PU responses, but allow max transmit power
		/* clarify this behavior */
		if (getNumberOfPUs() == 0) return new Response(-1, PMAX);
		List<Response> response_list = new LinkedList<Response>();
		double final_res_power = -1;
		int final_res_id = -1;
		for (List<PU> list : channels_List) {
			Collections.shuffle(list);
			PU minPU = null;
			double minPower = Double.MAX_VALUE;
			for (PU pu : list) {
				// System.out.println("Distance between SU and PU [" + pu.getID() + "] is: " + pu.getLocation().distTo(client.getLocation()) + " km");
				double resPower = MTP(pu.getLocation().distTo(client.getLocation()));
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

	// cheat
	public List<PU>[] getChannelsList() {
		if (channels_List == null) {
			System.out.println("Initialize Server first");
			return null;
		} 
		return channels_List;
	}

	// return numbers of PUs
	public int getNumberOfPUs() {
		int sum = 0;
		for (int i = 0; i < Number_Of_Channels; i++) {
			sum += channels_List[i].size();
		}
		if (sum != Number_Of_PUs) {
			throw new NumberOfPUsMismatchException("Number of PUs doesn't match");
		}
		return sum;
	}

	// print the location of PUs
	public void printInfoPU() {
		if (channels_List == null) return;
		for (int i = 0; i < Number_Of_Channels; i++) {
			for (PU pu : channels_List[i]) {
				pu.printInfo();
			}
		}
	}

	public void reset() {
		if (channels_List == null) return;
		for (int i = 0; i < Number_Of_Channels; i++) {
			for (PU pu : channels_List[i]) {
				pu.reset();
			}
		}
	}

	// print infomation about channel
	public void printInfoChannel() {
		if (channels_List == null) {
			System.out.println("Initialize server first");
		}
		for (int i = 0; i < Number_Of_Channels; i++) {
			System.out.print("Channel [" + i + "]: ");
			for (PU pu : channels_List[i]) {
				System.out.print("pu " + pu.getID() + ", ");
			}
			System.out.println();
		}
	}

    // MTP function
	private double MTP(double distance) {
		// System.out.println("Distance between PU and SU is: " + distance + " km");
		if (distance < MTP.d1) return 0;
		if (distance >= MTP.d1 && distance < MTP.d2) return 0.5 * PMAX;
		if (distance >= MTP.d2 && distance < MTP.d3) return 0.75 * PMAX;
		return PMAX;
	}
}