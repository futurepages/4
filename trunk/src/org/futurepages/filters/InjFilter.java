package org.futurepages.filters;

import org.futurepages.util.ReflectionUtil;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.Input;
import org.futurepages.core.control.InvocationChain;

/**
 * Injection Filter - Injeta um objeto de uma chave no objeto de outra chave
 * @author leandro
 */
public class InjFilter implements Filter {

    private String keyToInject;   //chave do input a ser injetado
    private String targetKey;    //chave do input do objeto que sofrerá a injeção

    public InjFilter(String targetKey, String keyToInject) {
        this.targetKey = targetKey;
        this.keyToInject = keyToInject;
    }

    public String filter(InvocationChain chain) throws Exception {
        Input input = chain.getAction().getInput();
        Object objectToInject = input.getValue(keyToInject);
        Object targetObject = input.getValue(targetKey);
        ReflectionUtil.setField(targetObject, keyToInject, objectToInject);
        return chain.invoke();
    }

    public void destroy() {
    }
}