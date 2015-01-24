package modules.admin.model.exceptions;

//import modules.sistema.beans.Usuario;

/**
 *
 * @author leandro
 */
public class DisabledUserException extends RuntimeException{


	public DisabledUserException(){
		super("Usuário Inativado.");
	}


}
