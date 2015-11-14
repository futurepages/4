package org.futurepages.util.templatizer.template.exceptions;

/**
 *
 * @author thiago
 */
public class TemplateWithSameNameAlreadyExistsException extends RuntimeException {
	
	public TemplateWithSameNameAlreadyExistsException(String msg) {
		super(msg);
	}
}
