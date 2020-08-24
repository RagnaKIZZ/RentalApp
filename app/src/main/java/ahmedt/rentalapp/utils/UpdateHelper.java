package ahmedt.rentalapp.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class UpdateHelper {

    public static String KEY_UPDATE_ENABLE = "is_update";
    public static String KEY_UPDATE_VERSION = "version";
    public static String KEY_UPDATE_URL = "update_url";

    public interface OnUpdateCheckLinstener {
        void onUpdateCheckListener(String urlApp);
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    private OnUpdateCheckLinstener onUpdateCheckLinstener;
    private Context context;

    public UpdateHelper(Context context, OnUpdateCheckLinstener onUpdateCheckLinstener) {
        this.onUpdateCheckLinstener = onUpdateCheckLinstener;
        this.context = context;
    }

    public void check() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        if (remoteConfig.getBoolean(KEY_UPDATE_ENABLE)) {
            String currentVersion = remoteConfig.getString(KEY_UPDATE_VERSION);
            String appVersion = getAppVersion(context);
            String updateURL = remoteConfig.getString(KEY_UPDATE_URL);

            if (!TextUtils.equals(currentVersion, appVersion) && onUpdateCheckLinstener != null) {
                onUpdateCheckLinstener.onUpdateCheckListener(updateURL);
            }
        }
    }


    public static boolean keyUpdateEnable() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        return remoteConfig.getBoolean(KEY_UPDATE_ENABLE);
    }

    private String getAppVersion(Context context) {
        String result = "";
        try {
            result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static class Builder {

        private Context context;
        private OnUpdateCheckLinstener onUpdateCheckLinstener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateCheck(OnUpdateCheckLinstener onUpdateCheckLinstener) {
            this.onUpdateCheckLinstener = onUpdateCheckLinstener;
            return this;
        }

        public UpdateHelper build() {
            return new UpdateHelper(context, onUpdateCheckLinstener);
        }

        public UpdateHelper check() {
            UpdateHelper updateHelper = build();
            updateHelper.check();

            return updateHelper;
        }


    }
}
