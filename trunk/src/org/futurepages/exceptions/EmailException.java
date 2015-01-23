package org.futurepages.exceptions;

// Revision: 191951

/**
 * EmailException
 * @author jakarta-commons
 */
public class EmailException extends Exception {

    public EmailException() {
        super();
    }

    public EmailException(String msg) {
        super(msg);
    }

    public EmailException(String msg, Throwable cause) {
        super(msg, cause);
        this.setStackTrace(cause.getStackTrace());
    }

    public EmailException(Throwable cause) {
        super(cause);
        this.setStackTrace(cause.getStackTrace());
    }

}
