/*
 * Polynomial.java
 *
 * Created on December 30, 2006, 1:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtick.math.statistics;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author seanmcox
 */
public class DoublePolynomial implements Polynomial<Double>, ArithmeticObject<DoublePolynomial>{
	/**
	 * 
	 */
	public static final DoublePolynomial UNIT = new DoublePolynomial(new double[] {1});
	/**
	 * 
	 */
	public static final DoublePolynomial ZERO = new DoublePolynomial(new double[0]);

	protected double[] coefficients;
    
    /**
     * @param args
     */
    public static void main(String[] args){
		DoublePolynomial p1=new DoublePolynomial(new double[] {1,1});
		DoublePolynomial p2=new DoublePolynomial(new double[] {25,1});
		DoublePolynomial p3=new DoublePolynomial(new double[] {-6,1});
		
		DoublePolynomial p=p1.multiply(p2).multiply(p3);
		
		System.out.print("p1=");
		p1.print();
		System.out.print("p2=");
		p2.print();
		System.out.print("p3=");
		p3.print();
		System.out.print("p=");
		p.print();
		try{
		    List<Double> zeros= p.findZeros();
		    System.out.println("Zeros of p:");
		    boolean comma = false;
		    for(Double zero:zeros){
				if(comma)
				    System.out.print(", ");
				else
				    comma=true;
				System.out.print(zero);
		    }
		    System.out.println();
		}
		catch(Throwable t){
		    t.printStackTrace();
		}
    }

    /**
     * Creates a Polynomial by copying the specified coefficients.
     *
     * @param coefficients a list of integer coefficients starting with the constant component at index start.
     * @param start the beginning index of the polynomial coefficients.
     * @param size the number of coefficients to copy.
     */
    public DoublePolynomial(double[] coefficients, int start, int size) {
		this.coefficients = new double[size];
		System.arraycopy(coefficients,start,this.coefficients,0,size);
    }
    
    /**
     * Creates a Polynomial using the given coefficients. (Doesn't make a copy.)
     *
     * @param coefficients a list of integer coefficients starting with the constant component.
     */
    public DoublePolynomial(double[] coefficients) {
		this.coefficients = coefficients;
    }
    
    @Override
	public DoublePolynomial getZero() {
		return ZERO;
	}

	@Override
	public DoublePolynomial getIdentity() {
		return UNIT;
	}

	/**
     * 
     * @param i
     * @return returns c_i from the the c_i*x^i element of the polynomial.
     */
    public double getCoefficient(int i) {
    	if((i<0)||(i>=coefficients.length))
    		return 0;
    	return coefficients[i];
    }
    
    /**
     * @param p
     * @return The multiplied polynomial.
     */
    public DoublePolynomial multiply(DoublePolynomial p){
    	return multiply(p.coefficients);
    }
    
    /**
     * @param p
     * @return The multiplied polynomial.
     */
    public DoublePolynomial multiply(double[] p){
    	if((p.length==0)||(coefficients.length==0))
    		return new DoublePolynomial(new double[0]);
		double[] c = new double[p.length + coefficients.length - 1];
		int i,j;
		for(i = 0;i<coefficients.length;i++)
		    for(j = 0;j<p.length;j++) {
		    	c[i+j]+=coefficients[i]*p[j];
			    if(Double.isNaN(c[i+j])) {
			    	System.err.println("Multiplier 1: "+Arrays.toString(p));
			    	System.err.println("Multiplier 2: "+Arrays.toString(this.coefficients));
			    	System.err.println("Index 1: "+j);
			    	System.err.println("Index 2: "+i);
			    	System.err.flush();
			    	throw new RuntimeException("NaN Found (multiply)!");
			    }
			    if(Double.isInfinite(c[i+j])) {
			    	System.err.println("Multiplier 1: "+Arrays.toString(p));
			    	System.err.println("Multiplier 2: "+Arrays.toString(this.coefficients));
			    	System.err.println("Index 1: "+j);
			    	System.err.println("Index 2: "+i);
			    	System.err.flush();
			    	throw new RuntimeException("Infinite Found (multiply)!");
			    }
		    }
		return new DoublePolynomial(c);
    }

