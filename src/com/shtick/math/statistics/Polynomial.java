/**
 * 
 */
package com.shtick.math.statistics;

import java.util.List;

/**
 * @author scox
 * @param <A> A Number type.
 *
 */
public interface Polynomial<A extends Number> {
	/**
     * @return A collection of values that would evaluate to zero in the polynomial. Null if all points would be zero. (Empty polynomial.)
     * @throws Throwable
	 */
	List<A> findZeros() throws Throwable;
}
