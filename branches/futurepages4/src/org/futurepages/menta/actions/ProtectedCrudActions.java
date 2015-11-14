package org.futurepages.menta.actions;

/**
 * Action que necessita de autenticação para ser acessada.
 * @author leandro
 */
public abstract class ProtectedCrudActions extends CrudActions {


    @Override
    public boolean bypassAuthentication(String inner){
        return false;
    }

}