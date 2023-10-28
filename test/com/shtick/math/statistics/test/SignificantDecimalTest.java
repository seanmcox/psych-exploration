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

	/*
	@Test
	void testMultiply() {
		{ // Simple case
			Quadruple q = new Quadruple(5*0x100000000L,0,false);
			Quadruple r = new Quadruple(4*0x100000000L,0,false);
			try {
				Quadruple m = q.multiply(r);
				assertEquals(20,m.intValue());
				assertEquals(new Quadruple(5*0x100000000L,0,false), q, "q not modified");
				assertEquals(new Quadruple(4*0x100000000L,0,false), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Negative case
			Quadruple q = new Quadruple(5*0x100000000L,0,true);
			Quadruple r = new Quadruple(4*0x100000000L,0,true);
			try {
				Quadruple m = q.multiply(r);
				assertEquals(20,m.intValue());
				assertEquals(new Quadruple(5*0x100000000L,0,true), q, "q not modified");
				assertEquals(new Quadruple(4*0x100000000L,0,true), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Cross-sign case
			Quadruple q = new Quadruple(5*0x100000000L,0,false);
			Quadruple r = new Quadruple(5*0x100000000L,0,true);
			try {
				Quadruple m = q.multiply(r);
				assertEquals(-25,m.intValue());
				assertEquals(new Quadruple(5*0x100000000L,0,false), q, "q not modified");
				assertEquals(new Quadruple(5*0x100000000L,0,true), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Scaled case
			Quadruple q = new Quadruple(5*0x100000000L,1,false);
			Quadruple r = new Quadruple(4*0x100000000L,1,false);
			try {
				Quadruple m = q.multiply(r);
				assertEquals(2000,m.intValue());
				assertEquals(new Quadruple(5*0x100000000L,1,false), q, "q not modified");
				assertEquals(new Quadruple(4*0x100000000L,1,false), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Large case
			Quadruple q = new Quadruple(5*0x100000000L,10,false);
			Quadruple r = new Quadruple(4*0x100000000L,10,false);
			try {
				Quadruple m = q.multiply(r);
				assertEquals(new Quadruple(20*0x100000000L,20,false),m);
				assertEquals(new Quadruple(5*0x100000000L,10,false), q, "q not modified");
				assertEquals(new Quadruple(4*0x100000000L,10,false), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Cross-scale case
			Quadruple q = new Quadruple(5*0x100000000L,1,false);
			Quadruple r = new Quadruple(5*0x100000000L,10,false);
			try {
				Quadruple m = q.multiply(r);
				assertEquals(2500000000000L,m.longValue());
				assertEquals(new Quadruple(5*0x100000000L,1,false), q, "q not modified");
				assertEquals(new Quadruple(5*0x100000000L,10,false), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Floating point case
			Quadruple q = new Quadruple(5*0x100000000L,-1,false);
			Quadruple r = new Quadruple(5*0x100000000L,10,false);
			try {
				Quadruple m = q.multiply(r);
				assertEquals(25000000000L,m.longValue());
				assertEquals(new Quadruple(5*0x100000000L,-1,false), q, "q not modified");
				assertEquals(new Quadruple(5*0x100000000L,10,false), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Twice floating point case
			Quadruple q = new Quadruple(5*0x100000000L,-1,false);
			Quadruple r = new Quadruple(5*0x100000000L,-1,false);
			try {
				Quadruple m = q.multiply(r);
				assertEquals(new Quadruple(25*0x100000000L,-2,false),m);
				assertEquals(new Quadruple(5*0x100000000L,-1,false), q, "q not modified");
				assertEquals(new Quadruple(5*0x100000000L,-1,false), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Twice floating point case with large upper values
			Quadruple q = new Quadruple(0x4000000000000008L,0,false);
			Quadruple r = new Quadruple(0x4000000000000008L,0,false);
			try {
				Quadruple m = q.multiply(r);
				// Note: This result has a little floating point rounding error in the least significant digit.
				// Maybe this could be improved a little by tracking LL and using this value to round the least significant binary digit up sometimes.  
				assertEquals("1000000000000003",Long.toHexString(m.longValue()),"Expected long value result.");
				assertEquals(new Quadruple(0x4000000000000008L,0,false), q, "q not modified");
				assertEquals(new Quadruple(0x4000000000000008L,0,false), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 0xX
			Quadruple q = new Quadruple(0);
			Quadruple r = new Quadruple(5);
			try {
				Quadruple m = q.multiply(r);
				assertEquals(m.getZero(),m,"Expected result.");
				assertEquals(new Quadruple(0), q, "q not modified");
				assertEquals(new Quadruple(5), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Xx0
			Quadruple q = new Quadruple(5);
			Quadruple r = new Quadruple(0);
			try {
				Quadruple m = q.multiply(r);
				assertEquals(m.getZero(),m,"Expected result.");
				assertEquals(new Quadruple(5), q, "q not modified");
				assertEquals(new Quadruple(0), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		
		{ // A case that caused some trouble
			Quadruple q = new Quadruple(-3.29621897555450565065257251262664794921875);
			Quadruple r = new Quadruple(1.18358655292489146930165588855743408203125);
			assertEquals(new Quadruple(-3.29621897555450565065257251262664794921875), q, "q not modified");
			assertEquals(new Quadruple(1.18358655292489146930165588855743408203125), r, "r not modified");
			try {
				Quadruple m = q.multiply(r);
				assertEqualsWithinMargin(new Quadruple(-3.901360454962174),m,"Expected result.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testDivide() {
		{ // Simple case
			Quadruple q = new Quadruple(5*0x100000000L,0,false);
			Quadruple r = new Quadruple(4*0x100000000L,0,false);
			try {
				Quadruple m = q.divide(r);
				assertEquals(new Quadruple(0x140000000L,0,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Negative case
			Quadruple q = new Quadruple(5*0x100000000L,0,true);
			Quadruple r = new Quadruple(4*0x100000000L,0,true);
			try {
				Quadruple m = q.divide(r);
				assertEquals(new Quadruple(0x140000000L,0,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Cross-sign case
			Quadruple q = new Quadruple(5*0x100000000L,0,false);
			Quadruple r = new Quadruple(4*0x100000000L,0,true);
			try {
				Quadruple m = q.divide(r);
				assertEquals(new Quadruple(0x140000000L,0,true),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Scaled case
			Quadruple q = new Quadruple(5*0x100000000L,1,false);
			Quadruple r = new Quadruple(4*0x100000000L,1,false);
			try {
				Quadruple m = q.divide(r);
				assertEquals(new Quadruple(0x140000000L,0,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Large case
			Quadruple q = new Quadruple(5*0x100000000L,10,false);
			Quadruple r = new Quadruple(4*0x100000000L,10,false);
			try {
				Quadruple m = q.divide(r);
				assertEquals(new Quadruple(0x140000000L,0,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Cross-scale case
			Quadruple q = new Quadruple(5*0x100000000L,1,false);
			Quadruple r = new Quadruple(4*0x100000000L,10,false);
			try {
				Quadruple m = q.divide(r);
				assertEquals(new Quadruple(0x140000000L,-9,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Floating point case
			Quadruple q = new Quadruple(5*0x100000000L,-1,false);
			Quadruple r = new Quadruple(4*0x100000000L,10,false);
			try {
				Quadruple m = q.divide(r);
				assertEquals(new Quadruple(0x140000000L,-11,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Twice floating point case
			Quadruple q = new Quadruple(5*0x100000000L,-1,false);
			Quadruple r = new Quadruple(4*0x100000000L,-1,false);
			try {
				Quadruple m = q.divide(r);
				assertEquals(new Quadruple(0x140000000L,0,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Twice floating point case
			Quadruple q = new Quadruple(0x5000000000000005L,0,false);
			Quadruple r = new Quadruple(0x4000000000000004L,0,false);
			try {
				Quadruple m = q.divide(r);
				assertEquals(new Quadruple(0x140000000L,0,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		
		{ // Divide by zero
			Quadruple q = new Quadruple(0x5000000000000005L,0,false);
			Quadruple r = new Quadruple(0);
			try {
				Quadruple m = q.divide(r);
				fail("Expected exception");
			}
			catch(Throwable t) {
				// Expected case.
			}
		}

		{ // Divide zero
			Quadruple q = new Quadruple(0);
			Quadruple r = new Quadruple(0x4000000000000004L,0,false);
			try {
				Quadruple m = q.divide(r);
				assertEquals(q,m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Divide equal numbers
			Quadruple q = new Quadruple(0x4000000000000004L,0,false);
			Quadruple r = new Quadruple(0x4000000000000004L,0,false);
			try {
				Quadruple m = q.divide(r);
				assertEqualsWithinMargin(new Quadruple(1),m,"a/a successful");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // A case that proved to be troublesome
			Quadruple q = new Quadruple(1);
			Quadruple r = new Quadruple(2);
			try {
				Quadruple m = q.divide(r);
				assertEquals(new Quadruple(0.5),m);
				assertEqualsWithinMargin(new Quadruple(0.5),m,"1/2 successful");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // A case that proved to be troublesome
			Quadruple q = new Quadruple(1);
			Quadruple r = new Quadruple(2);
			try {
				Quadruple m = q.divide(r).divide(r);
				assertEqualsWithinMargin(new Quadruple(0.25),m,"(1/2)/2 successful");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // A case that proved to be troublesome
			Quadruple q = new Quadruple(0.0000000017179868998937308788299560546875);
			Quadruple r = new Quadruple(2);
			try {
				Quadruple m = q.divide(r);
				assertEqualsWithinMargin(0.0000000017179868998937308788299560546875*1000000000/2.0,m.doubleValue()*1000000000,"0.0000000017179868998937308788299560546875/2 successful");
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
			Quadruple q = new Quadruple(5*0x100000000L,0,false);
			Quadruple r = new Quadruple(4*0x100000000L,0,false);
			try {
				Quadruple m = q.add(r);
				assertEquals(9,m.intValue());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Negative case
			Quadruple q = new Quadruple(5*0x100000000L,0,true);
			Quadruple r = new Quadruple(4*0x100000000L,0,true);
			try {
				Quadruple m = q.add(r);
				assertEquals(-9,m.intValue());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Cross-sign case
			Quadruple q = new Quadruple(5*0x100000000L,0,false);
			Quadruple r = new Quadruple(5*0x100000000L,0,true);
			try {
				Quadruple m = q.add(r);
				assertEquals(0,m.intValue());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Scaled case
			Quadruple q = new Quadruple(5*0x100000000L,1,false);
			Quadruple r = new Quadruple(4*0x100000000L,1,false);
			try {
				Quadruple m = q.add(r);
				assertEquals(90,m.intValue());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Large case
			Quadruple q = new Quadruple(5*0x100000000L,10,false);
			Quadruple r = new Quadruple(4*0x100000000L,10,false);
			try {
				Quadruple m = q.add(r);
				assertEquals(new Quadruple(9*0x100000000L,10,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Cross-scale case
			Quadruple q = new Quadruple(5*0x100000000L,1,false);
			Quadruple r = new Quadruple(5*0x100000000L,10,false);
			try {
				Quadruple m = q.add(r);
				assertEquals(50000000050L,m.longValue());
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Floating point case
			Quadruple q = new Quadruple(5*0x100000000L,-1,false);
			Quadruple r = new Quadruple(5*0x100000000L,10,false);
			try {
				Quadruple m = q.add(r);
				assertEquals(new Quadruple(Long.parseLong("1dcd650000000000",16)+(5*0x100000000L)/1000,2,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Twice floating point case
			Quadruple q = new Quadruple(5*0x100000000L,-1,false);
			Quadruple r = new Quadruple(5*0x100000000L,-1,false);
			try {
				Quadruple m = q.add(r);
				assertEquals(new Quadruple(1*0x100000000L,0,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Twice floating point case
			Quadruple q = new Quadruple(0x4000000000000002L,0,false);
			Quadruple r = new Quadruple(0x4000000000000002L,0,false);
			try {
				Quadruple m = q.add(r);
				assertEquals(new Quadruple(0x8000000000000004L,0,false),m);
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Another case (r value is greater than q value, and opposite signs.)
			Quadruple q = new Quadruple(4.99999999998201309777796268463134765625);
			Quadruple r = new Quadruple(-4.9999999999999999976716935634613037109375);
			try {
				Quadruple m = q.add(r);
				assertEqualsWithinMargin(m.getZero(), m, "Adding similar opposite-signed numbers.");
				assertEquals(new Quadruple(4.99999999998201309777796268463134765625), q, "q not modified");
				assertEquals(new Quadruple(-4.9999999999999999976716935634613037109375), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Another case (addition should cause a carry in the maximal binary digits) that was proving troublesome
			Quadruple q = new Quadruple(2.227901745543820726685225963592529296875);
			Quadruple r = new Quadruple(2.2279010511616673874668776988983154296875);
			try {
				Quadruple m = q.add(r);
				assertEqualsWithinMargin(new Quadruple(4.45580279670548814348876476287841796875), m, "Adding similar opposite-signed numbers.");
				assertEquals(new Quadruple(2.227901745543820726685225963592529296875), q, "q not modified");
				assertEquals(new Quadruple(2.2279010511616673874668776988983154296875), r, "r not modified");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

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
