package org.futurepages.util.templatizer.expressions.operators.numerical;

import org.futurepages.util.templatizer.expressions.operators.core.BinaryOperator;
import org.futurepages.util.templatizer.expressions.primitivehandle.Const;
import org.futurepages.util.templatizer.expressions.primitivehandle.Infinity;
import org.futurepages.util.templatizer.expressions.primitivehandle.NumHandle;
import org.futurepages.util.templatizer.util.ContextTemplateTag;


/**
 *
 * @author thiago
 */
public class Power extends BinaryOperator {
	
	public static Number execute(Object l, Object r) {
		if (isNum(l) && isNum(r)) {
			if (NumHandle.isInfinity(l)) {
				if (NumHandle.isInfinity(r)) {
					return ((Infinity)r).isNegative() ? 0 : Const.INFINITY;
				} else {
					return Const.INFINITY;
				}
			} else if (NumHandle.isInfinity(r)) {
				return ((Infinity)r).isNegative() ? 0 : Const.INFINITY;
			} else {
				Number n = power(l, r);
				
				if (Double.isNaN((Double)n)) {
					return Const.NAN;
				} else if (Double.isInfinite((Double)n)) {
					return Const.INFINITY;
				} else {
					return n;
				}
			}
		} else {
			return Const.NAN;
		}
	}

	protected static Number power(Object l, Object r) {
		Number [] nums = NumHandle.toLongOrDouble(l, r);

		if (NumHandle.isDouble(nums[0])) {
			return Math.pow(((Double)nums[0]), ((Double)nums[1]));
		} else { // Long
			return Math.pow(new Double((Long)nums[0]), new Double((Long)nums[1]));
		}
	}
	
	
	@Override
	public Object eval(ContextTemplateTag context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);

		return execute(left, right);
	}
	
	@Override
	public String toString() {
		return "**";
	}
}
