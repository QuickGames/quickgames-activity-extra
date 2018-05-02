package quickgames.extra;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

public final class RNames {

    private RNames() {
    }

    //region PRIVATE_METHODS

    static int getId(@NonNull Context context, String defType, String name) {
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        return resources.getIdentifier(name, defType, packageName);
    }

    //endregion

    public static final class id {

        //region PRIVATE

        private static final String DEF_TYPE = "id";

        //endregion

        //region PUBLIC_FIELDS

        public static final String ACTIVITY_DRAWER = string.ID_ACTIVITY_DRAWER;
        public static final String NAVIGATION_VIEW = string.ID_NAVIGATION_VIEW;

        //endregion

        public static int getId(@NonNull Context context, String name) {
            return RNames.getId(context, DEF_TYPE, name);
        }

    }

    public static final class layout {

        //region PRIVATE

        private static final String DEF_TYPE = "layout";

        //endregion

        //region PUBLIC_FIELDS

        public static final String ACTIVITY_DRAWER = string.LAYOUT_ACTIVITY_DRAWER;

        //endregion

        public static int getId(@NonNull Context context, String name) {
            return RNames.getId(context, DEF_TYPE, name);
        }

    }

    public static final class string {

        //region PRIVATE

        private static final String DEF_TYPE = "string";

        //endregion

        //region FIELDS

        static final String LAYOUT_ACTIVITY_DRAWER = "activity_drawer";
        static final String ID_ACTIVITY_DRAWER = "activity_drawer";
        static final String ID_NAVIGATION_VIEW = "navigation_view";

        //endregion

        //region PUBLIC_FIELDS

//        public static final String APP_VERSION_VERSION_NAME = "app_version_version";
//        public static final String APP_VERSION_BUILD_NAME = "app_version_build";
//        public static final String APP_VERSION_DATE_NAME = "app_version_date";
//        public static final String APP_VERSION_VERSION_TEXT_NAME = "app_version_version_text";
//        public static final String APP_VERSION_DATE_TEXT_NAME = "app_version_date_text";

        //endregion

        public static int getId(@NonNull Context context, String name) {
            return RNames.getId(context, DEF_TYPE, name);
        }

    }

}

