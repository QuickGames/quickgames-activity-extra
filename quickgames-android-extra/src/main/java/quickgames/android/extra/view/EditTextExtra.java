package quickgames.android.extra.view;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;

import quickgames.android.extra.interfaces.GetSetValue;

public class EditTextExtra extends EditText
        implements GetSetValue<String> {

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
