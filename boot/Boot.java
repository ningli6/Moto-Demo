package boot;

public class Boot {
	public static void main(String[] args) {
		BootParams bp = Parser.parse(args);
		if (bp == null) {
			System.out.println("FAILED");
			return;
		}
		if (!bp.isCountermeasure()) {
			// startDemo(bp);
		}
		else {
			if (bp.countermeasure() == null) {
				System.out.println("FAILED");
				return;
			}
			else {
				switch(bp.countermeasure()) {
					case "ADDITIVENOISE": 
						// startDemoWithAN(bp);
						break;
					case "TRANSFIGURATION": 
						// startDemoWithAN(bp);
						break;
					case "KANONYMITY": 
						// startDemoWithAN(bp);
						break;
					case "KCLUSTERING": 
						// startDemoWithAN(bp);
						break;
					default:
						System.out.println("FAILED");
						return;
				}
			}
		}
		System.out.println("OK");
	}
}