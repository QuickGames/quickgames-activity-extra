# quickgames-extra
- Reverse engineering and refactoring;
- Added processing custom views: Checkable, EditText, ViewText;
- Added processing views extends SetGetValue.


// Example of work:

    package quickgames.module.activity.extra;

    import android.widget.EditText;
    import android.widget.TextView;

    import quickgames.extra.activity.AppCompatActivityExtra;
    import quickgames.extra.annotation.ActivityParams;
    import quickgames.extra.annotation.FieldId;
    import quickgames.extra.annotation.FieldPreference;
    import quickgames.extra.annotation.FieldType;

    @ActivityParams(layoutResID = R.layout.activity_main, titleResId = R.string.app_name)
    public class ActivityMain extends AppCompatActivityExtra {

        @FieldId(R.id.textView)
        @FieldPreference(key = "textView", fieldType = FieldType.STRING)
        public TextView m_textView;

        @FieldId(R.id.editText)
        @FieldPreference(key = "editText", fieldType = FieldType.STRING, saveValue = true)
        public EditText m_editText;

    }
