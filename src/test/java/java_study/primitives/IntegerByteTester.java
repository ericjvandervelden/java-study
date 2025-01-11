package java_study.primitives;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class IntegerByteTester {
	
	/*
	 * lees https://stackoverflow.com/questions/11380062/what-does-value-0xff-do-in-java
	 * The point is that conversion to int happens before the & operator is applied.
	 */

	@Test
	public void test() {
		assertTrue(-1 == 0xffffffff);
		assertFalse(-1 == 0xff);
		assertTrue((byte) -1 == (byte) 0xff);
		assertTrue((-1 & 0xff) == 255);
		assertTrue(((byte) -1 & 0xff) == 255);
		assertTrue(((int) -1 & 0xff) == 255);
		assertTrue((-1 & 0xff) == 0x000000ff);
		assertFalse((byte) -1 == (-1 & 0xff));
		assertTrue((byte) -1 == (byte) (-1 & 0xff));
		assertFalse(-1 == 0xff);
		assertTrue(255 == 0xff);

		assertTrue((byte) -1 == (byte) 0xff);
		assertFalse((byte) -1 == 0xff);
		assertTrue((byte) -1 == -1);
		assertTrue((byte) -1 == (int) -1);
		assertFalse(0xffffffff == 0xff);
		assertTrue(0xffffffff == -1);
		assertTrue(0xffffffff == (byte) 0xff);
		assertTrue((-2 & 0xff) == 254);
		assertTrue(-2 == 0xfffffffe);

		assertTrue((-2 & 0xffff) == 65534);
		assertTrue((((byte) -2) & 0xff) == 254);
		assertFalse((byte) -2 == 0xfe);
		assertFalse((byte) -2 == 0xfffe);

	}

}
