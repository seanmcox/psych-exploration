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
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author seanmcox
 * @param <A> The type of ArithmeticObject
 * 
 */
public class ArithmeticPolynomial<A extends ArithmeticNumber<A>> implements ArithmeticObject<ArithmeticPolynomial<A>>{
    protected A[] coefficients;
    protected A sample;
    protected A two;
    protected A four;
    protected A basicallyZeroThreshold;
    protected A newtonsMethodCloseEnough;

    /**
	 * 
	 */
	private ArithmeticPolynomial<A> unit;
	/**
	 * 
	 */
	private ArithmeticPolynomial<A> zero;

    /**
     * Creates a Polynomial by copying the specified coefficients.
     *
     * @param coefficients a list of integer coefficients starting with the constant component at index start.
     * @param start the beginning index of the polynomial coefficients.
     * @param size the number of coefficients to copy.
     * @param sample 
     */
    public ArithmeticPolynomial(A[] coefficients, int start, int size, A sample) {
		this.coefficients = (A[])new Object[size];
		System.arraycopy(coefficients,start,this.coefficients,0,size);
		this.sample = sample;
    }
    
    /**
     * Creates a Polynomial using the given coefficients. (Doesn't make a copy.)
     *
     * @param coefficients a list of integer coefficients starting with the constant component.
     * @param sample 
     */
    public ArithmeticPolynomial(A[] coefficients, A sample) {
		this.coefficients = coefficients;
		this.sample = sample;
    }
    
    @Override
	public ArithmeticPolynomial<A> getZero() {
    	if(zero==null)
    		zero = new ArithmeticPolynomial<A>((A[])new ArithmeticNumber[0], sample);
		return zero;
	}

	@Override
	public ArithmeticPolynomial<A> getIdentity() {
    	if(unit==null)
    		unit = new ArithmeticPolynomial<A>((A[])new ArithmeticNumber[] {sample.getIdentity()}, sample);
		return unit;
	}
	
	private A get2A() {
		try {
			if(two==null)
				two = (A)sample.getClass().getDeclaredConstructor(int.class).newInstance(2);
			return two;
		}
		catch(InvocationTargetException|IllegalAccessException|InstantiationException|NoSuchMethodException t) {
			throw new RuntimeException(t);
		}
	}
	
	private A get4A() {
		try {
			if(four==null)
				four = (A)sample.getClass().getDeclaredConstructor(int.class).newInstance(4);
			return four;
		}
		catch(InvocationTargetException|IllegalAccessException|InstantiationException|NoSuchMethodException t) {
			throw new RuntimeException(t);
		}
	}
	
	private A getBasicallyZeroThresholdA() {
		try {
			if(basicallyZeroThreshold==null)
				basicallyZeroThreshold = (A)sample.getClass().getDeclaredConstructor(double.class).newInstance(-0.0001);
			return basicallyZeroThreshold;
		}
		catch(InvocationTargetException|IllegalAccessException|InstantiationException|NoSuchMethodException t) {
			throw new RuntimeException(t);
		}
	}
	
	private A getNewtonsMethodCloseEnoughA() {
		try {
			if(newtonsMethodCloseEnough==null) {
				long binaryPrecision = sample.getBinaryPrecision();
				if(binaryPrecision<=1) {
					newtonsMethodCloseEnough = (A)sample.getClass().getDeclaredConstructor(double.class).newInstance(0.0000001);
				}
				else {
					if(binaryPrecision>(long)Integer.MAX_VALUE)
						binaryPrecision = Integer.MAX_VALUE; // TODO Figure out how to work with even larger precision
					newtonsMethodCloseEnough = (A)sample.getClass().getDeclaredConstructor(double.class).newInstance(0.5);
					newtonsMethodCloseEnough = newtonsMethodCloseEnough.pow((A)sample.getClass().getDeclaredConstructor(int.class).newInstance((int)binaryPrecision)).multiply(get2A());
				}
			}
			return newtonsMethodCloseEnough;
		}
		catch(InvocationTargetException|IllegalAccessException|InstantiationException|NoSuchMethodException t) {
			throw new RuntimeException(t);
		}
	}

