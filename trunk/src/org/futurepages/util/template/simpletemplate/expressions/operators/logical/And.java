package org.futurepages.util.template.simpletemplate.expressions.operators.logical;

import org.futurepages.util.template.simpletemplate.expressions.operators.core.BinaryOperator;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class And extends BinaryOperator {

	public static Object case1(Object l_boolean, Object r_boolean) {
		return ((Boolean)l_boolean) && ((Boolean)r_boolean);
	}

	public static Object case2(Object l_boolean, Object r_object) {
		if (l_boolean != null) {
			if ((Boolean)l_boolean) {
				return r_object != null ? r_object : null;
			}

			return false;
		}
		
		return null;
	}

	public static Object case3(Object l_object, Object r_boolean) {
		return (l_object != null && r_boolean != null) ? r_boolean : null;
	}

	public static Object case4(Object l_object, Object r_object) {
		return (l_object != null && r_object != null) ? r_object : null;
	}
	
	public static Object execute(Object left, Object right) {
		if (isBool(left)) {
			if (isBool(right)) {
				return case1(left, right);
			} else {
				return case2(left, right);
			}
		} else { // isBool(left) -> false
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
		return "&&";
	}
}
