package org.futurepages.util.templatizer.expressions.operators.logical;

import org.futurepages.util.templatizer.expressions.operators.core.BinaryOperator;
import org.futurepages.util.templatizer.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class Or extends BinaryOperator {
	
	public static Object case1(Object l_boolean, Object r_boolean) {
		return ((Boolean)l_boolean) || ((Boolean)r_boolean);
	}

	public static Object case2(Object l_boolean, Object r_object) {
		return (l_boolean != null && (Boolean)l_boolean) ? l_boolean : r_object;
	}

	public static Object case3(Object l_object, Object r_boolean) {
		return (l_object != null) ? l_object : r_boolean;
	}

	public static Object case4(Object l_object, Object r_object) {
		return (l_object != null) ? l_object : r_object;
	}
	
	public static Object execute(Object left, Object right) {
		if (isBool(left)) {
			if (isBool(right)) {
				return case1(left, right);
			} else {
				return case2(left, right);
			}
		} else {
			if (isBool(right)) {
				return case3(left, right);
			} else {
				return case4(left, right);
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
		return "||";
	}
}
