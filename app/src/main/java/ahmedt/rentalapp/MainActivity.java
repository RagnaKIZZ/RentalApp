package ahmedt.rentalapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pixplicity.easyprefs.library.Prefs;

import ahmedt.rentalapp.ui.transaksi.TransaksiFragment;
import ahmedt.rentalapp.utils.SessionPrefs;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        String param = i.getStringExtra("trans");
//        Calendar calendar = Calendar.getInstance();
//        Prefs.putInt(SessionPrefs.YEAR, calendar.get(Calendar.YEAR));
//        Prefs.putInt(SessionPrefs.MONTH, calendar.get(Calendar.MONTH));
//        Prefs.putInt(SessionPrefs.DATE, calendar.get(Calendar.DAY_OF_MONTH));
        Prefs.putString(SessionPrefs.isLogin, "1");
        Log.d(TAG, "onCreate: UID" + Prefs.getString(SessionPrefs.U_ID, ""));
        Log.d(TAG, "onCreate: UID" + Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""));
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_trans, R.id.navigation_profile)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        if (param != null && param.equals("trans")) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_trans);
        }
    }

}
