package quickgames.activity.extra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Deprecated
public @interface FieldPreference {

    /**
     * The name of the preference to retrieve.
     */
    String key();

    FieldType fieldType();

    boolean saveValue() default false;

}
