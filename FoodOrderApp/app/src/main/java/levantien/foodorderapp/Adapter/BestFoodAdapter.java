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

public class BestFoodAdapter extends RecyclerView.Adapter<BestFoodAdapter.BestFoodViewHolder> {
    ArrayList<Foods> ds;
    Context context;

    public BestFoodAdapter(ArrayList<Foods> ds, Context context) {
        this.ds = ds;
        this.context = context;
    }

    @NonNull
    @Override
    public BestFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_bestfood, parent, false);
        return new BestFoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BestFoodViewHolder holder, int position) {
        Foods selected = ds.get(position);
        holder.tvTitle.setText(selected.getTitle());
        holder.tvStar.setText("" + selected.getStar());
        holder.tvTime.setText(selected.getTimeValue() + "min");
        holder.tvPrice.setText("$" + selected.getPrice());
        Glide.with(holder.itemView.getContext()).load(selected.getImagePath()).transform(new CenterCrop(), new RoundedCorners(30)).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", selected);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }


    public static class BestFoodViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvStar, tvTime, tvPrice;
        ImageView img;

        public BestFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitleB);
            tvStar = itemView.findViewById(R.id.tvStarB);
            tvTime = itemView.findViewById(R.id.tvTimeB);
            tvPrice = itemView.findViewById(R.id.tvPriceB);
            img = itemView.findViewById(R.id.imgB);
        }
    }
}
