package org.futurepages.core.view.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldNow {

	boolean create();
	boolean update();

}
