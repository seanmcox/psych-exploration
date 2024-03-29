/**
 * 
 */
package com.shtick.math.statistics;

import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * A numeric class designed around scientific notation and the need to keep track of significant digits.
 * 
 * TODO When does it make sense to keep track of nominally insignificant digits to promote greater accuracy in an overall operation?
 * For addition, it might seem like it makes sense to keep all of the most to least significant digits, except what about cases like this:
 * 
 * 1.5*10^10 + 2.3*10^-10 (seems crazy to track all of those digits)
 * But what about 1.5*10^10 + 2.3*10^-10 + 7.7*10^-10 + 9.0*10^-9 + 9.0*10^-8 + 9.0*10^-7 + ...
 * Than, it matters. It seems like, even if we do retain SOME insignificant figures to enhance some accuracy, we won't be retaining this many,
 * and the algorithm utilizing this data structure will have to use good sense in deciding what order to perform operations in. 
 * 
 * @author scox
 *
 */
public class SignificantDecimal extends ArithmeticNumber<SignificantDecimal> {
	private static final SignificantDecimal ZERO = new SignificantDecimal(0,true);
	private static final SignificantDecimal ONE = new SignificantDecimal(1,true);
	private static final SignificantDecimal TWO = new SignificantDecimal(2,true);
//	private static final ScientificDecimal POW_ERROR_THRESHOLD = new ScientificDecimal(10);

	/**
	 * Decimal digits, ordered from most significant to least significant.
	 */
	private int[] significantDigits;
	/**
	 * The order of magnitude of the leading digit.<br/>
	 * 0: 1s place<br/>
	 * 1: 10s place<br/>
	 * 2: 100s place<br/>
	 * -1: 10ths place<br/>
	 * <br/>
	 * This would also be the result of log_10(n) where n is a number comprised of 1 in the leading digit and 0 in all other digits. 
	 */
	private long orderOfMagnitude;
	private boolean negative;
	/**
	 * If true, then the number is treated as an exact number with no margin of error.
	 * (Not suitable for data, but useful for numbers used for purely mechanical purposes like doubling, squaring, etc.)
	 */
	private boolean exact;

	/**
	 * 
	 * @param d A simple double
	 * @param significantDigits The number of significant digits in the provided number
	 */
	public SignificantDecimal(double d, int significantDigits) {
		if(!Double.isFinite(d))
			throw new IllegalArgumentException("Non-finite double cannot be used to instantiate a ScientificDecimal.");
		this.exact=false;
		this.negative = d<0;
		long rawLongBits = Double.doubleToRawLongBits(d);
		int binaryExponent = (int)((rawLongBits&0x7FF0000000000000L)>>>52);
		boolean isSubnormal = binaryExponent==0;
		binaryExponent-=1023; // Factor in the bias.
		int decimalExponent = 0;
		long mantissa = rawLongBits&0x0FFFFFFFFFFFFFL;
		long mantissaLow = 0;
		if(isSubnormal) {
			if(mantissa==0) {
				this.significantDigits = new int[0];
				this.orderOfMagnitude = 0;
				return;
			}
			binaryExponent++;
		}
		else {
			mantissa |= 0x10000000000000L;
		}
		binaryExponent-=52;
		//Convert binary exponent to decimal.
		while(binaryExponent>0) {
			if((mantissa&0xC000000000000000L)==0){
				mantissa<<=1;
				mantissaLow<<=1;
				if((mantissaLow&0x100000000L)!=0) {
					mantissaLow&=0xFFFFFFFFL;
					mantissa++;
				}
				binaryExponent--;
			}
			else {
				mantissaLow+=(mantissa%5)<<32;
				mantissa/=5;
				mantissaLow/=5;
				binaryExponent--;
				decimalExponent++;
			}
		}
		while(binaryExponent<0) {
			if((mantissa&0xE000000000000000L)!=0){
				if((mantissa&0x1)>0)
					mantissaLow|=0x100000000L;
				mantissa>>>=1;
				mantissaLow>>=1;
				binaryExponent++;
			}
			else {
				mantissa*=5;
				mantissaLow*=5;
				mantissa+=mantissaLow>>32;
				binaryExponent++;
				decimalExponent--;
			}
		}
		this.significantDigits = new int[significantDigits];
		int[] digits = getDigits(mantissa,0);
		for(int i=0;i<Math.min(significantDigits, digits.length);i++)
			this.significantDigits[i] = digits[i];
		this.orderOfMagnitude = decimalExponent+digits.length-1;
		if((significantDigits<digits.length)&&(digits[significantDigits]>=5)) {
			this.significantDigits[significantDigits-1]++;
			for(int i = significantDigits-1;i>0;i--) {
				if(this.significantDigits[i]<=9)
					break;
				this.significantDigits[i]=0;
				this.significantDigits[i-1]++;
			}
			if(this.significantDigits[0]>9) {
				this.significantDigits[0]=1;
				this.orderOfMagnitude++;
			}
		}
	}

