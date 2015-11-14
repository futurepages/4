package org.futurepages.menta.annotations;

import org.futurepages.menta.core.tags.build.ContentTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation para anotar classe 'tag jsp' :p
 * @author Danilo
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tag {

    String name() default "";

    String displayName() default "";

    ContentTypeEnum bodyContent();
}