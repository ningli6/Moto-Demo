package demo;

import boot.*;

/**
 * Handle multiple simulation requests by generating new threads for each
 */

public class RunnableInterface implements Runnable {
    private Thread t;
    private String threadName;
    private BootParams bootParams;
    private double cellsize;
    private double mtpScale;
    private int interval;
    private String directory;

    public RunnableInterface(BootParams bp, double cs, double scale, int inter, String dir){
        this.bootParams = bp;
        threadName = bp.getCountermeasure();
        this.cellsize = cs;
        this.mtpScale = scale;
        this.interval = inter;
        this.directory = dir;
    }

    /**
     * According to the name of different countermeasure,
     * call different routine
     */
    @Override
    public void run() {
        System.out.println("Running " +  threadName );
        try {
            switch (threadName) {
                case "NOCOUNTERMEASURE":
                    DemoNoCountermeasure runNoCountermeasure = new DemoNoCountermeasure(bootParams, cellsize, mtpScale, interval, directory);
                    runNoCountermeasure.run();
                    break;
                case "ADDITIVENOISE":
                    DemoAdditiveNoise runAdditiveNoise = new DemoAdditiveNoise(bootParams, cellsize, mtpScale, interval, directory);
                    runAdditiveNoise.run();
                    break;
                case "TRANSFIGURATION":
                	DemoTransfiguration runTransfiguration = new DemoTransfiguration(bootParams, cellsize, mtpScale, interval, directory);
                	runTransfiguration.run();
                    break;
                case "KANONYMITY":
                    System.out.println("NOT IMPLEMENTED");
                    break;                                    
                case "KCLUSTERING":
                    System.out.println("NOT IMPLEMENTED");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Thread " +  threadName + " interrupted.");
            e.printStackTrace();
        }
        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start ()
    {
        if (t == null)
        {
            t = new Thread (this, threadName);
            t.start();
        }
    }
}