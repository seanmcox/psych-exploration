/**
 * 
 */
package com.shtick.math.statistics;

/**
 * This datum is designed, for the purposes of simplicity of memory management and speed, to be able to be reused and manipulated without creating new instances for each data point.
 * 
 * These objects may be modified when next() or previous() is called on the Iterator producing them. 
 * 
 * @author scox
 *
 */
public class WeightedDatum {
	/**
	 * 
	 */
	public double weight;
	/**
	 * 
	 */
	public double[] datum;
}
