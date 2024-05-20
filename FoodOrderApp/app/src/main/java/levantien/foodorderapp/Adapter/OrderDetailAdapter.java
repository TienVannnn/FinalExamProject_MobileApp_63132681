package levantien.foodorderapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

import levantien.foodorderapp.Domain.Foods;
import levantien.foodorderapp.Domain.Order;
import levantien.foodorderapp.R;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    ArrayList<Foods> ds;
    Context context;

    public OrderDetailAdapter(ArrayList<Foods> ds, Context context) {
        this.ds = ds;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_orderdetail, parent, false);
        return new OrderDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        Foods selected = ds.get(position);
        holder.title.setText(selected.getTitle());
        holder.price.setText(String.valueOf(selected.getPrice()));
        holder.quantity.setText(String.valueOf(selected.getQuantity()));
        holder.total.setText(String.valueOf(selected.getTotalPrice()));
        Glide.with(holder.itemView.getContext()).load(selected.getImagePath()).transform(new CenterCrop(), new RoundedCorners(30)).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }


    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder{
        TextView title, price, quantity, total;
        ImageView img;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleDetail);
            price = itemView.findViewById(R.id.priceDetail);
            quantity = itemView.findViewById(R.id.QuantityDetail);
            total = itemView.findViewById(R.id.totalDetail);
            img = itemView.findViewById(R.id.picDetail);
        }
    }
}
