package org.futurepages.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation para anotar atributo de 'tag' jsp
 * @author Danilo
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TagAttributeOverride {

    String name();
    
    TagAttribute tagAttribute() default @TagAttribute ;
}

