package modules.admin.model.exceptions;

/**
 *
 * @author leandro
 */
public class InvalidUserOrPasswordException extends Exception {
	public static final String ERROR_AUTHENTICATION = "Usuário e/ou senha inválida.";	

	@Override
	public String getMessage() {
		return ERROR_AUTHENTICATION;
	}
	
	
}
