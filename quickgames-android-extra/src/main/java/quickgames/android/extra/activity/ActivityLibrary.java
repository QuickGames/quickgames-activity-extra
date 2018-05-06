package quickgames.android.extra.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

import java.lang.reflect.Field;

import quickgames.android.extra.R;
import quickgames.android.extra.RNames;
import quickgames.android.extra.annotation.LayoutParamsName;
import quickgames.android.extra.annotation.FieldName;

public class ActivityLibrary extends AppCompatActivityExtra {

    //region OVERRIDE

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // LayoutParams processing
        mOnCreateLayoutParams();

        // Init views
        mOnCreateFields();

        super.onCreate(savedInstanceState);
    }

    //endregion

    //region PRIVATE_PROCESSING

    /**
     * LayoutParams processing
     */
    private void mOnCreateLayoutParams() {
        Class<? extends AppCompatActivityExtra> clazz = getClass();
        LayoutParamsName layoutParams = clazz.getAnnotation(LayoutParamsName.class);
        if (layoutParams != null) {

            // layoutRes
            String layoutRes = layoutParams.layoutRes();
            @LayoutRes int layoutId = RNames.layout.getId(this, layoutRes);
            if (layoutId == 0)
                showError(R.string.err_resource_id_not_found);
            else
                setContentView(layoutId);

            // titleRes
            String titleRes = layoutParams.titleRes();
            if (!titleRes.isEmpty()) {
                @StringRes int titleResId = RNames.string.getId(this, titleRes);
                String title = getString(titleResId);
                setTitle(title);
            }
        }
    }

    /**
     * Init views
     */
    private void mOnCreateFields() {
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
                        logError(e);
                    }
                }
            }
        }
    }

    //endregion

}
