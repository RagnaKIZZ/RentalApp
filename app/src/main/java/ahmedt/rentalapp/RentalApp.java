package ahmedt.rentalapp;

import android.app.Application;
import android.content.ContextWrapper;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.HashMap;
import java.util.Map;

import ahmedt.rentalapp.utils.UpdateHelper;

public class RentalApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        Map<String, Object> defaultValue = new HashMap<>();
        defaultValue.put(UpdateHelper.KEY_UPDATE_ENABLE, false);
        defaultValue.put(UpdateHelper.KEY_UPDATE_VERSION, "1.0.0");
        defaultValue.put(UpdateHelper.KEY_UPDATE_URL, "https://play.google.com/store/apps/details?id=ahmedt.rentalapp");


        remoteConfig.setDefaults(defaultValue);
        remoteConfig.fetch(1) //fetch data from firebase
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            remoteConfig.activateFetched();
                        }
                    }
                });

        AndroidNetworking.initialize(this);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
