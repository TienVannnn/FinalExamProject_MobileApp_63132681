package levantien.foodorderapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

        // Đặt key vào tvNotify
        holder.tvNotify.setText(orderKey);

        // Tính toán thời gian kể từ lúc đặt hàng đến thời điểm hiện tại
        long currentTime = System.currentTimeMillis();
        long orderTime = selected.getOrderTime();
        long duration = currentTime - orderTime;

        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;

        String timeSinceOrder = String.format(Locale.getDefault(), "%d days %d hours %d minutes", days, hours, minutes);

        // Đặt thời gian kể từ lúc đặt hàng vào tvTiemDate
        holder.tvTiemDate.setText(timeSinceOrder);
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }


    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView tvNotify, tvTiemDate;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotify = itemView.findViewById(R.id.tvNotify);
            tvTiemDate = itemView.findViewById(R.id.tvTimeDate);
        }
    }
}
