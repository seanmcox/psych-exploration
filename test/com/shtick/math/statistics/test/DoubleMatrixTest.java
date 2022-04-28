package com.shtick.math.statistics.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.shtick.math.statistics.DoubleMatrix;
import com.shtick.math.statistics.Eigensystem;

class DoubleMatrixTest {
	private static final double ERROR_MARGIN = 0.0001;

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

		{ // 5x5 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{1,2,3,4,5},{2,4,4,5,6},{3,4,7,6,7},{4,5,6,10,8},{5,6,7,8,13}});
			double determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(-216, determinant, ""+(-216)+"="+determinant);
		}

		{ // 2nd 5x5 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{-6,-3,-2,-1,0},{-3,-3,-1,0,1},{-2,-1,0,1,2},{-1,0,1,3,3},{0,1,2,3,6}});
			double determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(1.77635683940025E-15, determinant, ""+(1.77635683940025E-15)+"="+determinant);
		}

		{ // 3rd 5x5 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{-0.61,-3,-0.22,-1,0.03},{-3,-0.34,-1,0.05,1},{-0.26,-1,0.07,1,0.28},{-1,0.09,1,0.3,3},{0.01,1,0.22,3,0.63}});
			double determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(4.575367541, determinant, ""+(4.575367541)+"="+determinant);
		}

		{ // 4th 5x5 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {
				{2.9536729106383066, -0.7848049354590572, -0.8722520790355663, -1.1794750224630839, -0.6088192691845157},
				{-0.7848049354590572, 2.926003170065958, 1.4354217988876488, 1.1835865529248915, 0.9406825915525476},
				{-0.8722520790355663, 1.4354217988876488, 3.105852800247565, 1.143519357163422, 1.1115887819463408},
				{-1.1794750224630839, 1.1835865529248915, 1.143519357163422, 2.9946440555071696, 0.6974983537999642},
				{-0.6088192691845157, 0.9406825915525476, 1.1115887819463408, 0.6974983537999642, 3.3865066618536717}
			});
			double determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(116.43935355777167, determinant, ""+(116.43935355777167)+"="+determinant);
		}

		{ // 6x6 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {
				{2.9536729106383066, -0.7848049354590572, -0.8722520790355663, -1.1794750224630839, -0.6088192691845157,-1.2162845561497264},
				{-0.7848049354590572, 2.926003170065958, 1.4354217988876488, 1.1835865529248915, 0.9406825915525476, 1.2056741402335935},
				{-0.8722520790355663, 1.4354217988876488, 3.105852800247565, 1.143519357163422, 1.1115887819463408, 1.3674230472037368},
				{-1.1794750224630839, 1.1835865529248915, 1.143519357163422, 2.9946440555071696, 0.6974983537999642, 1.9613206884976744},
				{-0.6088192691845157, 0.9406825915525476, 1.1115887819463408, 0.6974983537999642, 3.3865066618536717, 0.8071688738919389},
				{-1.2162845561497264, 1.2056741402335935, 1.3674230472037368, 1.9613206884976744, 0.8071688738919389, 3.201729951257849}
			});
			double determinant = matrix.getDeterminant();
			assertEqualsWithinMargin(198.61985291914604, determinant, ""+(198.61985291914604)+"="+determinant);
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
			// Expected eigenvalues: 5.372281323269014, -0.3722813232690143
			Eigensystem eigensystem = matrix.getEigensystem();
			assertEquals(2, eigensystem.eigenvalues.length);
			for(int i=0;i<eigensystem.eigenvalues.length;i++) {
				DoubleMatrix columnEigenvector = new DoubleMatrix(new double[][] {eigensystem.eigenvectors[i]}).getTranspose();
				DoubleMatrix operationResult = matrix.operate(columnEigenvector);
				DoubleMatrix expectedResult = columnEigenvector.multiplyBy(eigensystem.eigenvalues[i]);
				assertZeroMatrixWithinMargin(operationResult.subtract(expectedResult),"Failed to validate eigenvector for eigenvalue, "+eigensystem.eigenvalues[i]);
			}
		}

