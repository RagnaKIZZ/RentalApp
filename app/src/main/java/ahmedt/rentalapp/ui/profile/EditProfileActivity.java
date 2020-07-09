package ahmedt.rentalapp.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.regex.Pattern;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.SessionPrefs;
import ahmedt.rentalapp.utils.UniversalModel;
import ahmedt.rentalapp.utils.UrlServer;
import es.dmoral.toasty.Toasty;
import okhttp3.Response;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditAccountActivity";
    private TextView txtTitle;
    private TextInputLayout txtName, txtEmail, txtPhone, txtNewPass, txtReNewPass, txtPass;
    private Context ctx;
    private Button btnChange;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=\\S+$)" +
                    ".{8,}" +
                    "$"
            );

    private static final Pattern PHONE_NUMB
            = Pattern.compile(
            "^[+]?[08][0-9]{10,13}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Edit Profil");
        ctx = this;
        findView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findView() {
        txtName = findViewById(R.id.txt_name_profile);
        txtEmail = findViewById(R.id.txt_email_profile);
        txtPhone = findViewById(R.id.txt_hp_profile);
        txtNewPass = findViewById(R.id.txt_new_password_profile);
        txtReNewPass = findViewById(R.id.txt_repassword_profile);
        txtPass = findViewById(R.id.txt_password_profile);
        btnChange = findViewById(R.id.btn_edit_profile);

        Intent i = getIntent();
        final String code = i.getStringExtra("code");
        Log.d(TAG, "findView: " + code);
        if (code.equals("1")) {
            txtName.setVisibility(View.VISIBLE);
        } else if (code.equals("2")) {
            txtEmail.setVisibility(View.VISIBLE);
        } else if (code.equals("3")) {
            txtPhone.setVisibility(View.VISIBLE);
        } else if (code.equals("4")) {
            txtNewPass.setVisibility(View.VISIBLE);
            txtReNewPass.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "findView: ");
        }

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.equals("1")) {
                    changeName(code);
                } else if (code.equals("2")) {
                    changeEmail(code);
                } else if (code.equals("3")) {
                    changePhone(code);
                } else if (code.equals("4")) {
                    changePass(code);
                } else {
                    Toast.makeText(ctx, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "findView: ");
                }
            }
        });
    }

    private void changeName(String code) {
        String name = txtName.getEditText().getText().toString().trim();
        String pass = txtPass.getEditText().getText().toString().trim();
        if (!name.isEmpty() && !pass.isEmpty()) {
            changeProfile(UrlServer.URL_CHANGE_NAME, "nama", name, pass, SessionPrefs.NAMA, code);
        } else {
            Toast.makeText(ctx, R.string.lengkapi_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void changeEmail(String code) {
        String email = txtEmail.getEditText().getText().toString().trim();
        String pass = txtPass.getEditText().getText().toString().trim().toLowerCase();
        if (!email.isEmpty() && !pass.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            changeProfile(UrlServer.URL_CHANGE_EMAIL, "email", email, pass, SessionPrefs.EMAIL, code);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(ctx, R.string.valid_email, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ctx, R.string.lengkapi_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void changePhone(String code) {
        String phone = txtPhone.getEditText().getText().toString().trim();
        String pass = txtPass.getEditText().getText().toString().trim();
        if (!phone.isEmpty() && !pass.isEmpty() && PHONE_NUMB.matcher(phone).matches()) {
            changeProfile(UrlServer.URL_CHANGE_TELEPON, "telepon", phone, pass, SessionPrefs.TELEPON, code);
        } else if (!PHONE_NUMB.matcher(phone).matches()) {
            Toast.makeText(ctx, R.string.valid_number, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ctx, R.string.lengkapi_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void changePass(String code) {
        String newPass = txtNewPass.getEditText().getText().toString().trim();
        String rePass = txtReNewPass.getEditText().getText().toString().trim();
        String pass = txtPass.getEditText().getText().toString().trim();
        if (!newPass.isEmpty() && !pass.isEmpty() && rePass.equals(newPass) && PASSWORD_PATTERN.matcher(newPass).matches()) {
            changeProfile(UrlServer.URL_CHANGE_PASSWORD, "password_baru", newPass, pass, "", code);
        } else if (!PASSWORD_PATTERN.matcher(newPass).matches()) {
            Toast.makeText(ctx, R.string.password_must_contains, Toast.LENGTH_LONG).show();
        } else if (!rePass.equals(newPass)) {
            Toast.makeText(ctx, R.string.doesnt_match, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ctx, R.string.lengkapi_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void changeProfile(String url, String param, final String value, String pass, final String prefsParam, final String code) {
        final ProgressDialog hud = new ProgressDialog(ctx);
        HelperClass.loading(hud, null, null, false);
        AndroidNetworking.post(url)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token_login", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .addBodyParameter("password", pass)
                .addBodyParameter(param, value)
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        if (okHttpResponse.isSuccessful()) {
                            hud.dismiss();
                            if (response.getCode() == 200) {
                                Toasty.success(ctx, "Change data success!", Toast.LENGTH_SHORT).show();
                                if (code != "4") {
                                    Prefs.putString(prefsParam, value);
                                }
                                Intent i = new Intent();
                                i.putExtra("extra", value);
                                setResult(RESULT_OK, i);
                                finish();
                            } else {
                                Log.d(TAG, "onResponse: " + response.getMsg());
                                Toast.makeText(ctx, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hud.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toast.makeText(ctx, R.string.server_error, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toast.makeText(ctx, R.string.cek_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}