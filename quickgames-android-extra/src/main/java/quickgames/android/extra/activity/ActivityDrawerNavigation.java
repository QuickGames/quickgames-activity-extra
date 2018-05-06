package quickgames.android.extra.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;

import quickgames.android.extra.RNames;
import quickgames.android.extra.annotation.FieldName;
import quickgames.android.extra.annotation.LayoutParamsName;

@LayoutParamsName(layoutRes = RNames.layout.ACTIVITY_DRAWER_NAVIGATION)
public class ActivityDrawerNavigation extends ActivityDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    //region FIELDS

    private MenuItem m_curMenuItem;

    @FieldName(RNames.id.NAVIGATION_VIEW)
    public NavigationView navigationView;

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
        if (m_curMenuItem != null) {
            m_curMenuItem.setChecked(true);

            onNavigationMenuItemSelected(m_curMenuItem);
        }

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

    //region VIRTUAL_METHODS

    @Nullable
    protected MenuItem onCreateNavigationMenu(Menu menu) {
        return null;
    }

    protected boolean onNavigationMenuItemSelected(@NonNull MenuItem item) {
        return false;
    }

    //endregion

}
