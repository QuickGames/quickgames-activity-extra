package quickgames.activity.extra;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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
