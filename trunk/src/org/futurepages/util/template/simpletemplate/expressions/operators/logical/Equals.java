package org.futurepages.util.template.simpletemplate.expressions.operators.logical;

import org.futurepages.util.template.simpletemplate.expressions.operators.core.BinaryOperator;
import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.Const;
import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.NumHandle;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class Equals extends BinaryOperator {
	
	@Override
	public String toString() {
		return "==";
	}
	
	public static boolean execute(Object left, Object right) {
		if (left != null && left != Const.NULL) {
			if (right != null && right != Const.NULL) {
				if (isNum(left) && isNum(right)) {
					Number [] nums = NumHandle.toLongOrDouble(left, right);

					return nums[0].equals(nums[1]);
				} else {
					return left.equals(right);
				}
			} else {
				return false;
			}
		} else {
			if (right != null && right != Const.NULL) {
				return false;
			} else {
				return true;
			}
		}
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);

		return execute(left, right);
	}
}
