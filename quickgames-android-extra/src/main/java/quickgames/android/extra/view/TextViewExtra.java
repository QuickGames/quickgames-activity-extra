package quickgames.android.extra.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import quickgames.android.extra.interfaces.GetSetValue;

public class TextViewExtra extends TextView
        implements GetSetValue<String> {

    //region CONSTRUCTOR

    public TextViewExtra(Context context) {
        super(context);
    }

    public TextViewExtra(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewExtra(Context context, AttributeSet attrs, int defStyleAttr) {
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
        CharSequence charSequence = getText();
        return charSequence.toString();
    }

    //endregion

}
