package org.futurepages.util.template.simpletemplate.expressions.exceptions;

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