    /**
     * @param p
     * @return The divided polynomial. All remainders are lost.
     */
    public DoublePolynomial divide(DoublePolynomial p){
    	return divide(p.coefficients);
    }
    
    /**
     * @param p
     * @return The divided polynomial. All remainders are lost.
     */
    public DoublePolynomial divide(double[] p){
		if(p.length>coefficients.length)
		    return new DoublePolynomial(new double[0]);
		double[] temp = new double[coefficients.length];
		System.arraycopy(coefficients,0,temp,0,coefficients.length);
		int i,j;
		double[] c = new double[coefficients.length - p.length + 1];
		for(i = temp.length - p.length;i>=0;i--){
		    c[i]=temp[i+p.length-1]/p[p.length-1];
		    temp[i+p.length-1]=0;
		    for(j = 0;j<(p.length-1);j++)
		    	temp[i+j]-=p[j]*c[i];
		}
		
		return new DoublePolynomial(c);
    }

    /**
     * @param p
     * @return The resulting polynomial.
     */
    public DoublePolynomial add(DoublePolynomial p){
		return add(p.coefficients);
    }
    
    /**
     * @param p
     * @return The resulting polynomial.
     */
    public DoublePolynomial add(double[] p){
		double[] c = new double[Math.max(p.length,coefficients.length)];
		int i;
		for(i = 0;i<coefficients.length;i++)
		    c[i]=coefficients[i];
		for(i = 0;i<p.length;i++) {
		    c[i]+=p[i];
		    if(Double.isNaN(c[i])) {
		    	System.err.println("Adder 1: "+Arrays.toString(p));
		    	System.err.println("Adder 2: "+Arrays.toString(this.coefficients));
		    	System.err.println("Problem index: "+i);
		    	System.err.flush();
		    	throw new RuntimeException("NaN Found (add)!");
		    }
		    if(Double.isInfinite(c[i])) {
		    	System.err.println("Adder 1: "+Arrays.toString(p));
		    	System.err.println("Adder 2: "+Arrays.toString(this.coefficients));
		    	System.err.println("Problem index: "+i);
		    	System.err.flush();
		    	throw new RuntimeException("Infinite Found (add)!");
		    }
		}
		return new DoublePolynomial(c);
    }
    
    /**
     * @param p
     * @return The resulting polynomial.
     */
    public DoublePolynomial subtract(DoublePolynomial p){
    	return subtract(p.coefficients);
    }
    
    /**
     * @param p
     * @return The resulting polynomial.
     */
    public DoublePolynomial subtract(double[] p){
		double[] c = new double[Math.max(p.length,coefficients.length)];
		int i;
		for(i = 0;i<coefficients.length;i++)
		    c[i]=coefficients[i];
		for(i = 0;i<p.length;i++) {
		    c[i]-=p[i];
		    if(Double.isNaN(c[i])) {
		    	System.err.println("Subtracter 1: "+Arrays.toString(p));
		    	System.err.println("Subtracter 2: "+Arrays.toString(this.coefficients));
		    	System.err.println("Problem index: "+i);
		    	System.err.flush();
		    	throw new RuntimeException("NaN Found (subtract)!");
		    }
		    if(Double.isInfinite(c[i])) {
		    	System.err.println("Subtracter 1: "+Arrays.toString(p));
		    	System.err.println("Subtracter 2: "+Arrays.toString(this.coefficients));
		    	System.err.println("Problem index: "+i);
		    	System.err.flush();
		    	throw new RuntimeException("Infinite Found (subtract)!");
		    }
		}
		return new DoublePolynomial(c);
    }
    
