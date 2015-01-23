package org.futurepages.util.template.simpletemplate.expressions.operators.logical;

import org.futurepages.util.template.simpletemplate.expressions.operators.core.BinaryOperator;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;


/**
 *
 * @author thiago
 */
public class NotEquals extends BinaryOperator {

	public static boolean execute(Object l, Object r) {
		return !Equals.execute(l, r);
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);

		return execute(left, right);
	}

	@Override
	public String toString() {
		return "!=";
	}
}
