package com.shtick.math.statistics.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.shtick.math.statistics.DoubleMatrix;
import com.shtick.math.statistics.Eigensystem;

class DoubleMatrixTest {
	private static final double ERROR_MARGIN = 0.00001;

	@Test
	void testConstructor() {
		{ // Empty case
			assertThrows(Throwable.class,()->{
				new DoubleMatrix(new double[][] {});
			});
		}

		{ // 1x0 case
			assertThrows(Throwable.class,()->{
				new DoubleMatrix(new double[][] {{}});
			});
		}

		{ // 1x1 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{5}});
			assertEquals(1, matrix.getRows());
			assertEquals(1, matrix.getColumns());
		}

		{ // 2x2 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{5,1},{3,1}});
			assertEquals(2, matrix.getRows());
			assertEquals(2, matrix.getColumns());
		}

		{ // 2x3 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{5,1,1},{3,1,9}});
			assertEquals(2, matrix.getRows());
			assertEquals(3, matrix.getColumns());
		}

		{ // Inconsistent case
			assertThrows(Throwable.class,()->{
				new DoubleMatrix(new double[][] {{5,1,1},{3,1}});
			});
		}
	}
	
	@Test
	void testDeterminant() {
		{ // 1x1 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{5}});
			double determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(5, determinant, ""+(5)+"="+determinant);
		}

		{ // 2x2 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,2},{3,4}});
			double determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(-2, determinant, ""+(-2)+"="+determinant);
		}

		{ // 3x3 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,2,3},{2,3,1},{3,1,2}});
			double determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(-18, determinant, ""+(-18)+"="+determinant);
		}

		{ // 4x4 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}});
			double determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(1, determinant, ""+(1)+"="+determinant);
		}

		{ // 4x4 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,1,1,1},{2,2,2,2},{3,3,3,3},{1,1,1,1}});
			double determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(0, determinant, ""+(0)+"="+determinant);
		}

		{ // 2nd 4x4 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,1,1,1},{2,2,2,2},{5,3,3,5},{5,1,1,1}});
			double determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(0, determinant, ""+(0)+"="+determinant);
		}

		{ // 3rd 4x4 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,1,1,1},{1,1,1,2},{1,1,3,3},{1,4,4,4}});
			double determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(-6, determinant, ""+(-6)+"="+determinant);
		}

		{ // Non-square case
			assertThrows(Throwable.class,()->{
				DoubleMatrix matrix = new DoubleMatrix(new double[][] {{5},{5}});
				matrix.getDeterminant();
			});
		}
	}

	@Test
	void testAdd() throws Throwable {
		{ // Success case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,2,3},{4,5,6}});
			DoubleMatrix result = matrix.add(matrix);
			assertEqualsMatrixWithinMargin(new DoubleMatrix(new double[][] {{2,4,6},{8,10,12}}), result, "Success case.");
		}

		{ // Wrong columns
			assertThrows(IllegalArgumentException.class,() -> {
				DoubleMatrix matrix1 = new DoubleMatrix(new double[][] {{1,2,3},{4,5,6}});
				DoubleMatrix matrix2 = new DoubleMatrix(new double[][] {{1,2},{4,5}});
				matrix1.add(matrix2);
			});
		}

		{ // Wrong rows
			assertThrows(IllegalArgumentException.class,() -> {
				DoubleMatrix matrix1 = new DoubleMatrix(new double[][] {{1,2,3},{4,5,6}});
				DoubleMatrix matrix2 = new DoubleMatrix(new double[][] {{1,2,3}});
				matrix1.add(matrix2);
			});
		}
	}

	@Test
	void testSubtract() throws Throwable {
		{ // Success case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,2,3},{4,5,6}});
			DoubleMatrix result = matrix.subtract(matrix);
			assertZeroMatrixWithinMargin(result, "Success case.");
		}

		{ // Wrong columns
			assertThrows(IllegalArgumentException.class,() -> {
				DoubleMatrix matrix1 = new DoubleMatrix(new double[][] {{1,2,3},{4,5,6}});
				DoubleMatrix matrix2 = new DoubleMatrix(new double[][] {{1,2},{4,5}});
				matrix1.subtract(matrix2);
			});
		}

		{ // Wrong rows
			assertThrows(IllegalArgumentException.class,() -> {
				DoubleMatrix matrix1 = new DoubleMatrix(new double[][] {{1,2,3},{4,5,6}});
				DoubleMatrix matrix2 = new DoubleMatrix(new double[][] {{1,2,3}});
				matrix1.subtract(matrix2);
			});
		}
	}

