package org.futurepages.errors;

import org.futurepages.exceptions.ErrorException;

/**
 *
 * @author leandro
 */
public class InvalidMailError extends ErrorException {

    public InvalidMailError(){
        super("O e-mail digitado é inválido");
    }

    public InvalidMailError(String msg){
        super(msg);
    }

}
