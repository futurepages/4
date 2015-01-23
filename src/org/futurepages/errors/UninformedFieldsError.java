package org.futurepages.errors;

import org.futurepages.exceptions.ErrorException;

/**
 *
 * @author leandro
 */
public class UninformedFieldsError extends ErrorException {

    public UninformedFieldsError(){
        super("Preencha todos os campos obrigatórios");
    }

    public UninformedFieldsError(String msg){
        super(msg);
    }
}
