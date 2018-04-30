package quickgames.activity.extra;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.lang.reflect.Field;

import quickgames.activity.extra.annotation.ActivityParams;
import quickgames.activity.extra.annotation.FieldId;
import quickgames.activity.extra.annotation.FieldPreference;
import quickgames.activity.extra.annotation.FieldType;

/**
 * Created by quickgames on 29.04.18 19:43.
 */
@Deprecated
public class AppCompatActivityExtra extends AppCompatActivity {

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

        // ActivityParams processing
        mOnCreateActivityParams();

        // Init views
        mOnCreateFieldId();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Save values
        mPreferencesApply();
    }

    //endregion

    //region PRIVATE_PROCESSING

    private void mOnCreateActivityParams() {
        // ActivityParams processing

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
        Field[] fields = clazz.getDeclaredFields();
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
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void mFieldPreference(Field field, View view) {
        // Init field preferences

        FieldPreference annotation = field.getAnnotation(FieldPreference.class);
        if (annotation != null) {
            String key = annotation.key();
            FieldType fieldType = annotation.fieldType();
            if (!key.isEmpty()) {
                SharedPreferences sp = Util.getSharedPreferences(this);
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

    @MainThread
    private <TypeValue> void mPreSetViewValue(View view, TypeValue value, TypeValue defValue) {
        if (value != defValue)
            setViewValue(view, value);
    }

    private void mPreferencesApply() {
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
                            switch (fieldType) {
                                case STRING:
                                    edit.putString(key, this.<String>getViewValue(view));
                                    break;
                                case INTEGER:
                                    edit.putInt(key, this.<Integer>getViewValue(view));
                                    break;
                                case LONG:
                                    edit.putLong(key, this.<Long>getViewValue(view));
                                    break;
                                case FLOAT:
                                    edit.putFloat(key, this.<Float>getViewValue(view));
                                    break;
                                case BOOLEAN:
                                    edit.putBoolean(key, this.<Boolean>getViewValue(view));
                                    break;
                            }
                            edit.apply();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
    }

    //endregion

    //region VIRTUAL_METHODS

    @MainThread
    protected <TypeValue> void setViewValue(View view, TypeValue value) {

    }

    @Nullable
    @MainThread
    protected <TypeValue> TypeValue getViewValue(View view) {
        return null;
    }

    //endregion

}
