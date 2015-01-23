package org.futurepages.annotations;


/**
 * Annotation para anotar atributo de 'tag' jsp
 * @author Danilo
 */

@java.lang.annotation.Target(value={java.lang.annotation.ElementType.TYPE})
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public abstract @interface TagAttributeOverrideArray {
  
  public abstract TagAttributeOverride[] value();

}
