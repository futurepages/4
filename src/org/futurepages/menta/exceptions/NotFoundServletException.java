package org.futurepages.menta.exceptions;

import javax.servlet.ServletException;

public class NotFoundServletException extends ServletException {

	public NotFoundServletException(String msg){
		super(msg);
	}

}
