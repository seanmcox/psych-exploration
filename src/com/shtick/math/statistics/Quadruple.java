/**
 * 
 */
package com.shtick.math.statistics;

import java.security.InvalidParameterException;
import java.util.LinkedList;

/**
 * @author scox
 *
 */
public class Quadruple extends ArithmeticNumber<Quadruple> {
	private static final Quadruple ZERO = new Quadruple(0);
	private static final Quadruple ONE = new Quadruple(1);
	private static final Quadruple TWO = new Quadruple(2);
	private static final Quadruple POW_ERROR_THRESHOLD = new Quadruple(10);
	private long value;
	private long orderOfMagnitude;
	private boolean negative;

	/**
	 * 
	 * @param d A simple double
	 */
	public Quadruple(double d) {
		if(!Double.isFinite(d))
			throw new IllegalArgumentException("Non-finite double cannot be used to instantiate a Quadruple.");
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
				this.value = 0;
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
		// Adjust to ideal whole/fractional parts
		while((mantissa&0xFFFFFFFF00000000L)!=0) {
			mantissaLow+=(mantissa%10)<<32;
			mantissa>>>=1;
			mantissa/=5;
			mantissaLow/=10;
			decimalExponent++;
		}
		this.value=mantissaLow+(mantissa<<32);
		this.orderOfMagnitude = decimalExponent;
	}
	
	

	/**
	 * 
	 * @param i A simple signed integer.
	 */
	public Quadruple(int i) {
		this(((i<0)?(long)-i:(long)i)*0x100000000L,0,i<0);
	}
	
	/**
	 * <p>Represents the number +/- value/2^32 * 10^orderOfMagnitude</p>
	 * 
	 * Examples:<br/>
	 * Quadruple(0x500000000, 0, false) // To creates an instance of the number 5.<br/> 
	 * Quadruple(0x500000000, 1, false) // To creates an instance of the number 50.<br/> 
	 * Quadruple(0x5000000000, 0, false) // To creates an instance of the number 80.<br/> 
	 * Quadruple(50*0x100000000, 1, false) // To creates an instance of the number 50.<br/> 
	 * Quadruple(80*0x100000000, 1, false) // To creates an instance of the number 80.<br/> 
	 * 
	 * @param value An unsigned long value.
	 * @param orderOfMagnitude
	 * @param negative
	 */
	public Quadruple(long value, long orderOfMagnitude, boolean negative) {
		super();
		while((value<=Long.MAX_VALUE/5)&&(value>0)) {
			// Is the value less than or equal to 1/10th of the maximum unsigned long value?
			// If so, then normalize the value by adjusting it up by a factor of 10 and reducing the order of magnitude.
			value*=5;
			value<<=1;
			orderOfMagnitude--;
		}
		this.value = value;
		this.orderOfMagnitude = (value==0)?0:orderOfMagnitude;
		this.negative = (value==0)?false:negative;
	}

	@Override
	public int compareTo(Quadruple o) {
		if(value==0) {
			if(o.value==0)
				return 0;
			return o.negative?1:-1;
		}
		if(o.value==0)
			return negative?-1:1;
		if(o.negative && !this.negative)
			return 1;
		if(!o.negative && this.negative)
			return -1;
		if(o.orderOfMagnitude!=orderOfMagnitude) {
			if(orderOfMagnitude < o.orderOfMagnitude)
				return negative?1:-1;
			return negative?-1:1;
		}
		if((value>>>32) < (o.value>>>32))
			return negative?1:-1;
		if((value>>>32) > (o.value>>>32))
			return negative?-1:1;
		if((value&0xFFFFFFFFL) < (o.value&0xFFFFFFFFL))
			return negative?1:-1;
		if((value&0xFFFFFFFFL) > (o.value&0xFFFFFFFFL))
			return negative?-1:1;
		return 0;
	}

	@Override
	public Quadruple getZero() {
		return ZERO;
	}

	@Override
	public Quadruple getIdentity() {
		return ONE;
	}
	
	/**
	 * 
	 * @return The absolute value of a hypothetical bit in the 1st position in this Quadruple's value.
	 */
	public Quadruple getSmallestPlaceValue() {
		return new Quadruple(1,orderOfMagnitude,false);
	}