	/**
     * 
     * @param i
     * @return returns c_i from the the c_i*x^i element of the polynomial.
     */
    public A getCoefficient(int i) {
    	if((i<0)||(i>=coefficients.length))
    		return null;
    	return coefficients[i];
    }
    
    /**
     * @param p
     * @return The multiplied polynomial.
     */
    public ArithmeticPolynomial<A> multiply(ArithmeticPolynomial<A> p){
    	return multiply(p.coefficients);
    }
    
    /**
     * @param p
     * @return The multiplied polynomial.
     */
    public ArithmeticPolynomial<A> multiply(A[] p){
    	if((p.length==0)||(coefficients.length==0))
    		return getZero();
		A[] c = (A[])new ArithmeticNumber[p.length + coefficients.length - 1];
		int i,j;
		for(i = 0;i<c.length;i++) {
			c[i] = sample.getZero();
		}
		for(i = 0;i<coefficients.length;i++)
		    for(j = 0;j<p.length;j++)
		    	c[i+j]=c[i+j].add(coefficients[i].multiply((A)p[j]));
		return new ArithmeticPolynomial<A>(c,sample);
    }

    /**
     * @param p
     * @return The divided polynomial. All remainders are lost.
     */
    public ArithmeticPolynomial<A> divide(ArithmeticPolynomial<A> p){
    	return divide(p.coefficients);
    }
    
    /**
     * @param p
     * @return The divided polynomial. All remainders are lost.
     */
    public ArithmeticPolynomial<A> divide(A[] p){
		if(p.length>coefficients.length)
		    return getZero();
		A[] temp = (A[])new ArithmeticNumber[coefficients.length];
		System.arraycopy(coefficients,0,temp,0,coefficients.length);
		int i,j;
		A[] c = (A[])new ArithmeticNumber[coefficients.length - p.length + 1];
		for(i = temp.length - p.length;i>=0;i--){
		    c[i]=temp[i+p.length-1].divide(p[p.length-1]);
		    temp[i+p.length-1]=p[0].getZero();
		    for(j = 0;j<(p.length-1);j++) {
		    	temp[i+j]=temp[i+j].subtract(p[j].multiply(c[i]));
		    }
		}
		
		return new ArithmeticPolynomial<A>(c,sample);
    }

    /**
     * @param p
     * @return The resulting polynomial.
     */
    public ArithmeticPolynomial<A> add(ArithmeticPolynomial<A> p){
		return add(p.coefficients);
    }
    
    /**
     * @param p
     * @return The resulting polynomial.
     */
    public ArithmeticPolynomial<A> add(A[] p){
		A[] c = (A[])new ArithmeticNumber[Math.max(p.length,coefficients.length)];
		int i;
		for(i = 0;i<coefficients.length;i++)
		    c[i]=coefficients[i];
		for(i = 0;i<p.length;i++)
		    c[i]=(c[i]==null)?p[i]:c[i].add(p[i]);
		return new ArithmeticPolynomial<A>(c,sample);
    }
    
    /**
     * @param p
     * @return The resulting polynomial.
     */
    public ArithmeticPolynomial<A> subtract(ArithmeticPolynomial<A> p){
    	return subtract(p.coefficients);
    }
    
    /**
     * @param p
     * @return The resulting polynomial.
     */
    public ArithmeticPolynomial<A> subtract(A[] p){
		A[] c = (A[])new ArithmeticNumber[Math.max(p.length,coefficients.length)];
		int i;
		for(i = 0;i<coefficients.length;i++)
		    c[i]=coefficients[i];
		for(i = 0;i<p.length;i++)
		    c[i]=(c[i]==null)?p[i].getNegative():c[i].subtract(p[i]);
		return new ArithmeticPolynomial<A>(c,sample);
    }
    