	/**
	 * 
	 * @param value A simple signed long integer.
	 * @param significantDigits The number of significant digits in the provided number.
	 *                          If more than the number of decimal digits in the value, then the number will be zero extended.
	 *                          If less than the number of decimal digits in the value, then the number will be rounded.
	 *                          If value is 0, then significantDigits will be used to determine an orderOfMagnitude for the zero.
	 *                          (this.orderOfMagnitude=-significantDigits)
	 */
	public SignificantDecimal(long value, int significantDigits) {
		boolean wasMinValue=value==Long.MIN_VALUE;
		if(value<0) {
			this.negative = true;
			if(wasMinValue)
				value = Long.MAX_VALUE;
			else
				value = -value;
		}
		else {
			this.negative = false;
			if(value==0) {
				this.significantDigits = new int[0];
				this.orderOfMagnitude=-significantDigits;
				return;
			}
		}
		this.significantDigits = new int[significantDigits];
		int[] digits = getDigits(value,0);
		for(int i=0;i<Math.min(significantDigits, digits.length);i++)
			this.significantDigits[i] = digits[i];
		if(significantDigits<digits.length) {
			if(digits[significantDigits]>=5)
				this.significantDigits[significantDigits-1]++;
		}
		else if(wasMinValue) {
			this.significantDigits[digits.length-1]++;
		}
		this.orderOfMagnitude = digits.length-1;
	}

	/**
	 * 
	 * @param value A simple signed long integer.
	 * @param exact If true, then the number is treated as an exact number with no margin of error.
	 *              If false, then the significant digits is the number of digits in the number.
	 *              (exact is not suitable for data, but useful for numbers used for purely mechanical purposes like doubling, squaring, etc.)
	 */
	public SignificantDecimal(long value, boolean exact) {
		boolean wasMinValue=value==Long.MIN_VALUE;
		this.exact = exact;
		if(value<0) {
			this.negative = true;
			if(wasMinValue)
				value = Long.MAX_VALUE;
			else
				value = -value;
		}
		else {
			this.negative = false;
			if(value==0) {
				this.significantDigits = new int[0];
				this.orderOfMagnitude=0;
				return;
			}
		}
		this.significantDigits = getDigits(value,0);
		if(wasMinValue)
			this.significantDigits[this.significantDigits.length-1]++;
		this.orderOfMagnitude = this.significantDigits.length-1;
		// Trim trailing zeros if exact
		if(exact) {
			int trailCount=0;
			for(int i=this.significantDigits.length-1;i>0;i--) {
				if(this.significantDigits[i]!=0)
					break;
				trailCount++;
			}
			if(trailCount>0) {
				int[] newDigits = new int[this.significantDigits.length-trailCount];
				System.arraycopy(this.significantDigits, 0, newDigits, 0, newDigits.length);
				this.significantDigits=newDigits;
			}
		}
	}
	
	/**
	 * @param significantDigits Decimal digits, ordered from most significant to least significant.
	 *                          For 0 this should be an empty array.
	 * @param orderOfMagnitude The order of magnitude of the leading digit.
	 *                         For 0-valued ScientificDecimals, the orderOfMagnitude identifies the lowest known 0 place value. (By which is meant, the place value that is taken to be 0 +/- 1.)
	 * @param negative
	 * @param exact
	 */
	public SignificantDecimal(int[] significantDigits, long orderOfMagnitude, boolean negative, boolean exact) {
		super();
		boolean isZero = significantDigits.length==0;
		for(int i=0;i<significantDigits.length;i++) {
			if((significantDigits[i]<0)||(significantDigits[i]>9))
				throw new NumberFormatException("Digits less than zero or greater than 9 are invalid for ScientificDecimal.");
			if(i!=0)
				isZero=false;
		}
		if((!isZero)&&(significantDigits[0]==0))
			throw new NumberFormatException("Leading digits cannot be 0 for non-zero value, and significantDigits should be an empty array for a 0-valued ScientificDecimal.");
		this.significantDigits = significantDigits;
		this.orderOfMagnitude = orderOfMagnitude;
		this.negative = negative;
		this.exact=exact;
	}
	
