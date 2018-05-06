package quickgames.android.extra.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;

import quickgames.android.extra.RNames;
import quickgames.android.extra.annotation.FieldName;
import quickgames.android.extra.annotation.LayoutParamsName;

@LayoutParamsName(layoutRes = RNames.layout.ACTIVITY_DRAWER)
public class ActivityDrawer extends ActivityContainer {

    //region FIELDS

    @FieldName(RNames.id.ACTIVITY_DRAWER)
    public DrawerLayout drawer;

    //endregion

    //region OVERRIDE

    //region SHOW_ERROR

    /**
     * Make a Snackbar to display a message
     *
     * <p>Snackbar will try and find a parent view to hold Snackbar's view from the value given
     * to {@code view}. Snackbar will walk up the view tree trying to find a suitable parent,
     * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
     * whichever comes first.
     *
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows Snackbar to enable
     * certain features, such as swipe-to-dismiss and automatically moving of widgets like
     * {@link FloatingActionButton}.
     *
     * @param text     The text to show.  Can be formatted text.
     */
    @Override
    public void showError(@NonNull CharSequence text) {
        if (drawer == null) {
            super.showError(text);
        } else
            Snackbar.make(drawer, text, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Make a Snackbar to display a message.
     *
     * <p>Snackbar will try and find a parent view to hold Snackbar's view from the value given
     * to {@code view}. Snackbar will walk up the view tree trying to find a suitable parent,
     * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
     * whichever comes first.
     *
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows Snackbar to enable
     * certain features, such as swipe-to-dismiss and automatically moving of widgets like
     * {@link FloatingActionButton}.
     *
     * @param resId    The resource id of the string resource to use. Can be formatted text.
     */
    @Override
    public void showError(int resId) {
        if (drawer == null) {
            super.showError(resId);
        } else
            Snackbar.make(drawer, resId, Snackbar.LENGTH_SHORT).show();
    }

    //endregion

    //endregion

}
