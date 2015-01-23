package org.futurepages.exceptions;

/**
 *
 * @author leandro
 */
public class ValidationException  extends RuntimeException{

    public ValidationException() {
    }

    public ValidationException(String msg) {
        super(msg);
    }
}