	/**
	 * Takes an array of digits which is longer than the number of digits to be present in the final number
	 * and rounds it to the right number digits for the final number. This constructor will properly handle
	 * rounding 9.99 down to 2 digits to generate a result of 10.
	 * 
	 * Should not be used to express a value of zero.
	 * 
	 * @param digits
	 * @param significantDigitCount The number of significant digits to round to.
	 * @param orderOfMagnitude The order of magnitude of the digits.
	 * @param negative
	 */
	private SignificantDecimal(int[] digits, int significantDigitCount, long orderOfMagnitude, boolean negative) {
		super();
		this.significantDigits = new int[significantDigitCount];
		System.arraycopy(digits, 0, this.significantDigits, 0, significantDigitCount);
		if(significantDigits[0]==0)
			throw new NumberFormatException("Leading digits cannot be 0 for non-zero value, and significantDigits should be an empty array for a 0-valued ScientificDecimal.");
		if(digits[significantDigitCount]>=5) {
			this.significantDigits[significantDigitCount-1]++;
			for(int i=significantDigitCount-1;i>0;i--) {
				if(this.significantDigits[i]<=9)
					break;
				this.significantDigits[i]=0;
				this.significantDigits[i-1]++;
			}
			if(this.significantDigits[0]>9) {
				this.significantDigits[0]=1;
				orderOfMagnitude++;
			}
		}
		this.orderOfMagnitude = orderOfMagnitude;
		this.negative = negative;
		this.exact=false;
	}

	/**
	 * 
	 * @param value A positive (ie. absolute value) integer
	 * @param nextOrderOfMagnitude
	 * @return
	 */
	private static int[] getDigits(long value, int nextOrderOfMagnitude) {
		if(value==0)
			return new int[nextOrderOfMagnitude];
		int placeValue = (int)(value%10);
		long continuingValue = (value>>>1)/5;
		int[] retval = getDigits(continuingValue, nextOrderOfMagnitude+1);
		retval[retval.length-1-nextOrderOfMagnitude] = placeValue;
		return retval;
	}

	@Override
	public int compareTo(SignificantDecimal o) {
		if(this==o)
			return 0;
		if(o.negative && !this.negative)
			return 1;
		if(!o.negative && this.negative)
			return -1;
		int negativeMultiplier = negative?-1:1;
		int compare = negativeMultiplier*absCompareTo(this, o);
		if(compare!=0)
			return compare;
		if(o.exact && !this.exact)
			return 1;
		if(!o.exact && this.exact)
			return -1;
		return 0;
	}
	
	private static int absCompareTo(SignificantDecimal a, SignificantDecimal b) {
		if(a==b)
			return 0;
		boolean aIsZero = a.isZero();
		boolean bIsZero = b.isZero();
		long aHighestNonzeroOrderOfMagnitude = aIsZero?(a.orderOfMagnitude-1):a.orderOfMagnitude;
		long bHighestNonzeroOrderOfMagnitude = bIsZero?(b.orderOfMagnitude-1):b.orderOfMagnitude;
		if(aHighestNonzeroOrderOfMagnitude!=bHighestNonzeroOrderOfMagnitude) {
			if(aHighestNonzeroOrderOfMagnitude>bHighestNonzeroOrderOfMagnitude)
				return 1;
			else
				return -1;
		}
		if(aIsZero&&bIsZero)
			return 0;
		if(aIsZero)
			return -1;
		if(bIsZero)
			return 1;
		for(int i=0;i<Math.min(a.significantDigits.length,b.significantDigits.length);i++)
			if(a.significantDigits[i]!=b.significantDigits[i])
				return (a.significantDigits[i]-b.significantDigits[i]);
		// The one with the most significant digits is always considered greater, when that is the last resort. 
		return a.significantDigits.length-b.significantDigits.length;
	}
	
	@Override
	public SignificantDecimal getZero() {
		return ZERO;
	}

	@Override
	public SignificantDecimal getIdentity() {
		return ONE;
	}

	@Override
	public int intValue() {
		if(isZero())
			return 0;
		if(orderOfMagnitude<0)
			return 0;
		int negativeMultiplier = negative?-1:1;
		long retval = 0;
		for(int digit:significantDigits) {
			retval*=10;
			retval+=digit;
			if(retval>=Integer.MAX_VALUE)
				return 0;
		}
		return negativeMultiplier*(int)retval;
	}

