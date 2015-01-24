package modules.admin.model.exceptions;

/**
 *
 * @author TJPI
 */
public class ExpiredPasswordException extends Exception {
	public static final String ERROR_EXPIRED_PASSWORD = "Senha expirada. Contate a administração do sistema.";
	
	@Override
	public String getMessage() {
		return ERROR_EXPIRED_PASSWORD;
	}
	
}
