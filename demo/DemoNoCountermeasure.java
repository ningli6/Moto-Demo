package demo;

import simulation.Simulation;
import boot.BootParams;

public class DemoNoCountermeasure {
	Simulation sim;

	public DemoNoCountermeasure(BootParams bp, double cs, double scale, int inter, String dir) {
		sim = new Simulation(bp, cs, scale, inter, dir);
	}

	public void run() {
		System.out.println("Simulation start!");
		/* run query for once */
		sim.singleSimulation();
		/* run different queries for multiple times and compute average ic */ 
		sim.multipleSimulation();
		/* plot probability on google map and IC vs Q */
		sim.plot(true, sim.plotICvsQuery(), "NOCOUNTERMEASURE");
		/* send email */
		sim.sendEmail(true, sim.plotICvsQuery());
	}
}