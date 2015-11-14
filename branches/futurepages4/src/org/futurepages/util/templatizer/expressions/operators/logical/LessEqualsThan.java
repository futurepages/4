package org.futurepages.util.templatizer.expressions.operators.logical;

import org.futurepages.util.templatizer.expressions.operators.core.BinaryOperator;
import org.futurepages.util.templatizer.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class LessEqualsThan extends BinaryOperator {
	
	public static boolean execute(Object l, Object r) {
		return LessThan.execute(l, r) || Equals.execute(l, r);
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);

		return execute(left, right);
	}

	@Override
	public String toString() {
		return "<=";
	}
}
