/*
 * Matrix.java
 *
 * Created on December 30, 2006, 10:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtick.math.statistics;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author seanmcox
 */
public class DoubleMatrix {
    protected double[][] matrix;    

    /**
     * Creates a new instance of Matrix 
     * @param matrix 
     * @throws IllegalArgumentException
     */
    public DoubleMatrix(double[][] matrix) throws IllegalArgumentException{
		int i;
		if(matrix.length == 0)
		    throw new IllegalArgumentException("Only non-empty matrix supported.");
		if(matrix[0].length == 0)
		    throw new IllegalArgumentException("Only non-empty matrix rows supported.");
		for(i = 1;i<matrix.length;i++)
			if(matrix[i].length != matrix[0].length)
				throw new IllegalArgumentException("Matrix rows not of equal length.");
		this.matrix=matrix;
    }
    
    /**
     * 
     * @return The number of rows in the matrix 
     */
    public int getRows() {
    	return matrix.length;
    }
    
    /**
     * 
     * @return The number of columns in the matrix 
     */
    public int getColumns() {
    	return matrix[0].length;
    }
    
    /**
     * 
     * @param row
     * @param column
     * @return The matrix element at the given row and column position.
     */
    public double getElementAt(int row, int column) {
    	return matrix[row][column];
    }
    
    /**
     * 
     * @return The determinant of the matrix.
     * @throws RuntimeException is called on a non-square matrix.
     */
    public double getDeterminant(){
    	if(!isSquare())
    		throw new RuntimeException("The determinant is only defined for square matrices.");
    	if(matrix.length==1)
    		return matrix[0][0];
    	if(matrix.length==2)
    		return matrix[0][0]*matrix[1][1]-matrix[1][0]*matrix[0][1];
    	if(matrix.length==3)
    		return matrix[0][0]*(matrix[1][1]*matrix[2][2]-matrix[2][1]*matrix[1][2])-matrix[1][0]*(matrix[0][1]*matrix[2][2]-matrix[2][1]*matrix[0][2])+matrix[2][0]*(matrix[0][1]*matrix[1][2]-matrix[1][1]*matrix[0][2]);

    	// Naive method
    	//int[] intList = new int[matrix.length];
    	//for(int i=0;i<intList.length;i++)
    	//	intList[i]=i+1;
    	// return getSubdeterminant(0,0,intList);
    	
    	// return getDeterminantByClow();
    	return getDeterminantByReduction();
    }

    /**
     * The algorithm below is based on https://page.mi.fu-berlin.de/rote/Papers/pdf/Division-free+algorithms.pdf
     * 
     * Runs in O(n^4) and requires O(n^2) memory.
     * 
     * It is slower than getDeterminantByReduction(), but it requires no divisions, so it makes a more precise calculation.
     * 
     * @return
     */
    private double getDeterminantByClow() {
    	// These arrays represent [clow_head][current_vertex]
    	double[][] sumsCurrent = new double[matrix.length][matrix.length];
    	double[][] sumsNext = new double[matrix.length][matrix.length];
    	double[][] sumsTemp;
    	for(int l=0;l<matrix.length;l++)
    		sumsCurrent[l][l]=((matrix.length%2)==1)?-1:1;
    	int c0; // Head index.
    	int c; // Current index.
    	for(int l=1;l<matrix.length;l++) {
    		for(c0=0;c0<matrix.length;c0++) {
    			for(c=c0;c<matrix.length;c++) {
        			if(sumsCurrent[c0][c]==0)
        				continue;
        			for(int cPlus=c0+1;cPlus<matrix.length;cPlus++)
        				sumsNext[c0][cPlus]+=sumsCurrent[c0][c]*matrix[c][cPlus];
        			for(int c0New=c0+1;c0New<matrix.length;c0New++)
        				sumsNext[c0New][c0New]-=sumsCurrent[c0][c]*matrix[c][c0];
        			sumsCurrent[c0][c] = 0;
        		}
    		}
    		sumsTemp = sumsNext;
    		sumsNext = sumsCurrent;
    		sumsCurrent = sumsTemp;
    	}
    	double retval = 0;
		for(c0=0;c0<matrix.length;c0++)
			for(c=c0;c<matrix.length;c++)
    			retval -= sumsCurrent[c0][c]*matrix[c][c0];
		return retval;
    }
    
