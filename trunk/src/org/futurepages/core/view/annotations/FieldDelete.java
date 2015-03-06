package org.futurepages.core.view.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDelete {
	boolean visibleWithRole() default false;
}
