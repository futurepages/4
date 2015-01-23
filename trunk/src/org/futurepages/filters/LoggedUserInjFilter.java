package org.futurepages.filters;

import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.Input;
import org.futurepages.core.control.InvocationChain;

public class LoggedUserInjFilter implements Filter{
	
	/** chave do input a ser injetado */
	private String targetKey;   
	
	/** nome do atributo a ser injetado */
	private String keyToInject;

	/**
	 * input.setValue(targetKey, loggedUser())
	 * @param targetKey chave do input a ser povoado
	 */
	public LoggedUserInjFilter(String targetKey) {
		this(targetKey,null);
	}
	
	/**
	 * targetKey.setKeyToInject( loggedUser())
	 * @param targetKey chave do input a ser povoado
	 * @param keyToInject nome do atributo a ser injetado
	 */
    public LoggedUserInjFilter(String targetKey, String keyToInject) {
        this.targetKey = targetKey;
        this.keyToInject = keyToInject;
    }

	@Override
	public String filter(InvocationChain chain) throws Exception {
        AbstractAction action = (AbstractAction) chain.getAction();
		Input input = action.getInput();
        
		DefaultUser user = action.loggedUser();
        Object targetObject = input.getValue(targetKey);
        
        if(keyToInject != null)
        	ReflectionUtil.setField(targetObject, keyToInject, user);
        else{
        	input.setValue(targetKey, user);
        }
        return chain.invoke();
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
