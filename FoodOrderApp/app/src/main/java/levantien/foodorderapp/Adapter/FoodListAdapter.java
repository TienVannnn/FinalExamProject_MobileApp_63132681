package levantien.foodorderapp.Adapter;

import android.content.Context;
import android.content.Intent;
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

import levantien.foodorderapp.Activity.DetailActivity;
import levantien.foodorderapp.Domain.Foods;
import levantien.foodorderapp.R;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodListViewHolder> {
    ArrayList<Foods> list;
    Context context;

    public FoodListAdapter(ArrayList<Foods> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v  = layoutInflater.inflate(R.layout.viewholder_listfood, parent, false);
        FoodListViewHolder f = new FoodListViewHolder(v);
        return f;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListViewHolder holder, int position) {
        holder.tilte.setText(list.get(position).getTitle());
        holder.time.setText(list.get(position).getTimeId() + "min");
        holder.price.setText("$" + list.get(position).getPrice());
        holder.rate.setText("" + list.get(position).getStar());
        Glide.with(context).load(list.get(position).getImagePath()).transform(new CenterCrop(), new RoundedCorners(50))
                .into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", list.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class FoodListViewHolder extends RecyclerView.ViewHolder{
        TextView tilte, price, time, rate;
        ImageView pic;
        public FoodListViewHolder(@NonNull View itemView) {
            super(itemView);
            tilte = itemView.findViewById(R.id.tvTitle);
            price = itemView.findViewById(R.id.tvPrice);
            time = itemView.findViewById(R.id.tvTime);
            rate = itemView.findViewById(R.id.tvRating);
            pic = itemView.findViewById(R.id.img);
        }
    }
}