	@Override
	public long longValue() {
		if(isZero())
			return 0;
		if(orderOfMagnitude<0)
			return 0;
		int negativeMultiplier = negative?-1:1;
		long retval = 0;
		long retvalHigh = 0;
		for(int digit:significantDigits) {
			retval*=10;
			retvalHigh*=10;
			retval+=digit;
			retvalHigh+=retval>>32;
			retval&=0xFFFFFFFFL;
			if(retvalHigh>=Integer.MAX_VALUE)
				return 0;
		}
		return negativeMultiplier*((retvalHigh<<32)+retval);
	}

	@Override
	public float floatValue() {
		if(isZero())
			return 0;
		if(Math.abs(orderOfMagnitude)>(126/3))
			return Float.NaN;
		float val = 0.0f;
		for(int i=0;i<Math.min(significantDigits.length,15);i++) {
			val *= 10;
			val += significantDigits[i];
		}
		long currentOrder = Math.min(significantDigits.length,7)-1;
		if(currentOrder>orderOfMagnitude) {
			while(currentOrder>(orderOfMagnitude+6)) {
				val/=1000000L;
				currentOrder-=6;
			}
			while(currentOrder>orderOfMagnitude) {
				val/=10;
				currentOrder-=1;
			}
		}
		else if(currentOrder<orderOfMagnitude) {
			while(currentOrder<(orderOfMagnitude-6)) {
				val*=1000000L;
				currentOrder+=6;
			}
			while(currentOrder<orderOfMagnitude) {
				val*=10;
				currentOrder+=1;
			}
		}
		return val;
	}

	@Override
	public double doubleValue() {
		if(isZero())
			return 0;
		if(Math.abs(orderOfMagnitude)>(1022/3))
			return Double.NaN;
		double val = 0.0;
		for(int i=0;i<Math.min(significantDigits.length,15);i++) {
			val *= 10;
			val += significantDigits[i];
		}
		long currentOrder = Math.min(significantDigits.length,15)-1;
		if(currentOrder>orderOfMagnitude) {
			while(currentOrder>(orderOfMagnitude+10)) {
				val/=10000000000L;
				currentOrder-=10;
			}
			while(currentOrder>orderOfMagnitude) {
				val/=10;
				currentOrder-=1;
			}
		}
		else if(currentOrder<orderOfMagnitude) {
			while(currentOrder<(orderOfMagnitude-10)) {
				val*=10000000000L;
				currentOrder+=10;
			}
			while(currentOrder<orderOfMagnitude) {
				val*=10;
				currentOrder+=1;
			}
		}
		return val;
	}
	
	/**
	 * 
	 * @param q
	 * @return The result of multiplication.
	 */
	public SignificantDecimal multiply(SignificantDecimal q) {
		boolean negative = this.negative^q.negative;
		long orderOfMagnitude = this.orderOfMagnitude+q.orderOfMagnitude;
		if(isZero()&&q.isZero())
			return new SignificantDecimal(new int[0],orderOfMagnitude, negative, exact||q.exact);
		if(isZero()||q.isZero())
			return new SignificantDecimal(new int[0],orderOfMagnitude+1, negative, (isZero()&&exact)||(q.isZero()&&q.exact));
		int[] digits = new int[this.significantDigits.length+q.significantDigits.length];
		for(int i=0;i<this.significantDigits.length;i++)
			for(int j=0;j<q.significantDigits.length;j++)
				digits[i+j]+=this.significantDigits[i]*q.significantDigits[j];
		int carry=0;
		for(int i=digits.length-1;i>=0;i--) {
			digits[i]+=carry;
			carry = digits[i]/10;
			digits[i]%=10;
		}
		int significantDigitLength;
		if(exact && q.exact) {
			significantDigitLength=digits.length;
			if(carry!=0)
				significantDigitLength++;
			for(int i=digits.length-1;i>=0;i--) {
				if(digits[i]!=0)
					break;
				significantDigitLength--;
			}
			int[] d = new int[significantDigitLength];
			if(carry>0) {
				d[0] = carry;
				System.arraycopy(digits, 0, d, 1, significantDigitLength-1);
				orderOfMagnitude++;
			}
			else {
				System.arraycopy(digits, 0, d, 0, significantDigitLength);
			}
			return new SignificantDecimal(d,orderOfMagnitude,negative,true);
		}
		if(exact)
			significantDigitLength = q.significantDigits.length;
		else if(q.exact)
			significantDigitLength = this.significantDigits.length;
		else
			significantDigitLength = Math.min(this.significantDigits.length,q.significantDigits.length);
		if(carry>0) {
			for(int i=significantDigitLength;i>0;i--)
				digits[i]=digits[i-1];
			digits[0] = carry;
			orderOfMagnitude++;
		}
		return new SignificantDecimal(digits, significantDigitLength, orderOfMagnitude, negative);
	}
	
