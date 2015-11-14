package org.futurepages.util.templatizer.expressions.operators.logical;

import org.futurepages.util.templatizer.expressions.operators.core.BinaryOperator;
import org.futurepages.util.templatizer.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class GreaterThan extends BinaryOperator {
	
	@Override
	public String toString() {
		return ">";
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);

		return !LessEqualsThan.execute(left, right);
	}
}
