package ahmedt.rentalapp.ui.home.destinasi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ahmedt.rentalapp.R;

public class DestinasiAdapter extends RecyclerView.Adapter<DestinasiAdapter.ViewHolder> {
    private Context context;
    private ArrayList<itemDestinasi> list = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    public DestinasiAdapter (Context context, ArrayList<itemDestinasi> list){
        this.context = context;
        this.list = list;
    }

    public void updateList(ArrayList<itemDestinasi> list){
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destinasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof  ViewHolder){
            final itemDestinasi item = getItem(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.txtNama.setText(item.getNama());
            Glide.with(context)
                    .load(item.getFoto())
                    .into(viewHolder.imgDestinasi);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private itemDestinasi getItem(int position){
        return list.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, itemDestinasi model);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama;
        private ImageView imgDestinasi;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.txtNama = (TextView) itemView.findViewById(R.id.txt_desitinasi);
            this.imgDestinasi = (ImageView) itemView.findViewById(R.id.img_destinasi);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), list.get(getAdapterPosition()));
                }
            });
        }
    }
}
