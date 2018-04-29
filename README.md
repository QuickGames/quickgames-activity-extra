# quickgames-activity-extra
Create repository:
- Added activity params;
- Added field id;
- Added field preferences get and put.


// Example of work:

package quickgames.module.activity.extra;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import quickgames.activity.extra.AppCompatActivityExtra;
import quickgames.activity.extra.annotation.ActivityParams;
import quickgames.activity.extra.annotation.FieldId;
import quickgames.activity.extra.annotation.FieldPreference;
import quickgames.activity.extra.annotation.FieldType;

@ActivityParams(layoutResID = R.layout.activity_main, titleResId = R.string.app_name)
public class ActivityMain extends AppCompatActivityExtra {

    @FieldId(R.id.textView)
    @FieldPreference(key = "textView", fieldType = FieldType.STRING)
    public TextView textView;

    @FieldId(R.id.editText)
    @FieldPreference(key = "editText", fieldType = FieldType.STRING, saveValue = true)
    public TextView editText;

    @Nullable
    @Override
    protected <TypeValue> TypeValue getViewValue(View view) {
        if (view.equals(editText))
            return (TypeValue) editText.getText().toString();

        return super.getViewValue(view);
    }

    @Override
    protected <TypeValue> void setViewValue(View view, TypeValue value) {

        if (view.equals(textView))
            textView.setText((CharSequence) value);
        if (view.equals(editText))
            editText.setText((CharSequence) value);

    }
}
