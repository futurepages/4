package org.futurepages.util.templatizer.expressions.operators.logical;

import org.futurepages.util.templatizer.expressions.operators.core.BinaryOperator;
import org.futurepages.util.templatizer.util.ContextTemplateTag;


/**
 *
 * @author thiago
 */
public class Xor extends BinaryOperator {

	@Override
	public Object eval(ContextTemplateTag context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);
		
		return execute(left, right);
	}
	
	public static Object execute(Object left, Object right) {
		if (isBool(left) && isBool(right)) {
				return ((Boolean)left) ^ ((Boolean)right);
		} else {
			Object l = And.execute(left, Not.execute(right));
			Object r = And.execute(Not.execute(left), right);

			return Or.execute(l, r);
		}
	}

	@Override
	public String toString() {
		return "^";
	}
}
