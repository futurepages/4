package org.futurepages.core.view.annotations;

import com.vaadin.data.util.AbstractContainer;
import org.futurepages.core.view.PreSelectListContainer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDependency {
	String preSelect() default "";
	Class<? extends AbstractContainer> customContainer() default PreSelectListContainer.class;
}