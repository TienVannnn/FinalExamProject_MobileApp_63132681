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

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavaritesViewHolder> {
    ArrayList<Foods> ds;
    Context context;

    public FavoritesAdapter(ArrayList<Foods> ds, Context context) {
        this.ds = ds;
        this.context = context;
    }

    @NonNull
    @Override
    public FavaritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_fav, parent, false);
        return new FavaritesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavaritesViewHolder holder, int position) {
        Foods selected = ds.get(position);
        holder.title.setText(selected.getTitle());
        holder.time.setText(selected.getTimeValue() + "min");
        holder.price.setText("$" + selected.getPrice());
        holder.rating.setText("" + selected.getStar());
        Glide.with(holder.itemView.getContext()).load(selected.getImagePath()).transform(new CenterCrop(), new RoundedCorners(30)).into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", ds.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }


    public static class FavaritesViewHolder extends RecyclerView.ViewHolder{
        TextView title, price, rating, time;
        ImageView pic;
        public FavaritesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitleF);
            time = itemView.findViewById(R.id.tvTimeF);
            price = itemView.findViewById(R.id.tvPriceF);
            rating = itemView.findViewById(R.id.tvRatingF);
            pic = itemView.findViewById(R.id.imgF);
        }
    }
}
