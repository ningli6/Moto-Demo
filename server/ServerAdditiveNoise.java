package server;

import utility.GridMap;
import utility.Response;
import client.Client;

/*
 * Server has an instance of GridMap, and uses HashSet to record the location of PUs
 */

public class ServerAdditiveNoise extends Server{
	private double noise = 0.5;                  // noise level   
	private int numberOfQueries = -1;            // number of queries
	private int NOISE_DECREASE_STEP = 1;         // decrease level
	private int actualLies;                      // number of actual lies has been made
	private int expectedLies;                    // number of expected lies

	/**
	 * Initiate additive noise server
	 * @param map     grid map
	 * @param noc     number of channels
	 * @param noq     number of queries (this is necessary to compute expected lies)
	 * @param noise   noise level
	 */
	public ServerAdditiveNoise(GridMap map, int noc, int noq, double noise) {
		super(map, noc);
		this.noise = noise;
		this.numberOfQueries = noq;
		this.actualLies = 0;
		this.expectedLies = (int) Math.round(numberOfQueries * noise / NOISE_DECREASE_STEP);
	}

	/**
	 * @Override
	 * Response queries from client
	 * Check if actual lies has met expected lies, decrease response if not
	 */
	public Response response(Client client) {
		Response res = super.response(client);
		if (res == null) return res;
		if (actualLies < expectedLies) {
			if (res.decrease(NOISE_DECREASE_STEP)) actualLies++;
		}
		return res;
	}

	public void updateLiesNeeded(int numberOfQueries) {
		actualLies = 0;
		this.numberOfQueries = numberOfQueries;
		expectedLies = (int) Math.round(numberOfQueries * noise / NOISE_DECREASE_STEP);
	}

	public double getNoiseLevel() {
		return noise;
	}
	
	/**
	 * Set new noise level
	 * set actual lies back to 0
	 * compute new expected lies needed
	 * @param lv   new noise level, must be in (0, 1)
	 */
	public void setNoiseLevel(double lv) {
		if (lv < 0 || lv > 1) {
			System.out.println("Invalid noise level");
			return;
		}
		this.noise = lv;
		updateLiesNeeded(numberOfQueries);
	}

	public int getNumberOfLies() {
		return actualLies;
	}

	public int getExpectedLies() {
		return expectedLies;
	}

	/**
	 * Check if noise requirement has been reached
	 * @return true if actual lies met expected lies
	 */
	public boolean reachNoiseLevel() {
		return actualLies == expectedLies;
	}

	/**
	 * Clear response records for all primary users
	 * Set actual number of lies back to 0
	 */
	public void reset() {
		super.reset();
		actualLies = 0;
	}
}