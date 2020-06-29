package ahmedt.rentalapp.ui.home.destinasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ahmedt.rentalapp.R;

public class DestinasiActivity extends AppCompatActivity {
    private ImageView imgDestinasi;
    private TextView txtTitle, txtAlamat, txtDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinasi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Destinasi Jakarta");
        getSupportActionBar().setElevation(0);
        findView();
    }

    private void findView(){
        imgDestinasi = findViewById(R.id.img_destinasi_desc);
        txtAlamat = findViewById(R.id.txt_alamat_destinasi);
        txtDesc = findViewById(R.id.txt_desc_destinasi);
        txtTitle = findViewById(R.id.txt_title_destinasi);

        Intent i = getIntent();
        itemDestinasi item = i.getParcelableExtra("data_item");

        Glide.with(this)
                .load(item.getFoto())
                .into(imgDestinasi);

        txtTitle.setText(item.getNama());
        txtAlamat.setText(item.getAlamat());
        txtDesc.setText(item.getDeskripsi());
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
}
