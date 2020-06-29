package ahmedt.rentalapp.ui.transaksi.transaksimodel;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.ui.transaksi.transaksimodel.DataItem;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.UrlServer;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DataItem> list = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    public TransaksiAdapter(Context context, ArrayList<DataItem> list){
        this.context = context;
        this.list = list;
    }

    public void updateList(ArrayList<DataItem> list){
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaksi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof  ViewHolder){
            final DataItem item = getItem(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.txtNama.setText(item.getNama());
            viewHolder.txtTipe.setText("(Tipe: "+item.getTipe()+")");
            if (item.getJenisOrder().equals("1")){
                viewHolder.txtJenisOrder.setText("Hanya Mobil");
            }else{
                viewHolder.txtJenisOrder.setText("Mobil dan Supir");
            }

            double harga = Double.parseDouble(item.getHarga());
            HelperClass.convertHarga(viewHolder.txtHarga, harga, "");
            HelperClass.parseDate(item.getStartDate(), viewHolder.txtStart);
            HelperClass.parseDate(item.getEndDate(), viewHolder.txtEnd);

            if (!item.getFoto().isEmpty()){
                HelperClass.loadGambar(context, UrlServer.URL_FOTO_MOBIL+item.getFoto(), viewHolder.progressBar, viewHolder.imgMobil);
            }else{
                viewHolder.progressBar.setVisibility(View.GONE);
            }

            if (item.getStatus().equals("0")){
                viewHolder.rlTrans.setBackgroundColor(Color.RED);
                viewHolder.txtStatus.setText("Belum Lunas");
            }else{
                viewHolder.txtStatus.setText("Sudah Lunas");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    viewHolder.rlTrans.setBackgroundColor( context.getColor(R.color.colorPrimary));
                } else {
                    //noinspection deprecation
                    viewHolder.rlTrans.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private DataItem getItem(int position){
        return list.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, DataItem model);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtTipe, txtStart, txtEnd, txtStatus, txtJenisOrder, txtHarga;
        private ImageView imgMobil;
        private RelativeLayout rlTrans;
        private ProgressBar progressBar;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.txtNama = (TextView) itemView.findViewById(R.id.txt_nama_mobil_trans);
            this.txtTipe = (TextView) itemView.findViewById(R.id.txt_tipe_mobil_trans);
            this.txtStart = (TextView) itemView.findViewById(R.id.txt_tanggal_pinjam_trans);
            this.txtEnd = (TextView) itemView.findViewById(R.id.txt_tanggal_kembali_trans);
            this.txtStatus = (TextView) itemView.findViewById(R.id.txt_status_trans);
            this.txtHarga = (TextView) itemView.findViewById(R.id.txt_harga_trans);
            this.txtJenisOrder = (TextView) itemView.findViewById(R.id.txt_tipe_trans);
            this.imgMobil = (ImageView) itemView.findViewById(R.id.img_mobil_trans);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            this.rlTrans = (RelativeLayout) itemView.findViewById(R.id.rl_trans);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), list.get(getAdapterPosition()));
//                }
//            });
        }
    }
}
