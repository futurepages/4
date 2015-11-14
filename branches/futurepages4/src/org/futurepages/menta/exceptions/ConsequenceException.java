package org.futurepages.menta.exceptions;


/**
 * An exception that can happen when a consequence is executed.
 * 
 * @author Sergio Oliveira
 */
public class ConsequenceException extends Exception {
	
	public ConsequenceException() {
		super();
	}
	
	public ConsequenceException(Exception e) {
		super(e);
		this.setStackTrace(e.getStackTrace());
	}
	
	public ConsequenceException(String msg) {
		super(msg);
	}

	public ConsequenceException(String msg, Exception e) {
		super(msg, e);
	}
}
