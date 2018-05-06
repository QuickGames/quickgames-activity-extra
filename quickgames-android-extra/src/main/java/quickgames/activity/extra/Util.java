package quickgames.activity.extra;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @deprecated As of release 0.0.2,
 * replaced by {@link #(quickgames.android.extra.Util)},
 * will be deleted of release 0.1
 */
@Deprecated
public class Util {

    private Util() {
    }

    public static SharedPreferences getSharedPreferences(Activity activity){
        String packageName = activity.getPackageName();
        Context applicationContext = activity.getApplicationContext();
        return applicationContext.getSharedPreferences(packageName, Context.MODE_PRIVATE);
    }

}
