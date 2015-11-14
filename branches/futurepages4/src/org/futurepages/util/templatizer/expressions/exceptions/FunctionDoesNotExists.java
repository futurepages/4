package org.futurepages.util.templatizer.expressions.exceptions;

/**
 *
 * @author thiago
 */
public class FunctionDoesNotExists extends ExpressionException {
	public FunctionDoesNotExists(String msg) {
		super(msg);
	}
	
	public FunctionDoesNotExists(String expression, int index, Object ...msg) {
		super(expression, index, concat(msg));
	}
}


