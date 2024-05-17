package levantien.foodorderapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

import levantien.foodorderapp.Domain.Foods;
import levantien.foodorderapp.Helper.ChangeNumberItemsListener;
import levantien.foodorderapp.Helper.ManagmentCart;
import levantien.foodorderapp.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    ArrayList<Foods> ds;
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    Context context;

    public CartAdapter(ArrayList<Foods> ds, ManagmentCart managmentCart, ChangeNumberItemsListener changeNumberItemsListener, Context context) {
        this.ds = ds;
        this.managmentCart = managmentCart;
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.viewholder_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Foods selected = ds.get(position);
        holder.title.setText(selected.getTitle());
        holder.feeEachItem.setText("$" + (selected.getNumberInCart() * selected.getPrice()));
        holder.num.setText(selected.getNumberInCart() + "");
        Glide.with(holder.itemView.getContext()).load(selected.getImagePath()).transform(new CenterCrop(), new RoundedCorners(30)).into(holder.pic);
        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.plusNumberItem(ds, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });
        holder.minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.minusNumberItem(ds, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });
        holder.trashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.removeItem(ds, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }


    public static class CartViewHolder extends RecyclerView.ViewHolder{
        TextView title, feeEachItem, plusItem, minusItem, num;
        ImageView pic;
        ConstraintLayout trashBtn;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            plusItem = itemView.findViewById(R.id.btnPlus);
            minusItem = itemView.findViewById(R.id.btnMinus);
            num = itemView.findViewById(R.id.tvNum);
            pic = itemView.findViewById(R.id.pic);
            trashBtn = itemView.findViewById(R.id.btnTrash);
        }
    }
}