    /**
     * A simple row reduction algorithm.
     * 
     * Runs in O(n^3) and requires O(n^2) memory.
     * 
     * @return
     */
    private double getDeterminantByReduction() {
    	// Note: This action only has meaning for square matrices
    	double[][] temp=new double[matrix.length][matrix.length];
    	for(int row=0;row<matrix.length;row++) {
        	for(int col=0;col<matrix.length;col++) {
        		temp[row][col] = matrix[row][col];
        	}
    	}
    	double t;
    	double retval = 1;
    	for(int i=0;i<temp.length;i++) {
    		if(temp[i][i]==0) {
    			int goodCol=-1;
    			double absColValue = 0;
    			for(int j=i+1;j<temp.length;j++) {
    				double abs = Math.abs(temp[i][j]);
    				if(abs>absColValue) {
    					absColValue = abs;
    					goodCol = j;
    				}
    			}
    			if(goodCol<0)
    				return 0;
    			swapCols(i,goodCol,temp);
    			retval*=-1;
    		}
    		retval*=temp[i][i];
    		for(int row=i+1;row<matrix.length;row++) {
    			t = temp[row][i];
    			temp[row][i] = 0;
        		for(int col=i;col<matrix.length;col++)
        			temp[row][col] -= t*temp[i][col]/temp[i][i];
    		}
    	}
    	return retval;
    }
    
    private static void swapCols(int col1, int col2, double[][] m) {
    	double t;
    	for(int i=0;i<m.length;i++) {
    		t = m[i][col1];
    		m[i][col1] = m[i][col2];
    		m[i][col2] = t;
    	}
    }
    
    /*
     * |a b c x|
     * |b d x c|
     * |c x d b|
     * |x c b a|
     * 
     * |a b       c       x      |
     * |0 d-b*b/a x-bc/a  c-bx/a |
     * |0 x-bc/a  d-c*c/a b-cx/a |
     * |0 c-bx/a  b-cx/a  a-x*x/a|
     * 
     * |a b       c                                  x                                  |
     * |0 d-b*b/a x-bc/a                             c-bx/a                             |
     * |0 0       d-c*c/a-(x-bc/a)^2/(d-b*b/a)       b-cx/a-(x-bc/a)*(c-bx/a)/(d-b*b/a) |
     * |0 0       b-cx/a-(x-bc/a)*(c-bx/a)/(d-b*b/a) a-x*x/a-(c-bx/a)^2/(d-b*b/a)       |
     * 
     * |a b       c                            x                                                                                                   |
     * |0 d-b*b/a x-bc/a                       c-bx/a                                                                                              |
     * |0 0       d-c*c/a-(x-bc/a)^2/(d-b*b/a) b-cx/a-(x-bc/a)*(c-bx/a)/(d-b*b/a)                                                                  |
     * |0 0       0                            a-x*x/a-(c-bx/a)^2/(d-b*b/a)-((b-cx/a-(x-bc/a)*(c-bx/a)/(d-b*b/a)))^2/(d-c*c/a-(x-bc/a)^2/(d-b*b/a))|
     * 
     * |a b  c              x                                                          |
     * |0 d' x-bc/a         c-bx/a                                                     |
     * |0 0  d''-(x-n)^2/d' b-cx/a-(x-bc/a)*(c-bx/a)/(d-b*b/a)                         |
     * |0 0  0              a-x*x/a-(n'-mx)^2-(b-cx/a-(x-n)*(n'-mx))^2/(d''-(x-n)^2/d')|
     * 
     */
    
