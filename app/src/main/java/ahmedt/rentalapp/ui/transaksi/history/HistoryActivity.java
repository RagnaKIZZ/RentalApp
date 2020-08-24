package ahmedt.rentalapp.ui.transaksi.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.ui.transaksi.detailtrans.DetailTransActivity;
import ahmedt.rentalapp.ui.transaksi.transaksimodel.DataItem;
import ahmedt.rentalapp.ui.transaksi.transaksimodel.TransaksiAdapterV2;
import ahmedt.rentalapp.ui.transaksi.transaksimodel.TransaksiModel;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.SessionPrefs;
import ahmedt.rentalapp.utils.UrlServer;
import okhttp3.Response;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "TransaksiFragment";
    private RecyclerView rvTrans;
    private ArrayList<DataItem> list = new ArrayList<>();
    private HistoryAdapter adapter;
    private LinearLayout include_lay;
    private Button btnRefresh;
    private TextView txtMsg;
    private ImageView imgMsg;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Riwayat Transaksi");

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
        rvTrans = findViewById(R.id.rv_transaksi);
        include_lay = findViewById(R.id.include_lay);
        imgMsg = findViewById(R.id.img_message);
        txtMsg = findViewById(R.id.txt_msg);
        btnRefresh = findViewById(R.id.btn_refresh);
        swipeRefreshLayout = findViewById(R.id.swipe_up_layout_transaksi);
        progressBar = findViewById(R.id.progress_bar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HistoryActivity.this);
        adapter = new HistoryAdapter(HistoryActivity.this, list);
        rvTrans.setLayoutManager(linearLayoutManager);
        rvTrans.setHasFixedSize(true);
        rvTrans.setAdapter(adapter);

        adapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position, DataItem model) {
                Intent i = new Intent(HistoryActivity.this, DetailTransActivity.class);
                i.putExtra("data_item", list.get(position));
                i.putExtra("param", "2");
                startActivity(i);
            }
        });

        include_lay.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeRefreshLayout.isRefreshing()) {
                    if (include_lay.getVisibility() == View.VISIBLE) {
                        include_lay.setVisibility(View.GONE);
                    }
                    getListOrder();
                }
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                include_lay.setVisibility(View.GONE);
                getListOrder();
            }
        });

        getListOrder();
    }

    private void getListOrder() {
        if (swipeRefreshLayout.isRefreshing()) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        AndroidNetworking.post(UrlServer.URL_ORDERAN)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .addBodyParameter("tipe", "2")
                .build()
                .getAsOkHttpResponseAndObject(TransaksiModel.class, new OkHttpResponseAndParsedRequestListener<TransaksiModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, TransaksiModel response) {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                list.clear();
                                adapter.updateList(list);
                                for (int i = 0; i < response.getData().size(); i++) {
                                    DataItem item = new DataItem();
                                    item.setEndDate(response.getData().get(i).getEndDate());
                                    item.setFinishDate(response.getData().get(i).getFinishDate());
                                    item.setFoto(response.getData().get(i).getFoto());
                                    item.setHarga(response.getData().get(i).getHarga());
                                    item.setJenisOrder(response.getData().get(i).getJenisOrder());
                                    item.setMetodePembayaran(response.getData().get(i).getMetodePembayaran());
                                    item.setMobilId(response.getData().get(i).getMobilId());
                                    item.setNama(response.getData().get(i).getNama());
                                    item.setOrderDate(response.getData().get(i).getOrderDate());
                                    item.setOrderId(response.getData().get(i).getOrderId());
                                    item.setStartDate(response.getData().get(i).getStartDate());
                                    item.setStatus(response.getData().get(i).getStatus());
                                    item.setTipe(response.getData().get(i).getTipe());
                                    item.setUserId(response.getData().get(i).getUserId());
                                    list.add(item);
                                }
                                adapter.updateList(list);
                            } else {
                                list.clear();
                                adapter.updateList(list);
                                HelperClass.responseError(include_lay, imgMsg, R.drawable.ic_account_balance_wallet_black_24dp, txtMsg, HistoryActivity.this.getString(R.string.no_trans));
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            if (!list.isEmpty()) {
                                Toast.makeText(HistoryActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                            } else {
                                HelperClass.responseError(include_lay, imgMsg, R.drawable.ic_cloud_off_black_24dp, txtMsg, getString(R.string.servererrorr));
                            }
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            if (!list.isEmpty()) {
                                Toast.makeText(HistoryActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT).show();
                            } else {
                                HelperClass.responseError(include_lay, imgMsg, R.drawable.ic_signal_wifi_off_black_24dp, txtMsg, getString(R.string.nointernett));
                            }
                        }
                    }
                });
    }
}