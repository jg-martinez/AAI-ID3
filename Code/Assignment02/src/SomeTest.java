import static org.junit.Assert.*;

import org.junit.Test;


public class SomeTest {

	@Test
	public void testcalculateEntropyDataSet() {
		assertEquals(0.940,Reader.mathEntropy(9, 5), 1);
	}
	
	@Test
	public void testcalculateEntropySunny() {
		assertEquals(0.971,Reader.mathEntropy(2, 3), 1);
	}
	@Test
	public void testcalculateEntropyOverCast() {
		assertEquals(0,Reader.mathEntropy(4, 0), 1);
	}
	@Test
	public void testcalculateEntropyRain() {
		assertEquals(0.971,Reader.mathEntropy(3, 2), 1);
	}

}
