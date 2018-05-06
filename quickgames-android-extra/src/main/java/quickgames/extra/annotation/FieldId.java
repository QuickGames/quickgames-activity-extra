package quickgames.extra.annotation;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @deprecated As of release 0.0.4,
 * replaced by {@link #(quickgames.android.extra.annotation.FieldId)},
 * will be deleted of release 0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldId {

    /**
     * ID for view.
     */
    @IdRes int value();

}
