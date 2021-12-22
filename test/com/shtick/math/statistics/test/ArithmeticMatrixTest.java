package com.shtick.math.statistics.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.shtick.math.statistics.Eigensystem;
import com.shtick.math.statistics.DoublePolynomial;
import com.shtick.math.statistics.ArithmeticMatrix;

class ArithmeticMatrixTest {
	private static final double ERROR_MARGIN = 0.00001;

	@Test
	void testConstructor() {
		{ // Empty case
			assertThrows(Throwable.class,()->{
				new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {});
			});
		}

		{ // 1x0 case
			assertThrows(Throwable.class,()->{
				new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {{}});
			});
		}

		{ // 1x1 case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {{new DoublePolynomial(new double[]{5})}});
			assertEquals(1, matrix.getRows());
			assertEquals(1, matrix.getColumns());
		}

		{ // 2x2 case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{1})},
				{new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{1})}
			});
			assertEquals(2, matrix.getRows());
			assertEquals(2, matrix.getColumns());
		}

		{ // 2x3 case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1})},
				{new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{9})}
			});
			assertEquals(2, matrix.getRows());
			assertEquals(3, matrix.getColumns());
		}

		{ // Inconsistent case
			assertThrows(Throwable.class,()->{
				new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
					{new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1})},
					{new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{1})}
				});
			});
		}
	}
	
	@Test
	void testDeterminant() {
		{ // 1x1 case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {{new DoublePolynomial(new double[]{5})}});
			DoublePolynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(5, determinant.getCoefficient(0), ""+(5)+"="+determinant);
		}

		{ // 2x2 case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2})},
				{new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{4})}
			});
			DoublePolynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(-2, determinant.getCoefficient(0), ""+(-2)+"="+determinant);
		}

		{ // 3x3 case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3})},
				{new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{1})},
				{new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2})}
			});
			DoublePolynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(-18, determinant.getCoefficient(0), ""+(-18)+"="+determinant);
		}

		{ // 4x4 case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{}),new DoublePolynomial(new double[]{}),new DoublePolynomial(new double[]{})},
				{new DoublePolynomial(new double[]{}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{}),new DoublePolynomial(new double[]{})},
				{new DoublePolynomial(new double[]{}),new DoublePolynomial(new double[]{}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{})},
				{new DoublePolynomial(new double[]{}),new DoublePolynomial(new double[]{}),new DoublePolynomial(new double[]{}),new DoublePolynomial(new double[]{1})}
			});
			DoublePolynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(1, determinant.getCoefficient(0), ""+(1)+"="+determinant);
		}

		{ // 4x4 case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1})},
				{new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{2})},
				{new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{3})},
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1})}
			});
			DoublePolynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(0, determinant.getCoefficient(0), ""+(0)+"="+determinant);
		}

		{ // 2nd 4x4 case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1})},
				{new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{2})},
				{new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{5})},
				{new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1})}
			});
			DoublePolynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(0, determinant.getCoefficient(0), ""+(0)+"="+determinant);
		}

		{ // 3rd 4x4 case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1})},
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2})},
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{3})},
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{4})}
			});
			DoublePolynomial determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(-6, determinant.getCoefficient(0), ""+(-6)+"="+determinant);
		}

		{ // Non-square case
			assertThrows(Throwable.class,()->{
				ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
					{new DoublePolynomial(new double[]{5})},
					{new DoublePolynomial(new double[]{5})}
				});
				matrix.getDeterminant();
			});
		}
	}

	@Test
	void testAdd() throws Throwable {
		{ // Success case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3})},
				{new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{6})}
			});
			ArithmeticMatrix<DoublePolynomial> result = matrix.add(matrix);
			assertEqualsMatrixWithinMargin(new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{6})},
				{new DoublePolynomial(new double[]{8}),new DoublePolynomial(new double[]{10}),new DoublePolynomial(new double[]{12})}
			}), result, "Success case.");
		}

		{ // Wrong columns
			assertThrows(IllegalArgumentException.class,() -> {
				ArithmeticMatrix<DoublePolynomial> matrix1 = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
					{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3})},
					{new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{6})}
				});
				ArithmeticMatrix<DoublePolynomial> matrix2 = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
					{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2})},
					{new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{5})}
				});
				matrix1.add(matrix2);
			});
		}

		{ // Wrong rows
			assertThrows(IllegalArgumentException.class,() -> {
				ArithmeticMatrix<DoublePolynomial> matrix1 = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
					{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3})},
					{new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{6})}
				});
				ArithmeticMatrix<DoublePolynomial> matrix2 = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
					{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3})}
				});
				matrix1.add(matrix2);
			});
		}
	}

	@Test
	void testSubtract() throws Throwable {
		{ // Success case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3})},
				{new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{6})}
			});
			ArithmeticMatrix<DoublePolynomial> result = matrix.subtract(matrix);
			assertZeroMatrixWithinMargin(result, "Success case.");
		}

		{ // Wrong columns
			assertThrows(IllegalArgumentException.class,() -> {
				ArithmeticMatrix<DoublePolynomial> matrix1 = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
					{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3})},
					{new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{6})}
				});
				ArithmeticMatrix<DoublePolynomial> matrix2 = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
					{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2})},
					{new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{5})}
				});
				matrix1.subtract(matrix2);
			});
		}

		{ // Wrong rows
			assertThrows(IllegalArgumentException.class,() -> {
				ArithmeticMatrix<DoublePolynomial> matrix1 = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
					{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3})},
					{new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{6})}
				});
				ArithmeticMatrix<DoublePolynomial> matrix2 = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
					{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3})}
				});
				matrix1.subtract(matrix2);
			});
		}
	}

	@Test
	void testOperate() throws Throwable {
		{ // Success case
			ArithmeticMatrix<DoublePolynomial> matrix1 = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3})},
				{new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{6})}
			});
			ArithmeticMatrix<DoublePolynomial> matrix2 = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{1})},
				{new DoublePolynomial(new double[]{2})},
				{new DoublePolynomial(new double[]{3})}
			});
			ArithmeticMatrix<DoublePolynomial> result = matrix1.operate(matrix2);
			assertEqualsMatrixWithinMargin(new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{14})},
				{new DoublePolynomial(new double[]{32})}
			}), result, "Success case.");
		}

		{ // Mismatch
			assertThrows(IllegalArgumentException.class,() -> {
				ArithmeticMatrix<DoublePolynomial> matrix1 = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
					{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3})},
					{new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{6})}
				});
				ArithmeticMatrix<DoublePolynomial> matrix2 = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
					{new DoublePolynomial(new double[]{1})},
					{new DoublePolynomial(new double[]{2})}
				});
				matrix1.operate(matrix2);
			});
		}
	}

	@Test
	void testMultiplyBy() throws Throwable {
		{ // Success case
			ArithmeticMatrix<DoublePolynomial> matrix = new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{1}),new DoublePolynomial(new double[]{2}),new DoublePolynomial(new double[]{3})},
				{new DoublePolynomial(new double[]{4}),new DoublePolynomial(new double[]{5}),new DoublePolynomial(new double[]{6})}
			});
			ArithmeticMatrix<DoublePolynomial> result = matrix.multiplyBy(new DoublePolynomial(new double[]{1.5}));
			assertEqualsMatrixWithinMargin(new ArithmeticMatrix<DoublePolynomial>(new DoublePolynomial[][] {
				{new DoublePolynomial(new double[]{1.5}),new DoublePolynomial(new double[]{3}),new DoublePolynomial(new double[]{4.5})},
				{new DoublePolynomial(new double[]{6}),new DoublePolynomial(new double[]{7.5}),new DoublePolynomial(new double[]{9})}
			}), result, "Success case.");
		}
	}
	
	private static void assertEqualsWithinMargin(double expected, double actual, String message) {
		assertTrue(actual>=expected-ERROR_MARGIN,message);
		assertTrue(actual<=expected+ERROR_MARGIN,message);
	}
	
	private static void assertZeroMatrixWithinMargin(ArithmeticMatrix<DoublePolynomial> m, String message) {
		for(int i=0;i<m.getRows();i++)
			for(int j=0;j<m.getColumns();j++)
				assertEqualsWithinMargin(0,m.getElementAt(i, j).getCoefficient(0),message+" (Non-zero, "+m.getElementAt(i, j).getCoefficient(0)+", at matrix element ("+i+","+j+").)");
	}
	
	private static void assertEqualsMatrixWithinMargin(ArithmeticMatrix<DoublePolynomial> mExpected, ArithmeticMatrix<DoublePolynomial> mActual, String message) {
		assertEquals(mExpected.getRows(),mActual.getRows(),message+" (Unequal Rows)");
		assertEquals(mExpected.getColumns(),mActual.getColumns(),message+" (Unequal Columns)");
		for(int i=0;i<mExpected.getRows();i++)
			for(int j=0;j<mExpected.getColumns();j++)
				assertEqualsWithinMargin(mExpected.getElementAt(i, j).getCoefficient(0),mActual.getElementAt(i, j).getCoefficient(0),message+" (Unexpected matrix element,"+mActual.getElementAt(i, j).getCoefficient(0)+" at ("+i+","+j+").)");
	}
}
