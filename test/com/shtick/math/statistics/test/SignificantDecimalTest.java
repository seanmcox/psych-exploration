package com.shtick.math.statistics.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.shtick.math.statistics.SignificantDecimal;

class SignificantDecimalTest {
	@Test
	void testDoubleConstructor() {
		{ // Simple case
			SignificantDecimal sd = new SignificantDecimal(Math.PI,2);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("3.1x10^0",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Simple case, but more digits, rounding down.
			SignificantDecimal sd = new SignificantDecimal(Math.PI,3);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("3.14x10^0",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Simple case, but more digits, rounding up.
			SignificantDecimal sd = new SignificantDecimal(Math.PI,4);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("3.142x10^0",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Simple case, but different magnitude.
			SignificantDecimal sd = new SignificantDecimal(Math.PI*10,2);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("3.1x10^1",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Simple case, but negative.
			SignificantDecimal sd = new SignificantDecimal(-Math.PI,2);
			try {
				assertTrue(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("-3.1x10^0",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Extreme rounding.
			SignificantDecimal sd = new SignificantDecimal(9.9999,2);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("1.0x10^1",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Trim excess zeros
			SignificantDecimal sd = new SignificantDecimal(500.0,1);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("5x10^2",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Add extra zeros
			SignificantDecimal sd = new SignificantDecimal(500.0,5);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("5.0000x10^2",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testIntConstructor() {
		{ // Simple extreme case
			SignificantDecimal sd = new SignificantDecimal(Long.MAX_VALUE,2);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("9.2x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Simple extreme case, with more digits
			SignificantDecimal sd = new SignificantDecimal(Long.MAX_VALUE,19);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("9.223372036854775807x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Test rounding up.
			SignificantDecimal sd = new SignificantDecimal(Long.MAX_VALUE,18);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("9.22337203685477581x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Asking for more digits than the number has.
			SignificantDecimal sd = new SignificantDecimal(Long.MAX_VALUE,20);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("9.2233720368547758070x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Simple less extreme case
			SignificantDecimal sd = new SignificantDecimal(Integer.MAX_VALUE,2);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("2.1x10^9",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Simple extreme case, but negative.
			SignificantDecimal sd = new SignificantDecimal(-Long.MAX_VALUE,2);
			try {
				assertTrue(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("-9.2x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Extreme negative case.
			SignificantDecimal sd = new SignificantDecimal(Long.MIN_VALUE,19);
			try {
				assertTrue(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("-9.223372036854775808x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Extreme negative case; asking for extra digits.
			SignificantDecimal sd = new SignificantDecimal(Long.MIN_VALUE,20);
			try {
				assertTrue(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("-9.2233720368547758080x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Trim excess zeros
			SignificantDecimal sd = new SignificantDecimal(500,1);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("5x10^2",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Add extra zeros
			SignificantDecimal sd = new SignificantDecimal(500,5);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("5.0000x10^2",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testExactableConstructor() {
		{ // Simple extreme case
			SignificantDecimal sd = new SignificantDecimal(Long.MAX_VALUE,true);
			try {
				assertFalse(sd.isNegative());
				assertTrue(sd.isExact());
				assertEquals("[9.223372036854775807]x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Simple extreme case, with more digits
			SignificantDecimal sd = new SignificantDecimal(Long.MAX_VALUE,false);
			try {
				assertFalse(sd.isNegative());
				assertFalse(sd.isExact());
				assertEquals("9.223372036854775807x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Simple less extreme case
			SignificantDecimal sd = new SignificantDecimal(Integer.MAX_VALUE,true);
			try {
				assertFalse(sd.isNegative());
				assertTrue(sd.isExact());
				assertEquals("[2.147483647]x10^9",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Simple extreme case, but negative.
			SignificantDecimal sd = new SignificantDecimal(-Long.MAX_VALUE,true);
			try {
				assertTrue(sd.isNegative());
				assertTrue(sd.isExact());
				assertEquals("[-9.223372036854775807]x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Extreme negative case.
			SignificantDecimal sd = new SignificantDecimal(Long.MIN_VALUE,true);
			try {
				assertTrue(sd.isNegative());
				assertTrue(sd.isExact());
				assertEquals("[-9.223372036854775808]x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Trailing zero elimination?
			SignificantDecimal sd = new SignificantDecimal(500,true);
			try {
				assertFalse(sd.isNegative());
				assertTrue(sd.isExact());
				assertEquals("[5]x10^2",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}
	
	@Test
	void testComparison() {
		{ // Self compare case
			SignificantDecimal sd = new SignificantDecimal(Math.PI, 5);
			try {
				assertTrue(sd.compareTo(sd)==0);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Opposite sign cases
			SignificantDecimal sdp = new SignificantDecimal(Math.PI, 5);
			SignificantDecimal sdn = new SignificantDecimal(-Math.PI, 5);
			try {
				assertTrue(sdp.compareTo(sdn)>0);
				assertTrue(sdn.compareTo(sdp)<0);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Positive with different values case
			SignificantDecimal sdg = new SignificantDecimal(Math.PI, 5);
			SignificantDecimal sdl = new SignificantDecimal(Math.PI-1, 5);
			try {
				assertTrue(sdg.compareTo(sdl)>0);
				assertTrue(sdl.compareTo(sdg)<0);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Negative with different values case
			SignificantDecimal sdg = new SignificantDecimal(Math.PI, 5).getNegative();
			SignificantDecimal sdl = new SignificantDecimal(Math.PI-1, 5).getNegative();
			try {
				assertTrue(sdg.compareTo(sdl)<0);
				assertTrue(sdl.compareTo(sdg)>0);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Same value in different objects
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 5);
			SignificantDecimal sd2 = new SignificantDecimal(Math.PI, 5);
			try {
				assertTrue(sd1.compareTo(sd2)==0);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact mismatch case
			SignificantDecimal sde = new SignificantDecimal(3, true);
			SignificantDecimal sdi = new SignificantDecimal(3, false);
			try {
				assertTrue(sdi.compareTo(sde)>0);
				assertTrue(sde.compareTo(sdi)<0);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}
	
	@Test
	void testNegative() {
		{ // Simple extreme case
			SignificantDecimal sd = new SignificantDecimal(Long.MAX_VALUE,true).getNegative();
			try {
				assertTrue(sd.isNegative());
				assertTrue(sd.isExact());
				assertEquals("[-9.223372036854775807]x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Opposite extreme case
			SignificantDecimal sd = new SignificantDecimal(Long.MIN_VALUE,true).getNegative();
			try {
				assertFalse(sd.isNegative());
				assertTrue(sd.isExact());
				assertEquals("[9.223372036854775808]x10^18",sd.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testMultiply() {
		{ // Simple case
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.PI, 1);
			try {
				SignificantDecimal m = sd1.multiply(sd2);
				assertEquals(new SignificantDecimal(9.0, 1),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Different significant digits case
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.PI, 2);
			try {
				SignificantDecimal m = sd1.multiply(sd2);
				assertEquals(new SignificantDecimal(9.7, 2),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Negative case
			SignificantDecimal sd1 = new SignificantDecimal(-Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(-Math.PI, 2);
			try {
				SignificantDecimal m = sd1.multiply(sd2);
				assertEquals(new SignificantDecimal(9.7, 2),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Cross-sign case
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(-Math.PI, 2);
			try {
				SignificantDecimal m = sd1.multiply(sd2);
				assertEquals(new SignificantDecimal(-9.7, 2),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Inexact times exact case
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(3, true);
			try {
				SignificantDecimal m = sd1.multiply(sd2);
				assertEquals(new SignificantDecimal(9.426, 4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Inexact times exact with a carry in the leading place
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(4, true);
			try {
				SignificantDecimal m = sd1.multiply(sd2);
				assertEquals(new SignificantDecimal(12.57, 4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact times exact case
			SignificantDecimal sd1 = new SignificantDecimal(51, true);
			SignificantDecimal sd2 = new SignificantDecimal(3, true);
			try {
				SignificantDecimal m = sd1.multiply(sd2);
				assertEquals(new SignificantDecimal(153, true),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Test extreme rounding
			SignificantDecimal sd1 = new SignificantDecimal(3.3333, 4);
			SignificantDecimal sd2 = new SignificantDecimal(3.0, 2);
			try {
				SignificantDecimal m = sd1.multiply(sd2);
				assertEquals(new SignificantDecimal(10.0, 2),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testDivide() {
		{ // Simple case (this also requires basic rounding up of the final decimal)
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.E, 4);
			try {
				SignificantDecimal m = sd1.divide(sd2);
				assertEquals(new SignificantDecimal(Math.PI/Math.E,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Negative case
			SignificantDecimal sd1 = new SignificantDecimal(-Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(-Math.E, 4);
			try {
				SignificantDecimal m = sd1.divide(sd2);
				assertEquals(new SignificantDecimal(Math.PI/Math.E,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Cross-sign case
			SignificantDecimal sd1 = new SignificantDecimal(-Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.E, 4);
			try {
				SignificantDecimal m = sd1.divide(sd2);
				assertEquals(new SignificantDecimal(-Math.PI/Math.E,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Mismatched significant digits
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.E, 2);
			try {
				SignificantDecimal m = sd1.divide(sd2);
				assertEquals(new SignificantDecimal(Math.PI/Math.E,2),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Fancier rounding up
			SignificantDecimal sd1 = new SignificantDecimal(0.9999, 4);
			SignificantDecimal sd2 = new SignificantDecimal(1.0, 2);
			try {
				SignificantDecimal m = sd1.divide(sd2);
				assertEquals(new SignificantDecimal(1.0,2),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact case
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(2,true);
			try {
				SignificantDecimal m = sd1.divide(sd2);
				assertEquals(new SignificantDecimal(Math.PI/2,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Double-exact case
			SignificantDecimal sd1 = new SignificantDecimal(3,true);
			SignificantDecimal sd2 = new SignificantDecimal(2,true);
			try {
				SignificantDecimal m = sd1.divide(sd2);
				assertEquals("[1.5]x10^0",m.toString());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		
		{ // Divide by zero
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			try {
				SignificantDecimal m = sd1.divide(sd1.getZero());
				fail("Expected exception");
			}
			catch(Throwable t) {
				// Expected case.
			}
		}

		{ // Divide by inexact zero
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(0.0, 4);
			try {
				SignificantDecimal m = sd1.divide(sd2);
				fail("Expected exception");
			}
			catch(Throwable t) {
				// Expected case.
			}
		}

		{ // Divide exact zero
			SignificantDecimal sd1 = new SignificantDecimal(0,true);
			SignificantDecimal sd2 = new SignificantDecimal(Math.E, 4);
			try {
				SignificantDecimal m = sd1.divide(sd2);
				assertEquals(new SignificantDecimal(0,true),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Divide inexact zero
			SignificantDecimal sd1 = new SignificantDecimal(0.0,4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.E, 4);
			try {
				SignificantDecimal m = sd1.divide(sd2);
				assertEquals(new SignificantDecimal(0.0,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Divide equal numbers
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.PI, 4);
			try {
				SignificantDecimal m = sd1.divide(sd2);
				assertEquals(new SignificantDecimal(1.0,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Test extreme rounding
			SignificantDecimal sd1 = new SignificantDecimal(9.999, 4);
			SignificantDecimal sd2 = new SignificantDecimal(10.0, 2);
			try {
				SignificantDecimal m = sd1.divide(sd2);
				assertEquals(new SignificantDecimal(1.0,2),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testAdd() {
		{ // Simple case
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.E, 4);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(5.860,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Negative case
			SignificantDecimal sd1 = new SignificantDecimal(-Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(-Math.E, 4);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(-5.860,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Positive+negative case
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(-Math.E, 4);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(0.424,3),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Negative+positive case
			SignificantDecimal sd1 = new SignificantDecimal(-Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.E, 4);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(-0.424,3),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Scale mismatch case (needs to round up that last digit to get the right answer)
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI*10, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.E, 4);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(34.14,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Scale mismatch case with a negative and a round up
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI*10, 4);
			SignificantDecimal sd2 = new SignificantDecimal(-2.712, 4);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(28.71,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Extreme scale mismatch case
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI*1000, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.E/1000, 4);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(3142.0,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Dueling zeros
			SignificantDecimal sd1 = new SignificantDecimal(new int[0],1,false, false);
			SignificantDecimal sd2 = new SignificantDecimal(new int[0],-1,false, false);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(sd1,m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Zero vs insignificant non-zero
			SignificantDecimal sd1 = new SignificantDecimal(new int[0],1,false, false);
			SignificantDecimal sd2 = new SignificantDecimal(new int[] {1},-1,false, false);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(sd1,m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Zero vs significant non-zero
			SignificantDecimal sd1 = new SignificantDecimal(new int[0],1,false, false);
			SignificantDecimal sd2 = new SignificantDecimal(new int[] {1},1,false, false);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(sd2,m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Zero vs boundary crossing non-zero
			SignificantDecimal sd1 = new SignificantDecimal(new int[0],1,false, false);
			SignificantDecimal sd2 = new SignificantDecimal(new int[] {1,2,3},2,false, false);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(new int[] {1,2},2,false, false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact zero vs boundary crossing non-zero
			SignificantDecimal sd1 = new SignificantDecimal(new int[0],1,false, true);
			SignificantDecimal sd2 = new SignificantDecimal(new int[] {1,2,3},2,false, false);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(sd2,m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Zero vs boundary crossing exact non-zero
			SignificantDecimal sd1 = new SignificantDecimal(new int[0],1,false, false);
			SignificantDecimal sd2 = new SignificantDecimal(new int[] {1,2,3},2,false, true);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(new int[] {1,2},2,false, false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact plus exact
			SignificantDecimal sd1 = new SignificantDecimal(5,true);
			SignificantDecimal sd2 = new SignificantDecimal(2,true);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(7, true),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact plus exact at different scales
			SignificantDecimal sd1 = new SignificantDecimal(500,true);
			SignificantDecimal sd2 = new SignificantDecimal(2,true);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(502, true),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact minus exact at different scales
			SignificantDecimal sd1 = new SignificantDecimal(500,true);
			SignificantDecimal sd2 = new SignificantDecimal(-2,true);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(498, true),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact plus inexact
			SignificantDecimal sd1 = new SignificantDecimal(5,true);
			SignificantDecimal sd2 = new SignificantDecimal(2,false);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(7, false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Inexact plus exact
			SignificantDecimal sd1 = new SignificantDecimal(5,false);
			SignificantDecimal sd2 = new SignificantDecimal(2,true);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(7, false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact plus inexact at different scales
			SignificantDecimal sd1 = new SignificantDecimal(500,true);
			SignificantDecimal sd2 = new SignificantDecimal(2,false);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(502, false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Inexact plus exact at different scales
			SignificantDecimal sd1 = new SignificantDecimal(new int[] {5},2,false,false);
			SignificantDecimal sd2 = new SignificantDecimal(2,true);
			try {
				SignificantDecimal m = sd1.add(sd2);
				assertEquals(new SignificantDecimal(new int[] {5},2,false,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}


	@Test
	void testSubtract() {
		{ // Simple case
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.E, 4);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(0.424,3),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Negative case
			SignificantDecimal sd1 = new SignificantDecimal(-Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(-Math.E, 4);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(-0.424,3),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Positive+negative case
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(-Math.E, 4);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(5.860,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Negative+positive case
			SignificantDecimal sd1 = new SignificantDecimal(-Math.PI, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.E, 4);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(-5.860,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Scale mismatch case (needs to round up that last digit to get the right answer)
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI*10, 4);
			SignificantDecimal sd2 = new SignificantDecimal(2.712, 4);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(28.71,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Scale mismatch case with a negative and a round up
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI*10, 4);
			SignificantDecimal sd2 = new SignificantDecimal(-Math.E, 4);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(34.14,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Extreme scale mismatch case
			SignificantDecimal sd1 = new SignificantDecimal(Math.PI*1000, 4);
			SignificantDecimal sd2 = new SignificantDecimal(Math.E/1000, 4);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(3142.0,4),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Dueling zeros
			SignificantDecimal sd1 = new SignificantDecimal(new int[0],1,false, false);
			SignificantDecimal sd2 = new SignificantDecimal(new int[0],-1,false, false);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(sd1,m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Zero vs insignificant non-zero
			SignificantDecimal sd1 = new SignificantDecimal(new int[0],1,false, false);
			SignificantDecimal sd2 = new SignificantDecimal(new int[] {1},-1,false, false);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(sd1,m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Zero vs significant non-zero
			SignificantDecimal sd1 = new SignificantDecimal(new int[0],1,false, false);
			SignificantDecimal sd2 = new SignificantDecimal(new int[] {1},1,false, false);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(sd2.getNegative(),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Zero vs boundary crossing non-zero
			SignificantDecimal sd1 = new SignificantDecimal(new int[0],1,false, false);
			SignificantDecimal sd2 = new SignificantDecimal(new int[] {1,2,3},2,false, false);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(new int[] {1,2},2,true, false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact zero vs boundary crossing non-zero
			SignificantDecimal sd1 = new SignificantDecimal(new int[0],1,false, true);
			SignificantDecimal sd2 = new SignificantDecimal(new int[] {1,2,3},2,false, false);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(sd2.getNegative(),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Zero vs boundary crossing exact non-zero
			SignificantDecimal sd1 = new SignificantDecimal(new int[0],1,false, false);
			SignificantDecimal sd2 = new SignificantDecimal(new int[] {1,2,3},2,false, true);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(new int[] {1,2},2,true, false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact minus exact
			SignificantDecimal sd1 = new SignificantDecimal(5,true);
			SignificantDecimal sd2 = new SignificantDecimal(2,true);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(3, true),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact minus exact at different scales
			SignificantDecimal sd1 = new SignificantDecimal(500,true);
			SignificantDecimal sd2 = new SignificantDecimal(2,true);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(498, true),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact plus exact at different scales
			SignificantDecimal sd1 = new SignificantDecimal(500,true);
			SignificantDecimal sd2 = new SignificantDecimal(-2,true);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(502, true),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact minus inexact
			SignificantDecimal sd1 = new SignificantDecimal(5,true);
			SignificantDecimal sd2 = new SignificantDecimal(2,false);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(3, false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Inexact minus exact
			SignificantDecimal sd1 = new SignificantDecimal(5,false);
			SignificantDecimal sd2 = new SignificantDecimal(2,true);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(3, false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Exact minus inexact at different scales
			SignificantDecimal sd1 = new SignificantDecimal(500,true);
			SignificantDecimal sd2 = new SignificantDecimal(2,false);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(498, false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Inexact minus exact at different scales
			SignificantDecimal sd1 = new SignificantDecimal(new int[] {5},2,false,false);
			SignificantDecimal sd2 = new SignificantDecimal(2,true);
			try {
				SignificantDecimal m = sd1.subtract(sd2);
				assertEquals(new SignificantDecimal(new int[] {5},2,false,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	/*
	@Test
	void testSqrt() {
		{ // Simple case
			Quadruple q = new Quadruple(25);
			try {
				Quadruple m = q.sqrt();
				assertEquals("5",m.toString());
				assertEquals(new Quadruple(25), q, "q not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Simple <1 case
			Quadruple q = new Quadruple(0.25);
			try {
				Quadruple m = q.sqrt();
				assertEquals("5*10^-1",m.toString());
				assertEquals(new Quadruple(0.25), q, "q not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Negative failure case
			Quadruple q = new Quadruple(-5);
			try {
				Quadruple m = q.sqrt();
				fail("Failure expected");
				assertEquals(new Quadruple(-5), q, "q not modified");
			}
			catch(Throwable t) {
				// Expected result.
			}
		}

		{ // Large case
			Quadruple q = new Quadruple(100000000);
			try {
				Quadruple m = q.sqrt();
				assertEquals("1*10^4",m.toString());
				assertEquals(new Quadruple(100000000), q, "q not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Large significant digit case
			Quadruple q = new Quadruple(10246.125*10246.125);
			try {
				Quadruple m = q.sqrt();
				assertTrue(m.toString().matches("^1.024612(?:(?:49[0-9]*)|(?:5(?:0[0-9]*)?))\\*10\\^4$"));
				assertEquals(new Quadruple(10246.125*10246.125), q, "q not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		
		{ // A case that was proving troublesome
			Quadruple q = new Quadruple(2.236067977499789);
			try {
				Quadruple m = q.sqrt();
				assertEqualsWithinMargin(new Quadruple(1.4953487812), m, "Square root of 2.236067977499789 found.");
				assertEquals(new Quadruple(2.236067977499789), q, "q not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testLog10() {
		{ // Simple case
			Quadruple q = new Quadruple(25);
			try {
				Quadruple m = q.log10();
				assertEqualsWithinMargin(new Quadruple(1.3979400086720376),m,"Log10(25) result as expected.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Simple <1 case
			Quadruple q = new Quadruple(0.25);
			try {
				Quadruple m = q.log10();
				assertEqualsWithinMargin(new Quadruple(-0.602059991327),m,"Log10(0.25) result as expected.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Negative failure case
			Quadruple q = new Quadruple(-5);
			try {
				Quadruple m = q.log10();
				fail("Failure expected");
			}
			catch(Throwable t) {
				// Expected result.
			}
		}

		{ // Large case
			Quadruple q = new Quadruple(100000000);
			try {
				Quadruple m = q.log10();
				assertEqualsWithinMargin(new Quadruple(8),m,"Log10(100000000) result as expected.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Large significant digit case
			Quadruple q = new Quadruple(10246.125*10246.125);
			try {
				Quadruple m = q.log10();
				assertEqualsWithinMargin(new Quadruple(8.0211192996),m,"Log10("+(10246.125*10246.125)+") result as expected.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testPow() {
		{ // n^0 case
			Quadruple q = new Quadruple(25);
			try {
				Quadruple m = q.pow(q.getZero());
				assertEquals(q.getIdentity(),m,"25^0 result as expected.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // n^1
			Quadruple q = new Quadruple(25);
			try {
				Quadruple m = q.pow(q.getIdentity());
				assertEquals(q,m,"25^1 result as expected.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 1^n
			Quadruple q = new Quadruple(1);
			try {
				Quadruple m = q.pow(new Quadruple(25));
				assertEquals(q,m,"1^25 result as expected.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 0^n
			Quadruple q = new Quadruple(0);
			try {
				Quadruple m = q.pow(new Quadruple(25));
				assertEquals(q,m,"0^25 result as expected.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 0^-n
			Quadruple q = new Quadruple(0);
			try {
				Quadruple m = q.pow(new Quadruple(25).getNegative());
				fail("0^-25 should fail.");
			}
			catch(Throwable t) {
				// Expected failure.
			}
		}

		{ // -25^1/n
			Quadruple q = new Quadruple(-25);
			try {
				Quadruple m = q.pow(new Quadruple(0.25));
				fail("-25^1/n should fail.");
			}
			catch(Throwable t) {
				// Expected failure.
			}
		}
		
		{ // 25^2
			Quadruple q = new Quadruple(25);
			try {
				Quadruple m = q.pow(new Quadruple(2));
				assertEqualsWithinMargin(new Quadruple(25*25),m,"25^2 result as expected.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		
		{ // 25^0.5
			Quadruple q = new Quadruple(25);
			try {
				Quadruple m = q.pow(new Quadruple(0.5));
				assertEqualsWithinMargin(q.sqrt(),m,"25^0.5 result as expected.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		
		{ // 25^2.5
			Quadruple q = new Quadruple(25);
			try {
				Quadruple m = q.pow(new Quadruple(2.5));
				assertEqualsWithinMargin(new Quadruple(25*25).multiply(q.sqrt()),m,"25^2.5 result as expected.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}
	
	private static void assertEqualsWithinMargin(Quadruple expected, Quadruple actual, String message) {
		assertTrue(actual.compareTo(expected.subtract(ERROR_MARGIN_Q))>=0,message+": "+actual+">="+expected.subtract(ERROR_MARGIN_Q));
		assertTrue(actual.compareTo(expected.add(ERROR_MARGIN_Q))<=0,message+": "+actual+"<="+expected.add(ERROR_MARGIN_Q));
	}
	
	private static void assertEqualsWithinMargin(double expected, double actual, String message) {
		assertTrue(actual>=(expected-ERROR_MARGIN_D),message+": "+actual+">="+(expected-ERROR_MARGIN_D));
		assertTrue(actual<=(expected+ERROR_MARGIN_D),message+": "+actual+"<="+(expected+ERROR_MARGIN_D));
	}
	*/
}
