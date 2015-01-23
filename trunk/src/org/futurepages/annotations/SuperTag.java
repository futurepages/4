package org.futurepages.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation para anotar super classes de tags annotadas com {@link Tag} :p
 * @author Danilo Medeiros
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface SuperTag {}