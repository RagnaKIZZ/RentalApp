package ahmedt.rentalapp;

import android.app.Application;
import android.content.ContextWrapper;

import com.androidnetworking.AndroidNetworking;
import com.pixplicity.easyprefs.library.Prefs;

public class RentalApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidNetworking.initialize(this);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