    /**
     * 
     * @param row The relevant starting row for the this determinant calculation.
     * @param startCol The first relevant column listed in intList
     * @param intList A kind of linked list. Each element in the array represents a column in the matrix. The first relevant column is identified by startCol. The value at each relevant column points to the next relevant column. 
     * @return The value of the identified subdeterminant.
     */
    private double getSubdeterminant(int row,int startCol, int[] intList) {
    	if(row==matrix.length-1)
    		return matrix[row][startCol];
    	double subSum = 0;
    	int sign=1;
    	int lastI=-1;
    	for(int i=startCol;i<matrix.length;i=intList[i]) {
			if(lastI<0) {
				subSum+=sign*matrix[row][i]*getSubdeterminant(row+1, intList[startCol], intList);
			}
			else{
				int temp = intList[lastI];
				intList[lastI] = intList[i];
				subSum+=sign*matrix[row][i]*getSubdeterminant(row+1, startCol, intList);
				intList[lastI] = temp;
			}
    		sign*=-1;
    		lastI=i;
    	}
    	return subSum;
    }
    
    /**
     * 
     * @param operatedOn
     * @return The result of operating on operatedOn with this matrix.
     */
    public DoubleMatrix operate(DoubleMatrix operatedOn) {
    	if(operatedOn.getRows()!=getColumns())
    		throw new IllegalArgumentException("operated must have the same number of rows as the operator has columns.");
    	double result[][] = new double[getRows()][operatedOn.getColumns()];
    	for(int ri=0;ri<result.length;ri++) {
        	for(int rj=0;rj<result[0].length;rj++) {
        		double sum=0;
        		for(int j=0;j<operatedOn.getRows();j++)
        			sum+=matrix[ri][j]*operatedOn.matrix[j][rj];
            	result[ri][rj] = sum;
        	}
    	}
    	return new DoubleMatrix(result);
    }
    
    /**
     * 
     * @return The transpose of the matrix.
     */
    public DoubleMatrix getTranspose() {
    	double result[][] = new double[getColumns()][getRows()];
    	for(int i=0;i<matrix.length;i++)
        	for(int j=0;j<matrix[0].length;j++)
            	result[j][i] = matrix[i][j];
    	return new DoubleMatrix(result);
    }
    
    /**
     * This operation is only valid for square matrices with a non-zero determinant.
     * 
     * @return The inverse of this matrix.
     */
    public DoubleMatrix getInverse() {
    	if(!isSquare())
    		throw new RuntimeException("Not a square matrix.");
    	double workingCopy[][] = new double[matrix.length][matrix.length];
    	double mirror[][] = new double[matrix.length][matrix.length];
    	for(int i=0;i<matrix.length;i++) {
    		mirror[i][i] = 1;
        	for(int j=0;j<matrix.length;j++) {
        		workingCopy[i][j] = matrix[i][j];
        	}
    	}
    	for(int row=0;row<matrix.length;row++) {
    		double factor = workingCopy[row][row];
    		if(factor==0) {
    			for(int i=row+1;i<matrix.length;i++) {
    				if(workingCopy[i][row]!=0) {
    					// Swap rows
    	    			for(int j=0;j<matrix.length;j++) {
    	    				if(j>=row) {
	    	    				double temp = workingCopy[row][j];
	    	    				workingCopy[row][j] = workingCopy[i][j];
	    	    				workingCopy[i][j] = temp;
    	    				}
    	    				double temp = mirror[row][j];
    	    				mirror[row][j] = mirror[i][j];
    	    				mirror[i][j] = temp;
    	    			}
    	    			factor = workingCopy[row][row];
    				}
    			}
    			if(factor==0)
    	    		throw new RuntimeException("Matrix is not invertible.");
    		}
        	for(int col=0;col<matrix.length;col++) {
        		if(col>row)
        			workingCopy[row][col]/=factor;
        		mirror[row][col]/=factor;
        	}
        	workingCopy[row][row] = 1;
        	for(int i=0;i<matrix.length;i++) {
        		if(i!=row) {
            		double eliminatedValue = workingCopy[i][row];
	            	for(int j=0;j<matrix.length;j++) {
	            		if(j>row)
	            			workingCopy[i][j]-=eliminatedValue*workingCopy[row][j];
	            		mirror[i][j]-=eliminatedValue*mirror[row][j];
	            	}
	            	workingCopy[i][row] = 0;
	            }
        	}
    	}
    	return new DoubleMatrix(mirror);
    }
    
