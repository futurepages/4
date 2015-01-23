package org.futurepages.util.template.simpletemplate.expressions.exceptions;

/**
 *
 * @author thiago
 */
public abstract class ExpressionException extends Exception {

	public static String concat(Object ...objs) {
		StringBuilder sb = new StringBuilder();
		
		for (Object obj : objs) {
			sb.append(obj.toString());
		}

		return sb.toString();
	}

	private int index;
	private String expression;
	private String pointError;
	
	public ExpressionException(String expression, int index, String msg) {
		this(msg);
		this.expression = expression;
		this.index = index;
	}
	
	public ExpressionException() {
		super();
	}

	public ExpressionException(String msg) {
		super(msg);
	}

	public String show() {
		
		if (pointError == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(expression).append("\n");
			for (int i = 0; i < index; i++) {
				sb.append(" ");
			}
			
			pointError = sb.append("^\n").toString();
		}

		return pointError;
	}
}
