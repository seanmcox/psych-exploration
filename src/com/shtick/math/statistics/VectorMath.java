/*
 * VectorMath.java
 *
 * Created on January 2, 2007, 3:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtick.math.statistics;

/**
 *
 * @author seanmcox
 */
public class VectorMath {
	/**
	 * @param a
	 * @param b
	 * @return The dot product of the two vectors.
	 * @throws IllegalArgumentException
	 */
	public static double dotProduct(double[] a,double[] b){
		if(a.length!=b.length)
			throw new IllegalArgumentException("Incompatible vectors.");
		double c=0;
		for(int i = 0;i<a.length;i++)
			c+=a[i]*b[i];
		return c;
	}

	/**
	 * Normalizes the given vector.
	 *
	 * @param a the vector to normalize
	 * @throws IllegalArgumentException 
	 */
	public static void normalize(double[] a){
		double c=Math.sqrt(dotProduct(a,a));
		if(c==0)
			throw new IllegalArgumentException("Cannot normalize magnitude zero vector.");
		for(int i = 0;i<a.length;i++)
			a[i]/=c;
	}
}
