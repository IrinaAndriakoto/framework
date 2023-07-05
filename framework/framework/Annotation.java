/**
 *
 * @author irina
 */
package utile;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation {
    public boolean isSegleton() default false;
    public String url() default"";
    public String[] ParametersNames() default {}; 
}