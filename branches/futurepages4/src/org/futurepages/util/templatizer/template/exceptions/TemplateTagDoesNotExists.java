package org.futurepages.util.templatizer.template.exceptions;

/**
 *
 * @author thiago
 */
public class TemplateTagDoesNotExists extends RuntimeException {
	public TemplateTagDoesNotExists(String msg) {
		super(msg);
	}
}
