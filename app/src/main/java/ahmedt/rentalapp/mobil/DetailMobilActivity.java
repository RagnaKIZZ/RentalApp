package ahmedt.rentalapp.mobil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.mobil.mobilmodel.DataItem;

public class DetailMobilActivity extends AppCompatActivity {
    private TextView txtWarna, txtTahun, txtTransmisi, txtKursi, txtPintu, txtBag, txTipe, txtHarga, txtNama;
    private SwitchMaterial switchMaterial;
    private ImageView imMobil;
    private Button btnMakeOrder, btnClock;
    private EditText edtStart, edtEnd;

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
        Intent i = getIntent();
        DataItem item = i.getParcelableExtra("data_item");

        String nama;
    }
}
