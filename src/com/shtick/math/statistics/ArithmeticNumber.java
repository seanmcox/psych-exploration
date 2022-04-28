/**
 * 
 */
package com.shtick.math.statistics;

/**
 * @author scox
 * @param <O> The type of the ArothmeticObject
 *
 */
public abstract class ArithmeticNumber<O extends ArithmeticNumber<O>> extends Number implements ArithmeticObject<O>, Comparable<O>{
	/**
	 * 
	 * @return The square root of this number. (Null if the result would be imaginary.)
	 */
	public abstract O sqrt();
	
	/**
	 * 
	 * @param exponent
	 * @return A value equal to this value raised to the given exponent.
	 */
	public abstract O pow(O exponent);
	
	/**
	 * 
	 * @return true if negative, and false otherwise.
	 */
	public abstract boolean isNegative();
	
	/**
	 * 
	 * @return The number of binary digits of precision provided by the data type or -1 if this measure is not helpful. (eg. Functionally infinite, impossibly ambiguous, or whatever.)
	 */
	public abstract long getBinaryPrecision();

	/**
	 * 
	 * @return true if the number encodes a zero and false otherwise.
	 */
	public O getAbs() {
		return isNegative()?getNegative():(O)this;
	}
}
