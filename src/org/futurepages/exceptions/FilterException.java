package org.futurepages.exceptions;


/**
 * An exception that can happen when a filter is executed.
 * 
 * @author Sergio Oliveira
 */
public class FilterException extends Exception {
	
	protected final Throwable rootCause;
	
	public FilterException() {
		super();
		
		this.rootCause = null;
	}
	
	public FilterException(Throwable e) {
		super(getMsg(e), e);
		
		Throwable root = getRootCause(e);
		
		this.setStackTrace(root.getStackTrace());
		
		this.rootCause = root == this ? null : root;
	}
	
	private static String getMsg(Throwable t) {
		
		Throwable root = getRootCause(t);
		
		String msg = root.getMessage();
		
		if (msg == null || msg.length() == 0) {
			
			msg = t.getMessage();
			
			if (msg == null || msg.length() == 0) return root.getClass().getName();
		}
		
		return msg;
	}
	
	
	public FilterException(String msg) {
		super(msg);
		
		this.rootCause = null;
	}
	
	public FilterException(String msg, Throwable e) {
		
		super(msg, e);
		
		Throwable root = getRootCause(e);
		
		this.setStackTrace(root.getStackTrace());
		
		this.rootCause = root == this ? null : root;
	}
	
	private static Throwable getRootCause(Throwable t) {
		
		Throwable root = t.getCause();
		
		if (root == null) return t;
		
		while(root.getCause() != null) {
			
			root = root.getCause();
		}
		
		return root;
		
	}	
	
	@Override
	public Throwable getCause() {
		
		return rootCause;
	}
	
}
