package org.futurepages.core.view.annotations;

import org.futurepages.core.formatter.AbstractFormatter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldShow {
	Class<? extends AbstractFormatter> formatter();

	String formatterParam() default "";
}
