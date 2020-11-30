/*
 * PrincipleComponentSystem.java
 *
 * Created on January 3, 2007, 3:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtick.math.statistics.pca;

import java.io.PrintStream;

import com.shtick.math.statistics.Eigensystem;
import com.shtick.math.statistics.VectorMath;

/**
 * A normalized Eigensystem with a mean value defined. The eigenvectors with
 * their corresponding eigenvalues are sorted by eigenvalue magnitude from
 * largest to smallest.
 *
 * @author seanmcox
 */
public class PrincipleComponentSystem extends Eigensystem{
    /**
     * 
     */
    public double mean[];
    
    /**
     * Creates a new instance of PrincipleComponentSystem
     *  
     * @param eigenvalues 
     * @param eigenvectors 
     * @param mean 
     * @throws Throwable
     */
    public PrincipleComponentSystem(double[] eigenvalues,double[][] eigenvectors,double[] mean) throws Throwable {
		super(eigenvalues,eigenvectors);
		prepare();
		this.mean=mean;
    }
    
	/**
	 * Sorts and normalizes eigenvectors.
	 */
	private void prepare() throws Throwable{
		int i,j;
	    double tmp;
	    double[] temp;
		
		for(i=0;i<eigenvectors.length;i++)
			VectorMath.normalize(eigenvectors[i]);
		
		for(i=0;i<eigenvalues.length;i++){
			for(j=i+1;j<eigenvalues.length;j++){
				if(Math.abs(eigenvalues[j-1])>Math.abs(eigenvalues[j])){
					tmp=eigenvalues[j];
					temp=eigenvectors[j];
					eigenvalues[j-1]=eigenvalues[j];
					eigenvectors[j-1]=eigenvectors[j];
					eigenvalues[j]=tmp;
					eigenvectors[j]=temp;
				}
			}//for
		}//for
	}//prepare()

    @Override
    public void print(){
    	print(System.out);
    }

    @Override
    public void print(PrintStream out){
        out.println("Mean Values:");
        for(int i=0;i<mean.length;i++)
            System.out.println(mean[i]);
        out.println("Eigensystem:");
        super.print(out);
    }
}
