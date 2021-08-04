package mx.kenzie.skript.runtime.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SourceData {
    
    int line();
    
    String file() default "Unknown";
    
    long compiled();
    
}
