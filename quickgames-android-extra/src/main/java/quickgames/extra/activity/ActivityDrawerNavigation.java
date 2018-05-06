package quickgames.extra.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import quickgames.android.extra.R;
import quickgames.extra.RNames;
import quickgames.extra.activity.ActivityLibrary.ActivityParamsName;

/**
 * @deprecated As of release 0.0.4,
 * replaced by {@link #(quickgames.android.extra.activity.ActivityDrawerNavigation)},
 * will be deleted of release 0.1
 */
@ActivityParamsName(layoutRes = RNames.layout.ACTIVITY_DRAWER)
public abstract class ActivityDrawerNavigation extends ActivityLibrary
        implements NavigationView.OnNavigationItemSelectedListener {

    private MenuItem m_curMenuItem;

    @FieldName(RNames.id.ACTIVITY_DRAWER)
    public DrawerLayout drawer;

    @FieldName(RNames.id.NAVIGATION_VIEW)
    public NavigationView navigationView;

    //region ABSTRACT

    protected abstract MenuItem onCreateNavigationMenu(Menu menu);
    protected abstract boolean onNavigationMenuItemSelected(@NonNull MenuItem item);

    //endregion

    //region OVERRIDE

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationView.setNavigationItemSelectedListener(this);
        m_curMenuItem = onCreateNavigationMenu(navigationView.getMenu());
        m_curMenuItem.setChecked(true);

        onNavigationMenuItemSelected(m_curMenuItem);
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // if the item is m_curMenuItem then close drawer
        if (!item.equals(m_curMenuItem)) {

            if (!onNavigationMenuItemSelected(item))
                return false;

            m_curMenuItem.setChecked(false);
            item.setChecked(true);

            CharSequence title = item.getTitle();
            setTitle(title);
            m_curMenuItem = item;
        }

        // hide navigation menu
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //endregion

    //region PROTECTED_METHODS

    protected void startFragment(Class fragmentClass) {
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();

            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.container_fragment, fragment).commit();

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //region SNACKBAR_SHOW

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
    protected void showError(@NonNull CharSequence text) {
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
    protected void showError(@StringRes int resId) {
        Snackbar.make(drawer, resId, Snackbar.LENGTH_SHORT).show();
    }

    //endregion

    //endregion

}
