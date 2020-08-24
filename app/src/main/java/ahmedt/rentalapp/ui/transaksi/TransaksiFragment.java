package ahmedt.rentalapp.ui.transaksi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.ui.transaksi.detailtrans.DetailTransActivity;
import ahmedt.rentalapp.ui.transaksi.history.HistoryActivity;
import ahmedt.rentalapp.ui.transaksi.transaksimodel.DataItem;
import ahmedt.rentalapp.ui.transaksi.transaksimodel.TransaksiAdapter;
import ahmedt.rentalapp.ui.transaksi.transaksimodel.TransaksiAdapterV2;
import ahmedt.rentalapp.ui.transaksi.transaksimodel.TransaksiModel;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.SessionPrefs;
import ahmedt.rentalapp.utils.UniversalModel;
import ahmedt.rentalapp.utils.UrlServer;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class TransaksiFragment extends Fragment {
    private static final String TAG = "TransaksiFragment";
    private RecyclerView rvTrans;
    private ArrayList<DataItem> list = new ArrayList<>();
    private TransaksiAdapterV2 adapter;
    private LinearLayout include_lay;
    private Button btnRefresh;
    private TextView txtMsg;
    private ImageView imgMsg;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trans, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_riwayat, menu);
        final MenuItem menuItem = menu.findItem(R.id.menu_history);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_history:
                startActivity(new Intent(getActivity(), HistoryActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findView(View view) {
        rvTrans = view.findViewById(R.id.rv_transaksi);
        include_lay = view.findViewById(R.id.include_lay);
        imgMsg = view.findViewById(R.id.img_message);
        txtMsg = view.findViewById(R.id.txt_msg);
        btnRefresh = view.findViewById(R.id.btn_refresh);
        swipeRefreshLayout = view.findViewById(R.id.swipe_up_layout_transaksi);
        progressBar = view.findViewById(R.id.progress_bar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new TransaksiAdapterV2(getActivity(), list);
        rvTrans.setLayoutManager(linearLayoutManager);
        rvTrans.setHasFixedSize(true);
        rvTrans.setAdapter(adapter);

//        adapter.setOnItemClickListener(new TransaksiAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position, final DataItem model) {
//                Button btnCancel = view.findViewById(R.id.btn_cancel_trans);
//                btnCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//
//                        alert.setTitle("Cancel");
//                        alert.setMessage("Apakah Anda yakin ingin membatalkan pemesanan?")
//                                .setCancelable(true)
//                                .setPositiveButton("ya", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (getActivity() != null) {
//                                            String uid = Prefs.getString(SessionPrefs.U_ID, "");
//                                            String token_login = Prefs.getString(SessionPrefs.TOKEN_LOGIN, "");
//                                            Log.d(TAG, "onClick: ID " + model.getOrderId());
//                                            cancelOrder(uid, token_login, model.getOrderId());
//                                        }
//                                    }
//                                })
//                                .setNegativeButton("tidak", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        alert.show();
//                    }
//                });
//            }
//        });

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

        if (isAdded()) {
            getListOrder();
        }
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
                .addBodyParameter("tipe", "1")
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

                                adapter.setOnItemClickListener(new TransaksiAdapterV2.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, final int position, DataItem model) {
                                        Intent i = new Intent(getActivity(), DetailTransActivity.class);
                                        i.putExtra("data_item", model);
                                        i.putExtra("param", "1");
                                        startActivityForResult(i, 22);
                                    }
                                });
                            } else {
                                if (isAdded()) {
                                    list.clear();
                                    adapter.updateList(list);
                                    HelperClass.responseError(include_lay, imgMsg, R.drawable.ic_account_balance_wallet_black_24dp, txtMsg, getActivity().getString(R.string.no_trans));
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
                            if (!list.isEmpty()) {
                                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                            } else {
                                if (isAdded()) {
                                    HelperClass.responseError(include_lay, imgMsg, R.drawable.ic_cloud_off_black_24dp, txtMsg, getString(R.string.servererrorr));
                                }
                            }
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            if (!list.isEmpty()) {
                                Toast.makeText(getActivity(), R.string.cek_internet, Toast.LENGTH_SHORT).show();
                            } else {
                                if (isAdded()) {
                                    HelperClass.responseError(include_lay, imgMsg, R.drawable.ic_signal_wifi_off_black_24dp, txtMsg, getString(R.string.nointernett));
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: " + "result");
            if (requestCode == 22) {
                if (isAdded()) {
                    Toast.makeText(getActivity(), "Result", Toast.LENGTH_SHORT).show();
                    list.clear();
                    adapter.updateList(list);
                    getListOrder();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}