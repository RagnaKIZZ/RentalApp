package ahmedt.rentalapp.booking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.pixplicity.easyprefs.library.Prefs;

import ahmedt.rentalapp.MainActivity;
import ahmedt.rentalapp.R;
import ahmedt.rentalapp.login.LoginActivity;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.SessionPrefs;
import ahmedt.rentalapp.utils.UniversalModel;
import ahmedt.rentalapp.utils.UrlServer;
import okhttp3.Response;

public class BookingActivity extends AppCompatActivity {
    private static final String TAG = "BookingActivity";
    private ImageView imgMobil, imgBca, imgWa;
    private TextView txtStart, txtEnd, txtTotal, txtNama, txtTipe, txtTipeOrder, txtHarga, txtByr, txtTf;
    private CardView cvLangsung, cvTransfer;
    private ProgressBar progressBar;
    private Button btnOrder;
    private Context context;
    int payParam = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Detail Booking");

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
        imgMobil = findViewById(R.id.img_mobil_detail_booking);
        txtStart = findViewById(R.id.txt_tanggal_pinjam_booking);
        txtEnd = findViewById(R.id.txt_tanggal_kembali_booking);
        txtTotal = findViewById(R.id.txt_total_hari);
        txtNama = findViewById(R.id.txt_nama_mobil_booking);
        txtTipe = findViewById(R.id.txt_tipe_mobil_booking);
        txtTipeOrder = findViewById(R.id.txt_tipe_booking);
        txtHarga = findViewById(R.id.txt_harga_booking);
        txtByr = findViewById(R.id.txt_bayar_ditempat);
        txtTf = findViewById(R.id.txttransfer);
        progressBar = findViewById(R.id.progress_bar);
        cvLangsung = findViewById(R.id.cv_bayar_ditempat);
        cvTransfer = findViewById(R.id.cv_bayar_transfer);
        btnOrder = findViewById(R.id.btn_booking);
        imgWa = findViewById(R.id.imgwa);
        imgBca = findViewById(R.id.img_bca);

        Intent i = getIntent();
        final String mobil_id = i.getStringExtra("id_mobil");
        String nama = i.getStringExtra("nama");
        String tipe = i.getStringExtra("tipe");
        String start_date = i.getStringExtra("start_date");
        String end_date = i.getStringExtra("end_date");
        final int isSupir = i.getIntExtra("isSupir", 0);
        String jam = i.getStringExtra("jam");
        final String harga = i.getStringExtra("harga");
        String total_date = i.getStringExtra("total_date");
        String foto = i.getStringExtra("foto");
        final String startDate = start_date + " " + jam;
        final String endDate = end_date + " " + jam;
        String tipe_order = "";
        double convHarga = Double.parseDouble(harga);
        double totalHari = Double.parseDouble(total_date);
        double supir = 300000;
        double totalHarga = 0;

        txtNama.setText(nama);
        txtTipe.setText("Tipe: " + tipe);
        if (isSupir == 1) {
            tipe_order = "Mobil saja";
            totalHarga = (convHarga) * totalHari;
        } else {
            tipe_order = "Mobil dan Supir";
            totalHarga = (convHarga + supir) * totalHari;
        }
        HelperClass.convertHarga(txtHarga, totalHarga, "");
        txtTipeOrder.setText(tipe_order);
        txtTotal.setText(total_date + " hari");

        if (foto.isEmpty()) {
            progressBar.setVisibility(View.GONE);
        } else {
            HelperClass.loadGambar(this, UrlServer.URL_FOTO_MOBIL + foto, progressBar, imgMobil);
        }
        HelperClass.parseDate2(startDate, txtStart);
        HelperClass.parseDate2(endDate, txtEnd);

        cvLangsung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet reducer = (AnimatorSet) AnimatorInflater.loadAnimator(BookingActivity.this, R.animator.reduce_size);
                reducer.setTarget(v);
                reducer.start();

                if (payParam == 2) {
                    AnimatorSet regainer = (AnimatorSet) AnimatorInflater.loadAnimator(BookingActivity.this, R.animator.regain_size);
                    regainer.setTarget(cvTransfer);
                    regainer.start();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        txtTf.setBackgroundColor(context.getColor(R.color.colorPrimary));
                    } else {
                        //noinspection deprecation
                        txtTf.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    }
                }
                payParam = 1;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    txtByr.setBackgroundColor(context.getColor(R.color.warningColor));
                } else {
                    //noinspection deprecation
                    txtByr.setBackgroundColor(context.getResources().getColor(R.color.warningColor));
                }

            }
        });

        cvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet reducer = (AnimatorSet) AnimatorInflater.loadAnimator(BookingActivity.this, R.animator.reduce_size);
                reducer.setTarget(v);
                reducer.start();

                if (payParam == 1) {
                    AnimatorSet regainer = (AnimatorSet) AnimatorInflater.loadAnimator(BookingActivity.this, R.animator.regain_size);
                    regainer.setTarget(cvLangsung);
                    regainer.start();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        txtByr.setBackgroundColor(context.getColor(R.color.colorPrimary));
                    } else {
                        //noinspection deprecation
                        txtByr.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    }
                }
                payParam = 2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    txtTf.setBackgroundColor(context.getColor(R.color.warningColor));
                } else {
                    //noinspection deprecation
                    txtTf.setBackgroundColor(context.getResources().getColor(R.color.warningColor));
                }
            }
        });

        Glide.with(context)
                .load(R.drawable.wa)
                .apply(new RequestOptions().override(100, 100))
                .into(imgWa);

        Glide.with(context)
                .load(R.drawable.bca)
                .apply(new RequestOptions().override(200, 200))
                .into(imgBca);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payParam != 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Booking");
                    alert.setMessage("Apakah Anda yakin ingin booking?")
                            .setCancelable(true)
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (context != null) {
                                        makeOrder(mobil_id, startDate, endDate, String.valueOf(payParam), String.valueOf(isSupir), harga);
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
                } else {
                    Toast.makeText(context, "Pilih metode pembayaran!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void makeOrder(String mobil_id, String start_date, String end_date, String metode, String jenis, String harga) {
        final ProgressDialog dialog = new ProgressDialog(this);
        HelperClass.loading(dialog, null, null, false);
        AndroidNetworking.post(UrlServer.URL_ORDER)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .addBodyParameter("mobil_id", mobil_id)
                .addBodyParameter("start_date", start_date)
                .addBodyParameter("end_date", end_date)
                .addBodyParameter("metode", metode)
                .addBodyParameter("jenis", jenis)
                .addBodyParameter("harga", harga)
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        dialog.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Toast.makeText(context, "Booking Mobil Sukses", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(context, MainActivity.class));
                                finishAffinity();
                            } else {
                                Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toast.makeText(context, R.string.cek_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