	@Override
	public int intValue() {
		if(orderOfMagnitude>=0)
			return 0;
		if(orderOfMagnitude<=-10)
			return 0;
		int retval = (int)(value>>>32);
		for(int i=0;i>orderOfMagnitude;i--) {
			retval>>>=1;
			retval/=5;
		}
		if(negative)
			retval = -retval;
		return retval;
	}

	@Override
	public long longValue() {
		if(orderOfMagnitude>=10)
			return 0;
		if(orderOfMagnitude<=-10)
			return 0;
		long retval = (long)(value>>>32);
		if(orderOfMagnitude<0) {
			for(int i=0;i>orderOfMagnitude;i--) {
				retval/=10;
			}
		}
		else {
			long floatingPart = (long)(value&0x0FFFFFFFFL);
			for(int i=0;i<orderOfMagnitude;i++) {
				retval*=10;
				floatingPart*=10;
			}
			floatingPart>>>=32;
			retval+=floatingPart;
		}
		if(negative)
			retval = -retval;
		return retval;
	}

	@Override
	public float floatValue() {
		// TODO Auto-generated method stub
		return Float.NaN;
	}

	@Override
	public double doubleValue() {
		if(this.isZero())
			return 0.0;
		double cmp = this.getAbs().compareTo(new Quadruple(Double.MAX_VALUE));
		if(cmp==0)
			return Double.MAX_VALUE;
		if(this.getAbs().compareTo(new Quadruple(Double.MAX_VALUE))>0)
			throw new NumberFormatException("Quadruple too large to convert to double.");
		if(this.getAbs().compareTo(new Quadruple(Double.MIN_VALUE))<0)
			return 0.0;
		boolean isSubnormal = (this.getAbs().compareTo(new Quadruple(Double.MIN_NORMAL))<0)?true:false;
		long lret = 0;
		if(negative)
			lret|=0x8000000000000000L;
		long order5 = orderOfMagnitude;
		long order2 = orderOfMagnitude-32;
		long workingValue = value;
		if(workingValue<0) {
			workingValue>>>=1;
			order2++;
		}
		if(isSubnormal) {
			while((order2>-1075)&&(order5<0)) {
				if(workingValue<0x4000000000000000L) {
					workingValue<<=1;
					order2--;
				}
				else {
					workingValue/=5;
					order5++;
				}
			}
			while(order5<0) {
				workingValue/=5;
				order5++;
			}
			lret|=workingValue>>>1;
		}
		else {
			while(order5>0) {
				if(workingValue<=(0x7FFFFFFFFFFFFFFFL/5L)) {
					workingValue>>>=1;
					order2++;
				}
				else {
					workingValue*=5;
					order5--;
				}
			}
			while(order5<0) {
				if(workingValue<0x4000000000000000L) {
					workingValue<<=1;
					order2--;
				}
				else {
					workingValue/=5;
					order5++;
				}
			}
			while(workingValue>0x1FFFFFFFFFFFFFL) {
				workingValue>>>=1;
				order2++;
			}
			lret|=((order2+1075)<<52)|(workingValue&0xFFFFFFFFFFFFFL);
		}
		return Double.longBitsToDouble(lret);
	}
	
	/**
	 * 
	 * @param q
	 * @return The result of multiplication.
	 */
	public Quadruple multiply(Quadruple q) {
		long orderOfMagnitude = this.orderOfMagnitude + q.orderOfMagnitude;
		if(value==0)
			return this;
		if(q.value==0)
			return q;
		long upperPartA = (value>>>32);
		long lowerPartA = (value&0xFFFFFFFFL);
		long upperPartB = (q.value>>>32);
		long lowerPartB = (q.value&0xFFFFFFFFL);
		long newValueUU = upperPartA*upperPartB;
		long newValueUL = upperPartA*lowerPartB;
		long newValueLU = upperPartB*lowerPartA;
		long newValueLL = lowerPartA*lowerPartB;
		long newValueHH = newValueUU>>>32;
		long newValueH = (newValueUU&0xFFFFFFFFL) + (newValueUL>>>32) + (newValueLU>>>32);
		long newValueL = (newValueUL&0xFFFFFFFFL) + (newValueLU&0xFFFFFFFFL) + (newValueLL>>>32);
		newValueH+=newValueL>>>32;
		newValueHH+=newValueH>>>32;
		// newValueLL &= 0xFFFFFFL; Unneeded because the lowest place won't carry and these lower order digits are disposable
		while(newValueHH>0) {
			long t = newValueHH % 10;
			newValueHH/=10;
			newValueH+=t<<32;
			t = newValueH % 10;
			newValueH/=10;
			newValueL+=t<<32;
			newValueL/=10;
			orderOfMagnitude++;
		}
		long newValue = (newValueH<<32) + newValueL;
		boolean negative = q.negative ^ this.negative;
		return new Quadruple(newValue,orderOfMagnitude,negative);
	}
	
