package utility;
/*
 * This class wrap information that Server passes to client.
 * Right now it wraps pu's channelID & available transmit power.
 * Note that in the future implementation, response may want to
 * hide pu's information to enhance privacy.
 */
public class Response implements Comparable<Response> {
	private PU pu = null;         // pu that makes response
	private double power = -1;    // response power
	private int channelID = -1;   // responser id

	public Response(PU pu, double power) {
		this.pu = pu;
		this.power = power;
		this.channelID = pu.getChannelID();
	}

	/**
	 * Use to construct (-1, PMAX) indicating that no pu on the map
	 * @param channelID
	 * @param power
	 */
	public Response(int channelID, double power) {
		this.pu = null;
		this.power = power;
		this.channelID = channelID;
	}

	public int getChannelID() {
		if (pu == null) {
			// System.out.println("Channel unavailable");
			return -1;
		}
		return channelID;
	}

	public double getPower() {
		return power;
	}

	public int getPUID() {
		if (pu == null) return -1;
		return pu.getID();
	}

	/* delete this!!! */
	public PU getPU() {
		return pu;
	}

	public boolean decrease(int step) {
		if (power == MTP.P_0) return false;
		if (power == MTP.P_50) power = MTP.P_0;
		if (power == MTP.P_75) power = MTP.P_50;
		if (power == MTP.P_100) power = MTP.P_75;
		return true;
	}

	public int compareTo(Response response) {
		if (this.power < response.getPower()) return -1;
		else if (this.power == response.getPower()) return 0;
		else return 1;
	}

	/* print response info in one line */
	public void printResponse() {
		System.out.println("Response=> channel: " + channelID + ", pu: " + pu.getID() + ", power: " + power);
	}
}