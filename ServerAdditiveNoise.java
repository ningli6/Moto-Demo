// package server;

import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

/*
 * Server has an instance of GridMap, and uses HashSet to record the location of PUs
 */

public class ServerAdditiveNoise extends Server{
	public static double noise = 0.5;
	public static int Number_Of_Queries = -1;
	public static int NOISE_DECREASE_STEP = 1;
	private int actual_lies;
	private int expected_lies;

	public ServerAdditiveNoise(GridMap map) {
		super(map);
		actual_lies = 0;
		expected_lies = (int) Math.round(Number_Of_Queries * noise / NOISE_DECREASE_STEP);
	}

	public ServerAdditiveNoise(GridMap map, double noise) {
		super(map);
		this.noise = noise;
		actual_lies = 0;
		expected_lies = (int) Math.round(Number_Of_Queries * noise / NOISE_DECREASE_STEP);
	}

	// @override
	// resonse to the query
	public Response response(Client client) {
		Response res = super.response(client);
		if (res == null) return res;
		if (actual_lies < expected_lies) {
			if (res.decrease(NOISE_DECREASE_STEP)) actual_lies++;
		}
		return res;
	}

	public void setNoise(int number_of_queries) {
		Number_Of_Queries = number_of_queries;
		expected_lies = (int) Math.round(Number_Of_Queries * noise / NOISE_DECREASE_STEP);
	}

	public double getNoiseLevel() {
		return noise;
	}

	public int getNumberOfLies() {
		return actual_lies;
	}

	public int getExpectedLies() {
		return expected_lies;
	}

	public boolean reachNoiseLevel() {
		return actual_lies == expected_lies;
	}

	public void reset() {
		super.reset();
		actual_lies = 0;
		// Number_Of_Queries = -1;
	}
}