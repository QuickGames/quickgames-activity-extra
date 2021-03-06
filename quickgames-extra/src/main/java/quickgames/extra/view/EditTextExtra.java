package quickgames.extra.view;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextExtra extends EditText
        implements SetGetValue<String> {

    //region CONSTRUCTOR

    public EditTextExtra(Context context) {
        super(context);
    }

    public EditTextExtra(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextExtra(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //endregion

    //region OVERRIDE

    @Override
    public void setViewValue(String value) {
        setText(value);
    }

    @Override
    public String getViewValue() {
        Editable editable = getText();
        return editable.toString();
    }

    //endregion

}
