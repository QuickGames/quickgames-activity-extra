package quickgames.activity.extra.annotation;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActivityParams {

    /**
     * Should be called instead of {@link Activity#setContentView(int)}}
     */
    @LayoutRes int layoutResID();

    /**
     * Change the title associated with this activity.  If this is a
     * top-level activity, the title for its window will change.  If it
     * is an embedded activity, the parent can do whatever it wants
     * with it.
     */
    @StringRes int titleResId() default 0;
}
