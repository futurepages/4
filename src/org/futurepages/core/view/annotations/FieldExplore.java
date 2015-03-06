package org.futurepages.core.view.annotations;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.core.view.types.AdvancedFilterType;
import org.futurepages.core.view.types.MainFilterType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldExplore {
	boolean formatAsShow();

	Class<? extends AbstractFormatter> formatter();

	String formatterParam() default "";

	boolean sortable() default false;

	boolean hidden() default false;

	int sortPriority() default Integer.MAX_VALUE;

	MainFilterType mainFilter() default MainFilterType.NONE;

	AdvancedFilterType advancedFilter() default AdvancedFilterType.NONE;
}