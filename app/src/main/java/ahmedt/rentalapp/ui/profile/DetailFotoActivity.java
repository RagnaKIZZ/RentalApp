package ahmedt.rentalapp.ui.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.ui.profile.updatemodel.UpdateModel;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.SessionPrefs;
import ahmedt.rentalapp.utils.UniversalModel;
import ahmedt.rentalapp.utils.UrlServer;
import okhttp3.Response;

public class DetailFotoActivity extends AppCompatActivity {
    private static final String TAG = "DetailFotoActivity";
    ProgressBar progressBar;
    PhotoView photoView;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_foto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Foto Profil");

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        photoView = (PhotoView) findViewById(R.id.photo_view);

        String foto = Prefs.getString(SessionPrefs.FOTO, "");
        if (foto.isEmpty()) {
            Glide.with(this)
                    .load(R.drawable.blank_profile)
                    .into(photoView);
        } else {
            HelperClass.loadGambar(DetailFotoActivity.this, UrlServer.URL_FOTO + foto, progressBar, photoView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_foto, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.foto_edit:
                ImagePicker.Companion.with(DetailFotoActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(720, 720)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
                break;
            case R.id.foto_delete:
                deleteFoto();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void deleteFoto() {
        AlertDialog.Builder alert = new AlertDialog.Builder(DetailFotoActivity.this);

        alert.setTitle("Hapus foto");
        alert.setMessage("Anda yakin ingin menghapus foto?")
                .setIcon(R.drawable.ic_baseline_delete_black_24)
                .setCancelable(true)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        hapusFoto(Prefs.getString(SessionPrefs.U_ID, ""), Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""));
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DetailFotoActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        alert.show();
    }

    private void hapusFoto(String id, String token) {
        final ProgressDialog dialog = new ProgressDialog(this);
        HelperClass.loading(dialog, null, null, false);
        AndroidNetworking.post(UrlServer.URL_HAPUS_FOTO)
                .addBodyParameter("id", id)
                .addBodyParameter("token_login", token)
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        dialog.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Glide.with(DetailFotoActivity.this)
                                        .load(R.drawable.blank_profile)
                                        .into(photoView);
                                Prefs.putString(SessionPrefs.FOTO, "");
                                Log.d(TAG, "onResponse: " + Prefs.getString(SessionPrefs.FOTO, ""));
                                Toast.makeText(DetailFotoActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailFotoActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toast.makeText(DetailFotoActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("ERR", "onError: " + anError.getErrorCode());
                            Log.d("ERR", "onError: " + anError.getErrorBody());
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toast.makeText(DetailFotoActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ImagePicker.REQUEST_CODE) {
                file = ImagePicker.Companion.getFile(data);
                uploadFoto(Prefs.getString(SessionPrefs.U_ID, ""), Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""), file);
            }
        }
    }

    private void uploadFoto(String id, String token, File file) {
        final ProgressDialog dialog = new ProgressDialog(this);
        HelperClass.loading(dialog, null, null, false);
        AndroidNetworking.upload(UrlServer.URL_UPDATE_FOTO)
                .addMultipartParameter("id", id)
                .addMultipartParameter("token_login", token)
                .addMultipartFile("foto", file)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.d(TAG, "bytesUploaded: " + (int) bytesUploaded + ", totalBytes:" + (int) totalBytes);
                    }
                })
                .getAsOkHttpResponseAndObject(UpdateModel.class, new OkHttpResponseAndParsedRequestListener<UpdateModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UpdateModel response) {
                        dialog.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                HelperClass.loadGambar(DetailFotoActivity.this, UrlServer.URL_FOTO + response.getFoto(), progressBar, photoView);
//                                Glide.with(DetailFotoActivity.this)
//                                        .load(UrlServer.URL_FOTO + response.getFoto())
//                                        .into(photoView);
                                Prefs.putString(SessionPrefs.FOTO, response.getFoto());
                                Log.d(TAG, "onResponse: " + Prefs.getString(SessionPrefs.FOTO, ""));
                                Toast.makeText(DetailFotoActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailFotoActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toast.makeText(DetailFotoActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("ERR", "onError: " + anError.getErrorCode());
                            Log.d("ERR", "onError: " + anError.getErrorBody());
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toast.makeText(DetailFotoActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}