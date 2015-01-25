package modules.admin.model.validators;

import modules.admin.model.entities.Param;
import modules.admin.model.entities.enums.ParamValueType;
import org.futurepages.core.validation.Validator;

public class ParamValidator extends Validator {

    public void update(Param param) {
//        if (Is.empty(param.getVal())) { //nao necessário já que se trata
//            error("val", "Informe um valor para o parâmetro");
//        }
//		else
		if (param.getValueType() == ParamValueType.INT) {
            try {
                int a = Integer.parseInt(param.getVal());
            } catch (NumberFormatException ex) {
                error("val", "O valor que você tentou inserir não corresponde ao tipo");
            }
        } else if (param.getValueType() == ParamValueType.BOOLEAN) {
            if (!(param.getVal().equals("true") || param.getVal().equals("false"))) {
                error("val", "O valor que você tentou inserir não corresponde ao tipo");
            }
        } else if (param.getValueType() == ParamValueType.DOUBLE) {
            try {
                float a = Float.parseFloat(param.getVal());
            } catch (NumberFormatException ex) {
                error("val", "O valor que você tentou inserir não corresponde ao tipo");
            }
        }
		
		validate();
    }

}
