package nyc.c4q.jonathancolon.inContaq.utlities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;

import javax.inject.Inject;


public class SharedPrefsUtils {

    private static final String INTRO_KEY = "intro";
    private SharedPreferences prefs;

    @Inject
    public SharedPrefsUtils(Context context) {
        prefs = getSharedPreferences(context);
    }

    private String getPrefsData(String key, @Nullable String defValue) {
        return prefs.getString(key, defValue);
    }

    private boolean getPrefsData(String key,  boolean defValue) {
        return prefs.getBoolean(key, defValue);
    }

    public void setSharedPreferencesData(String key, String defValue) {
        prefs.edit()
                .putString(key, defValue)
                .apply();
    }

    private void setSharedPreferencesData(String key, boolean defValue) {
        prefs.edit()
                .putBoolean(key, defValue)
                .apply();
    }

    public boolean hasPreviouslyOpenedApp(){
        return getPrefsData(INTRO_KEY, true);
    }

    public void setPreviouslyOpenedApp(boolean hasOpened){
        setSharedPreferencesData(INTRO_KEY, hasOpened);
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("inContaq", Context.MODE_PRIVATE);
    }
}
