package com.shtick.math.statistics.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.shtick.math.statistics.ArithmeticPolynomial;
import com.shtick.math.statistics.DoublePolynomial;
import com.shtick.math.statistics.Quadruple;

class ArithmeticPolynomialTest {
	private static final Quadruple ERROR_MARGIN = new Quadruple(0.00001);
	private static final Quadruple ZERO = new Quadruple(0);
	private static final Quadruple ONE = new Quadruple(1);
	private static final Quadruple TWO = new Quadruple(2);
	private static final Quadruple THREE = new Quadruple(3);
	private static final Quadruple FOUR = new Quadruple(4);
	private static final Quadruple FIVE = new Quadruple(5);
	private static final Quadruple SIX = new Quadruple(6);

	@Test
	void testConstructorAndFindZeros() {
		{ // Empty case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {},ZERO);
			try {
				List<Quadruple> zeros = p.findZeros();
				assertNull(zeros,"All points are zeros.");
				assertEqualsWithinMargin(ZERO,p.getValue(ZERO),""+(0)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(ZERO,p.getValue(ONE),""+(0)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Constant case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {FIVE},ZERO);
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(0, zeros.size(),"No points are zeros.");
				assertEqualsWithinMargin(FIVE,p.getValue(ZERO),""+(5)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(FIVE,p.getValue(ONE),""+(5)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Linear case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {FIVE,ONE},ZERO);
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(FIVE.getNegative(),zeros.get(0),""+(-5)+"="+zeros.get(0));
				assertEqualsWithinMargin(FIVE,p.getValue(ZERO),""+(5)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(SIX,p.getValue(ONE),""+(6)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Parabolic case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {FOUR.getNegative(),ZERO,ONE},ZERO);
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(2, zeros.size(),"Two points are zeros.");
				assertEqualsWithinMargin(TWO,zeros.get(0).getAbs(),""+(2)+"=|"+zeros.get(0)+"|");
				assertEqualsWithinMargin(TWO,zeros.get(1).getAbs(),""+(2)+"=|"+zeros.get(1)+"|");
				assertTrue((zeros.get(0).compareTo(ZERO)<0)^(zeros.get(1).compareTo(ZERO)<0),"Zeros on opposite sides of origin.");
				assertEqualsWithinMargin(FOUR.getNegative(),p.getValue(ZERO),""+(-4)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(THREE.getNegative(),p.getValue(ONE),""+(-3)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Parabolic case with 0 constant.
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ZERO,FIVE,ONE},ZERO);
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(2, zeros.size(),"Two points are zeros.");
				assertTrue(zeros.contains(ZERO),"Origin is a zero.");
				zeros.remove(ZERO);
				assertEqualsWithinMargin(FIVE.getNegative(),zeros.get(0),""+(-5)+"="+zeros.get(0));
				assertEqualsWithinMargin(ZERO,p.getValue(ZERO),""+(0)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(SIX,p.getValue(ONE),""+(6)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Simplifiable 3-factor case.
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {new Quadruple(-20),ZERO,ONE,ZERO,ONE},ZERO);
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(2, zeros.size(),"Two points are zeros.");
				assertEqualsWithinMargin(TWO,zeros.get(0).getAbs(),""+(2)+"=|"+zeros.get(0)+"|");
				assertEqualsWithinMargin(TWO,zeros.get(1).getAbs(),""+(2)+"=|"+zeros.get(1)+"|");
				assertTrue((zeros.get(0).compareTo(ZERO)<0)^(zeros.get(1).compareTo(ZERO)<0),"Zeros on opposite sides of origin.");
				assertEqualsWithinMargin(new Quadruple(-20),p.getValue(ZERO),""+(-20)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(new Quadruple(-18),p.getValue(ONE),""+(-18)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 4-factor case. (Forcing newton's method.)
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ONE,ONE,ONE,ONE},ZERO);
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is zeros.");
				assertEqualsWithinMargin(ONE.getNegative(),zeros.get(0),""+(-1)+"="+zeros.get(0));
				assertEqualsWithinMargin(ONE,p.getValue(ZERO),""+(1)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(FOUR,p.getValue(ONE),""+(4)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 4-factor case. (Forcing newton's method.) 0-slope at origin and no zeros.
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ONE,ZERO,ONE,ZERO,ONE,ZERO,ONE},ZERO);
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(0, zeros.size(),"no zeros.");
				assertEqualsWithinMargin(ONE,p.getValue(ZERO),""+(1)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(FOUR,p.getValue(ONE),""+(4)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 4-factor case. (Forcing newton's method.) 0-slope at origin and two zeros around origin.
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ONE.getNegative(),ZERO,ONE,ZERO,ONE,ZERO,ONE},ZERO);
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(2, zeros.size(),"Two points are zeros.");
				assertEqualsWithinMargin(new Quadruple(0.7373527057603276),zeros.get(0).getAbs(),""+(0.7373527057603276)+"=|"+zeros.get(0)+"|");
				assertEqualsWithinMargin(new Quadruple(0.7373527057603276),zeros.get(1).getAbs(),""+(0.7373527057603276)+"=|"+zeros.get(1)+"|");
				assertTrue((zeros.get(0).compareTo(ZERO)<0)^(zeros.get(1).compareTo(ZERO)<0),"Zeros on opposite sides of origin.");
				assertEqualsWithinMargin(ONE.getNegative(),p.getValue(ZERO),""+(-1)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(TWO,p.getValue(ONE),""+(2)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testAdd() {
		{ // Empty case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {},ZERO);
			p=p.add(new Quadruple[] {FIVE,ONE});
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(FIVE.getNegative(), zeros.get(0), ""+(-5)+"="+zeros.get(0));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Parabolic case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {FOUR.getNegative(),ZERO,ONE},ZERO);
			p=p.add(new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ZERO,ZERO,ONE},ZERO));
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(2, zeros.size(),"Two points are zeros.");
				assertEqualsWithinMargin(new Quadruple(1.41421356237309504859), zeros.get(0), ""+(1.41421356237309504859)+"=|"+zeros.get(0)+"|");
				assertEqualsWithinMargin(new Quadruple(1.41421356237309504859).getNegative(), zeros.get(1), ""+(-1.41421356237309504859)+"=|"+zeros.get(1)+"|");
				assertTrue((zeros.get(0).compareTo(ZERO)<0)^(zeros.get(1).compareTo(ZERO)<0),"Zeros on opposite sides of origin.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testSubtract() {
		{ // Empty case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {},ZERO);
			p=p.subtract(new Quadruple[] {FIVE,ONE});
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(FIVE.getNegative(), zeros.get(0), ""+(-5)+"="+zeros.get(0));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Parabolic case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {FOUR.getNegative(),ZERO,ONE},ZERO);
			p=p.subtract(new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ZERO,ZERO,ONE},ZERO));
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(0, zeros.size(),"No points are zeros.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testMultiply() {
		{ // Empty case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {},ZERO);
			p=p.multiply(new Quadruple[] {FIVE,ONE});
			try {
				List<Quadruple> zeros = p.findZeros();
				assertNull(zeros,"All points are zeros.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Empty case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ONE},ZERO);
			p=p.multiply(new Quadruple[] {FIVE,ONE});
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(FIVE.getNegative(), zeros.get(0), ""+(-5)+"="+zeros.get(0));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Empty case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ZERO,ONE},ZERO);
			p=p.multiply(new Quadruple[] {FIVE,ONE});
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(2, zeros.size(),"Two points are zeros.");
				assertTrue(zeros.contains(ZERO));
				zeros.remove(ZERO);
				assertEqualsWithinMargin(FIVE.getNegative(), zeros.get(0), ""+(-5)+"="+zeros.get(0));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testDivide() {
		{ // Empty case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {},ZERO);
			p=p.divide(new Quadruple[] {FIVE,ONE});
			try {
				List<Quadruple> zeros = p.findZeros();
				assertNull(zeros,"All points are zeros.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		
		{ // Empty case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {FIVE,ONE},ZERO);
			p=p.divide(new Quadruple[] {FIVE,ONE});
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(0, zeros.size(),"No point is a zero.");
				assertEqualsWithinMargin(ONE,p.getValue(ZERO),""+(1)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(ONE,p.getValue(ONE),""+(1)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Empty case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ZERO,FIVE,ONE},ZERO);
			p=p.divide(new Quadruple[] {FIVE,ONE});
			try {
				List<Quadruple> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(ZERO,zeros.get(0),""+(0)+"="+zeros.get(0));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}

	@Test
	void testDerivative() {
		{ // Empty case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {},ZERO);
			try {
				p=p.derivative();
				List<Quadruple> zeros = p.findZeros();
				assertNull(zeros,"All points are zeros.");
				assertEqualsWithinMargin(ZERO,p.getValue(ZERO),""+(0)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(ZERO,p.getValue(ONE),""+(0)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Constant case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {FIVE},ZERO);
			try {
				p=p.derivative();
				List<Quadruple> zeros = p.findZeros();
				assertNull(zeros,"All points are zeros.");
				assertEqualsWithinMargin(ZERO,p.getValue(ZERO),""+(0)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(ZERO,p.getValue(ONE),""+(0)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Linear case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {FIVE,ONE},ZERO);
			try {
				p=p.derivative();
				List<Quadruple> zeros = p.findZeros();
				assertEquals(0, zeros.size(),"No points are zeros.");
				assertEqualsWithinMargin(ONE,p.getValue(ZERO),""+(1)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(ONE,p.getValue(ONE),""+(1)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Parabolic case
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {FOUR.getNegative(),ZERO,ONE},ZERO);
			try {
				p=p.derivative();
				List<Quadruple> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(ZERO,zeros.get(0),""+(0)+"="+zeros.get(0));
				assertEqualsWithinMargin(ZERO,p.getValue(ZERO),""+(0)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(TWO,p.getValue(ONE),""+(2)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Parabolic case with 0 constant.
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ZERO,FIVE,ONE},ZERO);
			try {
				p=p.derivative();
				List<Quadruple> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(new Quadruple(-2.5),zeros.get(0),""+(-2.5)+"="+zeros.get(0));
				assertEqualsWithinMargin(FIVE,p.getValue(ZERO),""+(5)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(new Quadruple(7),p.getValue(ONE),""+(7)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Simplifiable 3-factor case.
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {new Quadruple(-20),ZERO,ONE,ZERO,ONE},ZERO);
			try {
				p=p.derivative();
				List<Quadruple> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(ZERO,zeros.get(0),""+(0)+"="+zeros.get(0));
				assertEqualsWithinMargin(ZERO,p.getValue(ZERO),""+(0)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(SIX,p.getValue(ONE),""+(6)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 4-factor case. (Forcing newton's method.)
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ONE,ONE,ONE,ONE},ZERO);
			try {
				p=p.derivative();
				List<Quadruple> zeros = p.findZeros();
				assertEquals(0, zeros.size(),"No point is zero.");
				assertEqualsWithinMargin(ONE,p.getValue(ZERO),""+(1)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(SIX,p.getValue(ONE),""+(6)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 4-factor case. (Forcing newton's method.) 0-slope at origin and no zeros.
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ONE,ZERO,ONE,ZERO,ONE,ZERO,ONE},ZERO);
			try {
				p=p.derivative();
				List<Quadruple> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(ZERO,zeros.get(0),""+(0)+"="+zeros.get(0));
				assertEqualsWithinMargin(ZERO,p.getValue(ZERO),""+(0)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(new Quadruple(12),p.getValue(ONE),""+(12)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 4-factor case. (Forcing newton's method.) 0-slope at origin and two zeros around origin.
			ArithmeticPolynomial<Quadruple> p = new ArithmeticPolynomial<Quadruple>(new Quadruple[] {ONE.getNegative(),ZERO,ONE,ZERO,ONE,ZERO,ONE},ZERO);
			try {
				p=p.derivative();
				List<Quadruple> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(ZERO,zeros.get(0),""+(0)+"="+zeros.get(0));
				assertEqualsWithinMargin(ZERO,p.getValue(ZERO),""+(0)+"="+p.getValue(ZERO));
				assertEqualsWithinMargin(new Quadruple(12),p.getValue(ONE),""+(12)+"="+p.getValue(ONE));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}
	
	private static void assertEqualsWithinMargin(Quadruple expected, Quadruple actual, String message) {
		assertTrue(actual.compareTo(expected.subtract(ERROR_MARGIN))>=0,message);
		assertTrue(actual.compareTo(expected.add(ERROR_MARGIN))<=0,message);
	}
}
