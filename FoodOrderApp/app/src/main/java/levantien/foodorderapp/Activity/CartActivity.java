package levantien.foodorderapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import levantien.foodorderapp.Adapter.CartAdapter;
import levantien.foodorderapp.Domain.Foods;
import levantien.foodorderapp.Helper.ChangeNumberItemsListener;
import levantien.foodorderapp.Helper.ManagmentCart;
import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {
    ActivityCartBinding binding;
    private ArrayList<Foods> cartItemList;
    private CartAdapter cartAdapter;
    private FirebaseDatabase myFirebaseDatabase;
    private DatabaseReference usersReference;
    private String phoneId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cartItemList = new ArrayList<>();
        myFirebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = myFirebaseDatabase.getReference("users");

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        phoneId = sharedPreferences.getString("phoneId", "");

        binding.recycleCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartAdapter = new CartAdapter(cartItemList, this, phoneId);
        binding.recycleCart.setAdapter(cartAdapter);

        loadCartItems();
        setVariable();
    }

    private void loadCartItems() {
        if (!isLogin) {
           binding.tvEmpty.setText("Your cart is empty");
        } else {
            usersReference.child(phoneId).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        cartItemList.clear();
                        double totalFee = 0; // Tạo biến để tính tổng giá tiền của giỏ hàng
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Foods cartItem = dataSnapshot.getValue(Foods.class);
                            cartItem.setKey(dataSnapshot.getKey());
                            cartItemList.add(cartItem);
                            totalFee += cartItem.getPrice() * cartItem.getQuantity(); // Tính toán tổng giá tiền
                        }
                        cartAdapter.notifyDataSetChanged();

                        if (cartItemList.isEmpty()) {
                            binding.tvEmpty.setVisibility(View.VISIBLE);
                        } else {
                            binding.tvEmpty.setVisibility(View.GONE);
                        }

                        // Sau khi tải danh sách giỏ hàng, tính toán và cập nhật giá tiền
                        updateTotalFee(totalFee);
                    } else {
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(CartActivity.this, "Failed to load cart items: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Phương thức này được thêm vào để cập nhật giá tiền
    public void updateTotalFee(double totalFee) {
        double percentTax = 0.02;
        double delivery = 10; // dollar
        double tax = Math.round(totalFee * percentTax * 100.0) / 100;
        double total = Math.round((totalFee + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(totalFee * 100) / 100;

        binding.tvTotalFee.setText("$" + itemTotal);
        binding.tvTax.setText("$" + tax);
        binding.tvDelivery.setText("$" + delivery);
        binding.tvTotal.setText("$" + total);
    }



    private void setVariable() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}