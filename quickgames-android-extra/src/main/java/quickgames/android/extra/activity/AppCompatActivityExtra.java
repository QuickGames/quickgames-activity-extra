package quickgames.android.extra.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;

import quickgames.android.extra.Util;
import quickgames.android.extra.annotation.FieldId;
import quickgames.android.extra.annotation.LayoutParams;
import quickgames.android.extra.interfaces.GetSetViewValue;
import quickgames.android.extra.interfaces.ReportError;

public class AppCompatActivityExtra extends AppCompatActivity
        implements GetSetViewValue, ReportError {

    //region PRIVATE_STATIC_FINAL_VALUES

    private static final String STRING_DEF_VALUE = "";
    private static final int INTEGER_DEF_VALUE = 0;
    private static final long LONG_DEF_VALUE = 0L;
    private static final float FLOAT_DEF_VALUE = 0.0f;
    private static final boolean BOOLEAN_DEF_VALUE = false;

    //endregion

    //region OVERRIDE

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // LayoutParams processing
        mOnCreateLayoutParams();

        // Init views
        mOnCreateFieldId();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Save values
        Util.savePreferences(this);
    }

    @Override
    public void logError(Throwable e) {
        Util.logError(this, e);
    }

    //region SHOW_ERROR

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param text The text to show.  Can be formatted text.
     */
    public void showError(@NonNull CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param resId The resource id of the string resource to use.  Can be formatted text.
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public void showError(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    //endregion

    //endregion

    //region PRIVATE_PROCESSING

    private void mOnCreateLayoutParams() {
        // LayoutParams processing

        Class<? extends AppCompatActivityExtra> clazz = getClass();
        LayoutParams layoutParams = clazz.getAnnotation(LayoutParams.class);
        if (layoutParams != null) {

            // layoutResID
            @LayoutRes int layoutResID = layoutParams.layoutResID();
            setContentView(layoutResID);

            // titleResId
            @StringRes int titleId = layoutParams.titleResId();
            if (titleId != 0) {
                String title = getString(titleId);
                setTitle(title);
            }
        }
    }

    private void mOnCreateFieldId() {
        // Init views

        Class<? extends AppCompatActivityExtra> clazz = getClass();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            FieldId annotation = field.getAnnotation(FieldId.class);
            if (annotation != null) {
                int value = annotation.value();
                if (value != 0) {
                    try {
                        View view = findViewById(value);
                        field.set(this, view);

                        // field preferences
                        Util.loadPreferenceView(this, field, view);
                    } catch (IllegalAccessException e) {
                        logError(e);
                    }
                }
            }
        }
    }

    //endregion

    //region VIRTUAL_METHODS

    @Override
    public void setViewValue(View view, Object value) {

    }

    @Nullable
    @Override
    public Object getViewValue(View view) {
        return null;
    }

    //endregion

}
