package boot;

import demo.*;

public class Boot {
	public static void main(String[] args) {
		BootParams bp = Parser.parse(args);
		// bp.printParams();
		RunnableInterface R;
		if (bp == null) {
			System.out.println("FAILED");
			return;
		}
		if (bp.isCountermeasure() && bp.countermeasure().equals("NOCOUNTERMEASURE")) {
			System.out.println("FAILED");
			return;
		}
		R = new RunnableInterface(bp);
		if (R == null) {
			System.out.println("FAILED");
			return;
		}
	    R.start();
		System.out.println("OK");
	}
}