public class Response {
	private double power;
	private int id;

	public Response(int id, double power) {
		this.id = id;
		this.power = power;
	}

	public int getID() {
		return id;
	}

	public double getPower() {
		return power;
	}
}