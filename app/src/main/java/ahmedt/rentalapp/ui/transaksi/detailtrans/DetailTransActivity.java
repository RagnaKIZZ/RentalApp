package ahmedt.rentalapp.ui.transaksi.detailtrans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.bumptech.glide.request.RequestOptions;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.ui.transaksi.transaksimodel.DataItem;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.SessionPrefs;
import ahmedt.rentalapp.utils.UniversalModel;
import ahmedt.rentalapp.utils.UrlServer;
import okhttp3.Response;

public class DetailTransActivity extends AppCompatActivity {
    private static final String TAG = "DetailTransActivity";
    private ImageView imgMobil, imgBca;
    private TextView txtStart, txtEnd, txtTotal, txtNama, txtTipe, txtTipeOrder, txtHarga, txtStatus;
    private CardView cvLangsung, cvTransfer;
    private ProgressBar progressBar;
    private Button btnCancel, btnWa;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trans);
        context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Detail Transaksi");

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
        txtStatus = findViewById(R.id.txt_status_trans);
        progressBar = findViewById(R.id.progress_bar);
        cvLangsung = findViewById(R.id.cv_bayar_ditempat);
        cvTransfer = findViewById(R.id.cv_bayar_transfer);
        btnCancel = findViewById(R.id.btn_cancel_trans);
        btnWa = findViewById(R.id.btn_kirim_wa);
        imgBca = findViewById(R.id.img_bca);


        Intent i = getIntent();
        DataItem item = i.getParcelableExtra("data_item");
        String param = i.getStringExtra("param");

        String nama = item.getNama();
        String foto = item.getFoto();
        double harga = Double.parseDouble(item.getHarga());
        String jenisOrder = item.getJenisOrder();
        String metodePembayaran = item.getMetodePembayaran();
        String mobil_id = item.getMobilId();
        final String order_id = item.getOrderId();
        String start_date = item.getStartDate();
        String end_date = item.getEndDate();
        String tipe = item.getTipe();
        String status = item.getStatus();
        String tipeOrder = "";
        String stat = "";


        txtNama.setText(nama);
        txtTipe.setText("Tipe: " + tipe);
        if (foto.isEmpty()) {
            progressBar.setVisibility(View.GONE);
        } else {
            HelperClass.loadGambar(this, UrlServer.URL_FOTO_MOBIL + foto, progressBar, imgMobil);
        }

        HelperClass.convertHarga(txtHarga, harga, "");

        if (metodePembayaran.equals("1")) {
            cvLangsung.setVisibility(View.VISIBLE);
            cvTransfer.setVisibility(View.GONE);
            getStatus(false, status);
        } else {
            cvLangsung.setVisibility(View.GONE);
            cvTransfer.setVisibility(View.VISIBLE);
            getStatus(true, status);
        }

        if (jenisOrder.equals("1")) {
            tipeOrder = "Mobil saja";
        } else {
            tipeOrder = "Mobil dan Supir";
        }

        txtTipeOrder.setText(tipeOrder);

        if (foto.isEmpty()) {
            progressBar.setVisibility(View.GONE);
        } else {
            HelperClass.loadGambar(this, UrlServer.URL_FOTO_MOBIL + foto, progressBar, imgMobil);
        }

        Glide.with(context)
                .load(R.drawable.bca)
                .apply(new RequestOptions().override(200, 200))
                .into(imgBca);

        HelperClass.parseDate(start_date, txtStart);
        HelperClass.parseDate(end_date, txtEnd);

        long start, end, total;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = format.parse(start_date);
            Date endDate = format.parse(end_date);
            start = startDate.getTime();
            end = endDate.getTime();
            total = end - start;
            long difTotal = (total / (24 * 60 * 60 * 1000));
            txtTotal.setText(String.valueOf(difTotal) + " hari");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btnWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = "+628998525177"; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = context.getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Cancel");
                alert.setMessage("Apakah Anda yakin ingin membatalkan pemesanan?")
                        .setCancelable(true)
                        .setPositiveButton("ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelOrder(Prefs.getString(SessionPrefs.U_ID, ""), Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""), order_id);
                            }
                        })
                        .setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alert.show();
            }
        });

//        if (param.equals("2")){
//            btnWa.setVisibility(View.GONE);
//            btnCancel.setVisibility(View.GONE);
//        }else{
//            btnWa.setVisibility(View.VISIBLE);
//            btnCancel.setVisibility(View.VISIBLE);
//        }


    }

    private void getStatus(boolean metode, String status) {
        String stat;
        if (status.equals("0")) {
            stat = "Belum Lunas";
            if (metode) {
                btnWa.setVisibility(View.VISIBLE);
            } else {
                btnCancel.setVisibility(View.VISIBLE);
                btnWa.setVisibility(View.GONE);
            }
        } else {
            btnWa.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            if (status.equals("1")) {
                stat = "Lunas";
            } else if (status.equals("3")) {
                stat = "Cancel";
            } else {
                stat = "Dikembalikan";
            }
        }
        txtStatus.setText(stat);
    }

    private void cancelOrder(String id, String token, String order_id) {
        final ProgressDialog dialog = new ProgressDialog(DetailTransActivity.this);
        HelperClass.loading(dialog, null, null, false);
        AndroidNetworking.post(UrlServer.URL_CANCEL)
                .addBodyParameter("id", id)
                .addBodyParameter("token", token)
                .addBodyParameter("order_id", order_id)
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        dialog.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Toast.makeText(DetailTransActivity.this, "Pesanan dibatalkan!", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(DetailTransActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toast.makeText(DetailTransActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());

                            Toast.makeText(DetailTransActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}