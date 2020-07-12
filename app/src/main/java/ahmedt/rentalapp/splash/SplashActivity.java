package ahmedt.rentalapp.splash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.pixplicity.easyprefs.library.Prefs;

import ahmedt.rentalapp.MainActivity;
import ahmedt.rentalapp.R;
import ahmedt.rentalapp.login.LoginActivity;
import ahmedt.rentalapp.utils.SessionPrefs;
import ahmedt.rentalapp.utils.UpdateHelper;

public class SplashActivity extends AppCompatActivity implements UpdateHelper.OnUpdateCheckLinstener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UpdateHelper.with(this)
                .onUpdateCheck(this)
                .check();

        if (UpdateHelper.keyUpdateEnable()) {

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Prefs.getString(SessionPrefs.isLogin, "").matches("1")) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }, 2000);
        }
    }

    @Override
    public void onUpdateCheckListener(final String urlApp) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.newupdate);
        alert.setMessage(R.string.plzupdate)
                .setCancelable(false)
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlApp)));
                        }
                    }
                })
                .setNegativeButton(R.string.nothx, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
        alert.show();
    }
}
