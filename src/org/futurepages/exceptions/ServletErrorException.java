
package org.futurepages.exceptions;

import javax.servlet.ServletException;

/**
 *
 * @author leandro
 */
public class ServletErrorException extends ServletException {

	public ServletErrorException(String message) {
		super(message);
	}

}
