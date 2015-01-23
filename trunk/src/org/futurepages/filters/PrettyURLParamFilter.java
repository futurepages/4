package org.futurepages.filters;

import org.futurepages.core.action.Action;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.Input;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.control.PrettyURLController;

/**
 * A Filter for injecting in the action input the parameters supplied by the
 * PrettyURLController in separated attributes.
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 * 
 * @see PrettyURLController
 */
public class PrettyURLParamFilter implements Filter {

	private String[] paramsOrder;

	public PrettyURLParamFilter(String ... paramOrder) {
		this.paramsOrder = paramOrder;
	}

	public void destroy() {

	}

	public String filter(InvocationChain chain) throws Exception {
		
		Action action = chain.getAction();
		
		Input input = action.getInput();

		for (int i = 0; i < paramsOrder.length; i++) {
			
			String key = String.valueOf(i);
			
			if (!input.hasValue(key)) break;
			
			input.setValue(paramsOrder[i], input.getValue(key));
			
			input.removeValue(key);
		}
		
		return chain.invoke();
	}

}
