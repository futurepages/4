package org.futurepages.errors;

import org.futurepages.exceptions.ErrorException;

/**
 *
 * @author leandro
 */
public class NullFileItemError extends ErrorException {

    public NullFileItemError(){
        super("Especifique o arquivo a ser enviado");
    }

    public NullFileItemError(String msg){
        super(msg);
    }

}
