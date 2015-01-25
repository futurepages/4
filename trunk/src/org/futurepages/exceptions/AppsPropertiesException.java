package org.futurepages.exceptions;

public class AppsPropertiesException extends RuntimeException{

	public AppsPropertiesException(String msg, Exception e){
		super(msg,e);
	}

}
