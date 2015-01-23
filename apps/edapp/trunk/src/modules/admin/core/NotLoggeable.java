package modules.admin.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import modules.admin.enums.LogType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotLoggeable {

    public LogType [] when() default {LogType.ALL};
    
}
