package org.futurepages.util.templatizer.expressions.exceptions;

/**
 *
 * @author thiago
 */
public class Unexpected extends ExpressionException {

	public Unexpected(String msg) {
		super(msg);
	}
	
	public Unexpected(String expression, int index, Object ...msg) {
		super(expression, index, concat(msg));
	}
}