	@Test
	void testOperate() throws Throwable {
		{ // Success case
			DoubleMatrix matrix1 = new DoubleMatrix(new double[][] {{1,2,3},{4,5,6}});
			DoubleMatrix matrix2 = new DoubleMatrix(new double[][] {{1},{2},{3}});
			DoubleMatrix result = matrix1.operate(matrix2);
			assertEqualsMatrixWithinMargin(new DoubleMatrix(new double[][] {{14},{32}}), result, "Success case.");
		}

		{ // Mismatch
			assertThrows(IllegalArgumentException.class,() -> {
				DoubleMatrix matrix1 = new DoubleMatrix(new double[][] {{1,2,3},{4,5,6}});
				DoubleMatrix matrix2 = new DoubleMatrix(new double[][] {{1},{2}});
				matrix1.operate(matrix2);
			});
		}
	}

	@Test
	void testMultiplyBy() throws Throwable {
		{ // Success case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,2,3},{4,5,6}});
			DoubleMatrix result = matrix.multiplyBy(1.5);
			assertEqualsMatrixWithinMargin(new DoubleMatrix(new double[][] {{1.5,3,4.5},{6,7.5,9}}), result, "Success case.");
		}
	}

	@Test
	void testGetEigensystem() throws Throwable {
		{ // 1x1 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{5}});
			Eigensystem eigensystem = matrix.getEigensystem();
			assertArrayEquals(new double[] {5}, eigensystem.eigenvalues);
			assertArrayEquals(new double[] {1}, eigensystem.eigenvectors[0]);
		}

		{ // 2x2 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,2},{2,4}});
			// Expected eigenvalues 0, 5
			// Expected eigenvectors [2,-1], [1,2]
			Eigensystem eigensystem = matrix.getEigensystem();
			for(int i=0;i<eigensystem.eigenvalues.length;i++) {
				DoubleMatrix columnEigenvector = new DoubleMatrix(new double[][] {eigensystem.eigenvectors[i]}).getTranspose();
				DoubleMatrix operationResult = matrix.operate(columnEigenvector);
				DoubleMatrix expectedResult = columnEigenvector.multiplyBy(eigensystem.eigenvalues[i]);
				assertZeroMatrixWithinMargin(operationResult.subtract(expectedResult),"Failed to validate eigenvector for eigenvalue, "+eigensystem.eigenvalues[i]);
			}
		}

		{ // 2x2 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,2},{3,4}});
			Eigensystem eigensystem = matrix.getEigensystem();
			assertEquals(2, eigensystem.eigenvalues.length);
			for(int i=0;i<eigensystem.eigenvalues.length;i++) {
				DoubleMatrix columnEigenvector = new DoubleMatrix(new double[][] {eigensystem.eigenvectors[i]}).getTranspose();
				DoubleMatrix operationResult = matrix.operate(columnEigenvector);
				DoubleMatrix expectedResult = columnEigenvector.multiplyBy(eigensystem.eigenvalues[i]);
				assertZeroMatrixWithinMargin(operationResult.subtract(expectedResult),"Failed to validate eigenvector for eigenvalue, "+eigensystem.eigenvalues[i]);
			}
		}

		{ // 3x3 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{3,4,-2},{1,4,-1},{2,6,-1}});
			// https://study.com/academy/lesson/eigenvalues-eigenvectors-definition-equation-examples.html
			Eigensystem eigensystem = matrix.getEigensystem();
			assertEquals(3, eigensystem.eigenvalues.length);
			for(int i=0;i<eigensystem.eigenvalues.length;i++) {
				DoubleMatrix columnEigenvector = new DoubleMatrix(new double[][] {eigensystem.eigenvectors[i]}).getTranspose();
				DoubleMatrix operationResult = matrix.operate(columnEigenvector);
				DoubleMatrix expectedResult = columnEigenvector.multiplyBy(eigensystem.eigenvalues[i]);
				assertZeroMatrixWithinMargin(operationResult.subtract(expectedResult),"Failed to validate eigenvector for eigenvalue, "+eigensystem.eigenvalues[i]);
			}
		}

		{ // 3rd 4x4 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,1,1,1},{1,1,1,2},{1,1,3,3},{1,4,4,4}});
			Eigensystem eigensystem = matrix.getEigensystem();
			assertEquals(4, eigensystem.eigenvalues.length);
			for(int i=0;i<eigensystem.eigenvalues.length;i++) {
				DoubleMatrix columnEigenvector = new DoubleMatrix(new double[][] {eigensystem.eigenvectors[i]}).getTranspose();
				DoubleMatrix operationResult = matrix.operate(columnEigenvector);
				DoubleMatrix expectedResult = columnEigenvector.multiplyBy(eigensystem.eigenvalues[i]);
				assertZeroMatrixWithinMargin(operationResult.subtract(expectedResult),"Failed to validate eigenvector for eigenvalue, "+eigensystem.eigenvalues[i]);
			}
		}

		{ // Non-square case
			assertThrows(Throwable.class,()->{
				DoubleMatrix matrix = new DoubleMatrix(new double[][] {{5},{5}});
				matrix.getEigensystem();
			});
		}
	}

