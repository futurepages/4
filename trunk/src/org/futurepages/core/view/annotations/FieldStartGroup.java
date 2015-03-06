package org.futurepages.core.view.annotations;

import org.futurepages.core.view.types.FieldGroupType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldStartGroup {
	String label();

	FieldGroupType type() default FieldGroupType.NONE;

	//String floatRatio() default ""; TODO how do it?
}