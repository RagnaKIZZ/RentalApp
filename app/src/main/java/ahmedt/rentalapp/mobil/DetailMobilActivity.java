package ahmedt.rentalapp.mobil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.booking.BookingActivity;
import ahmedt.rentalapp.mobil.mobilmodel.DataItem;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.SessionPrefs;
import ahmedt.rentalapp.utils.UrlServer;
import es.dmoral.toasty.Toasty;

public class DetailMobilActivity extends AppCompatActivity {
    private static final String TAG = "DetailMobilActivity";
    private String TOTAL_DAY = "total";
    private TextView txtWarna, txtTahun, txtTransmisi, txtKursi, txtPintu, txtBag, txTipe, txtHarga, txtNama, txtJam;
    private SwitchMaterial switchMaterial;
    private ImageView imgMobil;
    private Button btnMakeOrder, btnClock;
    private EditText edtStart, edtEnd;
    private ProgressBar progressBar;
    int isSupir = 1;
    long start, end;
    int th ;
    int bl ;
    int hr;
    String jam = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mobil);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Detail Mobil");

        findView();
    }

    private void findView(){
        txtNama = findViewById(R.id.txt_nama_mobil);
        txtTahun = findViewById(R.id.txt_tahun_mobil);
        txtWarna = findViewById(R.id.txt_warna_mobil);
        txtTransmisi = findViewById(R.id.txt_transmisi_mobil);
        txtKursi = findViewById(R.id.txt_kursi_mobil);
        txtBag = findViewById(R.id.txt_bag_mobil);
        txtPintu = findViewById(R.id.txt_pintu_mobil);
        txTipe = findViewById(R.id.txt_tipe_mobil);
        txtHarga = findViewById(R.id.txt_harga_sewa_mobil);
        imgMobil = findViewById(R.id.img_mobil_detail);
        progressBar = findViewById(R.id.progress_bar);
        switchMaterial = findViewById(R.id.switch_supir);
        btnClock = findViewById(R.id.btn_jam_sewa);
        btnMakeOrder = findViewById(R.id.btn_make_order);
        edtStart = findViewById(R.id.edt_start);
        edtEnd = findViewById(R.id.edt_end);
        txtJam = findViewById(R.id.txt_jam_dipilih);

        th = Calendar.getInstance().get(Calendar.YEAR);
        bl = Calendar.getInstance().get(Calendar.MONTH);
        getTomorrow(edtStart);

        Log.d(TAG, "findView: " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        Intent i = getIntent();
        DataItem item = i.getParcelableExtra("data_item");

        final String nama = item.getNama();
        final String foto = item.getFoto();
        String warna = item.getWarna();
        String tahun = item.getTahun();
        String transmisi = item.getTransmisi();
        final String tipe = item.getTipe();
        String kursi = item.getKursi();
        String pintu = item.getPintu();
        String bags = item.getAirBag();
        final String harga = item.getHarga();
        final String id_mobil = item.getMobilId();
        double hargaReal = Double.parseDouble(harga);

        txtNama.setText(nama);
        txTipe.setText("Tipe: " + tipe);
        txtWarna.setText(warna);
        HelperClass.convertHarga(txtHarga, hargaReal, "/hari");
        txtBag.setText(bags + " bags");
        txtPintu.setText(pintu + " doors");
        txtTransmisi.setText(transmisi);
        txtTahun.setText(tahun);
        txtKursi.setText(kursi+" seats");
        txtJam.setText("--:--");

        if (foto.isEmpty()){
            progressBar.setVisibility(View.GONE);
        }else{
            HelperClass.loadGambar(this, UrlServer.URL_FOTO_MOBIL+foto, progressBar, imgMobil);
        }

        edtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtStart.getText().toString().isEmpty()){
                    Toast.makeText(DetailMobilActivity.this, "Isikan tanggal mulai terlebih dahulu", Toast.LENGTH_SHORT).show();
                }else{
                    showDateDialog(edtEnd);
                }
            }
        });

        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtStart.getText().toString().isEmpty() && !edtEnd.getText().toString().isEmpty()){
                    showHourDialog(txtJam);
                }else{
                    Toast.makeText(DetailMobilActivity.this, R.string.lengkapi_data, Toast.LENGTH_SHORT).show();
                }
            }
        });

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSupir = 2;
                    Toast.makeText(DetailMobilActivity.this, "Menambahkan supir!", Toast.LENGTH_SHORT).show();
                } else {
                    isSupir = 1;
                    Toast.makeText(DetailMobilActivity.this, "Batal menambahkan supir!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtStart.getText().toString().isEmpty() && !edtEnd.getText().toString().isEmpty() && !jam.isEmpty()) {
                    Intent i = new Intent(DetailMobilActivity.this, BookingActivity.class);
                    i.putExtra("id_mobil", id_mobil);
                    i.putExtra("harga", harga);
                    i.putExtra("tipe", tipe);
                    i.putExtra("start_date", edtStart.getText().toString().trim());
                    i.putExtra("end_date", edtEnd.getText().toString().trim());
                    i.putExtra("isSupir", isSupir);
                    i.putExtra("jam", jam);
                    i.putExtra("nama", nama);
                    i.putExtra("total_date", TOTAL_DAY);
                    i.putExtra("foto", foto);
                    startActivity(i);
                } else {
                    Toast.makeText(DetailMobilActivity.this, R.string.lengkapi_data, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDateDialog(final EditText edtEndDate) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.d(TAG, "onDateSet: " + String.valueOf(th));
                Log.d(TAG, "onDateSet: " + String.valueOf(hr));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date endDate = calendar.getTime();
                /**
                 * Keterangan :
                 * to make condition that not allowed end date
                 * AMD
                 **/
                if (year < th) {
                    Toast.makeText(DetailMobilActivity.this, R.string.cant_endd, Toast.LENGTH_SHORT).show();
                } else if (year == th) {
                    if (month < bl) {
                        Toast.makeText(DetailMobilActivity.this, R.string.cant_endd, Toast.LENGTH_SHORT).show();
                    } else if (month == bl) {
                        if (dayOfMonth <= hr) {
                            Toast.makeText(DetailMobilActivity.this, R.string.cant_endd, Toast.LENGTH_SHORT).show();
                        } else {
                            edtEndDate.setText(simpleDateFormat.format(calendar.getTime()));
                            end = endDate.getTime();
                        }
                    } else if (month > bl) {
                        edtEndDate.setText(simpleDateFormat.format(calendar.getTime()));
                        end = endDate.getTime();
                    }
                } else if (year > th) {
                    edtEndDate.setText(simpleDateFormat.format(calendar.getTime()));
                    end = endDate.getTime();
                }
                /**
                 * Keterangan :
                 * to calculate total days
                 * AMD
                 **/
                long total = end - start;
                long difTotal = (total / (24 * 60 * 60 * 1000));
                TOTAL_DAY = String.valueOf(difTotal);
                Log.d(TAG, "onDateSet: " + TOTAL_DAY);
                Log.d(TAG, "onDateSet: "+calendar.get(Calendar.DAY_OF_MONTH));
            }
        };
        Log.d(TAG, "findView: "+Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        new DatePickerDialog(DetailMobilActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void showHourDialog(final TextView edtStartHour) {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                if (timeOfDay >= 0 && timeOfDay < 9) {
                    Toast.makeText(DetailMobilActivity.this, R.string.jam_startt, Toast.LENGTH_SHORT).show();
                } else if (timeOfDay >= 15 && timeOfDay < 24) {
                    Toast.makeText(DetailMobilActivity.this, R.string.jam_startt, Toast.LENGTH_SHORT).show();
                } else {
                    edtStartHour.setText(simpleDateFormat.format(calendar.getTime()));
                    jam = simpleDateFormat.format(calendar.getTime());
                }
            }
        };
        Log.d(TAG, "findView: "+Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        new TimePickerDialog(DetailMobilActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void getTomorrow(EditText edtStartDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date tomorrow = calendar.getTime();
        String getDate = simpleDateFormat.format(tomorrow);
        edtStartDate.setText(getDate);
        hr = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "getTomorrow: " + hr);
        start = tomorrow.getTime();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
