package modules.admin.validators;

import modules.admin.beans.User;
import org.futurepages.core.validation.Validator;
import org.futurepages.errors.UninformedFieldsError;
import org.futurepages.util.Is;

/**
 *
 * @author leandro
 */
public class LoginValidator extends Validator {

    public void eval(User user){
        if (Is.empty(user.getAccessKey()) || Is.empty(user.getPlainPassword())) {
            error(new UninformedFieldsError());
        }
    }
}