    /*
     * |a b|-1
     * |c d|
     * 
     * |1 b/a|1/a 0|
     * |c d  |0   1|
     * 
     * |1 b/a   | 1/a 0|
     * |0 d-cb/a|-c/a 1|
     * 
     * |1 b/a| 1/a       0        |
     * |0 1  |-c/(ad-cb) a/(ad-cb)|
     * 
     * |1 0| d/(ad-cb) -b/(ad-cb)|
     * |0 1|-c/(ad-cb)  a/(ad-cb)|
     * 
     */
    
    /**
     * 
     * @param n 
     * @return The result of multiplication by n.
     */
    public DoubleMatrix multiplyBy(double n) {
    	double result[][] = new double[getRows()][getColumns()];
    	for(int i=0;i<matrix.length;i++)
        	for(int j=0;j<matrix[0].length;j++)
            	result[i][j] = matrix[i][j]*n;
    	return new DoubleMatrix(result);
    }
    
    /**
     * 
     * @param m
     * @return The result of adding m.
     */
    public DoubleMatrix add(DoubleMatrix m) {
    	if((m.getColumns()!=getColumns())||(m.getRows()!=getRows()))
    		throw new IllegalArgumentException("Added matrices must have the rame number of rows and columns.");
    	double result[][] = new double[getRows()][getColumns()];
    	for(int i=0;i<matrix.length;i++)
        	for(int j=0;j<matrix[0].length;j++)
            	result[i][j] = matrix[i][j]+m.matrix[i][j];
    	return new DoubleMatrix(result);
    }
    
    /**
     * 
     * @param m
     * @return The result of adding m.
     */
    public DoubleMatrix subtract(DoubleMatrix m) {
    	if((m.getColumns()!=getColumns())||(m.getRows()!=getRows()))
    		throw new IllegalArgumentException("Added matrices must have the rame number of rows and columns.");
    	double result[][] = new double[getRows()][getColumns()];
    	for(int i=0;i<matrix.length;i++)
        	for(int j=0;j<matrix[0].length;j++)
            	result[i][j] = matrix[i][j]-m.matrix[i][j];
    	return new DoubleMatrix(result);
    }
    
    /**
     * 
     * @return An Eigensystem with both eigenvalues and eigenvectors.
     * @throws Throwable If imaginary eigenvectors are found.
     */
    public Eigensystem getEigensystem() throws Throwable{
    	return getEigensystem(null);
    }

