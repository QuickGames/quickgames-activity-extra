package quickgames.extra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @deprecated As of release 0.0.4,
 * replaced by {@link #(quickgames.android.extra.annotation.FieldPreference)},
 * will be deleted of release 0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldPreference {

    /**
     * The name of the preference to retrieve.
     */
    String key();

    FieldType fieldType();

    boolean saveValue() default false;

}
