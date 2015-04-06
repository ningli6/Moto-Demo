/*
 * This class wrap information that Server passes to client.
 * Right now it wraps pu's channelID & available transmit power.
 * Note that in the future implementation, response may want to
 * hide pu's information to enhance privacy.
 */
public class Response implements Comparable<Response> {
	private double power;
	private int channelID;
	private PU pu; // pu that makes response

	public Response(PU pu, double power) {
		this.pu = pu;
		this.power = power;
		this.channelID = pu.getChannelID();
	}

	public Response(int channelID, double power) {
		this.channelID = channelID;
		this.pu = null;
		this.power = power;
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

	public int compareTo(Response response) {
		if (this.power < response.getPower()) return -1;
		else if (this.power == response.getPower()) return 0;
		else return 1;
	}
}