    /**
     * 
     * @param statusTracker A StatusTracker for reporting progress. Can be null.
     * @return An Eigensystem with both eigenvalues and eigenvectors.
     * @throws Throwable If imaginary eigenvectors are found.
     */
    public Eigensystem getEigensystem(StatusTracker statusTracker) throws Throwable{
    	if(!isSquare())
    		throw new RuntimeException("Not a square matrix.");
    	if(statusTracker!=null)
    		statusTracker.updateStatus("Setup", 0, 0);
		double[] eigenvalues=new double[matrix.length];
		double[][] eigenvectors=new double[matrix.length][];
	
		PolynomialMatrix m = new PolynomialMatrix(matrix);
		PolynomialMatrix eigensystem=m.subtract(PolynomialMatrix.createScalingMatrix(m.getRows(),new Polynomial(new double[]{0,1})));
	
    	if(statusTracker!=null)
    		statusTracker.updateStatus("Find eigenvalues: get determinant", 0, 0.3);
		Polynomial determinant = eigensystem.getDeterminant();
    	if(statusTracker!=null)
    		statusTracker.updateStatus("Find eigenvalues: find zeros of polynomial", 0.5, 0.3);
		List<Double> eigenvalueList = determinant.findZeros();
		
    	if(statusTracker!=null)
    		statusTracker.updateStatus("Find eigenvectors", 0, 0.6);
		int i=0;
		for(Double d:eigenvalueList) {
	    	if(statusTracker!=null)
	    		statusTracker.updateStatus("Find eigenvectors", i/(double)eigenvalueList.size(), 0.6);
			eigenvalues[i]=d;
			eigenvectors[i]=getEigenvector(d,matrix);
		    i++;
		}
		
    	if(statusTracker!=null)
    		statusTracker.updateStatus("Done", 0, 1);
		return new Eigensystem(eigenvalues,eigenvectors);
	}
    
//    private Eigensystem getEigensystemByPowerMethod() {
//    	HashSet<GershgorinGroup> groups = new HashSet<>();
//    	for(int i=0;i<matrix.length;i++) {
//    		GershgorinCircle circle = new GershgorinCircle(i);
//    		GershgorinGroup matchedGroup=null;
//        	HashSet<GershgorinGroup> removalGroups = new HashSet<>();
//    		for(GershgorinGroup group:groups) {
//    			if(group.tryMerge(circle)) {
//    				if(matchedGroup==null) {
//    					matchedGroup = group;
//    				}
//    				else {
//    					matchedGroup.tryMerge(group);
//    					removalGroups.add(group);
//    				}
//    			}
//    		}
//    		if(matchedGroup==null)
//    			groups.add(new GershgorinGroup(circle));
//    		else
//        		groups.removeAll(removalGroups);
//    	}
//
//    	
//    	// TODO Hmm... for a correlation matrix, all of the diagonal values are 1 and and the absolute value of all the other values are less than or equal to 1. That will probably make this tricky.
//    }

