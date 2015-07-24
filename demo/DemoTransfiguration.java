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
		if (!sim.getCountermeasure().equals("TRANSFIGURATION")) {
			System.out.println("Countermeasure mismatch");
			return;
		}
		/* run query for once */
		sim.singleSimulation();
		/* run different queries for multiple times and compute average ic */ 
		sim.multipleSimulation();
		/* plot probability on google map and IC vs Q */
		sim.plot(sim.isFeasible(), sim.plotICvsQuery(), "TRANSFIGURATION");
		/* send email */
		sim.sendEmail(sim.isFeasible(), sim.plotICvsQuery());
	}
}