	/**
	 * 
	 * @param q
	 * @return The result of division.
	 */
	public Quadruple divide(Quadruple q) {
		if(q.value==0)
			throw new IllegalArgumentException("Divide by zero.");
		if(this.value==0)
			return this;
		long orderOfMagnitude = this.orderOfMagnitude - q.orderOfMagnitude;
		boolean negative = q.negative ^ this.negative;
		if(this.value==q.value)
			return new Quadruple(1*0x100000000L,orderOfMagnitude,negative);
		long upperPartA = (value>>>32);
		long lowerPartA = (value&0xFFFFFFFFL);
		long upperPartB = (q.value>>>32);
		long lowerPartB = (q.value&0xFFFFFFFFL);
		long result=0;
		int wholeBits=1;
		// Line up A (this) bits and B (q) bits for boolean division.
		while(upperPartB<upperPartA) {
			upperPartB<<=1;
			lowerPartB<<=1;
			if((lowerPartB&0x100000000L)>0) {
				upperPartB++;
				lowerPartB&=0xFFFFFFFFL;
			}
			wholeBits++;
		}
		while((upperPartB>upperPartA)||((upperPartB==upperPartA)&&(lowerPartB>lowerPartA))) {
			upperPartA<<=1;
			lowerPartA<<=1;
			if((lowerPartA&0x100000000L)>0) {
				upperPartA++;
				lowerPartA&=0xFFFFFFFFL;
			}
			wholeBits--;
		}
		// Perform boolean division
		for(long mask=0x8000000000000000L;mask!=0;mask>>>=1) {
			if((upperPartB<upperPartA)||((upperPartB==upperPartA)&&(lowerPartB<=lowerPartA))) {
				result |= mask;
				if(lowerPartB>lowerPartA) {
					upperPartA--;
					lowerPartA|=0x100000000L;
				}
				lowerPartA-=lowerPartB;
				upperPartA-=upperPartB;
			}
			upperPartA<<=1;
			lowerPartA<<=1;
			if((lowerPartA&0x100000000L)>0) {
				upperPartA++;
				lowerPartA&=0xFFFFFFFFL;
			}
		}
		// Scale result for optimal representation in Quadruple.
		long intPart = (wholeBits>0)?(result>>>(64-wholeBits)):0;
		long floatPart1 = (wholeBits>0)?(result<<wholeBits):(result>>>-wholeBits);
		long floatPart2 = floatPart1&0xFFFFFFFFL;
		floatPart1>>>=32;
		long floatPart3 = (wholeBits>=0)?0:((result<<(64+wholeBits))>>>32);
		while(intPart<0xFFFFFFFFL) {
			// Scale up by factors of 10 until we've gone too far (or nearly).
			intPart*=10;
			floatPart1*=10;
			intPart+=floatPart1>>>32;
			floatPart1&=0xFFFFFFFFL;
			floatPart2*=10;
			floatPart1+=floatPart2>>>32;
			floatPart2&=0xFFFFFFFFL;
			floatPart3*=10;
			floatPart2+=floatPart3>>>32;
			floatPart3&=0xFFFFFFFFL;
			orderOfMagnitude--;
		}
		if(intPart>0xFFFFFFFFL) {
			// If we've gone too far, then scale back by a factor of 10.
			// floatPart2 is superfluous for this operation.
			floatPart1=(floatPart1+((intPart%10)<<32))/10;
			intPart/=10;
			orderOfMagnitude++;
		}
		result = (intPart<<32)|floatPart1;
		return new Quadruple(result,orderOfMagnitude,negative);
	}
	
