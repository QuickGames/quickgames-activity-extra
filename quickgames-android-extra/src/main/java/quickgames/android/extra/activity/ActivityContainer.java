package quickgames.android.extra.activity;

import android.app.Fragment;
import android.app.FragmentManager;

import quickgames.android.extra.R;
import quickgames.android.extra.RNames;
import quickgames.android.extra.annotation.LayoutParamsName;

@LayoutParamsName(layoutRes = RNames.layout.ACTIVITY_CONTAINER)
public class ActivityContainer extends ActivityLibrary {

    //endregion

    //region PROTECTED_METHODS

    protected void startFragment(Class fragmentClass) {
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();

            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.container_fragment, fragment).commit();

        } catch (InstantiationException e) {
            logError( e);
        } catch (IllegalAccessException e) {
            logError( e);
        }
    }

    //endregion

}
