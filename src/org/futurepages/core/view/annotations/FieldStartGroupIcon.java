package org.futurepages.core.view.annotations;

import com.vaadin.server.FontAwesome;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldStartGroupIcon {
	FontAwesome value();
}
