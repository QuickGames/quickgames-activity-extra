package quickgames.android.extra;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

import quickgames.android.extra.activity.AppCompatActivityExtra;
import quickgames.android.extra.annotation.FieldPreference;
import quickgames.android.extra.annotation.FieldType;
import quickgames.android.extra.interfaces.GetSetValue;
import quickgames.android.extra.interfaces.GetSetViewValue;
import quickgames.android.extra.fragment.FragmentExtra;
import quickgames.android.extra.interfaces.ReportError;

public final class Util {

    private Util() {
    }

    //region PREFERENCE

    //region PRIVATE_STATIC_FINAL_VALUES

    private static final String STRING_DEF_VALUE = "";
    private static final int INTEGER_DEF_VALUE = 0;
    private static final long LONG_DEF_VALUE = 0L;
    private static final float FLOAT_DEF_VALUE = 0.0f;
    private static final boolean BOOLEAN_DEF_VALUE = false;

    //endregion

    public static SharedPreferences getSharedPreferences(@NonNull Context context) {
        String packageName = context.getPackageName();
        Context applicationContext = context.getApplicationContext();
        return applicationContext.getSharedPreferences(packageName, Context.MODE_PRIVATE);
    }

    public static Editor getSharedPreferencesEditor(@NonNull Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.edit();
    }

    //region LOAD_VIEW

    public static void loadPreferenceView(AppCompatActivityExtra context, Field field, View view) {
        // Init field preferences

        FieldPreference annotation = field.getAnnotation(FieldPreference.class);
        if (annotation != null) {
            String key = annotation.key();
            FieldType fieldType = annotation.fieldType();
            if (!key.isEmpty()) {
                SharedPreferences sp = getSharedPreferences(context);
                if (!mCustomViewValueSetter(view, sp, key)) {
                    switch (fieldType) {
                        case STRING:
                            mPreSetViewValue(context, view, sp.getString(key, STRING_DEF_VALUE), STRING_DEF_VALUE);
                            break;
                        case INTEGER:
                            mPreSetViewValue(context, view, sp.getInt(key, INTEGER_DEF_VALUE), INTEGER_DEF_VALUE);
                            break;
                        case LONG:
                            mPreSetViewValue(context, view, sp.getLong(key, LONG_DEF_VALUE), LONG_DEF_VALUE);
                            break;
                        case FLOAT:
                            mPreSetViewValue(context, view, sp.getFloat(key, FLOAT_DEF_VALUE), FLOAT_DEF_VALUE);
                            break;
                        case BOOLEAN:
                            mPreSetViewValue(context, view, sp.getBoolean(key, BOOLEAN_DEF_VALUE), BOOLEAN_DEF_VALUE);
                            break;
                    }
                }
            }
        }
    }

    public static void loadPreferenceView(FragmentExtra context, Field field, View view) {
        // Init field preferences

        FieldPreference annotation = field.getAnnotation(FieldPreference.class);
        if (annotation != null) {
            String key = annotation.key();
            FieldType fieldType = annotation.fieldType();
            if (!key.isEmpty()) {
                SharedPreferences sp = getSharedPreferences(context.getActivity());
                if (!mCustomViewValueSetter(view, sp, key)) {
                    switch (fieldType) {
                        case STRING:
                            mPreSetViewValue(context, view, sp.getString(key, STRING_DEF_VALUE), STRING_DEF_VALUE);
                            break;
                        case INTEGER:
                            mPreSetViewValue(context, view, sp.getInt(key, INTEGER_DEF_VALUE), INTEGER_DEF_VALUE);
                            break;
                        case LONG:
                            mPreSetViewValue(context, view, sp.getLong(key, LONG_DEF_VALUE), LONG_DEF_VALUE);
                            break;
                        case FLOAT:
                            mPreSetViewValue(context, view, sp.getFloat(key, FLOAT_DEF_VALUE), FLOAT_DEF_VALUE);
                            break;
                        case BOOLEAN:
                            mPreSetViewValue(context, view, sp.getBoolean(key, BOOLEAN_DEF_VALUE), BOOLEAN_DEF_VALUE);
                            break;
                    }
                }
            }
        }
    }

    @MainThread
    private static boolean mCustomViewValueSetter(View view, SharedPreferences sp, String key) {
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
    private static <T> void mPreSetViewValue(GetSetViewValue context, View view, T value, T defValue) {
        if (value != defValue)
            if (view instanceof GetSetValue)
                ((GetSetValue) view).setViewValue(value);
            else
                context.setViewValue(view, value);
    }

    //endregion

    //region SAVE

    public static void savePreferences(AppCompatActivityExtra context) {
        mSavePreferences(context, context);
    }

    public static void savePreferences(FragmentExtra context) {
        Activity activity = context.getActivity();
        mSavePreferences(activity, context);
    }

    private static <T extends GetSetViewValue & ReportError> void mSavePreferences(
            Activity activity, T context) {
        Class<? extends GetSetViewValue> clazz = context.getClass();
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
                            View view = (View) field.get(context);

                            Editor edit = getSharedPreferencesEditor(activity);
                            if (!mCustomViewValueGetter(view, edit, key)) {
                                switch (fieldType) {
                                    case STRING:
                                        edit.putString(key, Util.<String>mPreGetViewValue(context, view));
                                        break;
                                    case INTEGER:
                                        edit.putInt(key, Util.<Integer>mPreGetViewValue(context, view));
                                        break;
                                    case LONG:
                                        edit.putLong(key, Util.<Long>mPreGetViewValue(context, view));
                                        break;
                                    case FLOAT:
                                        edit.putFloat(key, Util.<Float>mPreGetViewValue(context, view));
                                        break;
                                    case BOOLEAN:
                                        edit.putBoolean(key, Util.<Boolean>mPreGetViewValue(context, view));
                                        break;
                                }
                            }

                            edit.apply();
                        } catch (IllegalAccessException e) {
                            logError(context, e);
                        }
                    }

                }
            }
        }
    }

    @MainThread
    private static boolean mCustomViewValueGetter(View view, Editor edit,
                                                  String key) {
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
    private static <T> T mPreGetViewValue(GetSetViewValue context, View view) {
        T result;

        if (view instanceof GetSetValue)
            result = (T) ((GetSetValue) view).getViewValue();
        else
            result = (T) context.getViewValue(view);

        return result;
    }

    //endregion

    //endregion

    public static <T extends ReportError> void logError(@NonNull T object, @NonNull Throwable e) {
        Class<? extends ReportError> clazz = object.getClass();
        String tag = clazz.getSimpleName();

        String errorMessage = e.getMessage();
        object.showError(errorMessage);
        Log.e(tag, errorMessage);
    }

    public static void startFragment(Activity activity, Class fragmentClass) throws AEException {
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();

            FragmentManager fm = activity.getFragmentManager();
            fm.beginTransaction().replace(R.id.container_fragment_1, fragment).commit();
        } catch (InstantiationException e) {
            throw new AEException(e);
        } catch (IllegalAccessException e) {
            throw new AEException(e);
        }
    }

}
