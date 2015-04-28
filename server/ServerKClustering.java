package server;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import utility.*;
import client.Client;

public class ServerKClustering extends Server {
	public static int K = 3; /* number of cluster */
	private List<PU>[] virtual_List; /* list for virtual pus on that channel */

	public ServerKClustering(GridMap map) {
		super(map);
		this.virtual_List = (List<PU>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++)
			virtual_List[i] = new LinkedList<PU>();
	}

	public ServerKClustering(GridMap map, int k) {
		super(map);
		if (k <= 0) throw new IllegalArgumentException();
		this.K = k;
		this.virtual_List = (List<PU>[]) new List[Number_Of_Channels];
		for (int i = 0; i < Number_Of_Channels; i++)
			virtual_List[i] = new LinkedList<PU>();
	}
}