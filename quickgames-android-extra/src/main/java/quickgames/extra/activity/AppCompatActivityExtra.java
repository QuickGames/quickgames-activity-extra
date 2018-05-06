package quickgames.extra.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

import quickgames.extra.Util;
import quickgames.extra.annotation.ActivityParams;
import quickgames.extra.annotation.FieldId;
import quickgames.extra.annotation.FieldPreference;
import quickgames.extra.annotation.FieldType;
import quickgames.extra.view.SetGetValue;

/**
 * @deprecated As of release 0.0.4,
 * replaced by {@link #(quickgames.android.extra.activity.AppCompatActivityExtra)},
 * will be deleted of release 0.1
 */
public class AppCompatActivityExtra extends AppCompatActivity {
    private static final String LOG_TAG = AppCompatActivityExtra.class.getSimpleName();

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
        mOnCreateActivityParams();

        // Init views
        mOnCreateFieldId();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Save values
        mFieldPreferencesApply();
    }

    //endregion

    //region PRIVATE_PROCESSING

    private void mOnCreateActivityParams() {
        // LayoutParams processing

        Class<? extends AppCompatActivityExtra> clazz = getClass();
        ActivityParams layoutParams = clazz.getAnnotation(ActivityParams.class);
        if (layoutParams != null) {

            // layoutResID
            int layoutResID = layoutParams.layoutResID();
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
                        mFieldPreference(field, view);
                    } catch (IllegalAccessException e) {
                        logError(LOG_TAG, e);
                    }
                }
            }
        }
    }

    //region PREFERENCE

    //region PREFERENCE_GET_VALUE

    private void mFieldPreference(Field field, View view) {
        // Init field preferences

        FieldPreference annotation = field.getAnnotation(FieldPreference.class);
        if (annotation != null) {
            String key = annotation.key();
            FieldType fieldType = annotation.fieldType();
            if (!key.isEmpty()) {
                SharedPreferences sp = Util.getSharedPreferences(this);
                if (!mCustomViewValueSetter(view, sp, key)) {
                    switch (fieldType) {
                        case STRING:
                            mPreSetViewValue(view, sp.getString(key, STRING_DEF_VALUE), STRING_DEF_VALUE);
                            break;
                        case INTEGER:
                            mPreSetViewValue(view, sp.getInt(key, INTEGER_DEF_VALUE), INTEGER_DEF_VALUE);
                            break;
                        case LONG:
                            mPreSetViewValue(view, sp.getLong(key, LONG_DEF_VALUE), LONG_DEF_VALUE);
                            break;
                        case FLOAT:
                            mPreSetViewValue(view, sp.getFloat(key, FLOAT_DEF_VALUE), FLOAT_DEF_VALUE);
                            break;
                        case BOOLEAN:
                            mPreSetViewValue(view, sp.getBoolean(key, BOOLEAN_DEF_VALUE), BOOLEAN_DEF_VALUE);
                            break;
                    }
                }
            }
        }
    }

    @MainThread
    private boolean mCustomViewValueSetter(View view, SharedPreferences sp, String key) {
        boolean result = true;

        if (view instanceof Checkable) {
            boolean value = sp.getBoolean(key, BOOLEAN_DEF_VALUE);
            if (value != BOOLEAN_DEF_VALUE)
                ((Checkable) view).setChecked(value);
        } else if (view instanceof EditText) {
            String value = sp.getString(key, STRING_DEF_VALUE);
            if (value != STRING_DEF_VALUE)
                ((EditText) view).setText(value);
        } else if (view instanceof TextView) {
            String value = sp.getString(key, STRING_DEF_VALUE);
            if (value != STRING_DEF_VALUE)
                ((TextView) view).setText(value);
        } else
            result = false;

        return result;
    }

    @MainThread
    private <T> void mPreSetViewValue(View view, T value, T defValue) {
        if (value != defValue)
            if (view instanceof SetGetValue)
                ((SetGetValue) view).setViewValue(value);
            else
                setViewValue(view, value);
    }

    //endregion

    //region PREFERENCE_PUT_VALUE

    private void mFieldPreferencesApply() {
        // Save values

        Class<? extends AppCompatActivityExtra> clazz = getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            FieldPreference annotation = field.getAnnotation(FieldPreference.class);
            if (annotation != null) {
                boolean saveValue = annotation.saveValue();
                if (saveValue) {

                    String key = annotation.key();
                    FieldType fieldType = annotation.fieldType();
                    if (!key.isEmpty()) {
                        try {
                            View view = (View) field.get(this);

                            SharedPreferences sp = Util.getSharedPreferences(this);
                            SharedPreferences.Editor edit = sp.edit();
                            if (!mCustomViewValueGetter(view, edit, key)) {
                                switch (fieldType) {
                                    case STRING:
                                        edit.putString(key, this.<String>mPreGetViewValue(view));
                                        break;
                                    case INTEGER:
                                        edit.putInt(key, this.<Integer>mPreGetViewValue(view));
                                        break;
                                    case LONG:
                                        edit.putLong(key, this.<Long>mPreGetViewValue(view));
                                        break;
                                    case FLOAT:
                                        edit.putFloat(key, this.<Float>mPreGetViewValue(view));
                                        break;
                                    case BOOLEAN:
                                        edit.putBoolean(key, this.<Boolean>mPreGetViewValue(view));
                                        break;
                                }
                            }

                            edit.apply();
                        } catch (IllegalAccessException e) {
                            logError(LOG_TAG, e);
                        }
                    }

                }
            }
        }
    }

    @MainThread
    private boolean mCustomViewValueGetter(View view, Editor edit, String key) {
        boolean result = true;

        if (view instanceof Checkable) {
            boolean checked = ((Checkable) view).isChecked();
            edit.putBoolean(key, checked);
        } else if (view instanceof EditText) {
            CharSequence text = ((TextView) view).getText();
            String value = text.toString();
            edit.putString(key, value);
        } else if (view instanceof TextView) {
            CharSequence text = ((TextView) view).getText();
            String value = text.toString();
            edit.putString(key, value);
        } else
            result = false;

        return result;
    }

    @MainThread
    private <T> T mPreGetViewValue(View view) {
        T result;

        if (view instanceof SetGetValue)
            result = (T) ((SetGetValue) view).getViewValue();
        else
            result = getViewValue(view);

        return result;
    }

    //endregion

    //endregion

    //endregion

    //region VIRTUAL_METHODS

    @MainThread
    protected <T> void setViewValue(View view, T value) {

    }

    @Nullable
    @MainThread
    protected <T> T getViewValue(View view) {
        return null;
    }

    //endregion

    //region STATIC_PROTECTED_METHODS

    protected static void logError(String tag, Throwable e) {
        String errorMessage = e.getMessage();
        Log.e(tag, errorMessage);
    }

    //endregion

}
