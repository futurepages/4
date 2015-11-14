package org.futurepages.menta.actions;

import org.futurepages.menta.core.action.AbstractAction;
import org.futurepages.menta.core.action.AuthenticationFree;
import org.futurepages.menta.core.action.RedirectAfterLogin;

/**
 * Action especializada em CRUD de um bean.
 * @author leandro
 */
public abstract class CrudActions extends AbstractAction implements RedirectAfterLogin, AuthenticationFree {

    @Override
    public String execute() {
        String type = getType();
        if (type == null) {
            type = SHOW;
        }
        if (type.equals(EXPLORE)) {
            listObjects();
        } else { //SHOW, CREATE, UPDATE, DELETE, ETC.
            if (type.equals(CREATE) || type.equals(UPDATE)) {
                doListDependencies();
            }
            if (!type.equals(CREATE)) { //UPDATE, DELETE, SHOW, ETC.
                restoreObject();
            }
            //not crud
            if (!type.equals(CREATE)
                    && !type.equals(SHOW)
                    && !type.equals(UPDATE)
                    && !type.equals(DELETE)) {

                fwdValue(TYPE);

            }
        }
        return type;
    }

	protected String getType() {
			Object value = input.getValue(TYPE);
			if (value != null && value instanceof String[]) {
				String[] array = (String[]) value;
				if (array.length > 0) {
					value = array[0];
				}
			}else if(value==null){
				value = getChain().getInnerAction();
			}
		return  (String) value;
	}

	@Override
    public void doListDependencies() {
        fwdValue(TYPE);
		super.doListDependencies();
    }

    protected void listObjects() {
    }

    protected void restoreObject() {
    }

    /**
    *  A exibição do bean é desprotegido.
    *  @param innerAction
    */
	@Override
    public boolean bypassAuthentication(String innerAction) {
        if (innerAction == null) {
            if (input.getStringValue(TYPE) == null) {
                return true;
            } else if (input.getStringValue(TYPE).equals(SHOW)) {
                return true;
            }
        }
        return false;
    }

    
    protected boolean bypassCrud(String innerAction, String crudType) {

        // cond1Pass: Action?type=crudType
        boolean cond1Pass = innerAction == null && input.getStringValue(TYPE).equals(crudType);
        // cond2Pass: Action-innerAction
        boolean cond2Pass = innerAction != null && innerAction.equals(crudType);

        return cond1Pass || cond2Pass;
    }

    /**
     * Os innerActions não redirecionam, somente o execute()
     */
	@Override
    public boolean shouldRedirect(String inner) {
        if (inner == null || inner.equals("execute")) {
            return true;
        }
        return false;
    }
}
