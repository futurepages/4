package org.futurepages.util.template.simpletemplate.expressions.operators.numerical;

import org.futurepages.util.template.simpletemplate.expressions.operators.core.BinaryOperator;
import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.Const;
import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.NumHandle;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;


/**
 *
 * @author thiago
 */
public class Mult extends BinaryOperator {
	
	public static Number execute(Object l, Object r) {
		if (isNum(l) && isNum(r)) {
			if (NumHandle.isInfinity(l) || NumHandle.isInfinity(r)) {
				boolean nl = isNegative(l);
				boolean nr = isNegative(r);

				return !(nl ^ nr) ? Const.INFINITY : Const._INFINITY;
			} else {
				return mult(l, r);
			}
		} else {
			return Const.NAN;
		}
	}

	protected static Number mult(Object l, Object r) {
		Number [] nums = NumHandle.toLongOrDouble(l, r);

		if (NumHandle.isDouble(nums[0])) {
			return ((Double)nums[0]) * ((Double)nums[1]);
		} else { // Long
			return ((Long)nums[0]) * ((Long)nums[1]);
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
		return "*";
	}
}
