package org.futurepages.menta.filters;

import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.core.output.Output;
import org.futurepages.menta.core.output.OutputWrapper;

import java.lang.reflect.Method;

/**
 * A filter that takes all the properties of the action and place them in the output,
 * so that you don't have to call output.setValue() manually.
 * 
 * Note that for plain ations this filter uses the method <i>action.getClass().getDeclaredMethods()</i> 
 * in order to find the getters, in other words,
 * it will only find getters of the base class and not from its superclasses. This is ok because
 * action inheritance is not very common.
 * 
 * If you are using a POJO action or a model-driven action it will use the method getMethods instead.
 * 
 * For ModelDriven actions, the method <i>action.getModel().getClass().getMethods()</i> is used instead, 
 * pretty much like in the OVFilter.
 * 
 * @author Sergio Oliveira
 */
public class OutjectionFilter extends OutputWrapper implements Filter {

	private ThreadLocal<Action> action = new ThreadLocal<Action>();

	/**
	 * Creates a OutjectionFilter.
	 */
	public OutjectionFilter() {
	}

	private String adjustName(String name) {
		StringBuffer sb = new StringBuffer(name.length() - 3);
		sb.append(name.substring(3, 4).toLowerCase());
		sb.append(name.substring(4, name.length()));
		return sb.toString();
	}

	public String filter(InvocationChain chain) throws Exception {

		String result = chain.invoke();

		Action action = chain.getAction();
		Output output = action.getOutput();

		Method[] methods = null;

		methods = action.getClass().getDeclaredMethods();

		for (int i = 0; i < methods.length; i++) {
			String name = methods[i].getName();
			if (name.length() > 3 && name.startsWith("get") && methods[i].getParameterTypes().length == 0) {

				if (name.equals("getClass")) {
					continue;
				}

				try {
					methods[i].setAccessible(true);

					Object value = null;

					value = methods[i].invoke(action, new Object[0]);

					output.setValue(adjustName(name), value);

				} catch (Exception e) {
					throw new Exception(e);
				}
			}
		}
		return result;
	}

	public void destroy() {
	}
}