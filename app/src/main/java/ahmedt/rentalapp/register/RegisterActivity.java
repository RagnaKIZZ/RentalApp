package ahmedt.rentalapp.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.UniversalModel;
import ahmedt.rentalapp.utils.UrlServer;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private TextInputLayout txtName, txtEmail, txtPhone, txtPass, txtRePass;
    private Button btnRegister;
    private Context ctx;
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
        setContentView(R.layout.activity_register);
        ctx = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Register");

        findView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findView() {
        btnRegister = findViewById(R.id.btn_register);
        txtEmail = findViewById(R.id.txt_email_register);
        txtPhone = findViewById(R.id.txt_hp_register);
        txtName = findViewById(R.id.txt_name_register);
        txtPass = findViewById(R.id.txt_password_register);
        txtRePass = findViewById(R.id.txt_repassword_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegister();
            }
        });
    }

    private void validateRegister() {
        String name, email, phone, password, rePassword;
        name = txtName.getEditText().getText().toString().trim();
        email = txtEmail.getEditText().getText().toString().trim().toLowerCase();
        phone = txtPhone.getEditText().getText().toString().trim();
        password = txtPass.getEditText().getText().toString().trim();
        rePassword = txtRePass.getEditText().getText().toString().trim();

        if (!name.isEmpty() && !password.isEmpty() && !email.isEmpty() && !phone.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                PHONE_NUMB.matcher(phone).matches() && PASSWORD_PATTERN.matcher(password).matches() && !rePassword.isEmpty() && rePassword.matches(password) ) {
            registerUser(name, email, phone, password);
        }

        if (name.isEmpty()) {
            txtName.setError(getString(R.string.cant_empty));
        } else {
            txtName.setErrorEnabled(false);
        }

        if (email.isEmpty()) {
            txtEmail.setError(getString(R.string.cant_empty));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError(getString(R.string.valid_email));
        } else {
            txtEmail.setErrorEnabled(false);
        }

        if (phone.isEmpty()) {
            txtPhone.setError(getString(R.string.cant_empty));
        } else if (!PHONE_NUMB.matcher(phone).matches()) {
            txtPhone.setError(getString(R.string.valid_number));
        } else {
            txtPhone.setErrorEnabled(false);
        }

        if (password.isEmpty()) {
            txtPass.setError(getString(R.string.cant_empty));
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            txtPass.setError(getString(R.string.password_must_contains));
        } else {
            txtPass.setErrorEnabled(false);
        }

        if (rePassword.isEmpty()) {
            txtRePass.setError(getString(R.string.cant_empty));
        } else if (!rePassword.matches(password)) {
            txtRePass.setError(getString(R.string.doesnt_match));
        } else {
            txtRePass.setErrorEnabled(false);
        }
    }

    private void registerUser(String name, String email, String phone, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        HelperClass.loading(progressDialog, null, null, false);
        AndroidNetworking.post(UrlServer.URL_REGISTER)
                .addBodyParameter("nama", name)
                .addBodyParameter("email", email)
                .addBodyParameter("telepon", phone)
                .addBodyParameter("password", password)
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        progressDialog.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Toast.makeText(ctx, response.getMsg(), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ctx, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
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
