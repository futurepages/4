package org.futurepages.util.template.simpletemplate.template.exceptions;

/**
 *
 * @author thiago
 */
public class TemplateWithSameNameAlreadyExistsException extends RuntimeException {
	
	public TemplateWithSameNameAlreadyExistsException(String msg) {
		super(msg);
	}
}
