package org.futurepages.filters;

import org.futurepages.core.action.Action;
import org.futurepages.core.context.Context;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.InputWrapper;
import org.futurepages.core.control.InvocationChain;

/**
 * This filter will place the cookie context in the action input.
 * 
 * Note that the CookieContext class is also a Map<String, Object> so
 * it can be easily injected in a pojo action without coupling with the 
 * framework API.
 * 
 * @author Sergio Oliveira Jr.
 */
public class CookiesFilter extends InputWrapper implements Filter {

	private final String name;
	private ThreadLocal<Action> action = new ThreadLocal<Action>();

	public CookiesFilter(String name) {
		this.name = name;
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		Action theAction = chain.getAction();
		super.setInput(theAction.getInput());
		theAction.setInput(this);
		this.action.set(theAction);
		return chain.invoke();
	}

	@Override
	public Object getValue(String name) {

		if (name.equals(this.name)) {
			Object value = super.getValue(name);
			if (value != null) {
				return value;
			}
			Action theAction = this.action.get();

			if (theAction == null) {
				throw new IllegalStateException("Action cannot be null here!");
			}

			Context cookies = theAction.getCookies();
			setValue(name, cookies);
			return cookies;

		} else {
			return super.getValue(name);
		}
	}

	@Override
	public void destroy() {
	}
}
