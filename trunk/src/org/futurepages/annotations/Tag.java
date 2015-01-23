package org.futurepages.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.futurepages.core.tags.build.ContentTypeEnum;

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