package org.futurepages.util.templatizer.expressions.operators.unevaluable;

import org.futurepages.util.templatizer.expressions.operators.core.Operator;
import org.futurepages.util.templatizer.util.ContextTemplateTag;


/**
 *
 * @author thiago
 */
public abstract class Parenthesis extends Operator {

	@Override
	public Object eval(ContextTemplateTag context) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
