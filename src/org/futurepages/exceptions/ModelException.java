
package org.futurepages.exceptions;

/**
 * Exception genérica de modelo. Lance-o de seu modelo, para prevenir mal-tratamento de exceptions que não são do modelo.
 * @author leandro
 */
public class ModelException extends Exception {

	public ModelException(){}

	public ModelException(String msg){
		super(msg);
	}
}
