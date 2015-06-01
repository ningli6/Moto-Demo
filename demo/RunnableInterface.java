package demo;

import boot.*;

public class RunnableInterface implements Runnable {
    private Thread t;
    private String threadName;
    private BootParams bootParams;

    public RunnableInterface(BootParams bp){
        this.bootParams = bp;
        threadName = bp.countermeasure();
        System.out.println("Creating " +  threadName );
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
                    break;
                case "TRANSFIGURATION":
                    break;
                case "KANONYMITY":
                    break;                                    
                case "KCLUSTERING":
                    break;
            }
        } catch (Exception e) {
            System.out.println("Thread " +  threadName + " interrupted.");
        }
        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start ()
    {
        System.out.println("Starting " +  threadName );
        if (t == null)
        {
            t = new Thread (this, threadName);
            t.start();
        }
    }
}