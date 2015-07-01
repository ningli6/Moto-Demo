package demo;

import simulation.Simulation;
import boot.BootParams;

public class RunNoCountermeasure {
	Simulation sim;

	public RunNoCountermeasure(BootParams bp, double cs, double scale, int inter, String dir) {
		sim = new Simulation(bp, cs, scale, inter, dir);
	}

	public void run() {
		System.out.println("Simulation start!");
		/* run query for once */
		sim.singleSimulation();
		/* run different queries for multiple times and compute average ic */ 
		sim.multipleSimulation();
		/* print probability and location of pu for each channel */
		sim.printSingle();
		/* print average ic for different queries */
		sim.printMultiple();
		/* plot probability on google map and IC vs Q */
		sim.plot();
		/* send email */
		sim.sendEmail();
	}
}