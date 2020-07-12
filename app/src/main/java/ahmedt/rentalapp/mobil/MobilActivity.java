package ahmedt.rentalapp.mobil;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.mobil.mobilmodel.DataItem;
import ahmedt.rentalapp.mobil.mobilmodel.MobilAdapter;
import ahmedt.rentalapp.mobil.mobilmodel.MobilModel;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.SessionPrefs;
import ahmedt.rentalapp.utils.UrlServer;
import okhttp3.Response;

public class MobilActivity extends AppCompatActivity {
    private static final String TAG = "MobilActivity";
    private RecyclerView rv_mobil;
    private ArrayList<DataItem> list = new ArrayList<>();
    private MobilAdapter adapter = new MobilAdapter(this, list);
    private LinearLayout include_lay;
    private ImageView imgMsg;
    private Button btnRefresh;
    private TextView txtMsg;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobil);

        findView();
    }

    private void findView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daftar Mobil");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv_mobil = findViewById(R.id.rv_mobil);
        progressBar = findViewById(R.id.progress_bar);
        include_lay = findViewById(R.id.include_lay);
        imgMsg = findViewById(R.id.img_message);
        txtMsg = findViewById(R.id.txt_msg);
        btnRefresh = findViewById(R.id.btn_refresh);
        swipeRefreshLayout = findViewById(R.id.swipe_up_layout_mobil);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_mobil.setLayoutManager(linearLayoutManager);
        rv_mobil.setHasFixedSize(true);
        rv_mobil.setAdapter(adapter);

        Intent i = getIntent();
        final String tipe = i.getStringExtra("tipe");
        final String alamat = i.getStringExtra("alamat");
        include_lay.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        Log.d(TAG, "findView: tipe" +tipe);
        Log.d(TAG, "findView: alamat"+alamat);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeRefreshLayout.isRefreshing()){
                    if (include_lay.getVisibility() == View.VISIBLE){
                        include_lay.setVisibility(View.GONE);
                    }
                    getMobil(tipe, alamat);
                }
            }
        });
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (include_lay.getVisibility() == View.VISIBLE){
//                    include_lay.setVisibility(View.GONE);
//                }
//                getMobil(tipe, alamat);
//            }
//        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (include_lay.getVisibility() == View.VISIBLE) {
                    include_lay.setVisibility(View.GONE);
                }
                getMobil(tipe, alamat);
            }
        });

        getMobil(tipe, alamat);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) this.getSystemService(this.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        EditText searchEdit = ((EditText) searchView.findViewById(R.id.search_src_text));
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Cari");

        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(40);
        fArray[1] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {

                    if (!Character.isLetterOrDigit(source.charAt(i)))
                        return "";
                }
                return null;
            }
        };
        searchEdit.setFilters(fArray);
        View v = searchView.findViewById(R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<DataItem> filterList = new ArrayList<DataItem>();
                if (s.length() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getNama().toLowerCase().contains(s.toString().toLowerCase())) {
                            filterList.add(list.get(i));
                            adapter.updateList(filterList);
                        }
                    }
                    swipeRefreshLayout.setEnabled(false);
                } else {
                    adapter.updateList(list);
                    swipeRefreshLayout.setEnabled(true);
                }
                return false;
            }
        });


        return true;

    }

    private void getMobil(String tipe, String alamat) {
        if (swipeRefreshLayout.isRefreshing()) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        AndroidNetworking.post(UrlServer.URL_MOBIL)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .addBodyParameter("jenis", tipe)
                .addBodyParameter("nama", "")
                .addBodyParameter("alamat_id", alamat)
                .build()
                .getAsOkHttpResponseAndObject(MobilModel.class, new OkHttpResponseAndParsedRequestListener<MobilModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, MobilModel response) {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                include_lay.setVisibility(View.GONE);
                                list.clear();
                                adapter.updateList(list);
                                for (int i = 0; i < response.getData().size(); i++) {
                                    final DataItem item = new DataItem();
                                    item.setAirBag(response.getData().get(i).getAirBag());
                                    item.setAlamatId(response.getData().get(i).getAlamatId());
                                    item.setFoto(response.getData().get(i).getFoto());
                                    item.setHarga(response.getData().get(i).getHarga());
                                    item.setJenis(response.getData().get(i).getJenis());
                                    item.setKursi(response.getData().get(i).getKursi());
                                    item.setMobilId(response.getData().get(i).getMobilId());
                                    item.setNama(response.getData().get(i).getNama());
                                    item.setPintu(response.getData().get(i).getPintu());
                                    item.setStatus(response.getData().get(i).getStatus());
                                    item.setTahun(response.getData().get(i).getTahun());
                                    item.setTransmisi(response.getData().get(i).getTransmisi());
                                    item.setWarna(response.getData().get(i).getWarna());
                                    item.setTipe(response.getData().get(i).getTipe());
                                    list.add(item);
                                }
                                adapter.updateList(list);
                            } else {
                                if (!list.isEmpty()) {
                                    Toast.makeText(MobilActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                } else {
                                    HelperClass.responseError(include_lay, imgMsg, R.drawable.ic_directions_car_gray_24dp, txtMsg, getString(R.string.no_cars));
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            if (!list.isEmpty()){
                                Toast.makeText(MobilActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                            }else{
                                HelperClass.responseError(include_lay, imgMsg, R.drawable.ic_cloud_off_black_24dp, txtMsg, getString(R.string.servererrorr));
                            }
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            if (!list.isEmpty()){
                                Toast.makeText(MobilActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT).show();
                            }else{
                                HelperClass.responseError(include_lay, imgMsg, R.drawable.ic_signal_wifi_off_black_24dp, txtMsg, getString(R.string.nointernett));
                            }
                        }
                    }
                });
    }
}
