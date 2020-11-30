/*
 * Eigensystem.java
 *
 * Created on January 2, 2007, 3:08 PM
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
public class Eigensystem {
    /**
     * The eigenvalues of the system.
     */
    public double[] eigenvalues;
    
    /**
     * The eigenvectors of the system.
     */
    public double[][] eigenvectors;
    
    /**
     * Creates a new instance of Eigensystem 
     * @param eigenvalues 
     * @param eigenvectors
     */
    public Eigensystem(double[] eigenvalues,double[][] eigenvectors) {
		this.eigenvalues=eigenvalues;
		this.eigenvectors=eigenvectors;
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
        int i,j;
        for(i=0;i<eigenvalues.length;i++){
            out.print(eigenvalues[i]);
            out.print(":");
            for(j=0;j<eigenvectors[i].length;j++){
                if(j!=0)
                    out.print(",");
                out.print(" "+eigenvectors[i][j]);
            }
            out.print("\n");
        }
    }
}
