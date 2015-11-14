package org.futurepages.menta.core.action;

import org.futurepages.menta.actions.AjaxAction;
import org.futurepages.menta.actions.DynAction;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.enums.AsynchronousActionType;
import org.futurepages.menta.annotations.AsynchronousAction;

import java.lang.reflect.Method;

import static org.futurepages.menta.enums.AsynchronousActionType.AJAX;
import static org.futurepages.menta.enums.AsynchronousActionType.DYN;

public class AsynchronousManager {

	public static boolean isAsynchronousAction(InvocationChain chain) {
		return isAsynchronousAction(chain.getMethod(), chain.getAction().getClass());
	}

	public static boolean isDynAction(InvocationChain chain) {
		return isDynAction(chain.getMethod(),chain.getAction().getClass());
	}

	public static boolean isAjaxAction(InvocationChain chain) {
		return isAjaxAction(chain.getMethod(), chain.getAction().getClass());
	}

	public static boolean isAsynchronousAction(Method method, Class actionClass) {
		return isDynAction(method, actionClass) || isAjaxAction(method,actionClass);
	}

	public static boolean isDynAction(Method method, Class actionClass) {
		if(isInnerActionAnnotatedWith(method, DYN)){
			return true;
		}
		if(isInnerActionAnnotatedWith(method, AJAX)){
			return false;
		}
		if(DynAction.class.isAssignableFrom(actionClass)|| isClassAnnotatedWith(actionClass, DYN)){
			return true;
		}
		return false;
	}

	public static boolean isAjaxAction(Method method, Class actionClass) {
		if(isInnerActionAnnotatedWith(method, AJAX)){
			return true;
		}
		if(isInnerActionAnnotatedWith(method, DYN)){
			return false;
		}
		if(AjaxAction.class.isAssignableFrom(actionClass)|| isClassAnnotatedWith(actionClass, AJAX)){
			return true;
		}
		return false;
	}
	
	private static boolean isInnerActionAnnotatedWith(Method method, AsynchronousActionType tipo){
		if(method!=null){
			AsynchronousAction annotation = method.getAnnotation(AsynchronousAction.class);
			if(annotation!=null && annotation.value().equals(tipo)){
				return true;
			}
		}
		return false;
	}

	private static boolean isClassAnnotatedWith(Class<?> actionClass, AsynchronousActionType tipo){
		AsynchronousAction annotation = actionClass.getAnnotation(AsynchronousAction.class);
		if(annotation!=null && annotation.value().equals(tipo)){
			return true;
		}
		return false;
	}
}

