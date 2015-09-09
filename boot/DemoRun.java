package boot;

import java.io.File;
import java.util.Arrays;

import javaEmail.BuildText;
import javaEmail.SendEmail;
import javaPlot.CmpPlot;
import javaPlot.MatPlot;
import javaPlot.TradeOffPlot;
import simulation.SimAdditiveNoise;
import simulation.SimKAnonymity;
import simulation.SimKClustering;
import simulation.SimTransfiguration;
import simulation.Simulation;

/**
 * Demo instance without using new threads, invoked in another proecss
 * @author Administrator
 *
 */
public class DemoRun {
    private BootParams bp;           // BootParams instance
    private double mtpScale;         // scale that determines mtp function
    private int interval;            // query points in the middle
    private String dataDir;        // output directory
    private String plotDir;        // directory that saves plots from python and maltab

    public DemoRun(BootParams bp, String dataDir, String plotDir){
    	this.bp = bp;
        this.mtpScale = bp.getMtpScale();
        this.interval = bp.getInterval();
        this.dataDir = dataDir;
        this.plotDir = plotDir;
    }

    public void run() {
        String emailInfo = null;  // additional information for the email
        try {
        	Long start = System.currentTimeMillis();
    		Simulation simSmart = new Simulation(bp, mtpScale, interval, dataDir);
    		simSmart.smartSimulation();
    		Long ends = System.currentTimeMillis();
    		System.out.println((ends - start) / 1000);
        	// program goes here
        	if (bp.containsCM("NOCOUNTERMEASURE")) {
        		Simulation sim = new Simulation(bp, mtpScale, interval, dataDir);
        		sim.multipleSimulation();
        		if (bp.plotGooglMapNO()) {
        			sim.singleSimulation();
        		}
        		if (bp.isSmartQuery()) {
            		sim.smartSimulation();
        		}
        	}
        	if (bp.containsCM("ADDITIVENOISE")) {
        		SimAdditiveNoise sim = new SimAdditiveNoise(bp, mtpScale, interval, dataDir);
        		sim.multipleSimulation();
        		if (bp.plotGooglMapAD()) {
        			sim.singleSimulation();
        		}
        		if (!sim.isFeasible()) {   // if noise level is not feasible, do not plot anything with additive noise
        			bp.delCountermeasure("ADDITIVENOISE");
        			bp.setGoogleMapAD(false);
        			emailInfo += "Noise requirement can't be reached.\n\n";
        		}
        		if (bp.isTradeOffAD()) {
        			sim.tradeOffCurve();
        		}
        	}
        	if (bp.containsCM("TRANSFIGURATION")) {
        		SimTransfiguration sim = new SimTransfiguration(bp, mtpScale, interval, dataDir);
        		sim.multipleSimulation();
        		if (bp.plotGooglMapTF()) {
        			sim.singleSimulation();
        		}
        		if (!sim.isFeasible()) {   // if sides is not feasible, do not plot anything with additive noise
        			bp.delCountermeasure("TRANSFIGURATION");
        			bp.setGoogleMapTF(false);
        			emailInfo += "Number of sides must be a positive integer equal or grater than 3.\n\n";
        		}
        		if (bp.isTradeOffTF()) {
        			sim.tradeOffCurve();
        		}
        	}
        	if (bp.containsCM("KANONYMITY")) {
        		SimKAnonymity sim = new SimKAnonymity(bp, mtpScale, interval, dataDir);
        		sim.multipleSimulation();
        		if (bp.plotGooglMapKA()) {
        			sim.singleSimulation();
        		}
        		if (!sim.isFeasible()) {   // if k is not feasible, do not plot anything with additive noise
        			bp.delCountermeasure("KANONYMITY");
        			bp.setGoogleMapKA(false);
        			emailInfo += "K must be a positive integer.\n\n";
        		}
        		if (bp.isTradeOffKA()) {
        			sim.tradeOffBar();
        		}
        	}
        	if (bp.containsCM("KCLUSTERING")) {
        		SimKClustering sim = new SimKClustering(bp, mtpScale, interval, dataDir);
        		sim.multipleSimulation();
        		if (bp.plotGooglMapKC()) {
        			sim.singleSimulation();
        		}
        		if (!sim.isFeasible()) {   // if k is not feasible, do not plot anything with additive noise
        			bp.delCountermeasure("KCLUSTERING");
        			bp.setGoogleMapKC(false);
        			emailInfo += "K must be a positive integer.\n\n";
        		}
        		if (bp.isTradeOffKC()) {
        			sim.tradeOffBar();
        		}
        	}
        	// plot ic vs q
        	if (!CmpPlot.plot(this.dataDir, this.plotDir, bp.containsCM("NOCOUNTERMEASURE"), bp.containsCM("ADDITIVENOISE"), bp.containsCM("TRANSFIGURATION"), bp.containsCM("KANONYMITY"), bp.containsCM("KCLUSTERING"))) {
        		System.out.println("Plot ic vs q failed");
        		return;
        	}
        	// plot google map
        	if (!MatPlot.plot(this.dataDir, this.plotDir, bp.getCellSize(), bp.getNumberOfChannels(), bp.getNorthLat(), 
        			bp.getSouthLat(), bp.getWestLng(), bp.getEastLng(), bp.plotGooglMapNO(), bp.plotGooglMapAD(), bp.plotGooglMapTF(), bp.plotGooglMapKA(), bp.plotGooglMapKC())) {
        		System.out.println("Plot Google Maps failed");
        		return;
        	}
        	// plot tradeOff curve/bar
        	if (!TradeOffPlot.plot(this.dataDir, this.plotDir, bp.isTradeOffAD(), bp.isTradeOffTF(), bp.isTradeOffKA(), bp.isTradeOffKC())) {
        		System.out.println("Plot trade-off failed");
        		return;
        	}
        	// send email
        	if (bp.getInputParams()) {
        		BuildText.printText(plotDir, "emailInfo.txt", bp.paramsToTextFile());
        	}
//        	if (!SendEmail.send(this.plotDir, "ningli@vt.edu", bp.getEmail(), emailInfo, 
//        			bp.getNumberOfChannels(), true, bp.plotGooglMapNO(), bp.plotGooglMapAD(), bp.plotGooglMapTF(), bp.plotGooglMapKA(), 
//        			bp.plotGooglMapKC(), bp.isTradeOffAD(), bp.isTradeOffTF(), bp.isTradeOffKA(), 
//        			bp.isTradeOffKC(), bp.getInputParams())) {
//        		System.out.println("Sending email failed");
//        		return;
//        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("Cleaning up folders...");
//        deleteDirectory(new File(dataDir));
//        deleteDirectory(new File(plotDir));
    }
    
    public static boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return(directory.delete());
    }
    
    public static void main(String[] args) {
		BootParams bp = Parser.parse(Arrays.copyOfRange(args, 0, args.length - 2));
		if (bp == null) {
			System.out.println("FAILED");
			return;
		}
		DemoRun demo = new DemoRun(bp, args[args.length - 2], args[args.length - 1]);
		demo.run();
    }
}
