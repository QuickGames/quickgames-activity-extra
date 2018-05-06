package quickgames.android.extra.interfaces;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public interface ReportError {

    void logError(Throwable e);

    //region SHOW_ERROR

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param text The text to show.  Can be formatted text.
     */
    void showError(@NonNull CharSequence text);

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param resId The resource id of the string resource to use.  Can be formatted text.
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    void showError(@StringRes int resId);

    //endregion

}
