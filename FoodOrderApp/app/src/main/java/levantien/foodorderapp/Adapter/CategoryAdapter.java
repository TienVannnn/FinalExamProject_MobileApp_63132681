package levantien.foodorderapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import levantien.foodorderapp.Domain.Category;
import levantien.foodorderapp.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    ArrayList<Category> items;
    Context context;

    public CategoryAdapter(ArrayList<Category> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater layoutInflater = LayoutInflater.from(context);
       View v = layoutInflater.inflate(R.layout.viewholder_category, parent, false);
       CategoryViewHolder c = new CategoryViewHolder(v);
       return c;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
