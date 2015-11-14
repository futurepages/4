package org.futurepages.menta.annotations;

import org.futurepages.menta.enums.AsynchronousActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation para anotar innerActions que são Asincronas
 * @author Danilo
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AsynchronousAction {

	AsynchronousActionType value();
}