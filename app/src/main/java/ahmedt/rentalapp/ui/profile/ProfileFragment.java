package ahmedt.rentalapp.ui.profile;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.bumptech.glide.Glide;
import com.pixplicity.easyprefs.library.Prefs;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.login.LoginActivity;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.SessionPrefs;
import ahmedt.rentalapp.utils.UniversalModel;
import ahmedt.rentalapp.utils.UrlServer;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private ImageView imgProfile;
    private ProgressBar progressBar;
    private TextView txtNama, txtEmail, txtPhone, txtPass;
    private String foto = Prefs.getString(SessionPrefs.FOTO, "");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_logout, menu);
        final MenuItem menuItem = menu.findItem(R.id.menu_exit);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exit:
                logOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
    }

    private void findView(View view) {
        imgProfile = view.findViewById(R.id.img_profile);
        progressBar = view.findViewById(R.id.progress_bar);
        txtNama = view.findViewById(R.id.txt_nama_profile);
        txtEmail = view.findViewById(R.id.txt_email_profile);
        txtPhone = view.findViewById(R.id.txt_phone_profile);
        txtPass = view.findViewById(R.id.txt_pass_profile);

        if (foto.isEmpty()) {
            Glide.with(getActivity())
                    .load(R.drawable.blank_profile)
                    .into(imgProfile);
            progressBar.setVisibility(View.GONE);
        } else {
            HelperClass.loadGambar(getActivity(), UrlServer.URL_FOTO + foto, progressBar, imgProfile);
        }

        txtNama.setText(Prefs.getString(SessionPrefs.NAMA, ""));
        txtNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                i.putExtra("code", "1");
                startActivityForResult(i, 1);
            }
        });

        txtEmail.setText(Prefs.getString(SessionPrefs.EMAIL, ""));
        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                i.putExtra("code", "2");
                startActivityForResult(i, 2);
            }
        });

        txtPhone.setText((Prefs.getString(SessionPrefs.TELEPON, "")));
        txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                i.putExtra("code", "3");
                startActivityForResult(i, 3);
            }
        });

        txtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                i.putExtra("code", "4");
                startActivityForResult(i, 4);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DetailFotoActivity.class);
                View shareView = imgProfile;
                String transitionName = getString(R.string.img);
                ActivityOptions transOpt = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    transOpt = ActivityOptions.makeSceneTransitionAnimation(getActivity(), shareView, transitionName);
                    startActivity(i, transOpt.toBundle());
                } else {
                    startActivity(i);
                }
            }
        });
    }

    private void logOut() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("Logout");
        alert.setMessage("Are you sure want to logout?")
                .setCancelable(true)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getActivity() != null) {
                            String uid = Prefs.getString(SessionPrefs.U_ID, "");
                            String token_login = Prefs.getString(SessionPrefs.TOKEN_LOGIN, "");
                            Log.d(TAG, "onClick: " + token_login);
                            logoutUser(uid, token_login);
                        }
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }

    private void logoutUser(String id, String token) {
        if (getActivity() != null) {
            final ProgressDialog hud = new ProgressDialog(getActivity());
            HelperClass.loading(hud, null, null, false);
            AndroidNetworking.post(UrlServer.URL_LOGOUT)
                    .addBodyParameter("id", id)
                    .addBodyParameter("token", token)
                    .build()
                    .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                        @Override
                        public void onResponse(Response okHttpResponse, UniversalModel response) {
                            hud.dismiss();
                            if (okHttpResponse.isSuccessful()) {
                                if (response.getCode() == 200) {
//                                    Toasty.success(getActivity(),R.string.suc_logout, Toast.LENGTH_SHORT, true).show();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    Prefs.clear();
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            hud.dismiss();
                            if (anError.getErrorCode() != 0) {
                                Log.d(TAG, "onError: " + anError.getErrorDetail());
                                Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "onError: " + anError.getErrorCode());
                                Log.d(TAG, "onError: " + anError.getErrorBody());
                                Log.d(TAG, "onError: " + anError.getErrorDetail());
                                Toast.makeText(getActivity(), getString(R.string.cek_internet), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Prefs.getString(SessionPrefs.FOTO, "").isEmpty()) {
            Glide.with(getActivity())
                    .load(R.drawable.blank_profile)
                    .into(imgProfile);
            Log.d(TAG, "onResume: kosong: " + Prefs.getString(SessionPrefs.FOTO, ""));
        } else {
            if (!foto.equals(Prefs.getString(SessionPrefs.FOTO, ""))) {
                HelperClass.loadGambar(getActivity(), UrlServer.URL_FOTO + Prefs.getString(SessionPrefs.FOTO, ""), progressBar, imgProfile);
                Log.d(TAG, "onResume: beda" + foto);
                Log.d(TAG, "onResume: beda: " + Prefs.getString(SessionPrefs.FOTO, ""));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String extra = data.getStringExtra("extra");
            if (requestCode == 1) {
                txtNama.setText(extra);
            } else if (requestCode == 2) {
                txtEmail.setText(extra);
            } else if (requestCode == 3) {
                txtPhone.setText(extra);
            } else {

            }
        }
    }
}