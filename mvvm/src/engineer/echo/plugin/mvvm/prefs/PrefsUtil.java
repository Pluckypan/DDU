package engineer.echo.plugin.mvvm.prefs;

import com.intellij.ide.util.PropertiesComponent;

public final class PrefsUtil {

    private static final String KEY_ACTIVITY = "KEY_FOR_ACTIVITY";
    private static final String DEFAULT_ACTIVITY = "androidx.appcompat.app.AppCompatActivity";

    public static String getBaseActivity() {
        return PropertiesComponent.getInstance().getValue(KEY_ACTIVITY, DEFAULT_ACTIVITY);
    }

    public static void setBaseActivity(String activity) {
        activity = activity != null && activity.length() > 0 ? activity : DEFAULT_ACTIVITY;
        PropertiesComponent.getInstance().setValue(KEY_ACTIVITY, activity);
    }
}
