/*******************************************************************************
 * Copyright (c) 2013 Rüdiger Herrmann
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Rüdiger Herrmann - initial API and implementation
 ******************************************************************************/
package org.futurepages.test;

import java.lang.reflect.Modifier;

import org.junit.Assume;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class ConditionalIgnoreRule implements MethodRule {

	public interface IgnoreCondition extends framework.IgnoreCondition {
	}

	public Statement apply(Statement base, FrameworkMethod method, Object target) {
		Statement result = base;
		if (hasConditionalIgnoreAnnotation(method)) {
			framework.IgnoreCondition condition = getIgnoreContition(method, target);
			if (condition.isSatisfied()) {
				result = new IgnoreStatement(condition);
			}
		}
		return result;
	}

	private boolean hasConditionalIgnoreAnnotation(FrameworkMethod method) {
		return method.getAnnotation(ConditionalIgnore.class) != null;
	}

	private framework.IgnoreCondition getIgnoreContition(FrameworkMethod method, Object instance) {
		ConditionalIgnore annotation = method.getAnnotation(ConditionalIgnore.class);
		return newCondition(annotation, instance);
	}

	private framework.IgnoreCondition newCondition(ConditionalIgnore annotation, Object instance) {
		final Class<? extends framework.IgnoreCondition> cond = annotation.condition();
		try {
			if (cond.isMemberClass()) {
				if (Modifier.isStatic(cond.getModifiers())) {
					return cond.getDeclaredConstructor(new Class<?>[]{}).newInstance();
				} else if (instance != null && instance.getClass().isAssignableFrom(cond.getDeclaringClass())) {
					return cond.getDeclaredConstructor(new Class<?>[]{instance.getClass()}).newInstance(instance);
				}
				throw new IllegalArgumentException("Conditional class: " + cond.getName() + " was an inner member class however it was not declared inside the test case using it. Either make this class a static class (by adding static keyword), standalone class (by declaring it in it's own file) or move it inside the test case using it");
			} else {
				return cond.newInstance();
			}
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	private static class IgnoreStatement extends Statement {
		private framework.IgnoreCondition condition;

		IgnoreStatement(framework.IgnoreCondition condition) {
			this.condition = condition;
		}

		@Override
		public void evaluate() {
			Assume.assumeTrue("Ignored by " + condition.getClass().getSimpleName(), false);
		}
	}

}