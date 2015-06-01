package boot;

import demo.*;

public class Boot {
	public static void main(String[] args) {
		BootParams bp = Parser.parse(args);
		RunnableInterface R;
		if (bp == null) {
			System.out.println("FAILED");
			return;
		}
		if (!bp.isCountermeasure()) {
			R = new RunnableInterface("NOCOUNTERMEASURE");
		}
		else {
			if (bp.countermeasure() == null) {
				System.out.println("FAILED");
				return;
			}
			else {
				switch(bp.countermeasure()) {
					case "ADDITIVENOISE": 
						R = new RunnableInterface("ADDITIVENOISE");
						break;
					case "TRANSFIGURATION": 
						R = new RunnableInterface("TRANSFIGURATION");
						break;
					case "KANONYMITY": 
						R = new RunnableInterface("KANONYMITY");
						break;
					case "KCLUSTERING": 
						R = new RunnableInterface("KCLUSTERING");
						break;
					default:
						System.out.println("FAILED");
						return;
				}
			}
		}
		if (R == null) {
			System.out.println("FAILED"); return;
		}
	    R.start();
		System.out.println("OK");
	}
}