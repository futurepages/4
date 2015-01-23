package org.futurepages.util.template.simpletemplate.expressions.exceptions;

/**
 *
 * @author thiago
 */
public class ExpectedOperator extends ExpressionException {
	
	public ExpectedOperator(String msg) {
		super(msg);
	}
	
	public ExpectedOperator(String expression, int index, Object ...msg) {
		super(expression, index, concat(msg));
	}
}