	/**
	 * 
	 * @param q
	 * @return The result of division. If this SignificantDecimal and q are both exact, the result might be
	 *         inexact due to the possibility that such division might require an infinite number of digits
	 *         to represent. (This doesn't mean that this is required to produce an inexact result. The result
	 *         will be rendered as inexact if it simply has too many digits before the calculation is
	 *         completed.)
	 */
	public SignificantDecimal divide(SignificantDecimal q) {
		if(q.isZero())
			throw new IllegalArgumentException("Divide by zero.");
		if(this.isZero())
			return this;
		boolean negative = this.negative^q.negative;
		long orderOfMagnitude = this.orderOfMagnitude - q.orderOfMagnitude+1;
		int numberOfSignificantDigits;
		if(this.exact) {
			if(q.exact)
				numberOfSignificantDigits = Math.max(this.significantDigits.length,q.significantDigits.length)+10;
			else
				numberOfSignificantDigits = q.significantDigits.length;
		}
		else if(q.exact){
			numberOfSignificantDigits = this.significantDigits.length;
		}
		else {
			numberOfSignificantDigits = Math.min(this.significantDigits.length,q.significantDigits.length);
		}
		int[] resultDigits = new int[numberOfSignificantDigits+1];
		int[] workingDigits = new int[Math.max(this.significantDigits.length,q.significantDigits.length)+1];
		int[][] denominatorMultiples = new int[9][q.significantDigits.length];
		for(int i=0;i<this.significantDigits.length;i++)
			workingDigits[i+1] = this.significantDigits[i];
		for(int d=1;d<10;d++) {
			for(int i=q.significantDigits.length-1;i>=0;i--) {
				denominatorMultiples[d-1][i] += d*q.significantDigits[i];
				if(i>0) {
					denominatorMultiples[d-1][i-1] += denominatorMultiples[d-1][i]/10;
					denominatorMultiples[d-1][i] %= 10;
				}
			}
		}
		int locator;
		boolean workingDigitsZero = false;
		for(int i=0;(i<resultDigits.length)&&(!workingDigitsZero);i++) {
			locator = workingDigits[0]/denominatorMultiples[0][0];
			if(locator>0) {
				if(locator>10)
					locator=9;
				// The result of dividing the whole number by the whole number will be locator or somewhat less.
				int compare = compareDigitsScaleless(workingDigits,denominatorMultiples[locator-1]);
				while(compare<0) {
					locator--;
					if(locator==0)
						break;
					compare = compareDigitsScaleless(workingDigits,denominatorMultiples[locator-1]);
				}
				if(compare==0)
					workingDigitsZero=true; // Working Digits will become exactly zero after the subtraction
				resultDigits[i] = locator;
			}
			else {
				resultDigits[i] = 0;
			}

			if(resultDigits[i]>0) {
				// Subtract resultDigit*divisor from the working digits.
				for(int j=0;j<Math.min(workingDigits.length,denominatorMultiples[0].length);j++)
					workingDigits[j] -= denominatorMultiples[resultDigits[i]-1][j];
				for(int j=workingDigits.length-1;j>0;j--) {
					// Do any necessary borrowing to get positive digits
					if(workingDigits[j]<0) {
						workingDigits[j]+=10;
						workingDigits[j-1]--;
					}
				}
			}
			// Shift the working digits left by 1 digit.
			workingDigits[0]*=10;
			for(int j=1;j<workingDigits.length;j++) {
				workingDigits[j-1] += workingDigits[j];
				workingDigits[j] = 0;
			}
			if((i==0)&&(resultDigits[0] == 0)) {
				orderOfMagnitude--;
				i--;
			}
		}
		if(this.exact&&q.exact&&workingDigitsZero) {
			// Trim the exact result.
			while(resultDigits[numberOfSignificantDigits-1]==0)
				numberOfSignificantDigits--;
			int[] finalResultDigits = new int[numberOfSignificantDigits];
			System.arraycopy(resultDigits, 0, finalResultDigits, 0, numberOfSignificantDigits);
			return new SignificantDecimal(finalResultDigits,orderOfMagnitude,negative, true);
		}
		// Make sure to properly round the last digit for the inexact result.
		return new SignificantDecimal(resultDigits, numberOfSignificantDigits, orderOfMagnitude, negative);
	}

