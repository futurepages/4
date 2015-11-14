package org.futurepages.menta.exceptions;

import org.jdom.JDOMException;

public class BadFormedConfigFileException extends RuntimeException {

	public BadFormedConfigFileException(String message) {
		super(message);
	}

	public BadFormedConfigFileException(JDOMException ex) {
		super(ex);
	}
	
}
