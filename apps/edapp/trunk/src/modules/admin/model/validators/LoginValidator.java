package modules.admin.model.validators;

import modules.admin.model.entities.User;
import org.futurepages.core.validation.Validator;
import org.futurepages.util.Is;

/**
 *
 * @author leandro
 */
public class LoginValidator extends Validator {

    public void eval(User user){
        if (Is.empty(user.getAccessKey()) || Is.empty(user.getPlainPassword())) {
            error("Preencha todos os campos obrigatórios");
        }
    }
}