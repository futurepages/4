package org.futurepages.util.templatizer.expressions.exceptions;

/**
 *
 * @author thiago
 */
public class ExpectedExpression extends ExpressionException {

	public ExpectedExpression(String msg) {
		super(msg);
	}
	
	public ExpectedExpression(String expression, int index, Object ...msg) {
		super(expression, index, concat(msg));
	}
}