    /**
     * @return The resulting polynomial.
     */
    public ArithmeticPolynomial<A> derivative(){
    	if(coefficients.length==0)
    		return this;
		A[] c = (A[])new ArithmeticNumber[coefficients.length-1];
		int i;
		try {
			for(i = 1;i<=c.length;i++)
			    c[i-1]=coefficients[i].multiply((A)coefficients[i].getClass().getDeclaredConstructor(int.class).newInstance(i));
		}
		catch(InvocationTargetException|IllegalAccessException|InstantiationException|NoSuchMethodException t) {
			throw new RuntimeException(t);
		}
		return new ArithmeticPolynomial<A>(c,sample);
    }
    
    /**
     * @return A collection of values that would evaluate to zero in the polynomial. Null if all point would be zero. (Empty polynomial.)
     * @throws Throwable
     */
    public List<A> findZeros() throws Throwable{
		compact();
		if(coefficients.length==0)
			return null;
		if(coefficients.length==1)
		    return new LinkedList<A>();
		if(coefficients.length==2){ // Simple linear terminal case
		    LinkedList<A> retVal = new LinkedList<A>();
		    retVal.add(coefficients[0].getZero().subtract(coefficients[0].divide(coefficients[1])));
		}
		if(coefficients[0].getZero().equals(coefficients[0])){ // Simplifying case: 0-constant
		    ArithmeticPolynomial<A> p= new ArithmeticPolynomial<A>(coefficients,1,coefficients.length-1,sample);
		    List<A> retval = p.findZeros();
		    retval.add(coefficients[0]);
		    return retval;
		}
		{ // Simplifying case: Powers of x are all multiples of some base number. Solve by substitution
			int gcd=-1;
			for(int i=1;i<coefficients.length;i++) {
				if(!coefficients[i].isZero()) {
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
				A[] newCoefficients = (A[])new ArithmeticNumber[(coefficients.length-1)/gcd+1];
				for(int i=0;i<newCoefficients.length;i++) 
					newCoefficients[i]=coefficients[i*gcd];
				ArithmeticPolynomial<A> substitute = new ArithmeticPolynomial<A>(newCoefficients,sample);
				List<A> subzeros = substitute.findZeros();
				List<A> retval = new LinkedList<A>();
				A one_over_gcd = sample.getIdentity().divide((A)sample.getClass().getDeclaredConstructor(int.class).newInstance(gcd));
				for(A subzero:subzeros) {
					if(gcd%2==0) {
						if(subzero.isNegative())
							continue; // Ignore imaginary roots.
						A unsubzero = subzero.pow(one_over_gcd);
						retval.add(unsubzero);
						retval.add(unsubzero.getNegative());
					}
					else {
						A unsubzero;
						if(subzero.isNegative())
							unsubzero = subzero.getNegative().pow(one_over_gcd).getNegative();
						else
							unsubzero = subzero.pow(one_over_gcd);
						retval.add(unsubzero);
					}
				}
				return retval;
			}
		}
		if(coefficients.length==3){ // Somewhat simple terminal case.
		    A a = coefficients[2];
		    A b = coefficients[1];
		    A c = coefficients[0];
		    
		    c = b.multiply(b).subtract(a.multiply(c).multiply(get4A()));
		    if(c.compareTo(sample.getZero())<0){
				if(c.compareTo(getBasicallyZeroThresholdA())>0)
				    c=sample.getZero();
				else
					return new LinkedList<A>(); // Ignore imaginary roots.
		    }
		    
		    A sqrt = c.sqrt();
		    A aa = a.add(a);
		    A r1 = sqrt.subtract(b).divide(aa);
		    A r2 = sqrt.add(b).getNegative().divide(aa);
		    LinkedList<A> retval = new LinkedList<A>();
		    retval.add(r1);
		    retval.add(r2);
		    return retval;
		}
		// Now it is time for Newton's method.
		A root = newtonsMethod();
		// divide out the root and continue recursively.
		ArithmeticPolynomial<A> p = divide(new ArithmeticPolynomial<A>((A[])new ArithmeticNumber[]{root,sample.getIdentity().getNegative()},sample));
		List<A> retVal = p.findZeros();
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

    protected A newtonsMethod() throws Throwable{
    	return newtonsMethod(sample.getZero());
    }
    
    protected A newtonsMethod(A startPos) throws Throwable{
		ArithmeticPolynomial<A> dp = derivative();
		A lastPos=startPos;
		A currentPos=startPos;
		A last=getValue(currentPos);
		A current=last;
	    if(current.isZero())
	    	return currentPos;
		A lastD=dp.getValue(currentPos);
		A currentD=lastD;
		A shift;
		while(current.getAbs().compareTo(getNewtonsMethodCloseEnoughA())>0){
		    if(!currentD.isZero()){
				lastD=currentD;
				lastPos=currentPos;
				last=current;
				shift=current.divide(currentD).getNegative();
		    }
		    else{
		    	shift=current.divide(lastD).getNegative();
		    }
		    if(shift.isZero())
		    	shift=currentPos.subtract(lastPos);
		    currentPos=currentPos.add(shift);
		    current=getValue(currentPos);
		    if(current.isZero())
		    	return currentPos;
		    currentD=dp.getValue(currentPos);
		    if((!lastD.isZero())&&(!currentD.isZero())&&(lastD.isNegative()!=currentD.isNegative())&&(last.isNegative()==current.isNegative())){
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
				A minMaxPos=dp.newtonsMethod(lastPos.add(currentPos).divide(get2A()));
				A minMax=getValue(minMaxPos);
				A product = minMax.multiply(current);
				if(!(product.isNegative()||product.isZero())){
				    System.out.println("p(x): "+toString());
				    System.out.println("dp/dx: "+dp.toString());
				    System.out.println("xn = "+minMaxPos);
				    System.out.println("p(xn) = "+minMax);
				    throw new Throwable("Imaginary root found at minimum.");
				}
		    	return minMaxPos;
		    }
		    if(last.isNegative()!=current.isNegative()){// We have stumbled upon a zero.
				A highPos;
				A lowPos;
				if(last.isNegative()){
				    highPos=currentPos;
				    lowPos=lastPos;
				}
				else{
				    highPos=lastPos;
				    lowPos=currentPos;
				}
				A tempPos = highPos.add(lowPos).divide(get2A());
				A tempVal;
				A closeEnough = getNewtonsMethodCloseEnoughA();
				A closeEnoughN = closeEnough.getNegative();
				while((tempPos.compareTo(max(highPos,lowPos))<0)&&(tempPos.compareTo(min(highPos,lowPos))>0)){
				    tempVal = getValue(tempPos);
				    if(tempVal.compareTo(closeEnough)>0)
						highPos=tempPos;
				    else if(tempVal.compareTo(closeEnoughN)<0)
						lowPos=tempPos;
				    else
				    	return tempPos;
				    tempPos = highPos.add(lowPos).divide(get2A());
				    if(highPos.subtract(lowPos).getAbs().compareTo(closeEnough)<=0)
				    	return tempPos;
				}
				return tempPos;
		    }
		}
		return currentPos;
    }    
    
    private A max(A a, A b) {
    	return (a.compareTo(b)>0)?a:b;
    }
    
    private A min(A a, A b) {
    	return (a.compareTo(b)>0)?b:a;
    }
    
    /**
     * @param x
     * @return The value of the polynomial with x set at the given value.
     */
    public A getValue(A x){
		if(coefficients.length==0)
		    return x.getZero();
		A c = coefficients[0];
		A x2 = coefficients[0].getIdentity();
		for(int i = 1; i<coefficients.length;i++){
		    x2=x2.multiply(x);
		    c=c.add(x2.multiply(coefficients[i]));
		}
		return c;
    }
    
    protected void compact(){
		int i = coefficients.length;
		while((i>0)&&(coefficients[i-1].equals(coefficients[i-1].getZero())))
		    i--;
		if(i!=coefficients.length){
		    A[] c = (A[])new Object[i];
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
		    retVal+=coefficients[i].toString();
		    if(i>0)
		    	retVal+="*x";
		    if(i>1)
		    	retVal+="^"+i;
		}
		return retVal;
    }
}