    /**
     * @return The resulting polynomial.
     */
    public DoublePolynomial derivative(){
    	if(coefficients.length==0)
    		return this;
		double[] c = new double[coefficients.length-1];
		int i;
		for(i = 1;i<=c.length;i++)
		    c[i-1]=coefficients[i]*i;
		return new DoublePolynomial(c);
    }
    
    public List<Double> findZeros() throws Throwable{
		compact();
		if(coefficients.length==0)
			return null;
		if(coefficients.length==1)
		    return new LinkedList<Double>();
		if(coefficients.length==2){ // Simple linear terminal case
		    LinkedList<Double> retVal = new LinkedList<Double>();
		    retVal.add(-coefficients[0]/coefficients[1]);
		}
		if(coefficients[0]==0){ // Simplifying case: 0-constant
		    DoublePolynomial p= new DoublePolynomial(coefficients,1,coefficients.length-1);
		    List<Double> retval = p.findZeros();
		    retval.add(0.0);
		    return retval;
		}
		{ // Simplifying case: Powers of x are all multiples of some base number. Solve by substitution
			int gcd=-1;
			for(int i=1;i<coefficients.length;i++) {
				if(coefficients[i]!=0) {
					if(gcd<0)
						gcd=i;
					else
						gcd=gcd(gcd,i);
				}
				if(gcd==1)
					break;
			}
			if(gcd>1) {
				// Substitute z=x^gcd
				double[] newCoefficients = new double[(coefficients.length-1)/gcd+1];
				for(int i=0;i<newCoefficients.length;i++) 
					newCoefficients[i]=coefficients[i*gcd];
				DoublePolynomial substitute = new DoublePolynomial(newCoefficients);
				List<Double> subzeros = substitute.findZeros();
				List<Double> retval = new LinkedList<Double>();
				for(Double subzero:subzeros) {
					if(gcd%2==0) {
						if(subzero<0)
							continue; // Ignore imaginary roots.
						double unsubzero = Math.pow(subzero, 1/(double)gcd);
						retval.add(unsubzero);
						retval.add(-unsubzero);
					}
					else {
						double unsubzero;
						if(subzero<0)
							unsubzero = -Math.pow(-subzero, 1/(double)gcd);
						else
							unsubzero = Math.pow(subzero, 1/(double)gcd);
						retval.add(unsubzero);
					}
				}
				return retval;
			}
		}
		if(coefficients.length==3){ // Somewhat simple terminal case.
		    double a = coefficients[2];
		    double b = coefficients[1];
		    double c = coefficients[0];
		    
		    System.out.flush();
		    c = b*b - 4*a*c;
		    if(c<0){
				if(c>-.0001)
				    c=0;
				else
					return new LinkedList<Double>(); // Ignore imaginary roots.
		    }
		    
		    double r1 = -b + Math.sqrt(c);
		    double r2 = -b - Math.sqrt(c);
		    r1/=(2*a);
		    r2/=(2*a);
		    LinkedList<Double> retval = new LinkedList<Double>();
		    retval.add(r1);
		    retval.add(r2);
		    return retval;
		}
		// Now it is time for Newton's method.
		double root = newtonsMethod();
		// divide out the root and continue recursively.
		DoublePolynomial p = divide(new DoublePolynomial(new double[]{root,-1.0}));
		List<Double> retVal = p.findZeros();
		retVal.add(root);
		return retVal;
    }
    
    /**
     * 
     * @param a
     * @param b
     * @return The greatest common denominator of a and b
     */
    private static int gcd(int a, int b) {
    	if(a==b)
    		return a;
    	if(a>b) {
    		int temp = a;
    		a=b;
    		b=temp;
    	}
        if (a == 0) 
            return 0; 
        return gcd_step(b%a, a); 
    } 

    private static int gcd_step(int a, int b) {
    	if(a==b)
    		return a;
    	if(a>b) {
    		int temp = a;
    		a=b;
    		b=temp;
    	}
        if (a == 0) 
            return b; 
        return gcd(b%a, a); 
    } 

