package com.shtick.math.statistics.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.shtick.math.statistics.VectorMath;

class VectorMathTest {
	private static final double ERROR_MARGIN = 0.00001;

	@Test
	void testDotProduct() {
		{ // Normal success case
			double result = VectorMath.dotProduct(new double[] {1,2,3},new double[] {4,5,6});
			assertTrue(result>=32-ERROR_MARGIN);
			assertTrue(result<=32+ERROR_MARGIN);
		}
		
		{ // Alternative success case
			double result = VectorMath.dotProduct(new double[] {1,2,3,4},new double[] {2,3,4,5});
			assertTrue(result>=40-ERROR_MARGIN);
			assertTrue(result<=40+ERROR_MARGIN);
		}
		
		{ // Empty case
			double result = VectorMath.dotProduct(new double[] {},new double[] {});
			assertTrue(result>=0-ERROR_MARGIN);
			assertTrue(result<=0+ERROR_MARGIN);
		}
		
		{ // Zero case
			double result = VectorMath.dotProduct(new double[] {0,0,0},new double[] {0,0,0});
			assertTrue(result>=0-ERROR_MARGIN);
			assertTrue(result<=0+ERROR_MARGIN);
		}
		
		{ // Failure case
			try {
				double result = VectorMath.dotProduct(new double[] {1,2,3},new double[] {4,5});
				fail("Should not be able to dot multiply vectors of different sizes.");
			}
			catch(Throwable t) {
				// Expected case
			}
		}
	}

	@Test
	void testNormalize() {
		{ // Normal success case
			double[] testData = new double[] {1,2,3};
			VectorMath.normalize(testData);
			double result = VectorMath.dotProduct(testData, testData);
			assertTrue(result>=1-ERROR_MARGIN);
			assertTrue(result<=1+ERROR_MARGIN);
		}

		{ // Alternative success case
			double[] testData = new double[] {1,2,3,4};
			VectorMath.normalize(testData);
			double result = VectorMath.dotProduct(testData, testData);
			assertTrue(result>=1-ERROR_MARGIN);
			assertTrue(result<=1+ERROR_MARGIN);
		}

		{ // Failure case
			try {
				double[] testData = new double[] {0,0,0};
				VectorMath.normalize(testData);
				fail("Should not be able to Normalize size-zero vector.");
			}
			catch(Throwable t) {
				// Faiulure expected
			}
		}
	}
}
