package org.futurepages.util.template.simpletemplate.expressions.operators.unevaluable;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.Operator;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;


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
