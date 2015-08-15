package boot;

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
 * Handle multiple simulation requests by generating new threads for each task
 */
public class Demo implements Runnable {
    private Thread t;                // thread instance
    private String threadName;       // thread name
    private BootParams bp;   // BootParams instance
    private double mtpScale;         // scale that determines mtp function
    private int interval;            // query points in the middle
    private String directory;        // output directory
    private String resultDir;        // directory that saves plots from python and maltab

    public Demo(BootParams bp, double scale, int inter, String dir){
    	this.threadName ="New Demo";
    	this.bp = bp;
        this.mtpScale = scale;
        this.interval = inter;
        this.directory = dir;
        this.resultDir = "C:\\Users\\Administrator\\Desktop\\motoPlot\\";
    }

    @Override
    public void run() {
        System.out.println("Running " +  threadName );
        String emailInfo = null;  // additional information for the email
        try {
        	// program goes here
        	if (bp.containsCM("NOCOUNTERMEASURE")) {
        		Simulation sim = new Simulation(bp, mtpScale, interval, directory);
        		sim.multipleSimulation();
        		if (bp.plotGooglMapNO()) {
        			sim.singleSimulation();
        		}
        	}
        	if (bp.containsCM("ADDITIVENOISE")) {
        		SimAdditiveNoise sim = new SimAdditiveNoise(bp, mtpScale, interval, directory);
        		sim.multipleSimulation();
        		if (bp.plotGooglMapAD()) {
        			sim.singleSimulation();
        		}
        		if (!sim.isFeasible()) {   // if noise level is not feasible, do not plot anything with additive noise
        			bp.delCountermeasure("ADDITIVENOISE");
        			bp.setGoogleMapAD(false);
        			emailInfo += "Noise requirement can't be reached.\n\n";
        		}
        		if (bp.tradeOffAD()) {
        			sim.tradeOffCurve();
        		}
        	}
        	if (bp.containsCM("TRANSFIGURATION")) {
        		SimTransfiguration sim = new SimTransfiguration(bp, mtpScale, interval, directory);
        		sim.multipleSimulation();
        		if (bp.plotGooglMapTF()) {
        			sim.singleSimulation();
        		}
        		if (!sim.isFeasible()) {   // if sides is not feasible, do not plot anything with additive noise
        			bp.delCountermeasure("TRANSFIGURATION");
        			bp.setGoogleMapTF(false);
        			emailInfo += "Number of sides must be a positive integer equal or grater than 3.\n\n";
        		}
        		if (bp.tradeOffTF()) {
        			sim.tradeOffCurve();
        		}
        	}
        	if (bp.containsCM("KANONYMITY")) {
        		SimKAnonymity sim = new SimKAnonymity(bp, mtpScale, interval, directory);
        		sim.multipleSimulation();
        		if (bp.plotGooglMapKA()) {
        			sim.singleSimulation();
        		}
        		if (!sim.isFeasible()) {   // if k is not feasible, do not plot anything with additive noise
        			bp.delCountermeasure("KANONYMITY");
        			bp.setGoogleMapKA(false);
        			emailInfo += "K must be a positive integer.\n\n";
        		}
        	}
        	if (bp.containsCM("KCLUSTERING")) {
        		SimKClustering sim = new SimKClustering(bp, mtpScale, interval, directory);
        		sim.multipleSimulation();
        		if (bp.plotGooglMapKC()) {
        			sim.singleSimulation();
        		}
        		if (!sim.isFeasible()) {   // if k is not feasible, do not plot anything with additive noise
        			bp.delCountermeasure("KCLUSTERING");
        			bp.setGoogleMapKC(false);
        			emailInfo += "K must be a positive integer.\n\n";
        		}
        	}
        	// plot ic vs q
        	if (!CmpPlot.plot(bp.containsCM("NOCOUNTERMEASURE"), bp.containsCM("ADDITIVENOISE"), bp.containsCM("TRANSFIGURATION"), bp.containsCM("KANONYMITY"), bp.containsCM("KCLUSTERING"))) {
        		System.out.println("Plot ic vs q failed");
        		return;
        	}
        	// plot google map
        	if (!MatPlot.plot(bp.getCellSize(), bp.getNumberOfChannels(), bp.getNorthLat(), bp.getSouthLat(), bp.getWestLng(), 
        			bp.getEastLng(), bp.plotGooglMapNO(), bp.plotGooglMapAD(), bp.plotGooglMapTF(), bp.plotGooglMapKA(), bp.plotGooglMapKC())) {
        		System.out.println("Plot Google Maps failed");
        		return;
        	}
        	// plot tradeOff curve
        	if (!TradeOffPlot.plot(bp.tradeOffAD(), bp.tradeOffTF())) {
        		System.out.println("Plot trade-off curves failed");
        		return;
        	}
        	// send email
        	if (bp.getInputParams()) {
        		BuildText.printText(resultDir, "emailInfo.txt", bp.paramsToTextFile());
        	}
        	if (!SendEmail.send("ningli@vt.edu", bp.getEmail(), emailInfo, bp.getNumberOfChannels(), 
        			true, bp.plotGooglMapNO(), bp.plotGooglMapAD(), bp.plotGooglMapTF(), bp.plotGooglMapKA(), bp.plotGooglMapKC(), 
        			bp.tradeOffAD(), bp.tradeOffTF(), bp.getInputParams())) {
        		System.out.println("Sending email failed");
        		return;
        	}
        } catch (Exception e) {
            System.out.println("Thread " +  threadName + " interrupted.");
            e.printStackTrace();
        }
        System.out.println("Thread " +  threadName + " exiting.");
    }

    /**
     * Start program in a new thread
     * run() is called
     */
    public void start ()
    {
        if (t == null)
        {
            t = new Thread (this, threadName);
            t.start();
        }
    }
}