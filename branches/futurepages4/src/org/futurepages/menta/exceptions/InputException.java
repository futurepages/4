package org.futurepages.menta.exceptions;


/**
 * An exception than can happen when trying to access an action input parameter.
 * 
 * @author Sergio Oliveira
 */
public class InputException extends RuntimeException {

	public InputException() {
		super();
	}
	
	public InputException(Exception e) {
		super(e);
		this.setStackTrace(e.getStackTrace());
	}
	
	public InputException(String msg) {
		super(msg);
	}
}	