	@Test
	void testGetInverse() throws Throwable {
		{ // 1x1 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{5}});
			assertEqualsMatrixWithinMargin(new DoubleMatrix(new double[][] {{0.2}}), matrix.getInverse(),"1x1 matrix inverse found as expected.");
		}

		{ // 2x2 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{4,7},{2,6}});
			assertEqualsMatrixWithinMargin(new DoubleMatrix(new double[][] {{0.6,-0.7},{-0.2,0.4}}), matrix.getInverse(),"2x2 matrix inverse found as expected.");
		}

		{ // 0-pivot case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{0,1},{1,1}});
			assertEqualsMatrixWithinMargin(new DoubleMatrix(new double[][] {{-1,1},{1,0}}), matrix.getInverse(),"2x2 0-pivot inverse found as expected.");
		}

		{ // 3x3 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{3,0,2},{2,0,-2},{0,1,1}});
			assertEqualsMatrixWithinMargin(new DoubleMatrix(new double[][] {{0.2,0.2,0},{-0.2,0.3,1},{0.2,-0.3,0}}), matrix.getInverse(),"3x3 matrix inverse found as expected.");
		}

		{ // 4x4 identity case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}});
			assertEqualsMatrixWithinMargin(matrix, matrix.getInverse(),"4x4 identity inverse found as expected.");
		}
	}
	
	private static void assertEqualsWithinMargin(double expected, double actual, String message) {
		assertTrue(actual>=expected-ERROR_MARGIN,message);
		assertTrue(actual<=expected+ERROR_MARGIN,message);
	}
	
	private static void assertZeroMatrixWithinMargin(DoubleMatrix m, String message) {
		for(int i=0;i<m.getRows();i++)
			for(int j=0;j<m.getColumns();j++)
				assertEqualsWithinMargin(0,m.getElementAt(i, j),message+" (Non-zero, "+m.getElementAt(i, j)+", at matrix element ("+i+","+j+").)");
	}
	
	private static void assertEqualsMatrixWithinMargin(DoubleMatrix mExpected, DoubleMatrix mActual, String message) {
		assertEquals(mExpected.getRows(),mActual.getRows(),message+" (Unequal Rows)");
		assertEquals(mExpected.getColumns(),mActual.getColumns(),message+" (Unequal Columns)");
		for(int i=0;i<mExpected.getRows();i++)
			for(int j=0;j<mExpected.getColumns();j++)
				assertEqualsWithinMargin(mExpected.getElementAt(i, j),mActual.getElementAt(i, j),message+" (Unexpected matrix element at ("+i+","+j+").)");
	}
}
