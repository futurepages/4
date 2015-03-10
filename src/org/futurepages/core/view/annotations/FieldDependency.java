package org.futurepages.core.view.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDependency {
	String showAttr() default "";
	String orderBy() default "";
	PreSelectDependency[] pre() default {};
	//TODO: cardinality="LOW|MEDIUM|LARGE"  - with Lazy ComboBox
}