    protected double newtonsMethod() throws Throwable{
    	return newtonsMethod(0);
    }
    
    protected double newtonsMethod(double startPos) throws Throwable{
		DoublePolynomial dp = derivative();
		double lastPos=startPos;
		double currentPos=startPos;
		double last=getValue(currentPos);
		double current=last;
		double lastD=dp.getValue(currentPos);
		double currentD=lastD;
		double shift;
		while(Math.abs(current)>0.0000001){
		    if(currentD!=0){
				lastD=currentD;
				lastPos=currentPos;
				last=current;
				shift=-current/currentD;
		    }
		    else{
		    	shift=-current/lastD;
		    }
		    if(Math.abs(current)<.001)
		    	shift=Math.signum(shift)*.001;
		    if(shift==0)
		    	shift=currentPos-lastPos;
		    currentPos+=shift;
		    current=getValue(currentPos);
		    currentD=dp.getValue(currentPos);
		    if((lastD*currentD<0)&&(!(last*current<0))){
		    	// We have stumbled upon either two zeros, or an indication of an imaginary root.
		    	/*
		    	 * Shape is as follows:
		    	 * 
		    	 * \   /
		    	 *  \_/
		    	 *  
		    	 * or:
		    	 *   _
		    	 *  / \
		    	 * /   \
		    	 * 
		    	 * Find the max/min point and determine if zero can exist.
		    	 * 
		    	 */
				double minMaxPos=dp.newtonsMethod((lastPos+currentPos)/2);
				double minMax=getValue(minMaxPos);
				if(minMax*current<0){
				    lastPos=currentPos;
				    currentPos=minMaxPos;
				    last=current;
				    current=minMax;
				    lastD=currentD;
				    currentD=dp.getValue(currentPos);
				}
				else if(minMax*current>0){
				    System.out.println("p(x): "+toString());
				    System.out.println("dp/dx: "+dp.toString());
				    System.out.println("xn = "+minMaxPos);
				    System.out.println("p(xn) = "+minMax);
				    throw new Throwable("Imaginary root found at minimum.");
				}
			    return minMaxPos;
		    }
		    if(last*current<0){// We have stumbled upon a zero.
				double highPos;
				double lowPos;
				if(current>last){
				    highPos=currentPos;
				    lowPos=lastPos;
				}
				else{
				    highPos=lastPos;
				    lowPos=currentPos;
				}
				double tempPos = (highPos+lowPos)/2;
				double tempVal;
				while((tempPos<Math.max(highPos,lowPos))&&(tempPos>Math.min(highPos,lowPos))){
				    tempVal = getValue(tempPos);
				    if(tempVal>0.0000001)
						highPos=tempPos;
				    else if(tempVal<0.0000001)
						lowPos=tempPos;
				    else
				    	return tempPos;
				    tempPos = (highPos+lowPos)/2;
				}
				return tempPos;
		    }
		}
		return currentPos;
    }
    
    /**
     * @param x
     * @return The value of the polynomial with x set at the given value.
     */
    public double getValue(double x){
		if(coefficients.length==0)
		    return 0;
		double c = coefficients[0];
		double x2 = 1;
		for(int i = 1; i<coefficients.length;i++){
		    x2*=x;
		    c+=x2*coefficients[i];
		}
		return c;
    }
    
    protected void compact(){
		int i = coefficients.length;
		while((i>0)&&(coefficients[i-1]==0))
		    i--;
		if(i!=coefficients.length){
		    double[] c = new double[i];
		    System.arraycopy(coefficients,0,c,0,i);
		    coefficients = c;
		}
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
		out.println(toString());
    }

    @Override
    public String toString(){
		int i;
		String retVal="";
		for(i = 0;i<coefficients.length;i++){
		    if(i>0)
		    	retVal+=" + ";
		    retVal+=coefficients[i];
		    if(i>0)
		    	retVal+="*x";
		    if(i>1)
		    	retVal+="^"+i;
		}
		return retVal;
    }
}
