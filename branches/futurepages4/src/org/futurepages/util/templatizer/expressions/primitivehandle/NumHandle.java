package org.futurepages.util.templatizer.expressions.primitivehandle;

/**
 *
 * @author thiago
 */
public class NumHandle {

	public static boolean equals(Number n1, Number n2) {
		return n1.equals(n2);
	}

	public static Long intShortByteToLong(Object o) {
		return ((Number) o).longValue();
	}

	public static Double longIntShortByteToDouble(Object o) {
		return ((Number) o).doubleValue();
	}
	
	public static Double floatToDouble(Object o) {
		if (o instanceof Double) {
			return (Double)o;
		}

		return new Double((Float)o);
	}
	
	public static boolean isFloat(Object o) {
		return o instanceof Float;
	}

	public static boolean isDouble(Object o) {
		return o instanceof Double;
	}

	public static boolean isDoubleOrFloat(Object o) {
		return o instanceof Double || o instanceof Float;
	}

	public static boolean isLongOrIntOrShortOrByte(Object o) {
		return o instanceof Long || o instanceof Integer || o instanceof Short || o instanceof Byte;
	}
	
	public static boolean isInfinity(Object o) {
		return o instanceof Infinity;
	}

	public static boolean isInfinity(Object l, Object r) {
		return l instanceof Infinity || l instanceof Infinity;
	}
	
	public static Number toLongOrDouble(Object num) {
		if (isDoubleOrFloat(num)) {
			Double d = floatToDouble(num);
			return d;
		} else {
			Long l = intShortByteToLong(num);
			return l;
		}
	}
	
	public static Number [] toLongOrDouble(Object l, Object r) {
		if (isDoubleOrFloat(l)) {
			if (isDoubleOrFloat(r)) {
				Double dl = floatToDouble(l);
				Double dr = floatToDouble(r);

				return new Number[] { dl, dr };
			} else if (isLongOrIntOrShortOrByte(r)) {
				Double dl = floatToDouble(l);
				Double dr = longIntShortByteToDouble(r);

				return new Number[] { dl, dr };
			} else { // Infinity
				Double dl = floatToDouble(l);
				
				return new Number[] {dl, (Infinity)r};
			}
		} else if (isLongOrIntOrShortOrByte(l)) {
			if (isDoubleOrFloat(r)) {
				Double dl = longIntShortByteToDouble(l);
				Double dr = floatToDouble(r);

				return new Number[] { dl, dr };
			} else if (isLongOrIntOrShortOrByte(r)) {
				Long ll = intShortByteToLong(l);
				Long lr = intShortByteToLong(r);

				return new Number[] { ll, lr };
			} else { // Infinity
				Long ll = intShortByteToLong(l);
				
				return new Number[] {ll, (Infinity)r};
			}
		} else { // Infinity
			if (isDoubleOrFloat(r)) {
				Double dr = floatToDouble(r);
				
				return new Number[] {(Infinity)l, dr};
			} else if (isLongOrIntOrShortOrByte(r)) {
				Long lr = intShortByteToLong(r);
				
				return new Number[] {(Infinity)l, lr};
			} else { // Infinity
				return new Number[] {(Infinity)l, (Infinity)r};
			}
		}
	}
	
	public static boolean isZero(Number obj) {
		if (obj instanceof Integer) {
			return (Integer)obj == 0;
		} else if (obj instanceof Long) {
			return (Long)obj == 0;
		} else if (obj instanceof Short) {
			return (Short)obj == 0;
		} else if (obj instanceof Byte) {
			return (Byte)obj == 0;
		} else if (obj instanceof Float) {
			return (Float)obj == 0.0f;
		} else { // Double
			return (Double)obj == 0.0d;
		}
	}
}
