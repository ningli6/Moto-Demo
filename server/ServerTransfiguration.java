package server;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import utility.GridMap;
import utility.PU;
import utility.PolyPU;
import utility.Response;
import client.Client;

public class ServerTransfiguration extends Server{
	private int numberOfSides;                     // number of sides for polygon
	private List<PolyPU>[] channels_poly_List;     // channel list for polyPU
	
	@SuppressWarnings("unchecked")
	public ServerTransfiguration(GridMap map, int noc, int numberOfSides) {
		super(map, noc);
		this.numberOfSides = numberOfSides; 
		channels_poly_List = (List<PolyPU>[]) new List[numberOfChannels];
		for (int i = 0; i < numberOfChannels; i++) {
			channels_poly_List[i] = new LinkedList<PolyPU>();
		}
	}

	/**
	 * Wrap primary users into PloyPU object
	 */
	public void transfigure() {
		if (getNumberOfPUs() == 0) return;
		int k = 0;
		for (List<PU> list : channelsList) {
			for (PU pu: list) {
				channels_poly_List[k].add(new PolyPU(pu, numberOfSides));
			}
			k++;
		}
	}
	
	/**
	 * Set new number of sides
	 * @param sides  new number of sides
	 */
	public void transfigure(int sides) {
		if (sides < 3) {
			System.out.println("Invalid number of sides for polygon");
			return;
		}
		if (getNumberOfPUs() == 0) return;
		for (List<PolyPU> list : channels_poly_List) {
			list.clear();
		}
		numberOfSides = sides;
		transfigure();
	}

	/**
	 * Instead of using MTP function to determine response power,
	 * transfiguration server use its polyPU to get pre-computed response power 
	 */
	@Override
	public Response response(Client client) {
		if (client == null) throw new NullPointerException("Querying client does not exist");
		if (!map.withInBoundary(client.getLocation())) throw new IllegalArgumentException("Client location is not in the range of map");
		// response with (-1, PMAX) means that no PU responses, but allow max transmit power
		if (getNumberOfPUs() == 0) return new Response(-1, PMAX);
		List<Response> response_list = new LinkedList<Response>();
		for (List<PolyPU> list : channels_poly_List) {
			Collections.shuffle(list);
			PU minPU = null;
			double minPower = Double.MAX_VALUE;
			for (PolyPU poly : list) {
				// System.out.println("Distance between SU and PU [" + pu.getID() + "] is: " + pu.getLocation().distTo(client.getLocation()) + " km");
				double resPower = poly.response(client.getRowIndex(), client.getColIndex());
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
	
//	public void printTransfiguredMap(String dir) {
//		if (dir == null || dir.length() == 0) return;
//		for (List<PolyPU> ppu : channels_poly_List) {
//			for (PolyPU polyPU : ppu) {
//				File file = new File(dir + "tf_" + polyPU.getPU().getID() + ".txt");
//				try {
//					PrintWriter out = new PrintWriter(file);
//					for (int i = 0; i < map.getRows(); i++) {
//						for (int j = 0; j < map.getCols(); j++) {
//							double val = polyPU.response(i, j);
//							out.print(val + " ");
//						}
//						out.println();
//					}
//					out.close (); // this is necessary
//				} catch (FileNotFoundException e) {
//					System.out.println("FileNotFoundException: " + e.getMessage());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
}