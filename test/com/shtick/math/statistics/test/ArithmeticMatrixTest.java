package com.shtick.math.statistics.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.shtick.math.statistics.Eigensystem;
import com.shtick.math.statistics.Polynomial;
import com.shtick.math.statistics.PolynomialMatrix;

class PolynomialMatrixTest {
	private static final double ERROR_MARGIN = 0.00001;

	@Test
	void testConstructor() {
		{ // Empty case
			assertThrows(Throwable.class,()->{
				new PolynomialMatrix(new double[][] {});
			});
		}

		{ // 1x0 case
			assertThrows(Throwable.class,()->{
				new PolynomialMatrix(new double[][] {{}});
			});
		}

		{ // 1x1 case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{5}});
			assertEquals(1, matrix.getRows());
			assertEquals(1, matrix.getColumns());
		}

		{ // 2x2 case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{5,1},{3,1}});
			assertEquals(2, matrix.getRows());
			assertEquals(2, matrix.getColumns());
		}

		{ // 2x3 case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{5,1,1},{3,1,9}});
			assertEquals(2, matrix.getRows());
			assertEquals(3, matrix.getColumns());
		}

		{ // Inconsistent case
			assertThrows(Throwable.class,()->{
				new PolynomialMatrix(new double[][] {{5,1,1},{3,1}});
			});
		}
	}
	
	@Test
	void testDeterminant() {
		{ // 1x1 case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{5}});
			Polynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(5, determinant.getCoefficient(0), ""+(5)+"="+determinant);
		}

		{ // 2x2 case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{1,2},{3,4}});
			Polynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(-2, determinant.getCoefficient(0), ""+(-2)+"="+determinant);
		}

		{ // 3x3 case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{1,2,3},{2,3,1},{3,1,2}});
			Polynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(-18, determinant.getCoefficient(0), ""+(-18)+"="+determinant);
		}

		{ // 4x4 case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}});
			Polynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(1, determinant.getCoefficient(0), ""+(1)+"="+determinant);
		}

		{ // 4x4 case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{1,1,1,1},{2,2,2,2},{3,3,3,3},{1,1,1,1}});
			Polynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(0, determinant.getCoefficient(0), ""+(0)+"="+determinant);
		}

		{ // 2nd 4x4 case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{1,1,1,1},{2,2,2,2},{5,3,3,5},{5,1,1,1}});
			Polynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(0, determinant.getCoefficient(0), ""+(0)+"="+determinant);
		}

		{ // 3rd 4x4 case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{1,1,1,1},{1,1,1,2},{1,1,3,3},{1,4,4,4}});
			Polynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(-6, determinant.getCoefficient(0), ""+(-6)+"="+determinant);
		}

		{ // Non-square case
			assertThrows(Throwable.class,()->{
				PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{5},{5}});
				matrix.getDeterminant();
			});
		}
	}

	@Test
	void testAdd() throws Throwable {
		{ // Success case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{1,2,3},{4,5,6}});
			PolynomialMatrix result = matrix.add(matrix);
			assertEqualsMatrixWithinMargin(new PolynomialMatrix(new double[][] {{2,4,6},{8,10,12}}), result, "Success case.");
		}

		{ // Wrong columns
			assertThrows(IllegalArgumentException.class,() -> {
				PolynomialMatrix matrix1 = new PolynomialMatrix(new double[][] {{1,2,3},{4,5,6}});
				PolynomialMatrix matrix2 = new PolynomialMatrix(new double[][] {{1,2},{4,5}});
				matrix1.add(matrix2);
			});
		}

		{ // Wrong rows
			assertThrows(IllegalArgumentException.class,() -> {
				PolynomialMatrix matrix1 = new PolynomialMatrix(new double[][] {{1,2,3},{4,5,6}});
				PolynomialMatrix matrix2 = new PolynomialMatrix(new double[][] {{1,2,3}});
				matrix1.add(matrix2);
			});
		}
	}

	@Test
	void testSubtract() throws Throwable {
		{ // Success case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{1,2,3},{4,5,6}});
			PolynomialMatrix result = matrix.subtract(matrix);
			assertZeroMatrixWithinMargin(result, "Success case.");
		}

		{ // Wrong columns
			assertThrows(IllegalArgumentException.class,() -> {
				PolynomialMatrix matrix1 = new PolynomialMatrix(new double[][] {{1,2,3},{4,5,6}});
				PolynomialMatrix matrix2 = new PolynomialMatrix(new double[][] {{1,2},{4,5}});
				matrix1.subtract(matrix2);
			});
		}

		{ // Wrong rows
			assertThrows(IllegalArgumentException.class,() -> {
				PolynomialMatrix matrix1 = new PolynomialMatrix(new double[][] {{1,2,3},{4,5,6}});
				PolynomialMatrix matrix2 = new PolynomialMatrix(new double[][] {{1,2,3}});
				matrix1.subtract(matrix2);
			});
		}
	}

	@Test
	void testOperate() throws Throwable {
		{ // Success case
			PolynomialMatrix matrix1 = new PolynomialMatrix(new double[][] {{1,2,3},{4,5,6}});
			PolynomialMatrix matrix2 = new PolynomialMatrix(new double[][] {{1},{2},{3}});
			PolynomialMatrix result = matrix1.operate(matrix2);
			assertEqualsMatrixWithinMargin(new PolynomialMatrix(new double[][] {{14},{32}}), result, "Success case.");
		}

		{ // Mismatch
			assertThrows(IllegalArgumentException.class,() -> {
				PolynomialMatrix matrix1 = new PolynomialMatrix(new double[][] {{1,2,3},{4,5,6}});
				PolynomialMatrix matrix2 = new PolynomialMatrix(new double[][] {{1},{2}});
				matrix1.operate(matrix2);
			});
		}
	}

	@Test
	void testMultiplyBy() throws Throwable {
		{ // Success case
			PolynomialMatrix matrix = new PolynomialMatrix(new double[][] {{1,2,3},{4,5,6}});
			PolynomialMatrix result = matrix.multiplyBy(1.5);
			assertEqualsMatrixWithinMargin(new PolynomialMatrix(new double[][] {{1.5,3,4.5},{6,7.5,9}}), result, "Success case.");
		}
	}
	
	private static void assertEqualsWithinMargin(double expected, double actual, String message) {
		assertTrue(actual>=expected-ERROR_MARGIN,message);
		assertTrue(actual<=expected+ERROR_MARGIN,message);
	}
	
	private static void assertZeroMatrixWithinMargin(PolynomialMatrix m, String message) {
		for(int i=0;i<m.getRows();i++)
			for(int j=0;j<m.getColumns();j++)
				assertEqualsWithinMargin(0,m.getElementAt(i, j).getCoefficient(0),message+" (Non-zero, "+m.getElementAt(i, j).getCoefficient(0)+", at matrix element ("+i+","+j+").)");
	}
	
	private static void assertEqualsMatrixWithinMargin(PolynomialMatrix mExpected, PolynomialMatrix mActual, String message) {
		assertEquals(mExpected.getRows(),mActual.getRows(),message+" (Unequal Rows)");
		assertEquals(mExpected.getColumns(),mActual.getColumns(),message+" (Unequal Columns)");
		for(int i=0;i<mExpected.getRows();i++)
			for(int j=0;j<mExpected.getColumns();j++)
				assertEqualsWithinMargin(mExpected.getElementAt(i, j).getCoefficient(0),mActual.getElementAt(i, j).getCoefficient(0),message+" (Unexpected matrix element at ("+i+","+j+").)");
	}
}