	/**
	 * Intended to compare two arrays of integers in which all values are greater than or equal to 0 and less than 10, except the first value.
	 * It is expected that the first value in the numbers might be greater than 10 but less than 100 as it may contain an overflow digit.
	 * 
	 * @param a
	 * @param b
	 * @return A value greater than 0 if a>b; A value less than 0 if b>a; or 0, if the arrays are equal.
	 */
	private static int compareDigitsScaleless(int[] a, int[] b) {
		for(int i=0;i<Math.min(a.length, b.length);i++) {
			if(a[i]>b[i])
				return 1;
			if(b[i]>a[i])
				return -1;
		}
		for(int i=Math.min(a.length, b.length);i<a.length;i++)
			if(a[i]>0)
				return 1;
		for(int i=Math.min(a.length, b.length);i<b.length;i++)
			if(b[i]>0)
				return -1;
		return 0;
	}
	
	/**
	 * Note: For proper results, a series of numbers being added together should
	 * be added in order of lowest order least significant digit to highest order least significant digit. 
	 * 
	 * ie. 101 + 1.56 + 12.276 should proceed as (12.276 + 1.56) + 101
	 * 
	 * @param q
	 * @return The result of addition.
	 */
	public SignificantDecimal add(SignificantDecimal q) {
		if(q.isZero()) {
			if(q.exact||(q.orderOfMagnitude<=(this.orderOfMagnitude-this.significantDigits.length+1)))
				return this;
			if(q.orderOfMagnitude>this.orderOfMagnitude)
				return q;
			return this.round((int)(this.orderOfMagnitude-q.orderOfMagnitude+1));
		}
		if(isZero()) {
			if(exact||(this.orderOfMagnitude<=(q.orderOfMagnitude-q.significantDigits.length+1)))
				return q;
			if(this.orderOfMagnitude>q.orderOfMagnitude)
				return this;
			return q.round((int)(q.orderOfMagnitude-this.orderOfMagnitude+1));
		}

		if(this.negative^q.negative) {
			if(absCompareTo(this,q)>=0)
				return absSubtract(this,q,this.negative);
			return absSubtract(q,this,q.negative);
		}
		return absAdd(this,q,this.negative);
	}
	
	/**
	 * eg. round(1) called on 3.14159x10^0 would yield 3*10^0
	 * eg. round(4) called on 3.14159x10^0 would yield 3.142*10^0
	 * eg. round(2) called on 9.999x10^0 would yield 1.00*10^1
	 * 
	 * @param digitToRound Identifies the digit to round. (Must be greater than 1 and less than the number of significant digits.)
	 * @return
	 */
	private SignificantDecimal round(int digitToRound) {
		if(digitToRound<=0)
			throw new RuntimeException("round called with an invalid digit.");
		if(digitToRound>=this.significantDigits.length)
			throw new RuntimeException("round called with no rounding");
		int[] retvalDigits = new int[digitToRound];
		System.arraycopy(this.significantDigits, 0, retvalDigits, 0, digitToRound);
		if(this.significantDigits[digitToRound]>=5)
			retvalDigits[digitToRound-1]++;
		for(int i = digitToRound-1;i>0;i--) {
			if(retvalDigits[i]<=9)
				break;
			retvalDigits[i]=0;
			retvalDigits[i-1]++;
		}
		long orderOfMagnitude = this.orderOfMagnitude;
		if(retvalDigits[0]>9) {
			retvalDigits = new int[digitToRound+1];
			retvalDigits[0]=1;
			orderOfMagnitude++;
		}
		return new SignificantDecimal(retvalDigits,orderOfMagnitude,this.negative,false);
	}
	
