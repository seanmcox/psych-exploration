/**
 * 
 */
package com.shtick.math.statistics;

/**
 * @author scox
 * @param <O> The type of the ArithmeticObject
 *
 */
public interface ArithmeticObject<O extends ArithmeticObject<O>> {
	/**
	 * @param o The object to add.
	 * @return The result of addition.
	 */
	O add(O o);

	/**
	 * @param o The object to subtract.
	 * @return The result of subtraction.
	 */
	O subtract(O o);

	/**
	 * @param o The object to multiply by.
	 * @return The result of multiplication.
	 */
	O multiply(O o);

	/**
	 * @param o The object to divide by.
	 * @return The result of division.
	 */
	O divide(O o);
	
	/**
	 * 
	 * @return A zero represented as this ArithmeticObject
	 */
	O getZero();
	
	/**
	 * 
	 * @return An identity value represented as this ArithmeticObject.
	 */
	O getIdentity();

	/**
	 * 
	 * @return true if the number encodes a zero and false otherwise.
	 */
	default boolean isZero() {
		return this.equals(getZero());
	}

	/**
	 * 
	 * @return true if the number encodes a zero and false otherwise.
	 */
	default O getNegative() {
		return getZero().subtract((O)this);
	}
}
