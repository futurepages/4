package org.futurepages.util.template.simpletemplate.expressions.operators.logical;

import org.futurepages.util.template.simpletemplate.expressions.operators.core.BinaryOperator;
import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.Infinity;
import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.NumHandle;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;


/**
 *
 * @author thiago
 */
public class LessThan extends BinaryOperator {
	
	protected static boolean lessThan(Object l, Object r) {
		Number [] nums = NumHandle.toLongOrDouble(l, r);
		
		if (NumHandle.isDouble(nums[0])) {
			return ((Double)nums[0]) < ((Double)nums[1]);
		} else { // Long
			return ((Long)nums[0]) < ((Long)nums[1]);
		}
	}
	
	protected static boolean _lessThan(Number l, Number r) {
		if (NumHandle.isInfinity(l)) {
			if (NumHandle.isInfinity(r)) {
				return l.equals(r)
					? false
					: ((Infinity)l).isNegative();
			} else {
				return ((Infinity)l).isNegative();
			}
		} else if (NumHandle.isInfinity(r)) {
			return !((Infinity)r).isNegative();
		} else {
			return lessThan(l, r);
		}
	}
	
	public static boolean execute(Object l, Object r) {
		if (l == null) {
			if (r == null) {
				return false;
			} else {
				return true;
			}
		}

		if (isNum(l)) {
			if(isNum(r)) {
				return _lessThan((Number) l, (Number) r);
			} else {
				return true;
			}
		} else {
			if (isNum(r)) {
				return false;
			} else {
				return false;
			}
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
		return "<";
	}
}
