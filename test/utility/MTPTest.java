package utility;

import static org.junit.Assert.*;

import org.junit.Test;

public class MTPTest {

	@Test
	public void testChangeMult() {
		MTP.ChangeMult(1);
		assertEquals(0, MTP.d0, 0.001);
		assertEquals(8, MTP.d1, 0.001);
		assertEquals(14, MTP.d2, 0.001);
		assertEquals(25, MTP.d3, 0.001);
	}

}
