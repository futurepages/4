package org.futurepages.exceptions;

import org.jdom.JDOMException;

public class FuturepagesPropertiesException extends RuntimeException{

	public FuturepagesPropertiesException(String msg, Exception e){
		super(msg,e);
	}

}
