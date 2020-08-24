package ahmedt.rentalapp.ui.transaksi.history;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ahmedt.rentalapp.R;
import ahmedt.rentalapp.ui.transaksi.transaksimodel.DataItem;
import ahmedt.rentalapp.utils.HelperClass;
import ahmedt.rentalapp.utils.UrlServer;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DataItem> list = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    public HistoryAdapter(Context context, ArrayList<DataItem> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final DataItem item = getItem(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            if (item.getJenisOrder().equals("1")) {
                viewHolder.txtType.setText("Hanya Mobil");
            } else {
                viewHolder.txtType.setText("Mobil dan Supir");
            }

            double harga = Double.parseDouble(item.getHarga());
            HelperClass.convertHarga(viewHolder.txtPrice, harga, "");

            if (item.getStatus().equals("2")) {
                viewHolder.txtStatus.setText("Selesai");
                viewHolder.txtStatus.setTextColor(Color.GREEN);
            } else if (item.getStatus().equals("3")) {
                viewHolder.txtStatus.setText("Dibatalkan");
                viewHolder.txtStatus.setTextColor(Color.RED);
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date transDate = format.parse(item.getOrderDate());
                HelperClass.getDate(transDate, item.getOrderDate(), viewHolder.txtDate);
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
        private TextView txtType, txtDate, txtPrice, txtStatus;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.txtStatus = (TextView) itemView.findViewById(R.id.txt_status_payment);
            this.txtType = (TextView) itemView.findViewById(R.id.txt_type_payment);
            this.txtDate = (TextView) itemView.findViewById(R.id.txt_date_payment);
            this.txtPrice = (TextView) itemView.findViewById(R.id.txt_price_payment);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), list.get(getAdapterPosition()));
                }
            });
        }
    }
}
