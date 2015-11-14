package org.futurepages.menta.filters;

import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.core.input.Input;
import org.futurepages.util.EnumerationUtil;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.The;

import java.util.List;

/**
 * Injeta uma coleção de objetos persistentes
 * Similar ao {@link PIFilter}, para uma lista
 * @author diogenes
 */
public class PLIFilter extends HQLProvider implements Filter {

    private Class classToInject;   //classe dos objetos a serem injetados
    private String keyToInject;  //chave do input a ser injetado
    private String targetKey;   //chave do input do objeto que sofrerá a injeção

    public PLIFilter(String targetKey, String keyToInject, Class classToInject) {
        this.targetKey = targetKey;
        this.classToInject = classToInject;
        this.keyToInject = keyToInject;

    }

	@Override
    public String filter(InvocationChain chain) throws Exception {
        Input input = chain.getAction().getInput();
        String[] keyValues = input.getStringValues(keyToInject);
        String[] explodedTarget = The.explodedToArray(targetKey, ".");
        Object targetObject = input.getValue(explodedTarget[0]);
        if (explodedTarget.length > 1) {
            for (int i = 1; i < explodedTarget.length; i++) {
                targetObject = ReflectionUtil.getField(targetObject, explodedTarget[i]);
            }
        }

        ReflectionUtil.setField(targetObject, keyToInject, keyObjects(keyValues, classToInject));

        return chain.invoke();
    }

	@Override
    public void destroy() {
    }

    private static List keyObjects(String[] keyValues, Class classToInject) {
        if (keyValues != null) {
	        if (classToInject.isEnum()) {
		        return EnumerationUtil.listByKeys(classToInject, keyValues);
	        } else {
		        return Dao.getInstance().list(hql(classToInject, field(Dao.getInstance().getIdName(classToInject)).in(keyValues)));
	        }
        }
        return null;
    }
}