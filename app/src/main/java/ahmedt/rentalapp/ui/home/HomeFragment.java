package ahmedt.rentalapp.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.mobil.MobilActivity;
import ahmedt.rentalapp.ui.home.alamatmodel.AlamatAdapter;
import ahmedt.rentalapp.ui.home.alamatmodel.AlamatModel;
import ahmedt.rentalapp.ui.home.alamatmodel.DataItem;
import ahmedt.rentalapp.ui.home.destinasi.DestinasiActivity;
import ahmedt.rentalapp.ui.home.destinasi.DestinasiAdapter;
import ahmedt.rentalapp.ui.home.destinasi.itemDestinasi;
import ahmedt.rentalapp.utils.SessionPrefs;
import ahmedt.rentalapp.utils.UrlServer;
import es.dmoral.toasty.Toasty;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private RecyclerView rvDestinasi;
    private ArrayList<itemDestinasi> list = new ArrayList<>();
    private DestinasiAdapter adapter;
    private CardView cdMini, cdSedan, cdAlamat;
    private TextView txtAlamat;
    private ImageView imgMini, imgSedan;
    String alamat = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
    }

    private void findView(View view) {
        cdAlamat = view.findViewById(R.id.cd_alamat);
        cdMini = view.findViewById(R.id.cd_mobil_minvan);
        cdSedan = view.findViewById(R.id.cd_mobil_sedan);
        rvDestinasi = view.findViewById(R.id.rv_destinasi);
        txtAlamat = view.findViewById(R.id.txt_select_alamat);
        imgSedan = view.findViewById(R.id.img_sedan);
        imgMini = view.findViewById(R.id.img_minivan);
        adapter = new DestinasiAdapter(getActivity(), list);
        rvDestinasi.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvDestinasi.setLayoutManager(gridLayoutManager);
        rvDestinasi.setAdapter(adapter);
        setAdapter();
        Log.d(TAG, "onCreate: UID" + Prefs.getString(SessionPrefs.U_ID, ""));
        Log.d(TAG, "onCreate: UID" + Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""));
        adapter.setOnItemClickListener(new DestinasiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, itemDestinasi model) {
                Intent i = new Intent(getActivity(), DestinasiActivity.class);
                i.putExtra("data_item", list.get(position));
                startActivity(i);
            }
        });
        Glide.with(getActivity())
                .load(R.drawable.tes)
                .apply(new RequestOptions().override(400, 200))
                .into(imgMini);

        Glide.with(getActivity())
                .load(R.drawable.tes2)
                .apply(new RequestOptions().override(400, 200))
                .into(imgSedan);

        cdSedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!alamat.isEmpty()) {
                    Intent i = new Intent(getActivity(), MobilActivity.class);
                    i.putExtra("tipe", "2");
                    i.putExtra("alamat", alamat);
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "Alamat belum dipilih", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cdMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!alamat.isEmpty()) {
                    Intent i = new Intent(getActivity(), MobilActivity.class);
                    i.putExtra("tipe", "1");
                    i.putExtra("alamat", alamat);
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "Alamat belum dipilih", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cdAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlamat();
            }
        });
    }

    private void setAdapter() {
        list.clear();
        list.add(new itemDestinasi(getString(R.string.ancol), getString(R.string.ancol_alamat), getString(R.string.ancol_desc), R.drawable.ancol_));
        list.add(new itemDestinasi(getString(R.string.monas_name), getString(R.string.monas_alamat), getString(R.string.monas_desc), R.drawable.monas));
        list.add(new itemDestinasi(getString(R.string.tmii_nama), getString(R.string.tmii_alamat), getString(R.string.tmii_desc), R.drawable.tamanmini));
        list.add(new itemDestinasi(getString(R.string.ragunan_nama), getString(R.string.ragunan_alamat), getString(R.string.ragunan_desc), R.drawable.ragunan_));
        list.add(new itemDestinasi(getString(R.string.kotu_nama), getString(R.string.kotu_alamat), getString(R.string.kotu_desc), R.drawable.kota_tua));
        list.add(new itemDestinasi(getString(R.string.dufan_nama), getString(R.string.dufan_alamat), getString(R.string.dufan_desc), R.drawable.dufan));
        adapter.updateList(list);
    }

    private void getAlamat() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_address);
        RecyclerView rv_address;
        Button btnCancel;
        final AlamatAdapter adapter1;
        final ProgressBar progressBar;
        final ArrayList<DataItem> list1 = new ArrayList<>();
        rv_address = dialog.findViewById(R.id.rv_alamat);
        progressBar = dialog.findViewById(R.id.progress_bar);
        btnCancel = dialog.findViewById(R.id.btn_cancel_address);

        adapter1 = new AlamatAdapter(getActivity(), list1);
        rv_address.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv_address.setLayoutManager(linearLayoutManager);
        rv_address.setAdapter(adapter1);

        AndroidNetworking.post(UrlServer.URL_ALAMAT)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .setTag("alamat")
                .build()
                .getAsOkHttpResponseAndObject(AlamatModel.class, new OkHttpResponseAndParsedRequestListener<AlamatModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, final AlamatModel response) {
                        progressBar.setVisibility(View.GONE);
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200){
                                for (int i = 0; i < response.getData().size(); i++) {
                                    final DataItem item = new DataItem();
                                    item.setKota(response.getData().get(i).getKota());
                                    item.setAlamatDetail(response.getData().get(i).getAlamatDetail());
                                    item.setAlamatId(response.getData().get(i).getAlamatId());
                                    list1.add(item);
                                }
                                adapter1.updateList(list1);
                                adapter1.setOnItemClickListener(new AlamatAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position, DataItem model) {
                                        txtAlamat.setText(model.getKota());
                                        alamat = model.getAlamatId();
                                        Log.d("Alamat", "onItemClick: "+model.getAlamatId());
                                        dialog.dismiss();
                                    }
                                });
                            }else{
                                dialog.dismiss();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toast.makeText(getActivity(), R.string.cek_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidNetworking.cancel("alamat");
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }
}