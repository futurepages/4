package org.futurepages.util.template.simpletemplate.expressions.exceptions;

/**
 *
 * @author thiago
 */
public class BadExpression extends ExpressionException {

	public BadExpression(String msg) {
		super(msg);
	}
	
	public BadExpression(String expression, int index, Object ...msg) {
		super(expression, index, concat(msg));
	}
}
