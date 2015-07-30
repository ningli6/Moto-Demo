package demo;

import simulation.SimAdditiveNoise;
import boot.BootParams;

public class DemoAdditiveNoise {
	SimAdditiveNoise sim;

	public DemoAdditiveNoise(BootParams bp, double cs, double scale, int inter, String dir) {
		sim = new SimAdditiveNoise(bp, cs, scale, inter, dir);
	}

	public void run() {
		System.out.println("Simulation start wtih addtive noise!");
		/* check if countermeasure is additive noise */
		if (!sim.getCountermeasure().equals("ADDITIVENOISE")) {
			System.out.println("Countermeasure mismatch");
			return;
		}
		/* run query for once */
		sim.singleSimulation();
		/* run different queries for multiple times and compute average ic */ 
		sim.multipleSimulation();
		/* plot probability on google map and IC vs Q */
		sim.plot(sim.isFeasible(), sim.plotICvsQuery(), "ADDITIVENOISE");
		/* send email */
		sim.sendEmail(sim.isFeasible(), sim.plotICvsQuery());
	}
}