	/**
	 * 
	 * @param q
	 * @return The result of addition.
	 */
	public Quadruple add(Quadruple q) {
		if(q.value==0)
			return this;
		if(value==0)
			return q;
		if(q.orderOfMagnitude>this.orderOfMagnitude)
			return q.add(this);
		long qValue = q.value;
		long qMagnitude = q.orderOfMagnitude;
		while(qMagnitude<this.orderOfMagnitude) {
			qValue>>>=1;
			qValue/=5;
			qMagnitude++;
		}
		long upperPartA = (value>>>32);
		long lowerPartA = (value&0xFFFFFFFFL);
		long upperPartB = (qValue>>>32);
		long lowerPartB = (qValue&0xFFFFFFFFL);
		boolean negative = this.negative;
		long newValueH;
		long newValueL;
		if(this.negative^q.negative) {
			if((upperPartB>upperPartA)
				||((upperPartB==upperPartA)&&(lowerPartB>lowerPartA))) {
				negative = !negative;
				long t = upperPartA;
				upperPartA = upperPartB;
				upperPartB = t;
				t = lowerPartA;
				lowerPartA = lowerPartB;
				lowerPartB = t;
			}
			
			if(lowerPartB>lowerPartA) {
				lowerPartA+=0x100000000L;
				upperPartA--;
			}
			newValueL=lowerPartA-lowerPartB;
			newValueH=upperPartA-upperPartB;
		}
		else {
			newValueL=lowerPartA+lowerPartB;
			newValueH=upperPartA+upperPartB;
			if(newValueL>=0x100000000L) {
				newValueL&=0xFFFFFFFFL;
				newValueH++;
			}
			if(newValueH>0xFFFFFFFFL) {
				long t = newValueH % 10;
				newValueH/=10;
				newValueL+=t<<32;
				newValueL/=10;
				orderOfMagnitude++;
			}
		}
		long newValue = (newValueH<<32) + newValueL;
		return new Quadruple(newValue,orderOfMagnitude,negative);
	}
	
	/**
	 * 
	 * @param q
	 * @return The result of subtraction.
	 */
	public Quadruple subtract(Quadruple q) {
		return this.add(new Quadruple(q.value,q.orderOfMagnitude,!q.negative));
	}

	@Override
	public boolean isZero() {
		return value==0;
	}

	@Override
	public boolean isNegative() {
		return negative;
	}
	
	/**
	 * 
	 * @return true if this quadruple is a whole number and false otherwise.
	 */
	public boolean isWholeNumber() {
		if(value==0)
			return true;
		long orderOfMagnitude = this.orderOfMagnitude;
		if(orderOfMagnitude<0)
			return false;
		long fractionalLong = value&0xFFFFFFFFL;
		while(fractionalLong!=0) {
			fractionalLong<<=1;
			fractionalLong&=0xFFFFFFFFL;
			orderOfMagnitude--;
		}
		return orderOfMagnitude>=0;
	}

