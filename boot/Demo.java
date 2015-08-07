package boot;

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
    private String threadName;       // thread name (countermeausre name)
    private BootParams bootParams;   // BootParams instance
    private double cellSize;         // cell size
    private double mtpScale;         // scale that determines mtp function
    private int interval;            // query points in the middle
    private String directory;        // output directory

    public Demo(BootParams bp, double cs, double scale, int inter, String dir){
    	this.threadName ="New Demo";
    	this.bootParams = bp;
        this.cellSize = cs;
        this.mtpScale = scale;
        this.interval = inter;
        this.directory = dir;
    }

    @Override
    public void run() {
        System.out.println("Running " +  threadName );
        try {
        	// program goes here
        	if (bootParams.containsCM("NOCOUNTERMEASURE")) {
        		Simulation sim = new Simulation(bootParams, cellSize, mtpScale, interval, directory);
        		sim.multipleSimulation();
        		if (bootParams.plotGooglMapNO()) {
        			sim.singleSimulation();
        		}
        	}
        	if (bootParams.containsCM("ADDITIVENOISE")) {
        		SimAdditiveNoise sim = new SimAdditiveNoise(bootParams, cellSize, mtpScale, interval, directory);
        		sim.multipleSimulation();
        		if (bootParams.plotGooglMapAD()) {
        			sim.singleSimulation();
        		}
        		if (bootParams.tradeOffAD()) {
        			sim.tradeOffCurve();
        		}
        	}
        	if (bootParams.containsCM("TRANSFIGURATION")) {
        		SimTransfiguration sim = new SimTransfiguration(bootParams, cellSize, mtpScale, interval, directory);
        		sim.multipleSimulation();
        		if (bootParams.plotGooglMapTF()) {
        			sim.singleSimulation();
        		}
        		if (bootParams.tradeOffTF()) {
        			sim.tradeOffCurve();
        		}
        	}
        	if (bootParams.containsCM("KANONYMITY")) {
        		SimKAnonymity sim = new SimKAnonymity(bootParams, cellSize, mtpScale, interval, directory);
        		sim.multipleSimulation();
        		if (bootParams.plotGooglMapKA()) {
        			sim.singleSimulation();
        		}
        	}
        	if (bootParams.containsCM("KCLUSTERING")) {
        		SimKClustering sim = new SimKClustering(bootParams, cellSize, mtpScale, interval, directory);
        		sim.multipleSimulation();
        		if (bootParams.plotGooglMapKC()) {
        			sim.singleSimulation();
        		}
        	}
        	// plot ic vs q
        	if (!CmpPlot.plot(bootParams.containsCM("NOCOUNTERMEASURE"), bootParams.containsCM("ADDITIVENOISE"), bootParams.containsCM("TRANSFIGURATION"), bootParams.containsCM("KANONYMITY"), bootParams.containsCM("KCLUSTERING"))) {
        		System.out.println("Plot ic vs q failed");
        		return;
        	}
        	// plot google map
        	if (!MatPlot.plot(bootParams.getNumberOfChannels(), bootParams.getNorthLat(), bootParams.getSouthLat(), bootParams.getWestLng(), bootParams.getEastLng(), 
        			bootParams.plotGooglMapNO(), bootParams.plotGooglMapAD(), bootParams.plotGooglMapTF(), bootParams.plotGooglMapKA(), bootParams.plotGooglMapKC())) {
        		System.out.println("Plot Google Maps failed");
        		return;
        	}
        	// plot tradeOff curve
        	if (!TradeOffPlot.plot(bootParams.tradeOffAD(), bootParams.tradeOffTF())) {
        		System.out.println("Plot trade-off curves failed");
        		return;
        	}
        	// send email
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