// TODO Interesting test case to investigate further (Case1)
//		{ // 3x3 case
//			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{3,4,-2},{1,4,-1},{2,6,-1}});
//			// Expected eigenvalues: 3, 2, 1
//			// https://study.com/academy/lesson/eigenvalues-eigenvectors-definition-equation-examples.html
//			Eigensystem eigensystem = matrix.getEigensystem();
//			assertEquals(3, eigensystem.eigenvalues.length);
//			for(int i=0;i<eigensystem.eigenvalues.length;i++) {
//				DoubleMatrix columnEigenvector = new DoubleMatrix(new double[][] {eigensystem.eigenvectors[i]}).getTranspose();
//				DoubleMatrix operationResult = matrix.operate(columnEigenvector);
//				DoubleMatrix expectedResult = columnEigenvector.multiplyBy(eigensystem.eigenvalues[i]);
//				assertZeroMatrixWithinMargin(operationResult.subtract(expectedResult),"Failed to validate eigenvector for eigenvalue, "+eigensystem.eigenvalues[i]);
//			}
//		}

		{ // 3x3 case
			DoubleMatrix matrix = new DoubleMatrix(new double[][] {{3,4,-2},{0,4,-1},{0,6,-1}});
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

		DoubleMatrix[] matrices = new DoubleMatrix[]{
				new DoubleMatrix(new double[][] {
					{2.9536729106383066, -0.7848049354590572, -0.8722520790355663, -1.1794750224630839},
					{-0.7848049354590572, 2.926003170065958, 1.4354217988876488, 1.1835865529248915},
					{-0.8722520790355663, 1.4354217988876488, 3.105852800247565, 1.143519357163422},
					{-1.1794750224630839, 1.1835865529248915, 1.143519357163422, 2.9946440555071696},
				}),
//				new DoubleMatrix(new double[][] {{1,2,3,4,5},{2,4,4,5,6},{3,4,7,6,7},{4,5,6,10,8},{5,6,7,8,13}}),
				new DoubleMatrix(new double[][] {
					{2.9536729106383066, -0.7848049354590572, -0.8722520790355663, -1.1794750224630839, -0.6088192691845157},
					{-0.7848049354590572, 2.926003170065958, 1.4354217988876488, 1.1835865529248915, 0.9406825915525476},
					{-0.8722520790355663, 1.4354217988876488, 3.105852800247565, 1.143519357163422, 1.1115887819463408},
					{-1.1794750224630839, 1.1835865529248915, 1.143519357163422, 2.9946440555071696, 0.6974983537999642},
					{-0.6088192691845157, 0.9406825915525476, 1.1115887819463408, 0.6974983537999642, 3.3865066618536717}
				}),
				new DoubleMatrix(new double[][] {
					{2.9536729106383066, -0.7848049354590572, -0.8722520790355663, -1.1794750224630839, -0.6088192691845157,-1.2162845561497264},
					{-0.7848049354590572, 2.926003170065958, 1.4354217988876488, 1.1835865529248915, 0.9406825915525476, 1.2056741402335935},
					{-0.8722520790355663, 1.4354217988876488, 3.105852800247565, 1.143519357163422, 1.1115887819463408, 1.3674230472037368},
					{-1.1794750224630839, 1.1835865529248915, 1.143519357163422, 2.9946440555071696, 0.6974983537999642, 1.9613206884976744},
					{-0.6088192691845157, 0.9406825915525476, 1.1115887819463408, 0.6974983537999642, 3.3865066618536717, 0.8071688738919389},
					{-1.2162845561497264, 1.2056741402335935, 1.3674230472037368, 1.9613206884976744, 0.8071688738919389, 3.201729951257849}
				}),
		};
		for(DoubleMatrix matrix:matrices){
			String testCaseDescription = ""+matrix.getRows()+"x"+matrix.getColumns();
			Eigensystem eigensystem=null;
			try {
				eigensystem = matrix.getEigensystem();
			}
			catch(Throwable t) {
				fail(testCaseDescription+" failure",t);
			}
			assertEquals(matrix.getRows(), eigensystem.eigenvalues.length);
			for(int i=0;i<eigensystem.eigenvalues.length;i++) {
				DoubleMatrix columnEigenvector = new DoubleMatrix(new double[][] {eigensystem.eigenvectors[i]}).getTranspose();
				DoubleMatrix operationResult = matrix.operate(columnEigenvector);
				DoubleMatrix expectedResult = columnEigenvector.multiplyBy(eigensystem.eigenvalues[i]);
				assertZeroMatrixWithinMargin(operationResult.subtract(expectedResult),testCaseDescription+" - "+eigensystem.eigenvalues[i]+" - Failed to validate eigenvector for eigenvalue, "+eigensystem.eigenvalues[i]);
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
