/*
 * PrincipleComponentAnalysis.java
 *
 * Created on December 30, 2006, 11:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtick.math.statistics.pca;

import com.shtick.math.statistics.Eigensystem;
import com.shtick.math.statistics.DoubleMatrix;
import com.shtick.math.statistics.StatsDataSet;
import com.shtick.math.statistics.StatusTracker;
import com.shtick.math.statistics.SubtaskStatusTracker;
import com.shtick.math.statistics.WeightedDatum;

/**
 * This class performs a PCA on a 3-dimensional data set.
 * @author seanmcox
 */
public class PrincipleComponentAnalysis {
    /**
     * @param data a dataset of three variables where the first index is the
     *             entry in the dataset and the second index is the index of
     *             the variable.
     * @param statusTracker A StatusTracker for reporting progress. Can be null.
     * @return A PrincipleComponentSystem based on the provided data.
     * @throws Throwable 
     */
    public static PrincipleComponentSystem getPrincipleComponents(StatsDataSet data, StatusTracker statusTracker) throws Throwable{
		double[] means=new double[data.getVariableCount()];
		int i,j;
		// Calculate means for each variable.
		if(statusTracker!=null)
			statusTracker.updateStatus("Calculating means", 0, 0);
		double weightTotal=0;
		for(WeightedDatum datum:data) {
			weightTotal+=datum.weight;
		    for(j=0;j<means.length;j++)
				means[j]+=datum.datum[j]*datum.weight;
		}
		for(i=0;i<means.length;i++){
		    means[i]/=weightTotal;
		    data.setMean(i,(float)(means[i]));
		}
	
		
		// Calculate covariance matrix
		if(statusTracker!=null)
			statusTracker.updateStatus("Calculating covariance matrix", 0, 0.3);
		double[][] cvm=new double[means.length][means.length];
		for(WeightedDatum datum:data) {
		    for(i=0;i<cvm.length;i++){
				for(j=0;j<=i;j++){
				    cvm[i][j]+=datum.datum[i]*datum.datum[j]*datum.weight;
				    if(i!=j)
				    	cvm[j][i]=cvm[i][j];
				}
		    }
		}
		
		for(i=0;i<cvm.length;i++)
		    for(j=0;j<cvm.length;j++)
		    	cvm[i][j]/=weightTotal;
	
		DoubleMatrix m = new DoubleMatrix(cvm);
	
		Eigensystem system;
		if(statusTracker!=null)
			system=m.getEigensystem(new SubtaskStatusTracker(statusTracker, "Finding eigenvalues and eigenvectors", 0.6));
		else
			system=m.getEigensystem();
		if(statusTracker!=null)
			statusTracker.updateStatus("Done", 0, 1);
		return new PrincipleComponentSystem(system.eigenvalues,system.eigenvectors,means);
    }
}
