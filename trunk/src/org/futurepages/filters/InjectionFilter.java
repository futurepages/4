package org.futurepages.filters;

import org.futurepages.core.action.Action;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.util.InjectionUtils;

/**
 * This filter will inject input values into the action or model.
 *
 * @author Sergio Oliveira
 */
public class InjectionFilter implements Filter {

	private boolean tryField = true;
	private boolean convert = true;
	private boolean convertNullToFalse = false;

	/**
	 * Creates a InjectionFilter that can be used by any action class.
	 * You may use this filter per action or as a global filter.
	 */
	public InjectionFilter() {
	}

	/**
	 * Creates an InjectionFilter that can be used by any action class.
	 * You may use this filter per action or as a global filter.
	 * If tryField is true and it cannot find a setter for the input value,
	 * it will try to directly access the attribute, even if it is a private field.
	 *
	 * @param tryField A flag indicating whether this filter should try to access private attributes.
	 */
	public InjectionFilter(boolean tryField) {
		this.tryField = tryField;
	}

	/**
	 * Creates an InjectionFilter that can be used by any action class.
	 * You may use this filter per action or as a global filter.
	 * If tryField is true and it cannot find a setter for the input value,
	 * it will try to directly access the attribute, even if it is a private field.
	 * If convert flag is true (default), it will try to automatically convert.
	 *
	 * @param tryField A flag indicating whether this filter should try to access private attributes.
	 */
	public InjectionFilter(boolean tryField, boolean convert) {
		this(tryField);
		this.convert = convert;
	}

	/**
	 * Force NULL values to be converted to FALSE booleans (only when type is Boolean or boolean!)
	 *
	 * @param tryField
	 * @param convert
	 * @param convertNullToFalse
	 * @since 1.11
	 */
	public InjectionFilter(boolean tryField, boolean convert, boolean convertNullToFalse) {
		this(tryField, convert);
		this.convertNullToFalse = convertNullToFalse;
	}

	public String filter(InvocationChain chain) throws Exception {

		Action action = chain.getAction();
		InjectionUtils.getObject(action, action.getInput(), action.getLocale(), tryField, null, convert, convertNullToFalse);

		return chain.invoke();
	}

	public void destroy() {
	}
}        