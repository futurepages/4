package org.futurepages.core.quartz;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation Taks Quartz
 * @author Danilo
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface CronTrigger {

    String name() default "";
    String expression() default "";
}