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

import java.util.ArrayList;

import levantien.foodorderapp.Domain.Category;
import levantien.foodorderapp.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    ArrayList<Category> items;
    Context context;

    public CategoryAdapter(ArrayList<Category> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category,parent,false);
       return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.tvCatName.setText(items.get(position).getName());
        int idImg = context.getResources().getIdentifier(items.get(position).getImagePath(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(context).load(items.get(position).getImagePath()).into(holder.imgCat);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView imgCat;
        TextView tvCatName;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCat = itemView.findViewById(R.id.imgCat);
            tvCatName = itemView.findViewById(R.id.tvCatName);
        }
    }
}
