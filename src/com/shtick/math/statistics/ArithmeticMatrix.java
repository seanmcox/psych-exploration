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

/**
 *
 * @author seanmcox
 */
public class PolynomialMatrix {
	private static final Polynomial UNIT = new Polynomial(new double[] {1});
	private static final Polynomial ZERO = new Polynomial(new double[0]);
    protected Polynomial[][] matrix;    

    /**
     * Creates a new instance of PolynomialMatrix 
     * @param matrix 
     * @throws IllegalArgumentException
     */
    public PolynomialMatrix(Polynomial[][] matrix) throws IllegalArgumentException{
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
     * Creates a new instance of PolynomialMatrix 
     * @param matrix 
     * @throws IllegalArgumentException
     */
    public PolynomialMatrix(DoubleMatrix matrix) throws IllegalArgumentException{
		this(matrix.matrix);
    }

    /**
     * Creates a new instance of PolynomialMatrix 
     * @param matrix 
     * @throws IllegalArgumentException
     */
    public PolynomialMatrix(double[][] matrix) throws IllegalArgumentException{
		if(matrix.length == 0)
		    throw new IllegalArgumentException("Only non-empty matrix supported.");
		if(matrix[0].length == 0)
		    throw new IllegalArgumentException("Only non-empty matrix rows supported.");
		this.matrix = new Polynomial[matrix.length][matrix[0].length];
		int i,j;
		for(i = 0;i<this.matrix.length;i++)
			for(j = 0;j<this.matrix[0].length;j++)
				this.matrix[i][j] = new Polynomial(new double[] {matrix[i][j]});
    }
    
    /**
     * 
     * @param size
     * @return An identity matrix of the given size.
     */
    public static PolynomialMatrix createIdentityMatrix(int size) {
    	return createScalingMatrix(size, UNIT);
    }
    
    /**
     * 
     * @param size
     * @param scale
     * @return A matrix equal to the identity matrix multiplied by scale.
     */
    public static PolynomialMatrix createScalingMatrix(int size, Polynomial scale) {
    	Polynomial[][] matrix = new Polynomial[size][size];
		int i,j;
		for(i = 0;i<size;i++)
			for(j = 0;j<size;j++)
				matrix[i][j] = (i==j)?scale:ZERO;
		return new PolynomialMatrix(matrix);
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
    public Polynomial getElementAt(int row, int column) {
    	return matrix[row][column];
    }
    
    /**
     * 
     * @param operatedOn
     * @return The result of operating on operatedOn with this matrix.
     */
    public PolynomialMatrix operate(PolynomialMatrix operatedOn) {
    	if(operatedOn.getRows()!=getColumns())
    		throw new IllegalArgumentException("operated must have the same number of rows as the operator has columns.");
    	Polynomial result[][] = new Polynomial[getRows()][operatedOn.getColumns()];
    	for(int ri=0;ri<result.length;ri++) {
        	for(int rj=0;rj<result[0].length;rj++) {
        		Polynomial sum=new Polynomial(new double[] {});
        		for(int j=0;j<operatedOn.getRows();j++)
        			sum = sum.add(matrix[ri][j].multiply(operatedOn.matrix[j][rj]));
            	result[ri][rj] = sum;
        	}
    	}
    	return new PolynomialMatrix(result);
    }
    
    /**
     * 
     * @param m
     * @return
     */
    public PolynomialMatrix subtract(PolynomialMatrix m) {
    	if((getRows()!=m.getRows())||(getColumns()!=m.getColumns()))
    		throw new IllegalArgumentException("Only matrices of the same size and shape may be subtracted.");
    	Polynomial[][] retvalMatrix = new Polynomial[getRows()][getColumns()];
		int i,j;
		for(i = 0;i<retvalMatrix.length;i++)
			for(j = 0;j<retvalMatrix[0].length;j++)
				retvalMatrix[i][j] = matrix[i][j].subtract(m.matrix[i][j]);
    	return new PolynomialMatrix(retvalMatrix);
    }
    
    /**
     * 
     * @param m
     * @return
     */
    public PolynomialMatrix add(PolynomialMatrix m) {
    	if((getRows()!=m.getRows())||(getColumns()!=m.getColumns()))
    		throw new IllegalArgumentException("Only matrices of the same size and shape may be added.");
    	Polynomial[][] retvalMatrix = new Polynomial[getRows()][getColumns()];
		int i,j;
		for(i = 0;i<retvalMatrix.length;i++)
			for(j = 0;j<retvalMatrix[0].length;j++)
				retvalMatrix[i][j] = matrix[i][j].add(m.matrix[i][j]);
    	return new PolynomialMatrix(retvalMatrix);
    }
    
    /**
     * 
     * @param n 
     * @return The result of multiplication by n.
     */
    public PolynomialMatrix multiplyBy(double n) {
    	return multiplyBy(new Polynomial(new double[] {n}));
    }
    
    /**
     * 
     * @param n 
     * @return The result of multiplication by n.
     */
    public PolynomialMatrix multiplyBy(Polynomial n) {
    	Polynomial result[][] = new Polynomial[getRows()][getColumns()];
    	for(int i=0;i<matrix.length;i++)
        	for(int j=0;j<matrix[0].length;j++)
            	result[i][j] = matrix[i][j].multiply(n);
    	return new PolynomialMatrix(result);
    }
    
    /**
     * 
     * @return The determinant of the matrix.
     * @throws RuntimeException is called on a non-square matrix.
     */
    public Polynomial getDeterminant(){
    	if(!isSquare())
    		throw new RuntimeException("The determinant is only defined for square matrices.");
    	if(matrix.length==1)
    		return matrix[0][0];
    	if(matrix.length==2)
    		return matrix[0][0].multiply(matrix[1][1]).subtract(matrix[1][0].multiply(matrix[0][1]));
    	if(matrix.length==3)
    		return matrix[0][0].multiply(matrix[1][1].multiply(matrix[2][2]).subtract(matrix[2][1].multiply(matrix[1][2])))
    				.subtract(matrix[1][0].multiply(matrix[0][1].multiply(matrix[2][2]).subtract(matrix[2][1].multiply(matrix[0][2]))))
    				.add(matrix[2][0].multiply(matrix[0][1].multiply(matrix[1][2]).subtract(matrix[1][1].multiply(matrix[0][2]))));
    	return getDeterminantByClow();
//    	int[] intList = new int[matrix.length];
//    	for(int i=0;i<intList.length;i++)
//    		intList[i]=i+1;
//    	return getSubdeterminant(0,0,intList);
    }
    
    private Polynomial getDeterminantByClow() {
    	// These arrays represent [clow_head][current_vertex]
    	Polynomial[][] sumsCurrent = new Polynomial[matrix.length][matrix.length];
    	Polynomial[][] sumsNext = new Polynomial[matrix.length][matrix.length];
    	Polynomial[][] sumsTemp;
    	for(int l=0;l<matrix.length;l++)
    		sumsCurrent[l][l]=((matrix.length%2)==1)?new Polynomial(new double[] {-1}):new Polynomial(new double[] {1});
    	int c0; // Head index.
    	int c; // Current index.
    	for(int l=1;l<matrix.length;l++) {
    		for(c0=0;c0<matrix.length;c0++) {
    			for(c=c0;c<matrix.length;c++) {
        			if((sumsCurrent[c0][c]==null)||(sumsCurrent[c0][c].coefficients.length==0))
        				continue;
        			for(int cPlus=c0+1;cPlus<matrix.length;cPlus++) {
        				if(sumsNext[c0][cPlus]==null)
        					sumsNext[c0][cPlus] = new Polynomial(new double[0]);
        				sumsNext[c0][cPlus]=sumsNext[c0][cPlus].add(sumsCurrent[c0][c].multiply(matrix[c][cPlus]));
        			}
        			for(int c0New=c0+1;c0New<matrix.length;c0New++) {
        				if(sumsNext[c0New][c0New]==null)
        					sumsNext[c0New][c0New] = new Polynomial(new double[0]);
        				sumsNext[c0New][c0New]=sumsNext[c0New][c0New].subtract(sumsCurrent[c0][c].multiply(matrix[c][c0]));
        			}
        			sumsCurrent[c0][c] = null;
        		}
    		}
    		sumsTemp = sumsNext;
    		sumsNext = sumsCurrent;
    		sumsCurrent = sumsTemp;
    	}
    	Polynomial retval = new Polynomial(new double[0]);
		for(c0=0;c0<matrix.length;c0++)
			for(c=c0;c<matrix.length;c++)
				if(sumsCurrent[c0][c]!=null)
					retval = retval.subtract(sumsCurrent[c0][c].multiply(matrix[c][c0]));
		return retval;
    }
    
/*
    private Polynomial getDeterminantByReduction() {
    	Polynomial[][] numerators = new Polynomial[matrix.length][matrix.length];
    	Polynomial[][] denominators = new Polynomial[matrix.length][matrix.length];
    	for(int i=0;i<matrix.length;i++) {
        	for(int j=0;j<matrix.length;j++) {
        		numerators[i][j] = matrix[i][j];
        		denominators[i][j] = new Polynomial(new double[] {1});
        	}
    	}
    	// TODO The polynomial is not resilient to division by a non-constant polynomial
    	Polynomial destinationFactorN;
    	Polynomial destinationFactorD;
    	Polynomial sourceFactorN;
    	Polynomial sourceFactorD;
    	Polynomial retvalN = new Polynomial(new double[] {1});
    	Polynomial retvalD = new Polynomial(new double[] {1});
    	
    	for(int i=0;i<matrix.length;i++) {
        	sourceFactorN = numerators[i][i];
        	sourceFactorD = denominators[i][i];
    		retvalN = retvalN.multiply(sourceFactorN);
    		retvalD = retvalD.multiply(sourceFactorD);
        	for(int j=i+1;j<matrix.length;j++) {
        		destinationFactorN = numerators[j][i];
        		destinationFactorD = denominators[j][i];
        		numerators[j][i] = new Polynomial(new double[] {});
        		denominators[j][i] = new Polynomial(new double[] {1});
            	for(int k=i+1;k<matrix.length;k++) {
            		matrix[j][k] = matrix[j][k].subtract(matrix[i][j].divide(sourceFactor).multiply(destinationFactor));
            	}
        	}
    	}
    	return retval;
    }
*/
    /*
     * |M|=
     * 
     * | A B C |
     * | D E F |
     * | G H I |
     * 
     * = (By naive method)
     * A*(EI-FH)-B*(DI-FG)+C((DH-EG)
     * 
     * = (By row reduction)
     * | A B      C      |
     * | 0 E-BD/A F-CD/A |
     * | 0 H-BG/A I-CG/A |
     * 
     * =
     * | A B      C                                |
     * | 0 E-BD/A F-CD/A                           |
     * | 0 0      I-CG/A-(F-CD/A)(H-BG/A)/(E-BD/A) |
     * 
     * =
     * A*(E-BD/A)*(I-CG/A-(F-CD/A)(H-BG/A)/(E-BD/A))
     * 
     * =
     * A*((E-BD/A)*(I-CG/A)-(F-CD/A)(H-BG/A))
     * 
     * =
     * A*((E-BD/A)*(I-CG/A)-(F-CD/A)(H-BG/A))
     * 
     * =
     * A*(EI-IBD/A-ECG/A+CGBD/A^2-FH+FBG/A+HCD/A-CDBG/A^2)
     * 
     * =
     * A*(EI-IBD/A-ECG/A-FH+FBG/A+HCD/A)
     * 
     * =
     * A*(EI-FH)-IBD-ECG+FBG+HCD
     * 
     * =
     * A*(EI-FH)-B*(ID-FG)+C*(HD-EG)
     * 
     * Define m(i,j,n) = m(i,j,n-1)-m(i,n-1,n-1)*m(n-1,j,n-1)/m(n-1,n-1,n-1) for n>0, i>=n, and j>=n.
     * Define each m(i,j,0) is a constant defined as the element of a matrix M at row i, and column j.
     * 
     * m(i,j,n) = m(i,j,n-1)-m(i,n-1,n-1)*m(n-1,j,n-1)/m(n-1,n-1,n-1)
     * m(i,j,n) = m(i,j,n-2)-m(i,n-2,n-2)*m(n-2,j,n-2)/m(n-2,n-2,n-2)-(m(i,n-1,n-2)-m(i,n-2,n-2)*m(n-2,n-1,n-2)/m(n-2,n-2,n-2))*(m(n-1,j,n-2)-m(n-1,n-2,n-2)*m(n-2,j,n-2)/m(n-2,n-2,n-2))/m(n-1,n-1,n-1)
     * m(n,n,n) = m(n,n,n-1)
     *           -m(n,n-1,n-1)*m(n-1,n,n-1)/m(n-1,n-1,n-1)
     * m(n-1,n-1,n-1)*m(n,n,n) = m(n-1,n-1,n-1)*m(n,n,n-1)
     *                          -m(n,n-1,n-1)*m(n-1,n,n-1)
     * m(n-1,n-1,n-1)*m(n,n,n) = (m(n-1,n-1,n-2)-m(n-1,n-2,n-2)*m(n-2,n-1,n-2)/m(n-2,n-2,n-2))*(m(n,n,n-2)-m(n,n-2,n-2)*m(n-2,n,n-2)/m(n-2,n-2,n-2))
     *                          -(m(n,n-1,n-2)-m(n,n-2,n-2)*m(n-2,n-1,n-2)/m(n-2,n-2,n-2))*(m(n-1,n,n-2)-m(n-1,n-2,n-2)*m(n-2,n,n-2)/m(n-2,n-2,n-2))
     * m(n-1,n-1,n-1)*m(n,n,n) = m(n-1,n-1,n-2)*m(n,n,n-2) - m(n,n,n-2)*m(n-1,n-2,n-2)*m(n-2,n-1,n-2)/m(n-2,n-2,n-2) - m(n-1,n-1,n-2)*m(n,n-2,n-2)*m(n-2,n,n-2)/m(n-2,n-2,n-2) + m(n-1,n-2,n-2)*m(n-2,n-1,n-2)*m(n,n-2,n-2)*m(n-2,n,n-2)/m^2(n-2,n-2,n-2)
     *                          -m(n,n-1,n-2)*m(n-1,n,n-2) + m(n-1,n,n-2)*m(n,n-2,n-2)*m(n-2,n-1,n-2)/m(n-2,n-2,n-2) + m(n,n-1,n-2)*m(n-1,n-2,n-2)*m(n-2,n,n-2)/m(n-2,n-2,n-2) - m(n,n-2,n-2)*m(n-2,n-1,n-2)*m(n-1,n-2,n-2)*m(n-2,n,n-2)/m^2(n-2,n-2,n-2)
     * m(n-1,n-1,n-1)*m(n,n,n) = m(n-1,n-1,n-2)*m(n,n,n-2) - m(n,n,n-2)*m(n-1,n-2,n-2)*m(n-2,n-1,n-2)/m(n-2,n-2,n-2) - m(n-1,n-1,n-2)*m(n,n-2,n-2)*m(n-2,n,n-2)/m(n-2,n-2,n-2)
     *                          -m(n,n-1,n-2)*m(n-1,n,n-2) + m(n-1,n,n-2)*m(n,n-2,n-2)*m(n-2,n-1,n-2)/m(n-2,n-2,n-2) + m(n,n-1,n-2)*m(n-1,n-2,n-2)*m(n-2,n,n-2)/m(n-2,n-2,n-2)
     * m(n-1,n-1,n-1)*m(n,n,n) = m(n-1,n-1,n-2)*m(n,n,n-2)
     *                          -m(n,n-1,n-2)*m(n-1,n,n-2)
     *                          +m(n,n-2,n-2)*m(n-2,n-1,n-2)*m(n-1,n,n-2)/m(n-2,n-2,n-2)
     *                          +m(n,n-1,n-2)*m(n-1,n-2,n-2)*m(n-2,n,n-2)/m(n-2,n-2,n-2)
     *                          -m(n-1,n-1,n-2)*m(n,n-2,n-2)*m(n-2,n,n-2)/m(n-2,n-2,n-2)
     *                          -m(n,n,n-2)*m(n-1,n-2,n-2)*m(n-2,n-1,n-2)/m(n-2,n-2,n-2)
     * 
     * |M| = m(0,0,0)*m(1,1,1)*...*m(l-1,l-1,l-1) where l is the number of rows or columns of the square matrix, M.
     * =>
     * |M| = m(0,0,0)*(m(1,1,0)-m(1,0,0)*m(0,1,0)/m(0,0,0))*...*m(l-2,l-2,l-2)*(m(l-1,l-1,l-2)-m(l-1,l-2,l-2)*m(l-2,l-1,l-2)/m(l-2,l-2,l-2))
     * =>
     * |M| = m(0,0,0)*(m(1,1,0)-m(1,0,0)*m(0,1,0)/m(0,0,0))*...*(m(l-2,l-2,l-2)*m(l-1,l-1,l-2)-m(l-1,l-2,l-2)*m(l-2,l-1,l-2))
     * 
     */
    
    
    /**
     * 
     * @param row The relevant starting row for the this determinant calculation.
     * @param startCol The first relevant column listed in intList
     * @param intList A kind of linked list. Each element in the array represents a column in the matrix. The first relevant column is identified by startCol. The value at each relevant column points to the next relevant column. 
     * @return The value of the identified subdeterminant.
     */
    private Polynomial getSubdeterminant(int row, int startCol, int[] intList) {
    	if(row==matrix.length-1)
    		return matrix[row][startCol];
    	Polynomial subSum = new Polynomial(new double[0]);
    	boolean add=true;
    	int lastI=-1;
    	Polynomial term;
    	for(int i=startCol;i<matrix.length;i=intList[i]) {
			if(lastI<0) {
				term=matrix[row][i].multiply(getSubdeterminant(row+1, intList[startCol], intList));
			}
			else{
				int temp = intList[lastI];
				intList[lastI] = intList[i];
				term=matrix[row][i].multiply(getSubdeterminant(row+1, startCol, intList));
				intList[lastI] = temp;
			}
			subSum=add?subSum.add(term):subSum.subtract(term);
    		add=!add;
    		lastI=i;
    	}
    	return subSum;
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
		int i, j;
		for(i = 0;i<matrix.length;i++){
		    for(j = 0;j<matrix[i].length;j++){
			if(j>0)
			    out.print(", ");
			out.print(matrix[i][j]);
		    }
		    out.print("\n");
		}
    }
}
