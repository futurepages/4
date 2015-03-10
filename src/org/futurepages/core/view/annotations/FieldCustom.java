package org.futurepages.core.view.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldCustom {
	String label() default "";

	String mask() default ""; //TODO how script it?

	String floatLeft() default "";
}
