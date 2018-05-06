package quickgames.extra.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import quickgames.extra.RNames;

/**
 * @deprecated As of release 0.0.4,
 * replaced by {@link #( quickgames.android.extra.activity.ActivityLibrary )},
 * will be deleted of release 0.1
 */
public class ActivityLibrary extends AppCompatActivityExtra {
    private static final String LOG_TAG = ActivityLibrary.class.getSimpleName();

    //region INNER_ANNOTATIONS

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    protected @interface ActivityParamsName {

        /**
         * Should be called instead of {@link Activity#setContentView(int)}}
         */
        String layoutRes();

        /**
         * Change the title associated with this activity.  If this is a
         * top-level activity, the title for its window will change.  If it
         * is an embedded activity, the parent can do whatever it wants
         * with it.
         */
        String titleRes() default "app_name";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    protected @interface FieldName {

        /**
         * Name for view.
         */
        String value();

    }

    //endregion

    //region OVERRIDE

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // LayoutParams processing
        mOnCreateActivityParams();

        // Init views
        mOnCreateFieldId();

        super.onCreate(savedInstanceState);
    }

    //endregion

    //region PRIVATE_PROCESSING

    private void mOnCreateActivityParams() {
        // LayoutParams processing

        Class<? extends AppCompatActivityExtra> clazz = getClass();
        ActivityParamsName layoutParams = clazz.getAnnotation(ActivityParamsName.class);
        if (layoutParams != null) {

            // layoutRes
            String layoutRes = layoutParams.layoutRes();
            int layoutId = RNames.layout.getId(this, layoutRes);
            setContentView(layoutId);

            // titleRes
            String titleRes = layoutParams.titleRes();
            if (!titleRes.isEmpty()) {
                int titleResId = RNames.string.getId(this, titleRes);
                String title = getString(titleResId);
                setTitle(title);
            }
        }
    }

    private void mOnCreateFieldId() {
        // Init views

        Class<? extends AppCompatActivityExtra> clazz = getClass();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            FieldName annotation = field.getAnnotation(FieldName.class);
            if (annotation != null) {
                String value = annotation.value();
                if (!value.isEmpty()) {
                    try {
                        int id = RNames.id.getId(this, value);
                        View view = findViewById(id);
                        field.set(this, view);
                    } catch (IllegalAccessException e) {
                        logError(LOG_TAG, e);
                    }
                }
            }
        }
    }

    //endregion

}
