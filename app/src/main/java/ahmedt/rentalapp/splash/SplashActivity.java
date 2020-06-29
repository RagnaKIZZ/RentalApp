package ahmedt.rentalapp.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pixplicity.easyprefs.library.Prefs;

import ahmedt.rentalapp.MainActivity;
import ahmedt.rentalapp.R;
import ahmedt.rentalapp.login.LoginActivity;
import ahmedt.rentalapp.utils.SessionPrefs;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Prefs.getString(SessionPrefs.isLogin, "").matches("1")){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }
        }, 2000);
    }
}
