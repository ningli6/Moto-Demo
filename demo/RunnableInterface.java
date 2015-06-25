package demo;

import boot.*;

public class RunnableInterface implements Runnable {
    private Thread t;
    private String threadName;
    private BootParams bootParams;

    public RunnableInterface(BootParams bp){
        this.bootParams = bp;
        threadName = bp.countermeasure();
        // System.out.println("Creating " +  threadName );
    }
    public void run() {
        System.out.println("Running " +  threadName );
        try {
            switch (threadName) {
                case "NOCOUNTERMEASURE":
                    RunNoCountermeasure runNoCountermeasure = new RunNoCountermeasure(bootParams);
                    runNoCountermeasure.run();
                    break;
                case "ADDITIVENOISE":
                    RunAdditiveNoise runAdditiveNoise = new RunAdditiveNoise(bootParams);
                    runAdditiveNoise.run();
                    break;
                case "TRANSFIGURATION":
                    System.out.println("NOT IMPLEMENTED");
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
        // System.out.println("Starting " +  threadName );
        if (t == null)
        {
            t = new Thread (this, threadName);
            t.start();
        }
    }
}