package org.futurepages.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.Input;
import org.futurepages.core.control.InvocationChain;

import org.futurepages.util.Is;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.The;

public class CollectionOfElementsFilter implements Filter {

	private String beanPath;
	private String keyToInject;

	public CollectionOfElementsFilter(String beanPath, String keyToInject) {
		this.beanPath = beanPath;
		this.keyToInject = keyToInject;
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		Input input = chain.getAction().getInput();
		Collection col = null;
		Object inputValue = input.getValue(keyToInject);
		if (inputValue!=null) {
			String toStringValue = input.getValue(keyToInject).toString();
			String[] array = null;
			if (inputValue instanceof String) {
				array = new String[]{toStringValue};
			} else if (inputValue instanceof String[]) {
				array = (String[]) inputValue;
			} else {
				return null;
			}
			Class beanClass = input.getValue(The.firstTokenOf(beanPath, ".")).getClass();
			Class targetType = ReflectionUtil.getFieldType(beanClass, keyToInject);
			if (List.class.isAssignableFrom(targetType)) {
				col = new ArrayList();
			} else if (Set.class.isAssignableFrom(targetType)) {
				col = new LinkedHashSet();
			} else {
				col = null;
			}
			if (col != null) {
				for (String element : array) {
					if (!Is.empty(element)) {
						col.add(element);
					}
				}
			}
		}

		inject(input, col);
		return chain.invoke();
	}

	/**
	 * Se o alvo é targetKey é não nulo então:
	 * <li> se o objeto já existe no input, injeta-se o valor encontrato em tal objeto:setField( obj, targetObjetc)
	 * <li> se o objeto ainda não existe no input: input.setValue(targetKey, obj)
	 * 	
	 * @param input
	 * @param objectValue
	 */
	private void inject(Input input, Object objectValue) {
		String[] explodedTarget = The.explodedToArray(beanPath, ".");
		Object targetObject = input.getValue(explodedTarget[0]);
		setField(objectValue, explodedTarget, targetObject);
	}

	private void setField(Object objectValue, String[] explodedTarget, Object targetObject) {
		if (explodedTarget.length > 1) {
			for (int i = 1; i < explodedTarget.length; i++) {
				targetObject = ReflectionUtil.getField(targetObject, explodedTarget[i]);
			}
		}
		ReflectionUtil.setField(targetObject, keyToInject, objectValue);
	}

	@Override
	public void destroy() {
	}
}
