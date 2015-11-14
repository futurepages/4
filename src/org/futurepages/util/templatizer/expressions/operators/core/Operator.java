package org.futurepages.util.templatizer.expressions.operators.core;

import org.futurepages.util.templatizer.expressions.primitivehandle.Infinity;
import org.futurepages.util.templatizer.expressions.primitivehandle.NaN;
import org.futurepages.util.templatizer.expressions.primitivehandle.NumHandle;
import org.futurepages.util.templatizer.expressions.tree.Exp;


/**
 *
 * @author thiago
 * 
 */
public abstract class Operator implements Exp {

	public Operator () {
	}
	
	public static boolean isBool(Object o) {
		return o instanceof Boolean;
	}

	public static boolean isNum(Object o) {
		return o instanceof Number && !(o instanceof NaN);
	}

	public static boolean isNegative(Object o) {
		if (!NumHandle.isInfinity(o)) {
			if (NumHandle.isDoubleOrFloat(o)) {
				Double d = NumHandle.floatToDouble(o);
				
				return d < 0;
			} else {
				Long l = NumHandle.intShortByteToLong(o);
				
				return l < 0;
			}
		} else {
			return ((Infinity)o).isNegative();
		}
	}
	
	@Override
	public abstract String toString();
	
	@Override
	public abstract void toString(StringBuilder sb);
}
