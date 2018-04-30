package quickgames.extra;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public final class Util {

    private Util() {
    }

    public static SharedPreferences getSharedPreferences(Activity activity){
        String packageName = activity.getPackageName();
        Context applicationContext = activity.getApplicationContext();
        return applicationContext.getSharedPreferences(packageName, Context.MODE_PRIVATE);
    }

}