	@Override
	public Quadruple sqrt() {
		if(negative)
			return null;
		if(value==0)
			return this;
		
		// Given that val is a 128 bit number comprised of valHigh=value and valLow=0:
		// m = orderOfMagnitude
		// n = val/(2^96) * 10^m
		// => sqrt(n)
		// = sqrt(val/(2^96) * 10^m)
		// = sqrt(val)/(2^48) * 10^(m/2)
		// Since m could be odd, we could end up with a stray sqrt(10) to calculate.
		// We could either factor out a 10 from val to make m even, or we could precalculate sqrt(10) and multiply the result by that constant.
		// Or we could split up the 10s into 2s and 5s and treat them separately, then in the even of odd m, we could do:
		// = sqrt(val*2/5)/(2^48) * 2^((m-1)/2) * 5^((m+1)/2)
		
		long valHigh = value;
		long valLow = 0;
		long order5 = this.orderOfMagnitude;
		long order2 = -48+order5/2;
		if(order5%2!=0) {
			if(order5<0)
				order2--;
			// val*=2/5
			long valLower = 0;
			if(valHigh%2>0)
				valLow = 0x8000000000L;
			valHigh>>>=1;
			valLow+=(valHigh%5)<<40;
			valHigh/=5;
			valLower=(valLow%5)<<40;
			valLow/=5;
			valHigh<<=2;
			valLow<<=2;
			valLower<<=2;
			valHigh+=valLow>>>40;
			valLow+=valLower>>>40;
			valLow<<=24;
			valLow+=(valLower&0xFFFFFF0000L)>>16;
			order5++;
		}
		order5/=2;
		
		long rHigh=0;
		long rLow=0;
		long bHigh=0x4000000000000000L;
		long bLow=0;
		long t,t2;
		long tHigh;
		long tLow;
		while(Long.compareUnsigned(bHigh,value)>0)
			bHigh>>>=2;
		while((bHigh|bLow)!=0) {
			// Temporary storage for r+b
			tHigh=rHigh+bHigh;
			tLow=rLow+bLow;
			if(((rLow&0x8000000000000000L)!=0)&&((tLow&0x8000000000000000L)==0)) {
				tHigh++;
			}
			t=Long.compareUnsigned(valHigh, tHigh);
			t2=Long.compareUnsigned(valLow, tLow);
			if((t>0)||((t==0)&&(t2>=0))/*val>=r+b*/) {
				// val = val - r - b;
				valHigh-=tHigh;
				if(t2<0)
					valHigh--;
				valLow-=tLow;
				
				rLow>>>=1;
				if(rHigh%2>0)
					rLow|=0x8000000000000000L;
				rHigh>>>=1;
				
				// r+=b;
				if(bHigh!=0) {
					rHigh+=bHigh;
				}
				else {
					t = rLow+bLow;
					if(((rLow&0x8000000000000000L)!=0)&&((t&0x8000000000000000L)==0))
						rHigh++;
					rLow = t;
				}
			}
			else {
				rLow>>>=1;
				if(rHigh%2>0)
					rLow|=0x8000000000000000L;
				rHigh>>>=1;
			}
			if(bHigh>0) {
				if(bHigh==1) {
					bLow = 0x4000000000000000L;
				}
				bHigh>>>=2;
			}
			else {
				bLow>>>=2;
			}
		}
		long order10 = order5;
		order2-=order5;
		// s = r * 2^order5 * 2^order10
		// We want 32 whole bits and 32 fractional bits. We have, instead an unknown number of whole bits and 48 or 47 fractional bits.
		int shift = (int)(64+order2);
		rHigh<<=shift;
		rHigh+=rLow>>>-order2;
		rLow<<=shift;
		long rLower = rLow&0xFFFFFFFFL;
		rLow>>>=32;
		while(rHigh<(0xFFFFFFFFL/10)) {
			rHigh*=10;
			rLow*=10;
			rLower*=10;
			rHigh+=rLow>>>32;
			rLow+=rLower>>>32;
			rLow&=0xFFFFFFFFL;
			rLower&=0xFFFFFFFFL;
			order10--;
		}
		if(rHigh>0xFFFFFFFFL) {
			rLow+=(rHigh%10)<<32;
			rHigh/=10;
			rLow/=10;
			order10++;
		}
		
		return new Quadruple((rHigh<<32)+rLow, order10, false);
	}