    /**
     * In practice, there could be multiple eigenvectors for a given eigenvalue.
     * TODO Extend this method to find cases with multiple eigenvectors.
     * TODO If a row or two in the M - lI matrix is zero, then I think it will break this method. Test out this case and develop a resilient method for handling.
     * 
     * @param eigenvalue
     * @param m
     * @return A normalized eigenvector
     */
    private static double[] getEigenvector(double eigenvalue, double[][] m){
    	// Setup M - lI matrix.
    	double[][] m2 = new double[m.length][m[0].length];
    	for(int row=0;row<m.length;row++){
    		for(int col=0;col<m[0].length;col++)
    			m2[row][col]=m[row][col];
    		m2[row][row]-=eigenvalue;
    	}

    	double[] retval = new double[m.length];
    	boolean[] retvalCalculated = new boolean[m.length];
    	boolean[] rowPicked = new boolean[m.length];
    	int retvalCalculatedCount=0;
    	double[] rowSum = new double[m.length];
    	int nonzeroRowCount=0;
    	int[] rowComplexity = new int[m.length];
    	int[] columnComplexity = new int[m.length];
    	boolean hasOne=false; // True if there is a row with a row complexity of 1 and false otherwise.
    	// Get some row/column statistics
    	for(int row=0;row<m2.length;row++){
    		for(int col=0;col<m2.length;col++) {
    			if((m2[row][col]!=0)&&(!retvalCalculated[col])) {
    				rowComplexity[row]++;
    				columnComplexity[col]++;
    			}
    		}
    		if(rowComplexity[row]==1)
    			hasOne=true;
    	}
    	int i,j;
    	// Look for an easy answer
    	for(i=0;i<m2.length;i++){
    		if((rowComplexity[i]==0)&&(columnComplexity[i]==0)) {
    			retval[i] = 1;
    			return retval;
    		}
    	}
    	// Look for a harder answer.
    	while(retvalCalculatedCount<m2.length) {
    		if(hasOne) {
		    	for(i=0;i<m2.length;i++){
					if(rowComplexity[i]==1) {
						int col = -1;
			    		for(j=0;j<m2.length;j++) {
			    			if(m2[i][j]!=0) {
		    					col=j;
			    			}
			    		}
						retval[col] = rowSum[i]/m2[i][col];
						retvalCalculated[col] = true;
						retvalCalculatedCount++;
						hasOne=false;
						nonzeroRowCount = 0;
			    		for(j=0;j<m2.length;j++) {
			    			if(m2[j][col]!=0) {
			    				rowComplexity[j]--;
			    				rowSum[j] -= retval[col]*m2[j][col];
			    				m2[j][col] = 0;
			    			}
			    			if(rowSum[j]!=0)
			    				nonzeroRowCount++;
			        		if(rowComplexity[j]==1)
			        			hasOne=true;
			    		}
					}
	    		}
	    	}
    		else {
    			int complexCol=-1;
    			int complexity=0;
    			for(int col=0;col<m2.length;col++) {
    				if(retvalCalculated[col])
    					continue;
    				if(complexCol<0) {
    					complexCol = col;
    					complexity = columnComplexity[col];
    				}
    				else {
    					if(columnComplexity[col]>complexity) {
        					complexCol = col;
        					complexity = columnComplexity[col];
    					}
    				}
    			}
    			if(complexity==1) {
    				throw new RuntimeException("Ran into an interesting case that may need further investigation;");
    			}
    			if(complexity==0) {
					retvalCalculatedCount++;
					retvalCalculated[complexCol] = true;
    				continue;
    			}
    			if(nonzeroRowCount==0) {
	    			// Assume 1 as the value of a variable.
					retvalCalculatedCount++;
					retvalCalculated[complexCol] = true;
    				retval[complexCol] = 1;
    		    	for(i=0;i<m2.length;i++){
    		    		if(m2[i][complexCol]!=0) {
    		    			rowSum[i] = -m2[i][complexCol];
    		    			m2[i][complexCol] = 0;
    		    			rowComplexity[i]--;
    		    			nonzeroRowCount++;
    		    			if(rowComplexity[i]==1)
    		    				hasOne=true;
    		    		}
    		    	}
	    		}
	    		else {
	    			// Pick a row to represent the picked column.
	    			int pickedRow=-1;
	    			int pickedRowComplexity=0;
    		    	for(i=0;i<m2.length;i++){
    		    		if((rowComplexity[i]>0)&&(!rowPicked[i])&&(rowSum[i]!=0)&&(m2[i][complexCol]!=0)) {
    		    			if(pickedRow<0) {
    		    				pickedRow = i;
    		    				pickedRowComplexity = rowComplexity[i];
    		    			}
    		    			else if(rowComplexity[i]<pickedRowComplexity) {
    		    				pickedRow = i;
    		    				pickedRowComplexity = rowComplexity[i];
    		    			}
    		    		}
    		    	}
    		    	rowPicked[pickedRow] = true;

    		    	// Normalize the row to complexCol value == 1
    		    	for(j=0;j<m2.length;j++){
    		    		if(j!=complexCol) {
    		    			m2[pickedRow][j] /= m2[pickedRow][complexCol];
    		    		}
    		    	}
		    		rowSum[pickedRow] /= m2[pickedRow][complexCol];
		    		m2[pickedRow][complexCol] = 1;

		    		// Subtract the picked row from all others to zero out the complexCol value.
    		    	for(i=0;i<m2.length;i++){
    		    		if((i!=pickedRow)&&(rowComplexity[i]>0)&&(m2[i][complexCol]!=0)) {
    	    		    	for(j=0;j<m2.length;j++) {
    	    		    		if(j!=complexCol) {
    	    		    			if(m2[i][j]==0) {
    	    		    				m2[i][j] -= m2[pickedRow][j]*m2[i][complexCol];
    	    		    				if(m2[i][j]!=0)
    	    		    					rowComplexity[i]++;
    	    		    			}
    	    		    			else {
    	    		    				m2[i][j] -= m2[pickedRow][j]*m2[i][complexCol];
    	    		    				if(m2[i][j]==0)
    	    		    					rowComplexity[i]--;
    	    		    			}
    	    		    		}
    	    		    	}
    			    		rowSum[i] -= rowSum[pickedRow]*m2[i][complexCol];
    			    		if(m2[i][complexCol]!=0) {
    			    			rowComplexity[i]--;
        		    			if(rowComplexity[i]==1)
        		    				hasOne=true;
        	    		    	m2[i][complexCol] = 0;
    			    		}
    		    		}
    		    	}
    		    	columnComplexity[complexCol] = 1;
	    		}
    		}
    	}

    	VectorMath.normalize(retval);
    	return retval;
    }

