package org.futurepages.core.view.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PreSelectDependency {
	String groupBy();
	String showAttr() default "";
	String orderBy() default "";
}