	@Override
	public Quadruple pow(Quadruple exponent) {
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
		
		/*
		 * Since the exponent will be expressed as
		 * val * 10^n
		 * 
		 * Then we are left with the result being
		 * (this^(val))^(10^n)
		 *
		 * If val has a margin of error, e, then the exponent is
		 * (val +/- e)*10^n
		 * 
		 * For the quadruple, we assume that e is 2^-32
		 * 
		 * If val has a margin of error, e, then the exponent is
		 * this^((val +/- e)*10^n)
		 * =(this^(val +/- e))^(10^n)
		 * =(this^(val)*this^(+/-e))^(10^n)
		 * =(this^(+/-e))^(10^n)*(this^(val))^(10^n)
		 * 
		 * => if (this^(+/-e))^(10^n)<10, then our final result is at least not off by an order of magnitude
		 * => this^(+/-e)<10^(10^-n)
		 * => log_10(this)*(+/-e)<10^-n
		 * => log_10(this)<(10^-n)*(2^32)
		 *    log_10(this)>-(10^-n)*(2^32)
		 * => abs(log_10(this))<(10^-n)*(2^32)
		 * 
		 */
		Quadruple threshold = new Quadruple(0x8000000000000000L/5,1-exponent.orderOfMagnitude,false);
		if(this.log10().getAbs().compareTo(threshold)>=0)
			throw new InvalidParameterException("Cannot calculate "+this+" raised to the power, "+exponent+". The error in the calculation would be more than an order of magnitude.");

		// First, plan to calculate the power base on a binary decomposition of the exponent
		// eg. this^(1011b) = this*this^2*this^4
		// and calculate this^1, this^2,... and this^-1, this^-2 
		LinkedList<Quadruple> powers = new LinkedList<>();
		Quadruple placePower = this;
		Quadruple placeMarker = getIdentity();
		Quadruple workingMarker = null;
		if(exponent.compareTo(ONE)>0) {
			while(placeMarker.compareTo(exponent)<=0) {
				workingMarker = placeMarker;
				powers.addFirst(placePower);
				placePower = placePower.multiply(placePower);
				placeMarker = placeMarker.multiply(TWO);
			}
		}
		placePower = this.sqrt();
		placeMarker = ONE.divide(TWO);
		if(!exponent.isWholeNumber()) {
			while(placeMarker.compareTo(exponent)>0) {
				placePower = placePower.sqrt();
				placeMarker = placeMarker.divide(TWO);
			}
			if(workingMarker==null) {
				workingMarker = placeMarker;
			}
			while(powers.size()<64) {
				powers.addLast(placePower);
				placePower = placePower.sqrt();
			}
		}
		
		// Now that we have all the necessary parts, use a binary decomposition of the exponent to identify all the parts we need to multiply together.
		Quadruple workingExponent = exponent;
		Quadruple retval = getIdentity();
		while(powers.size()>0) {
			Quadruple power = powers.removeFirst();
			if(workingMarker.compareTo(workingExponent)<=0) {
				retval = retval.multiply(power);
				workingExponent = workingExponent.subtract(workingMarker);
			}
			workingMarker = workingMarker.divide(TWO);
		}
		return retval;
	}
	
	/**
	 * 
	 * @return
	 */
	public Quadruple log10() {
		long retval = orderOfMagnitude;
		double tempVal = value>>>32;
		while(tempVal>=10) {
			tempVal/=10;
			retval++;
		}
		if(tempVal==1)
			return new Quadruple(retval);
		Quadruple q = new Quadruple(Math.log10(tempVal));
		Quadruple r = new Quadruple(retval);
		return r.add(q);
	}

	@Override
	public String toString() {
		if(value==0)
			return "0";
		long orderOfMagnitude = this.orderOfMagnitude;
		long wholeLong = value>>>32;
		long fractionalLong = value&0xFFFFFFFFL;
		if(fractionalLong==0) {
			while(wholeLong%10==0) {
				orderOfMagnitude++;
				wholeLong/=10;
			}
		}
		String signPart = negative?"-":"";
		String wholePart = ""+wholeLong;
		orderOfMagnitude+=wholePart.length()-1;
		String orderOfMagnitudePart = (orderOfMagnitude!=0)?("*10^"+orderOfMagnitude):"";
		if(fractionalLong==0) {
			if(wholePart.length()==1)
				return signPart+wholePart+orderOfMagnitudePart;
			return signPart+wholePart.substring(0,1)+"."+wholePart.substring(1)+orderOfMagnitudePart;
		}
		String fractionalPart = "";
		while(fractionalLong!=0) {
			fractionalLong*=10;
			fractionalPart+=(fractionalLong>>32);
			fractionalLong&=0xFFFFFFFFL;
		}
		return signPart+wholePart.substring(0,1)+"."+wholePart.substring(1)+fractionalPart+orderOfMagnitudePart;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (negative ? 1231 : 1237);
		result = prime * result + (int) (orderOfMagnitude ^ (orderOfMagnitude >>> 32));
		result = prime * result + (int) (value ^ (value >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Quadruple other = (Quadruple) obj;
		if (value != other.value)
			return false;
		if(value==0)
			return true;
		if (negative != other.negative)
			return false;
		if (orderOfMagnitude != other.orderOfMagnitude)
			return false;
		return true;
	}
}
