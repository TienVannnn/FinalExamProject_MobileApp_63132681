package levantien.foodorderapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import levantien.foodorderapp.Activity.OrderDetailActivity;
import levantien.foodorderapp.Domain.Order;
import levantien.foodorderapp.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    ArrayList<Order> ds;
    ArrayList<String> orderKeys; // Danh sách các key của đơn hàng
    Context context;

    public OrderAdapter(ArrayList<Order> ds, ArrayList<String> orderKeys, Context context) {
        this.ds = ds;
        this.orderKeys = orderKeys;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_orders, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order selected = ds.get(position);
        String orderKey = orderKeys.get(position);
        holder.tvNotify.setText(orderKey);
        holder.tvTotalPrice.setText(selected.getToTal());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("orderKey", orderKey);
            intent.putExtra("orderName", selected.getName());
            intent.putExtra("orderPhone", selected.getPhoneNumber());
            intent.putExtra("orderAddress", selected.getAddress());
            intent.putExtra("orderTotalPrice", selected.getToTal());
            intent.putExtra("orderKey", orderKey);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }


    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView tvNotify, tvTotalPrice;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotify = itemView.findViewById(R.id.tvNotify);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPriceOr);
        }
    }
}
