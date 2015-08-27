package simulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import boot.BootParams;
import boot.Parser;

public class SimulationTest {

	@Test
	public void testSimulation() {
		String args = "-cd 0.005 -a 40 -90 35 -85 -c 2 -C 38.41916639395372 -87.7103271484375 38.16047628099622 -87.1937255859375 -C 38.06539235133249 -88.7764892578125 37.65773212628274 -89.0401611328125 -cm -no -1 -an 0.5 -tf 3 -ka 1 -kc 1 -gm no ad tf ka kc -tr ad tf ka kc -q 100 -e ningli@vt.edu -opt pa ";
		BootParams bp = Parser.parse(args.split(" "));
		Simulation sim = new Simulation(bp, 1, 5, "C:\\Users\\Administrator\\Desktop\\motoData\\");
		assertSame(sim.bootParams, bp);
		assertEquals(0.005, sim.cellsize, 0.001);
		assertEquals(1, sim.mtpScale, 0.001);
		assertEquals(5, sim.interval);
		assertEquals(2, sim.noc);
		assertEquals(100, sim.noq);
		assertEquals(4, sim.server.getNumberOfPUs());
	}
}
