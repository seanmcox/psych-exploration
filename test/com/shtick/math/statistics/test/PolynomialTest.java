package com.shtick.math.statistics.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.shtick.math.statistics.Polynomial;

class PolynomialTest {
	private static final double ERROR_MARGIN = 0.00001;

	@Test
	void testConstructorAndFindZeros() {
		{ // Empty case
			Polynomial p = new Polynomial(new double[] {});
			try {
				List<Double> zeros = p.findZeros();
				assertNull(zeros,"All points are zeros.");
				assertEqualsWithinMargin(0,p.getValue(0),""+(0)+"="+p.getValue(0));
				assertEqualsWithinMargin(0,p.getValue(1),""+(0)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Constant case
			Polynomial p = new Polynomial(new double[] {5});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(0, zeros.size(),"No points are zeros.");
				assertEqualsWithinMargin(5,p.getValue(0),""+(5)+"="+p.getValue(0));
				assertEqualsWithinMargin(5,p.getValue(1),""+(5)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Linear case
			Polynomial p = new Polynomial(new double[] {5,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(-5,zeros.get(0),""+(-5)+"="+zeros.get(0));
				assertEqualsWithinMargin(5,p.getValue(0),""+(5)+"="+p.getValue(0));
				assertEqualsWithinMargin(6,p.getValue(1),""+(6)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Parabolic case
			Polynomial p = new Polynomial(new double[] {-4,0,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(2, zeros.size(),"Two points are zeros.");
				assertEqualsWithinMargin(2,Math.abs(zeros.get(0)),""+(2)+"=|"+zeros.get(0)+"|");
				assertEqualsWithinMargin(2,Math.abs(zeros.get(1)),""+(2)+"=|"+zeros.get(1)+"|");
				assertTrue((zeros.get(0)<0)^(zeros.get(1)<0),"Zeros on opposite sides of origin.");
				assertEqualsWithinMargin(-4,p.getValue(0),""+(-4)+"="+p.getValue(0));
				assertEqualsWithinMargin(-3,p.getValue(1),""+(-3)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Parabolic case with 0 constant.
			Polynomial p = new Polynomial(new double[] {0,5,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(2, zeros.size(),"Two points are zeros.");
				assertTrue(zeros.contains(0.0),"Origin is a zero.");
				zeros.remove(0.0);
				assertEqualsWithinMargin(-5,zeros.get(0),""+(-5)+"="+zeros.get(0));
				assertEqualsWithinMargin(0,p.getValue(0),""+(0)+"="+p.getValue(0));
				assertEqualsWithinMargin(6,p.getValue(1),""+(6)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Simplifiable 3-factor case.
			Polynomial p = new Polynomial(new double[] {-20,0,1,0,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(2, zeros.size(),"Two points are zeros.");
				assertEqualsWithinMargin(2,Math.abs(zeros.get(0)),""+(2)+"=|"+zeros.get(0)+"|");
				assertEqualsWithinMargin(2,Math.abs(zeros.get(1)),""+(2)+"=|"+zeros.get(1)+"|");
				assertTrue((zeros.get(0)<0)^(zeros.get(1)<0),"Zeros on opposite sides of origin.");
				assertEqualsWithinMargin(-20,p.getValue(0),""+(-20)+"="+p.getValue(0));
				assertEqualsWithinMargin(-18,p.getValue(1),""+(-18)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 4-factor case. (Forcing newton's method.)
			Polynomial p = new Polynomial(new double[] {1,1,1,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is zeros.");
				assertEqualsWithinMargin(-1,zeros.get(0),""+(-1)+"="+zeros.get(0));
				assertEqualsWithinMargin(1,p.getValue(0),""+(1)+"="+p.getValue(0));
				assertEqualsWithinMargin(4,p.getValue(1),""+(4)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 4-factor case. (Forcing newton's method.) 0-slope at origin and no zeros.
			Polynomial p = new Polynomial(new double[] {1,0,1,0,1,0,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(0, zeros.size(),"no zeros.");
				assertEqualsWithinMargin(1,p.getValue(0),""+(1)+"="+p.getValue(0));
				assertEqualsWithinMargin(4,p.getValue(1),""+(4)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 4-factor case. (Forcing newton's method.) 0-slope at origin and two zeros around origin.
			Polynomial p = new Polynomial(new double[] {-1,0,1,0,1,0,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(2, zeros.size(),"Two points are zeros.");
				assertEqualsWithinMargin(0.7373527057603276,Math.abs(zeros.get(0)),""+(0.7373527057603276)+"=|"+zeros.get(0)+"|");
				assertEqualsWithinMargin(0.7373527057603276,Math.abs(zeros.get(1)),""+(0.7373527057603276)+"=|"+zeros.get(1)+"|");
				assertTrue((zeros.get(0)<0)^(zeros.get(1)<0),"Zeros on opposite sides of origin.");
				assertEqualsWithinMargin(-1,p.getValue(0),""+(-1)+"="+p.getValue(0));
				assertEqualsWithinMargin(2,p.getValue(1),""+(2)+"="+p.getValue(1));
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
			Polynomial p = new Polynomial(new double[] {});
			p=p.add(new double[] {5,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertTrue(zeros.get(0)>=-5-ERROR_MARGIN,""+(-5)+"="+zeros.get(0));
				assertTrue(zeros.get(0)<=-5+ERROR_MARGIN,""+(-5)+"="+zeros.get(0));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Parabolic case
			Polynomial p = new Polynomial(new double[] {-4,0,1});
			p=p.add(new Polynomial(new double[] {0,0,1}));
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(2, zeros.size(),"Two points are zeros.");
				assertTrue(Math.abs(zeros.get(0))>=1.4142135623730951-ERROR_MARGIN,""+(1.4142135623730951)+"=|"+zeros.get(0)+"|");
				assertTrue(Math.abs(zeros.get(0))<=1.4142135623730951+ERROR_MARGIN,""+(1.4142135623730951)+"=|"+zeros.get(0)+"|");
				assertTrue(Math.abs(zeros.get(1))>=1.4142135623730951-ERROR_MARGIN,""+(1.4142135623730951)+"=|"+zeros.get(0)+"|");
				assertTrue(Math.abs(zeros.get(1))<=1.4142135623730951+ERROR_MARGIN,""+(1.4142135623730951)+"=|"+zeros.get(0)+"|");
				assertTrue((zeros.get(0)<0)^(zeros.get(1)<0),"Zeros on opposite sides of origin.");
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
			Polynomial p = new Polynomial(new double[] {});
			p=p.subtract(new double[] {5,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertTrue(zeros.get(0)>=-5-ERROR_MARGIN,""+(-5)+"="+zeros.get(0));
				assertTrue(zeros.get(0)<=-5+ERROR_MARGIN,""+(-5)+"="+zeros.get(0));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Parabolic case
			Polynomial p = new Polynomial(new double[] {-4,0,1});
			p=p.subtract(new Polynomial(new double[] {0,0,1}));
			try {
				List<Double> zeros = p.findZeros();
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
			Polynomial p = new Polynomial(new double[] {});
			p=p.multiply(new double[] {5,1});
			try {
				List<Double> zeros = p.findZeros();
				assertNull(zeros,"All points are zeros.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Empty case
			Polynomial p = new Polynomial(new double[] {1});
			p=p.multiply(new double[] {5,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertTrue(zeros.get(0)>=-5-ERROR_MARGIN,""+(-5)+"="+zeros.get(0));
				assertTrue(zeros.get(0)<=-5+ERROR_MARGIN,""+(-5)+"="+zeros.get(0));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Empty case
			Polynomial p = new Polynomial(new double[] {0,1});
			p=p.multiply(new double[] {5,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(2, zeros.size(),"Two points are zeros.");
				assertTrue(zeros.contains(0.0));
				zeros.remove(0.0);
				assertTrue(zeros.get(0)>=-5-ERROR_MARGIN,""+(-5)+"="+zeros.get(0));
				assertTrue(zeros.get(0)<=-5+ERROR_MARGIN,""+(-5)+"="+zeros.get(0));
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
			Polynomial p = new Polynomial(new double[] {});
			p=p.divide(new double[] {5,1});
			try {
				List<Double> zeros = p.findZeros();
				assertNull(zeros,"All points are zeros.");
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		
		{ // Empty case
			Polynomial p = new Polynomial(new double[] {5,1});
			p=p.divide(new double[] {5,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(0, zeros.size(),"No point is a zero.");
				assertEqualsWithinMargin(1,p.getValue(0),""+(1)+"="+p.getValue(0));
				assertEqualsWithinMargin(1,p.getValue(1),""+(1)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
		{ // Empty case
			Polynomial p = new Polynomial(new double[] {0,5,1});
			p=p.divide(new double[] {5,1});
			try {
				List<Double> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(0,zeros.get(0),""+(0)+"="+zeros.get(0));
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
			Polynomial p = new Polynomial(new double[] {});
			try {
				p=p.derivative();
				List<Double> zeros = p.findZeros();
				assertNull(zeros,"All points are zeros.");
				assertEqualsWithinMargin(0,p.getValue(0),""+(0)+"="+p.getValue(0));
				assertEqualsWithinMargin(0,p.getValue(1),""+(0)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Constant case
			Polynomial p = new Polynomial(new double[] {5});
			try {
				p=p.derivative();
				List<Double> zeros = p.findZeros();
				assertNull(zeros,"All points are zeros.");
				assertEqualsWithinMargin(0,p.getValue(0),""+(0)+"="+p.getValue(0));
				assertEqualsWithinMargin(0,p.getValue(1),""+(0)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Linear case
			Polynomial p = new Polynomial(new double[] {5,1});
			try {
				p=p.derivative();
				List<Double> zeros = p.findZeros();
				assertEquals(0, zeros.size(),"No points are zeros.");
				assertEqualsWithinMargin(1,p.getValue(0),""+(1)+"="+p.getValue(0));
				assertEqualsWithinMargin(1,p.getValue(1),""+(1)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Parabolic case
			Polynomial p = new Polynomial(new double[] {-4,0,1});
			try {
				p=p.derivative();
				List<Double> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(0,zeros.get(0),""+(0)+"="+zeros.get(0));
				assertEqualsWithinMargin(0,p.getValue(0),""+(0)+"="+p.getValue(0));
				assertEqualsWithinMargin(2,p.getValue(1),""+(2)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Parabolic case with 0 constant.
			Polynomial p = new Polynomial(new double[] {0,5,1});
			try {
				p=p.derivative();
				List<Double> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(-2.5,zeros.get(0),""+(-2.5)+"="+zeros.get(0));
				assertEqualsWithinMargin(5,p.getValue(0),""+(5)+"="+p.getValue(0));
				assertEqualsWithinMargin(7,p.getValue(1),""+(7)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // Simplifiable 3-factor case.
			Polynomial p = new Polynomial(new double[] {-20,0,1,0,1});
			try {
				p=p.derivative();
				List<Double> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(0,zeros.get(0),""+(0)+"="+zeros.get(0));
				assertEqualsWithinMargin(0,p.getValue(0),""+(0)+"="+p.getValue(0));
				assertEqualsWithinMargin(6,p.getValue(1),""+(6)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 4-factor case. (Forcing newton's method.)
			Polynomial p = new Polynomial(new double[] {1,1,1,1});
			try {
				p=p.derivative();
				List<Double> zeros = p.findZeros();
				assertEquals(0, zeros.size(),"No point is zero.");
				assertEqualsWithinMargin(1,p.getValue(0),""+(1)+"="+p.getValue(0));
				assertEqualsWithinMargin(6,p.getValue(1),""+(6)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 4-factor case. (Forcing newton's method.) 0-slope at origin and no zeros.
			Polynomial p = new Polynomial(new double[] {1,0,1,0,1,0,1});
			try {
				p=p.derivative();
				List<Double> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(0,zeros.get(0),""+(0)+"="+zeros.get(0));
				assertEqualsWithinMargin(0,p.getValue(0),""+(0)+"="+p.getValue(0));
				assertEqualsWithinMargin(12,p.getValue(1),""+(12)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}

		{ // 4-factor case. (Forcing newton's method.) 0-slope at origin and two zeros around origin.
			Polynomial p = new Polynomial(new double[] {-1,0,1,0,1,0,1});
			try {
				p=p.derivative();
				List<Double> zeros = p.findZeros();
				assertEquals(1, zeros.size(),"One point is a zero.");
				assertEqualsWithinMargin(0,zeros.get(0),""+(0)+"="+zeros.get(0));
				assertEqualsWithinMargin(0,p.getValue(0),""+(0)+"="+p.getValue(0));
				assertEqualsWithinMargin(12,p.getValue(1),""+(12)+"="+p.getValue(1));
			}
			catch(Throwable t) {
				t.printStackTrace();
				fail("Unexpected exception: "+t.getMessage());
			}
		}
	}
	
	private static void assertEqualsWithinMargin(double expected, double actual, String message) {
		assertTrue(actual>=expected-ERROR_MARGIN,message);
		assertTrue(actual<=expected+ERROR_MARGIN,message);
	}
}