	/**
	 * Adds a to b, treating both as positive numbers. The result is given the sign as specified by the negative parameter.
	 * @param a
	 * @param b
	 * @param negative If true then the result will be negative. If false then the result will be a positive number.
	 * @return
	 */
	private static SignificantDecimal absAdd(SignificantDecimal a,SignificantDecimal b, boolean negative) {
		long highA = a.orderOfMagnitude;
		long highB = b.orderOfMagnitude;
		long lowA = a.orderOfMagnitude-a.significantDigits.length+1;
		long lowB = b.orderOfMagnitude-b.significantDigits.length+1;
		long orderOfMagnitude = Math.max(highA, highB);
		long lowestDigitToKeep;
		boolean roundLastDigitOut=true;
		if(lowA==lowB) {
			lowestDigitToKeep = lowA;
			roundLastDigitOut=false;
		}
		else if(lowA>lowB) {
			if(a.exact) {
				lowestDigitToKeep = lowB;
				roundLastDigitOut=false;
			}
			else {
				lowestDigitToKeep = lowA-1;
			}
		}
		else {
			if(b.exact) {
				lowestDigitToKeep = lowA;
				roundLastDigitOut=false;
			}
			else {
				lowestDigitToKeep = lowB-1;
			}
		}
		
		// If b is insignificant.
		if(highB<lowestDigitToKeep)
			return new SignificantDecimal(a.significantDigits, a.orderOfMagnitude, negative, false);
		// If a is insignificant.
		if(highA<lowestDigitToKeep)
			return new SignificantDecimal(b.significantDigits, b.orderOfMagnitude, negative, false);

		// Add decimal places
		int[] returnDigits = new int[(int)(orderOfMagnitude-lowestDigitToKeep+1)];
		long magnitude;
		for(int i=0;i<returnDigits.length;i++) {
			magnitude = orderOfMagnitude-i;
			if((magnitude<=highA)&&(magnitude>=lowA))
				returnDigits[i] = a.significantDigits[(int)(highA-magnitude)];
			if((magnitude<=highB)&&(magnitude>=lowB))
				returnDigits[i] += b.significantDigits[(int)(highB-magnitude)];
		}
		
		// Carry
		for(int i=returnDigits.length-1;i>0;i--) {
			returnDigits[i-1] += returnDigits[i]/10;
			returnDigits[i] %= 10;
		}
		if(returnDigits[0]>=10) {
			int[] newReturnDigits = new int[returnDigits.length+1];
			newReturnDigits[0] = returnDigits[0]/10;
			newReturnDigits[1] = returnDigits[0]%10;
			for(int i=1;i<returnDigits.length;i++)
				newReturnDigits[i+1] = returnDigits[i];
		}
		
		SignificantDecimal retval = new SignificantDecimal(returnDigits, orderOfMagnitude, negative, a.exact&&b.exact);
		if(roundLastDigitOut)
			retval=retval.round(retval.significantDigits.length-1);
		return retval;
	}
	
	/**
	 * 
	 * @param q
	 * @return The result of subtraction.
	 */
	public SignificantDecimal subtract(SignificantDecimal q) {
		if(q.isZero()) {
			if(q.exact||(q.orderOfMagnitude<=(this.orderOfMagnitude-this.significantDigits.length+1)))
				return this;
			if(q.orderOfMagnitude>this.orderOfMagnitude)
				return q;
			return this.round((int)(this.orderOfMagnitude-q.orderOfMagnitude+1));
		}
		if(isZero()) {
			if(exact||(this.orderOfMagnitude<=(q.orderOfMagnitude-q.significantDigits.length+1)))
				return q.getNegative();
			if(this.orderOfMagnitude>q.orderOfMagnitude)
				return this;
			return q.round((int)(q.orderOfMagnitude-this.orderOfMagnitude+1)).getNegative();
		}

		if(this.negative^q.negative)
			return absAdd(this,q,this.negative);
		if(absCompareTo(this, q)>0)
			return absSubtract(this,q,this.negative);
		return absSubtract(q,this,q.negative);
	}