    /**
     * 
     * @return true if the matrix is a square matrix and false otherwise.
     */
    public boolean isSquare() {
    	return matrix.length==matrix[0].length;
    }
    
    /**
     * 
     */
    public void print(){
    	print(System.out);
    }

    /**
     * @param out
     */
    public void print(PrintStream out){
    	printArray(matrix, out);
    }

    /**
     * @param out
     */
    private static void printArray(double[][] array, PrintStream out){
		int i, j;
		for(i = 0;i<array.length;i++){
		    for(j = 0;j<array[i].length;j++){
			if(j>0)
			    out.print(", ");
			out.print(array[i][j]);
		    }
		    out.print("\n");
		}
    }
    
    private class GershgorinCircle {
    	public double center;
    	public double radius;
    	public double min;
    	public double max;
    	
    	/**
    	 * 
    	 * @param row
    	 */
    	public GershgorinCircle(int row) {
    		center = matrix[row][row];
    		for(int i=0;i<matrix.length;i++)
    			if(i!=row)
    				radius+=Math.abs(matrix[row][i]);
    		min = center-radius;
    		max = center+radius;
    	}
    	
    	public boolean overlaps(GershgorinCircle circle) {
    		if((min<circle.min)&&(max>circle.min))
    			return true;
    		if((min<circle.max)&&(max>circle.max))
    			return true;
    		if((circle.min<max)&&(circle.max>max))
    			return true;
    		return false;
    	}
    }
    
    private class GershgorinGroup {
    	public HashSet<GershgorinCircle> members = new HashSet<>();
    	public double min;
    	public double max;
    	
    	/**
    	 * 
    	 * @param seed
    	 */
    	public GershgorinGroup(GershgorinCircle seed) {
    		members.add(seed);
    		min = seed.min;
    		max = seed.max;
    	}
    	
    	public boolean tryMerge(GershgorinCircle circle) {
    		if(((min<circle.min)&&(max>circle.min))||
    		   ((min<circle.max)&&(max>circle.max))||
    		   ((circle.min<max)&&(circle.max>max))) {
    			members.add(circle);
    			min=Math.min(circle.min,min);
    			max=Math.min(circle.max,max);
    			return true;
    		}
    		return false;
    	}
    	
    	public boolean tryMerge(GershgorinGroup group) {
    		if(((min<group.min)&&(max>group.min))||
    		   ((min<group.max)&&(max>group.max))||
    		   ((group.min<max)&&(group.max>max))) {
    			members.addAll(group.members);
    			min=Math.min(group.min,min);
    			max=Math.min(group.max,max);
    			return true;
    		}
    		return false;
    	}
    }
}
