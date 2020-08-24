package ahmedt.rentalapp.ui.transaksi.transaksimodel;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.ui.transaksi.detailtrans.DetailTransActivity;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.UrlServer;

public class TransaksiAdapterV2 extends RecyclerView.Adapter<TransaksiAdapterV2.ViewHolder> {
    private Context context;
    private ArrayList<DataItem> list = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    public TransaksiAdapterV2(Context context, ArrayList<DataItem> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(ArrayList<DataItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaksi_v2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final DataItem item = getItem(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.txtNama.setText(item.getNama());
            viewHolder.txtTipe.setText("(Tipe: " + item.getTipe() + ")");
            if (item.getJenisOrder().equals("1")) {
                viewHolder.txtJenisOrder.setText("Hanya Mobil");
            } else {
                viewHolder.txtJenisOrder.setText("Mobil dan Supir");
            }

            double harga = Double.parseDouble(item.getHarga());
            HelperClass.convertHarga(viewHolder.txtHarga, harga, "");

            if (!item.getFoto().isEmpty()) {
                HelperClass.loadGambarSmall(context, UrlServer.URL_FOTO_MOBIL + item.getFoto(), viewHolder.progressBar, viewHolder.imgMobil);
            } else {
                viewHolder.progressBar.setVisibility(View.GONE);
            }

            if (item.getStatus().equals("0")) {
                viewHolder.rlTrans.setBackgroundColor(Color.RED);
                viewHolder.txtStatus.setText("Belum Lunas");
            } else {
                viewHolder.txtStatus.setText("Sudah Lunas");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    viewHolder.rlTrans.setBackgroundColor(context.getColor(R.color.colorPrimary));
                } else {
                    //noinspection deprecation
                    viewHolder.rlTrans.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                }
            }

            viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailTransActivity.class);
                    i.putExtra("data_item", item);
                    context.startActivity(i);
                }
            });

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date transDate = format.parse(item.getOrderDate());
                HelperClass.getDate(transDate, item.getOrderDate(), viewHolder.txtTglTrans);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private DataItem getItem(int position) {
        return list.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, DataItem model);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtTipe, txtTglTrans, txtStatus, txtJenisOrder, txtHarga;
        private ImageView imgMobil;
        private RelativeLayout rlTrans;
        private ProgressBar progressBar;
        private Button btnDetail;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.txtNama = (TextView) itemView.findViewById(R.id.txt_nama_mobil_trans);
            this.txtTipe = (TextView) itemView.findViewById(R.id.txt_tipe_mobil_trans);
            this.txtStatus = (TextView) itemView.findViewById(R.id.txt_status_trans);
            this.txtTglTrans = (TextView) itemView.findViewById(R.id.txt_tanggal_trans);
            this.txtHarga = (TextView) itemView.findViewById(R.id.txt_harga_trans);
            this.txtJenisOrder = (TextView) itemView.findViewById(R.id.txt_tipe_trans);
            this.imgMobil = (ImageView) itemView.findViewById(R.id.img_mobil_trans);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            this.rlTrans = (RelativeLayout) itemView.findViewById(R.id.rl_trans);
            this.btnDetail = (Button) itemView.findViewById(R.id.btn_lihat_detail);
        }
    }
}
