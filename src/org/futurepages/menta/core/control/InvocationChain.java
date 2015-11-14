package org.futurepages.menta.core.control;

import org.futurepages.menta.filters.MethodParamFilter;
import org.futurepages.menta.tags.core.webcomponent.ImportComponentRes;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.action.AsynchronousManager;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.core.input.Input;
import org.futurepages.menta.util.InjectionUtils;

import javax.servlet.ServletException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * When an action is executed, a chain of filters is created.
 * The last step of any InvocationChain is the action.
 * An action may have one or more filters and global filters.
 *
 * @author Sergio Oliveira
 */
public class InvocationChain {

	private LinkedList<Filter> filters = new LinkedList<Filter>();
	private Action action;
	private String innerAction = null;
	private final String actionName;
	private Method innerMethod = null;

	/**
	 * Creates an InvocationChain for this action.
	 *
	 * @param action The action for what this InvocationChain will be created.
	 */
	public InvocationChain(String actionName, Action action) {

		this.actionName = actionName;

		this.action = action;
		this.action.setChain(this);
	}

	public Filter getFilter(Class<? extends Filter> filterClass) {

		Iterator<Filter> iter = filters.iterator();

		while (iter.hasNext()) {

			Filter f = iter.next();

			if (filterClass.isAssignableFrom(f.getClass())) {

				return f;
			}
		}

		return null;
	}

	/**
	 * Gets the action of this InvocationChain
	 *
	 * @return The action of this InvocationChain
	 */
	public Action getAction() {
		return action;
	}

	void addFilter(Filter filter) {
		filters.add(filter);
	}

	void addFilters(List<Filter> list) {
		filters.addAll(list);
	}

	void clearFilters() {
		filters.clear();
	}

	/**
	 * Invoke and execute the next step in this InvocationChain.
	 * This can be the next filter or the action.
	 *
	 * @return The result of a filter or the action.
	 */
	public String invoke() throws Exception {

		if (!filters.isEmpty()) {
			Filter f = (Filter) filters.removeFirst();
//			System.out.println("Filter "+f.getClass().getName()); //for DEBUG-MODE
			return f.filter(this);
		}
//		System.out.println("executing action method..."); //for DEBUG-MODE
		Method metodo = getMethod();
		if(metodo != null){
			Object[] paramValues = getParametersValues(metodo);

			if(AsynchronousManager.isDynAction(this)) {
				ImportComponentRes.destroyAsyncResources();
			}
			
			Object valorRetornado = metodo.invoke(action, paramValues);

			String result;
			Class<?> tipoRetorno = metodo.getReturnType();
			if (tipoRetorno.equals(Void.TYPE)) {
				result = Action.SUCCESS;
			} else{
				if (valorRetornado == null) {
					result = Action.NULL;
				} else {
					result = valorRetornado.toString();
				}
			}
			return result;
		}else{
			throw new ServletException("The inner action does not exist: " + innerAction);
		}
	}

	/**
	 * Returns the {@link java.lang.reflect.Method} to be invoked.
	 * @return
	 */
	public Method getMethod() {

		if(this.innerMethod == null){
			String methodToExec = getInnerAction();
			if (methodToExec == null) {
				methodToExec = "execute";
			}

			Method[] metodos = getAction().getClass().getMethods();
			for (Method metodo: metodos) {
				if (!Modifier.isPublic(metodo.getModifiers())) {
					continue;
				}
				if (metodo.getName().equals(methodToExec)) {
					this.innerMethod = metodo;
				}
			}
		}
		return this.innerMethod;
	}

	/**
	 * Found the arguments to the action invocation
	 * @param method
	 * @return
	 * @throws javax.servlet.ServletException
	 */
	private Object[] getParametersValues(Method method) throws ServletException {

		Class<?>[] params = method.getParameterTypes();
		Input input = action.getInput();
		Set<String> paramKeys = new HashSet<String>();
		Object[] values = new Object[params.length];

		for (int pos = 0; pos < params.length; pos++) {
			boolean found = false;

			// check if we are using the MethodParamFilter!
			List<String> list = (List<String>) input.getValue(MethodParamFilter.PARAM_KEY);
			Iterator<String> keys;
			if (list == null) {
				keys = input.keys();
			} else {
				keys = list.iterator();
			}
			while (keys.hasNext()) {
				String key = keys.next();
				if (paramKeys.contains(key)) {
					continue;
				}
				Object o = input.getValue(key);
				o = InjectionUtils.trimValue(o);

				if (params[pos].isInstance(o)) {
					values[pos] = o;
					paramKeys.add(key);
					found = true;
					break;
				} else {
					Object converted = InjectionUtils.tryToConvert(o, params[pos], action.getLocale(), true);
					if ((converted != null) || ((params[pos] == Integer.class) || (params[pos] == Long.class))) {
						values[pos] = converted;
						paramKeys.add(key);
						found = true;
						break;
					}
				}
			}

			if (!found) {
				// let's try to create an object on the fly here...
				// if we have something like add(User u1, User u2) we may get in trouble here,
				// but for this case the user should configure the parameters by hand using a
				// VOFilter ou MethodParamFilter...

				// The if is because this is suppose to be a POJO, not a java.lang.String for example...

				if (!params[pos].getName().startsWith("java.lang.") && !params[pos].isPrimitive() && InjectionUtils.hasDefaultConstructor(params[pos])) {
					Object obj = action.getInput().getObject(params[pos]);
					String key = params[pos].getSimpleName().toLowerCase();
					paramKeys.add(key);
					values[pos] = obj;
					action.getInput().setValue(key, obj);
					found = true;
				}
			}
			if (!found) {
				throw new ServletException("Cannot find parameter value for method: " + method.getName() + " / " + params[pos]);
			}
		}
		return values;
	}

	/**
	 * Initialize inner action class contexts with the main action contexts...
	 *
	 * @param mainAction The main action.
	 * @param innerAction The inner action object. (inner class)
	 * @since 1.9
	 */
	protected void initInnerAction(Action mainAction, Action innerAction) {

		innerAction.setInput(mainAction.getInput());
		innerAction.setOutput(mainAction.getOutput());
		innerAction.setSession(mainAction.getSession());
		innerAction.setApplication(mainAction.getApplication());
		innerAction.setCookies(mainAction.getCookies());
		innerAction.setLocale(mainAction.getLocale());

	}

	/**
	 * Sets an inner action to be executed.
	 * An inner action is a method inside the action implementation that can be executed instead of the execute() method.
	 *
	 * @param innerAction The name of the method to be executed as an inner action
	 */
	public void setInnerAction(String innerAction) {
		this.innerAction = innerAction;
	}

	/**
	 * Returns the inner action being executed in the invocation chain.
	 *
	 * @return The innner action or null if there is no inner action being executed.
	 * @since 1.2.1
	 */
	public String getInnerAction() {
		return innerAction;
	}

	/**
	 * Returns the name of the action being executed in the invocation chain.
	 *
	 * @return The action name like HelloMentawai
	 * @since 1.8
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * Returns the filters of this invocation chain.
	 *
	 * @return all filters of this invocation chain
	 * @since 1.4
	 */
	public List<Filter> getFilters() {

		return filters;

	}
}
