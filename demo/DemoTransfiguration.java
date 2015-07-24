package demo;

import simulation.SimTransfiguration;
import boot.BootParams;

public class DemoTransfiguration {
	SimTransfiguration sim;

	public DemoTransfiguration(BootParams bp, double cs, double scale, int inter, String dir) {
		sim = new SimTransfiguration(bp, cs, scale, 10, dir); // need more points when plotting transfiguration
	}

	public void run() {
		System.out.println("Simulation start wtih transfiguration!");
		/* check if countermeasure is transfiguration */
		if (!sim.getCountermeasure().equals("TRANSFIGURATION")) return;
		/* run query for once */
		if (sim.isFeasible()) sim.singleSimulation();
		/* run different queries for multiple times and compute average ic */ 
		if (sim.isFeasible()) sim.multipleSimulation();
		/* print probability and location of pu for each channel */
		if (sim.isFeasible()) sim.printSingle(null, null, null);
		/* print average ic for different queries */
		if (sim.isFeasible()) sim.printMultiple(null, null, null, null);
		/* plot probability on google map and IC vs Q */
		if (sim.isFeasible()) sim.plot(null, null, null);
		/* send email */
		sim.sendEmail(null, null);
	}
}