	/**
	 * Subtract b from a, treating both as positive numbers. The result is given the sign as specified by the negative parameter.
	 * 
	 * @param a Must be a large number than b. (absCompareTo(a,b) > 1)
	 * @param b
	 * @param negative If true then the result will be negative. If false then the result will be a positive number.
	 * @return The result of subtracting the absolute value of b from the absolute value of a, with the sign of the result determined by the negative parameter.
	 */
	private static SignificantDecimal absSubtract(SignificantDecimal a,SignificantDecimal b, boolean negative) {
		long highA = a.orderOfMagnitude;
		long highB = b.orderOfMagnitude;
		long lowA = a.orderOfMagnitude-a.significantDigits.length+1;
		long lowB = b.orderOfMagnitude-b.significantDigits.length+1;
		long orderOfMagnitude = highA;
		long lowestDigitToKeep;
		boolean roundLastDigitOut=true;
		if(lowA==lowB) {
			lowestDigitToKeep = lowA;
			roundLastDigitOut=false;
		}
		else if(lowA>lowB) {
			if(a.exact) {
				lowestDigitToKeep = lowB;
				roundLastDigitOut=false;
			}
			else {
				lowestDigitToKeep = lowA-1;
			}
		}
		else {
			if(b.exact) {
				lowestDigitToKeep = lowA;
				roundLastDigitOut=false;
			}
			else {
				lowestDigitToKeep = lowB-1;
			}
		}
		
		// If b is insignificant.
		if(highB<lowestDigitToKeep)
			return new SignificantDecimal(a.significantDigits, a.orderOfMagnitude, negative, false);
		
		// Add decimal places
		int[] workingDigits = new int[(int)(orderOfMagnitude-lowestDigitToKeep+1)];
		long magnitude;
		for(int i=0;i<workingDigits.length;i++) {
			magnitude = orderOfMagnitude-i;
			if((magnitude<=highA)&&(magnitude>=lowA))
				workingDigits[i] = a.significantDigits[(int)(highA-magnitude)];
			if((magnitude<=highB)&&(magnitude>=lowB))
				workingDigits[i] -= b.significantDigits[(int)(highB-magnitude)];
		}
		
		// Borrow
		for(int i=workingDigits.length-1;i>0;i--) {
			if(workingDigits[i]<0) {
				workingDigits[i-1]--;
				workingDigits[i] += 10;
			}
		}
		// Assemble result.
		int exWorkingDigitCount = 0;
		for(int i=0;i<workingDigits.length;i++) {
			if(workingDigits[i]!=0)
				break;
			exWorkingDigitCount++;
		}
		orderOfMagnitude-=exWorkingDigitCount;
		int[] returnDigits = new int[(int)(orderOfMagnitude-lowestDigitToKeep+1)];
		for(int i=0;i<returnDigits.length;i++)
			returnDigits[i] = workingDigits[i+exWorkingDigitCount];
		
		SignificantDecimal retval = new SignificantDecimal(returnDigits, orderOfMagnitude, negative, a.exact&&b.exact);
		if(roundLastDigitOut)
			retval=retval.round(retval.significantDigits.length-1);
		return retval;
	}
	
	@Override
	public SignificantDecimal getNegative() {
		return new SignificantDecimal(significantDigits, orderOfMagnitude, !negative, exact);
	}

	@Override
	public boolean isZero() {
		return significantDigits.length==0;
	}

	@Override
	public boolean isNegative() {
		return negative;
	}

	public boolean isExact() {
		return exact;
	}
	
	@Override
	public long getBinaryPrecision() {
		return significantDigits.length*3+(significantDigits.length-1)/3;
	}

	/**
	 * Note: This method only reports whether the significant digits are all non-fractional.
	 * @return true if this quadruple is a whole number and false otherwise.
	 */
	public boolean isWholeNumber() {
		return (this.orderOfMagnitude-significantDigits.length)>-1;
	}

	@Override
	public SignificantDecimal sqrt() {
		if(negative)
			return null;
		if(isZero())
			return this;
		
		// TODO This will be hard to improve on.
		// Probably it will require converting the number to a binary integer representation and executing a binary square root algorithm.
		// One easier step is to extend out to 64 value bits (18 reliable significant digits) but to extend to the general case will be a pain. 
		if(significantDigits.length>15)
			throw new RuntimeException("sqrt() is not currently capable of handling more than 15 significant digits.");
		// TODO Handling the different orders of magnitudes case shouldn't be too hard.
		if(Math.abs(orderOfMagnitude)>(1023/3))
			throw new RuntimeException("sqrt() is not currently capable of handling orders of magitude greater than "+(1023/3)+".");
		
		return new SignificantDecimal(Math.sqrt(this.doubleValue()), significantDigits.length);
	}

	@Override
	public SignificantDecimal pow(SignificantDecimal exponent) {
		// TODO The following is only really true if the zero and one are exact numbers. There should be a separate function taking an integer value for exact exponents.
		if(exponent.isZero())
			return ONE;
		if(exponent.equals(ONE))
			return this;
		if(this.equals(ONE))
			return this;
		if(this.isZero()) {
			if(exponent.isNegative())
				throw new InvalidParameterException("Cannot find the negative power of zero.");
			return this;
		}
		if(this.negative&&(!exponent.isWholeNumber()))
			throw new InvalidParameterException("Cannot find the fractional power of a negative number. (Imaginary/complex result.)");
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 
	 * @return
	 */
	public SignificantDecimal log10() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		String retval = negative?"-":"";
		if(isZero()) {
			retval="0";
		}
		else {
			for(int i=0;i<significantDigits.length;i++) {
				retval+=significantDigits[i];
				if((i==0)&&(significantDigits.length>1))
					retval+=".";
			}
		}
		if(exact)
			retval="["+retval+"]";
		retval+="x10^"+orderOfMagnitude;
		return retval;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SignificantDecimal other = (SignificantDecimal) obj;
		return compareTo(other)==0;
	}
}
