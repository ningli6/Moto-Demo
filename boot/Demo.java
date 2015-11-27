package boot;

import java.io.File;
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
 * Demo instance without using new threads, invoked in another process
 */
public class Demo {
	
    private BootParams bp;           // BootParams instance
    private double mtpScale;         // scale that determines mtp function
    private int interval;            // query points in the middle
    private String dataDir;          // output directory
    private String plotDir;          // directory that saves plots from python and maltab
    private String emailInfo;        // feedback information of the email

    public Demo(BootParams bp, String dataDir, String plotDir){
    	this.bp = bp;
        this.mtpScale = bp.getMtpScale();
        this.interval = bp.getInterval();
        this.dataDir = dataDir;
        this.plotDir = plotDir;
    }

    public void run() {
        try {
        	if((bp.isRandomQuery() && bp.isSmartQuery()) || bp.containsCM("NOCOUNTERMEASURE")) {  // if no countermeasure is selected
        		Simulation sim = new Simulation(bp, mtpScale, interval, dataDir);
        		if (bp.isRandomQuery()) {  // if using random query
            		sim.randomSimulation();
            		if (bp.plotGooglMapNO()) {
            			sim.singleRandomSimulation();
            		}
        		}
        		if (bp.isSmartQuery()) {   // if using smart query
        			sim.smartSimulation(); // output ic as well as inference map, implicitly available to plot google map
        		}
        	}
        	if (bp.containsCM("ADDITIVENOISE")) {  // if additive noise is selected
        		SimAdditiveNoise sim = new SimAdditiveNoise(bp, mtpScale, interval, dataDir);
        		if (bp.isRandomQuery()) {    // if using random query
            		sim.randomSimulation();  // would be able to figure out if the noise level is feasible here
            		if (sim.isFeasible() && bp.plotGooglMapAD()) {
            			sim.singleRandomSimulation();
            		}
            		if (!sim.isFeasible()) {   // if noise level is not feasible, do not plot anything with additive noise
            			bp.delCountermeasure("ADDITIVENOISE");
            			bp.setGoogleMapAD(false);
            			emailInfo += "Noise requirement can't be reached.\n\n";
            		}
            		if (bp.isTradeOffAD()) {  // plot trade off bar of additive noise anyway if user selected
            			sim.randomTradeOffCurve();
            		}
        		}
        		if (bp.containsCM("ADDITIVENOISE") && bp.isSmartQuery()) {
            		sim.smartSimulation();
//            		sim.testSmartSimulation(20);
            		if (!sim.isFeasible()) {   // if noise level is not feasible, do not plot anything with additive noise
            			bp.delCountermeasure("ADDITIVENOISE");
            			bp.setGoogleMapAD(false);
            			emailInfo += "Noise requirement can't be reached.\n\n";
            		}
            		if (bp.isTradeOffAD()) {  // plot trade off bar using smart location algorithm
            			sim.smartTradeOffCurve();
            		}
        		}
        	}
        	if (bp.containsCM("TRANSFIGURATION")) {  // user selected transfiguration
        		SimTransfiguration sim = new SimTransfiguration(bp, mtpScale, interval, dataDir);
        		if (!sim.isFeasible()) {  // check feasibility
        			bp.delCountermeasure("TRANSFIGURATION");
        			bp.setGoogleMapTF(false);
        			emailInfo += "Number of sides must be a positive integer equal or grater than 3.\n\n";
        		}
        		if (bp.isRandomQuery()) {  // if using random query
        			if (sim.isFeasible()) sim.randomSimulation();
        			if (sim.isFeasible() && bp.plotGooglMapTF()) {
        				sim.singleRandomSimulation();
        			}
        			if (bp.isTradeOffTF()) {
        				sim.randomTradeOffCurve();
        			}
        		}
        		if (bp.isSmartQuery()) {  // if using smart query
        			if (sim.isFeasible()) sim.smartSimulation();
        			if (bp.isTradeOffTF()) {
        				sim.smartTradeOffCurve();  // trade off bar using smart query
        			}
        		}
        	}
        	if (bp.containsCM("KANONYMITY")) {  // user selects k anonymity
        		SimKAnonymity sim = new SimKAnonymity(bp, mtpScale, interval, dataDir);
        		if (!sim.isFeasible()) {   // if k is not feasible, do not plot anything with additive noise
        			bp.delCountermeasure("KANONYMITY");
        			bp.setGoogleMapKA(false);
        			emailInfo += "K must be a positive integer.\n\n";
        		}
        		if (bp.isRandomQuery()) {  // if using random query
            		if (sim.isFeasible()) sim.randomSimulation();
            		if (sim.isFeasible() && bp.plotGooglMapKA()) {
            			sim.singleRandomSimulation();
            		}
            		if (bp.isTradeOffKA()) {  // plot trade off bar anyway
            			sim.randomTradeOffBar();
            		}
        		}
        		if (bp.isSmartQuery()) {  // if using smart query
        			if (sim.isFeasible()) sim.smartSimulation();
            		if (bp.isTradeOffKA()) {  // plot trade off bar with smart query
            			sim.smartTradeOffBar();
            		}
        		}
        	}
        	if (bp.containsCM("KCLUSTERING")) { // if k cluster is selected
        		SimKClustering sim = new SimKClustering(bp, mtpScale, interval, dataDir);
        		if (!sim.isFeasible()) {   // if k is not feasible, do not plot anything with additive noise
        			bp.delCountermeasure("KCLUSTERING");
        			bp.setGoogleMapKC(false);
        			emailInfo += "K must be a positive integer.\n\n";
        		}
        		if (bp.isRandomQuery()) {  // using random query
        			if (sim.isFeasible()) sim.randomSimulation();
            		if (sim.isFeasible() && bp.plotGooglMapKC()) {
            			sim.singleRandomSimulation();
            		}
            		if (bp.isTradeOffKC()) {  // plot trade off bar anyway
            			sim.randomTradeOffBar();
            		}
        		}
        		if (bp.isSmartQuery()) {  // using smart query
        			if (sim.isFeasible()) sim.smartSimulation();
            		if (bp.isTradeOffKC()) {  // plot trade off bar using smart query
            			sim.smartTradeOffBar();
            		}
        		}
        	}
        	
        	/* plot ic vs q */
        	if (bp.isRandomQuery()) {
            	if (!CmpPlot.plotRandom(this.dataDir, this.plotDir, bp.containsCM("NOCOUNTERMEASURE"), bp.containsCM("ADDITIVENOISE"), bp.containsCM("TRANSFIGURATION"), bp.containsCM("KANONYMITY"), bp.containsCM("KCLUSTERING"))) {
            		System.out.println("Plot randomly queried ic vs q failed");
            		return;
            	}
        	}
        	if (bp.isSmartQuery()) {
            	if (!CmpPlot.plotSmart(this.dataDir, this.plotDir, bp.containsCM("NOCOUNTERMEASURE"), bp.containsCM("ADDITIVENOISE"), bp.containsCM("TRANSFIGURATION"), bp.containsCM("KANONYMITY"), bp.containsCM("KCLUSTERING"))) {
            		System.out.println("Plot smart queried ic vs q failed");
            		return;
            	}
        	}
        	if (bp.isRandomQuery() && bp.isSmartQuery()) { // user selected both
        		// plot ic vs q without countermeasure compared with both method
            	if (!CmpPlot.plotRandom_vs_Smart(this.dataDir, this.plotDir)) {
            		System.out.println("Plot ic vs q compared between random queries and smart queries failed");
            		return;
            	}
            	//  plot bar for each countermeasure
            	if (!CmpPlot.plotRandomSmartBar(this.dataDir, this.plotDir, bp.containsCM("NOCOUNTERMEASURE"), bp.containsCM("ADDITIVENOISE"), bp.containsCM("TRANSFIGURATION"), bp.containsCM("KANONYMITY"), bp.containsCM("KCLUSTERING"))) {
            		System.out.println("Plot smart queried ic vs q failed");
            		return;
            	}
        	}
        	
        	/* plot tradeOff curve/bar */
        	if (bp.isRandomQuery()) {
            	if (!TradeOffPlot.plotRandom(this.dataDir, this.plotDir, bp.isTradeOffAD(), bp.isTradeOffTF(), bp.isTradeOffKA(), bp.isTradeOffKC())) {
            		System.out.println("Plot trade-off failed");
            		return;
            	}
        	}
        	if (bp.isSmartQuery()) {
            	if (!TradeOffPlot.plotSmart(this.dataDir, this.plotDir, bp.isTradeOffAD(), bp.isTradeOffTF(), bp.isTradeOffKA(), bp.isTradeOffKC())) {
            		System.out.println("Plot trade-off failed");
            		return;
            	}
        	}

//        	/* plot google map, just working for random queries */
        	if (!MatPlot.plot(this.dataDir, this.plotDir, bp.getCellSize(), bp.getNumberOfChannels(), bp.getNorthLat(), 
        			bp.getSouthLat(), bp.getWestLng(), bp.getEastLng(), bp.isRandomQuery(), bp.isSmartQuery(), bp.plotGooglMapNO(), bp.plotGooglMapAD(), bp.plotGooglMapTF(), bp.plotGooglMapKA(), bp.plotGooglMapKC())) {
        		System.out.println("Plot Google Maps failed");
        		return;
        	}
    	
        	/* send email, works only for random queries */
        	if (bp.getInputParams()) {
        		BuildText.printText(plotDir, "emailInfo.txt", bp.paramsToTextFile());
        	}
        	if (!SendEmail.send(plotDir, "ningli@vt.edu", bp.getEmail(), emailInfo, 
        			bp.getNumberOfChannels(), true, bp.plotGooglMapNO(), bp.plotGooglMapAD(), bp.plotGooglMapTF(), bp.plotGooglMapKA(), 
        			bp.plotGooglMapKC(), bp.isTradeOffAD(), bp.isTradeOffTF(), bp.isTradeOffKA(), 
        			bp.isTradeOffKC(), bp.getInputParams())) {
        		System.out.println("Sending email failed");
        		return;
        	}
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
}
