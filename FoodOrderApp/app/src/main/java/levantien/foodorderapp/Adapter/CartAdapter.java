package levantien.foodorderapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import levantien.foodorderapp.Activity.CartActivity;
import levantien.foodorderapp.Domain.Foods;
import levantien.foodorderapp.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    ArrayList<Foods> ds;

    Context context;
    DatabaseReference cartReference;

    public CartAdapter(ArrayList<Foods> ds, Context context, String phoneId) {
        this.ds = ds;
        this.context = context;
        this.cartReference = FirebaseDatabase.getInstance().getReference("users").child(phoneId).child("cart");
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
        holder.feeEachItem.setText("$" + (selected.getQuantity() * selected.getPrice()));
        holder.num.setText(selected.getQuantity() + "");
        Glide.with(holder.itemView.getContext()).load(selected.getImagePath()).transform(new CenterCrop(), new RoundedCorners(30)).into(holder.pic);

        holder.plusItem.setOnClickListener(v -> {
            int currentNumber = selected.getQuantity();
            selected.setQuantity(currentNumber + 1);
            selected.setTotalPrice(selected.getQuantity() * selected.getPrice());
            updateCartItemInFirebase(selected.getKey(), selected.getQuantity(), selected.getTotalPrice());
            notifyDataSetChanged();

            double totalFee = calculateTotalFee();
            ((CartActivity)context).updateTotalFee(totalFee);
        });

        holder.minusItem.setOnClickListener(v -> {
            int currentNumber = selected.getQuantity();
            if (currentNumber > 1) {
                selected.setQuantity(currentNumber - 1);
                selected.setTotalPrice(selected.getQuantity() * selected.getPrice());
                updateCartItemInFirebase(selected.getKey(), selected.getQuantity(), selected.getTotalPrice());
                notifyDataSetChanged();
                double totalFee = calculateTotalFee();
                ((CartActivity)context).updateTotalFee(totalFee);
            } else {
                Toast.makeText(context, "Minimum quantity reached", Toast.LENGTH_SHORT).show();
            }
        });

        holder.trashBtn.setOnClickListener(v -> {
            ds.remove(position);
            removeCartItemFromFirebase(selected.getKey());
            notifyDataSetChanged();
            double totalFee = calculateTotalFee();
            ((CartActivity)context).updateTotalFee(totalFee);
            if(ds.isEmpty()){
                ((CartActivity)context).hideLayout();
            }
        });
    }

    private void updateCartItemInFirebase(String key, int quantity, double totalPrice) {
        cartReference.child(key).child("Quantity").setValue(quantity);
        cartReference.child(key).child("TotalPrice").setValue(totalPrice);
    }

    private void removeCartItemFromFirebase(String key) {
        cartReference.child(key).removeValue();
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }

    private double calculateTotalFee() {
        double totalFee = 0;
        for (Foods item : ds) {
            totalFee += item.getPrice() * item.getQuantity();
        }
        return totalFee;
    }


    public static class CartViewHolder extends RecyclerView.ViewHolder{
        TextView title, feeEachItem, plusItem, minusItem, num;
        ImageView pic;
        ConstraintLayout trashBtn;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            feeEachItem = itemView.findViewById(R.id.tvPriceDe);
            plusItem = itemView.findViewById(R.id.btnPlus);
            minusItem = itemView.findViewById(R.id.btnMinus);
            num = itemView.findViewById(R.id.tvNum);
            pic = itemView.findViewById(R.id.picDetail);
            trashBtn = itemView.findViewById(R.id.btnTrash);
        }
    }
}
