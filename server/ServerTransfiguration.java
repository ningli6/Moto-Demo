package server;

import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

import utility.*;
import client.Client;

public class ServerTransfiguration extends Server{
	public static int NUMBER_OF_SIDES = 3;
	private List<PolyPU>[] channels_poly_List;

	public ServerTransfiguration(GridMap map) {
		super(map);
		channels_poly_List = (List<PolyPU>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++) {
			channels_poly_List[i] = new LinkedList<PolyPU>();
		}
	}

	public ServerTransfiguration(GridMap map, int number_of_sides) {
		super(map);
		NUMBER_OF_SIDES = number_of_sides;
		channels_poly_List = (List<PolyPU>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++) {
			channels_poly_List[i] = new LinkedList<PolyPU>();
		}
	}

	public void transigure(NUMBER_OF_SIDES) {
		if (getNumberOfPUs() == 0) return;
		int channel = 0;
		for (List<PU> list : channels_List) {
			for (PU pu: list) {
				channels_poly_List[channel].add(new PolyPU(pu, NUMBER_OF_SIDES));
			}
			channel++;
		}
	}

	// @override
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
		for (List<PolyPU> list : channels_poly_List) {
			Collections.shuffle(list);
			PU minPU = null;
			double minPower = Double.MAX_VALUE;
			for (PolyPU poly : list) {
				// System.out.println("Distance between SU and PU [" + pu.getID() + "] is: " + pu.getLocation().distTo(client.getLocation()) + " km");
				double resPower = poly.response(client.getLocation());
				// System.out.println("Server compute dist: [" + pu.getID() + "] " + resPower);
				if (resPower <= minPower) {
					minPU = poly.getPU();
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
}