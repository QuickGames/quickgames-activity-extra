package quickgames.android.extra.interfaces;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.view.View;

public interface GetSetViewValue {
    @MainThread
    void setViewValue(View view, Object value);

    @Nullable
    @MainThread
    Object getViewValue(View view);
}
