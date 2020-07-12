package ahmedt.rentalapp.mobil.mobilmodel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.mobil.DetailMobilActivity;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.UrlServer;

public class MobilAdapter extends RecyclerView.Adapter<MobilAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DataItem> list = new ArrayList<>();

    public MobilAdapter(Context context, ArrayList<DataItem> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mobil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final DataItem item = getItem(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.txtNama.setText(item.getNama());
            if (item.getFoto() != null) {
                if (!item.getFoto().isEmpty()) {
                    HelperClass.loadGambar(context, UrlServer.URL_FOTO_MOBIL + item.getFoto(), viewHolder.progressBar, viewHolder.imgMobil);
                } else {

                }
            }
            double harga = Double.parseDouble(item.getHarga());
            HelperClass.convertHarga(viewHolder.txtHarga, harga, "/hari");
            viewHolder.txtTipe.setText("Tipe: "+item.getTipe());
            viewHolder.txtTransmisi.setText(item.getTransmisi());
            viewHolder.txtKursi.setText(item.getKursi() + " seats");
            viewHolder.txtPintu.setText(item.getPintu() + " doors");
            viewHolder.txtBags.setText(item.getAirBag() + " bags");

            String status = item.getStatus();
            if (status.equals("0")) {
                viewHolder.btnPesan.setBackground(context.getDrawable(R.drawable.bg_btn));
                viewHolder.btnPesan.setText("Pesan");
                viewHolder.btnPesan.setClickable(true);
                viewHolder.btnPesan.setEnabled(true);
                viewHolder.btnPesan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), DetailMobilActivity.class);
                        i.putExtra("data_item", item);
                        v.getContext().startActivity(i);
                    }
                });
            } else {
                viewHolder.btnPesan.setBackground(context.getDrawable(R.drawable.bg_btn_gray));
                viewHolder.btnPesan.setText("Kosong");
                viewHolder.btnPesan.setClickable(false);
                viewHolder.btnPesan.setEnabled(false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtTransmisi, txtPintu, txtKursi, txtBags, txtTipe, txtHarga;
        private ImageView imgMobil;
        private ProgressBar progressBar;
        private Button btnPesan;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.txtNama = (TextView) itemView.findViewById(R.id.txt_nama_mobil);
            this.txtTransmisi = (TextView) itemView.findViewById(R.id.txt_transmisi_mobil);
            this.txtKursi = (TextView) itemView.findViewById(R.id.txt_kursi_mobil);
            this.txtPintu = (TextView) itemView.findViewById(R.id.txt_pintu_mobil);
            this.txtBags = (TextView) itemView.findViewById(R.id.txt_bag_mobil);
            this.txtTipe = (TextView) itemView.findViewById(R.id.txt_tipe_mobil);
            this.txtHarga = (TextView) itemView.findViewById(R.id.txt_harga_mobil);
            this.imgMobil = (ImageView) itemView.findViewById(R.id.img_mobil);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            this.btnPesan = (Button) itemView.findViewById(R.id.btn_pesan_mobil);
        }
    }
}
