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
		/* countermeasure not implemented */
		if (bp.isCountermeasure()) {
			System.out.println("NOT IMPLEMENTED");
			return;
		}
		R = new RunnableInterface(bp);

		/* start program, return ok */
		System.out.println("OK");
	    R.start();
	}
}