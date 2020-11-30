/*
 * PrincipleComponentDataSet.java
 *
 * Created on January 3, 2007, 10:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtick.math.statistics;

/**
 * 
 *
 * @author seanmcox
 */
public abstract class StatsDataSet implements Iterable<WeightedDatum>{
    /**
     * Sets the mean value of the dataSet. This will be used to rezero the axis.
     * This should affect the values returned by the iterator. (The return value should be the raw value, r, minus the mean, m. ie. r-m.)
     *
     * @param var the variable to set the mean for.
     * @param mean the position of the mean.
     */
    public abstract void setMean(int var, float mean);
    
    /**
     * Returns the number of entries in the data set.
     *
     * @return the number of data.
     */
    public abstract int size();

    /**
     * Returns the number of variables recorded in the data set. Hence, this
     * is the size of a datum.
     *
     * @return the size of a datum.
     */
    public abstract int getVariableCount();
}
