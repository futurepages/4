package org.futurepages.util.template.simpletemplate.template.exceptions;

/**
 *
 * @author thiago
 */
public class TemplateNestingException extends Exception {
	
	private int line;

	public TemplateNestingException(String msg) {
		super(msg);
		this.line = 0;
	}

	public TemplateNestingException(String msg, int line) {
		super(msg);
		this.line = line;
	}

	public int getLine() {
		return line;
	}
}
