package levantien.foodorderapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import levantien.foodorderapp.Adapter.OrderDetailAdapter;
import levantien.foodorderapp.Domain.Foods;
import levantien.foodorderapp.Domain.Order;
import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.ActivityOrderDetailBinding;

public class OrderDetailActivity extends BaseActivity {
    ActivityOrderDetailBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersReference;
    String phoneId;
    boolean isLogin;
    String orderKey;
    private OrderDetailAdapter adapter;
    private ArrayList<Foods> ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ds = new ArrayList<>();
        orderKey = getIntent().getStringExtra("orderKey");

        SharedPreferences sharedPreferences = OrderDetailActivity.this.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        usersReference = database.getReference("users");
        phoneId = sharedPreferences.getString("phoneId", "");

//        String orderKey = getIntent().getStringExtra("orderKey");
//        String orderName = getIntent().getStringExtra("orderName");
//        String orderPhone = getIntent().getStringExtra("orderPhone");
//        String orderAddress = getIntent().getStringExtra("orderAddress");
//        String orderTotalPrice = getIntent().getStringExtra("orderTotalPrice");
//
//        // Hiển thị dữ liệu lên giao diện
//        binding.tvKeyOrDe.setText(orderKey);
//        binding.tvNameOrDe.setText(orderName);
//        binding.tvPhoneOrDe.setText(orderPhone);
//        binding.tvAddOrDe.setText(orderAddress);
//        binding.tvToTalOrDe.setText(orderTotalPrice);





        loadOrderDetail();
        setVariable();
        binding.recycleOrDe.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this, LinearLayoutManager.VERTICAL, false));

        adapter = new OrderDetailAdapter(ds, this);
        binding.recycleOrDe.setAdapter(adapter);

    }

    private void setVariable() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadOrderDetail() {
        usersReference.child(phoneId).child("orders").child(orderKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null) {
                        // Hiển thị thông tin đơn hàng
                        binding.tvKeyOrDe.setText("Order: " + orderKey);
                        binding.tvNameOrDe.setText(order.getName());
                        binding.tvPhoneOrDe.setText(order.getPhoneNumber());
                        binding.tvAddOrDe.setText(order.getAddress());
                        binding.tvToTalOrDe.setText(order.getToTal());

                        // Cập nhật danh sách sản phẩm
                        ds.clear();
                        for (DataSnapshot itemSnapshot : snapshot.child("items").getChildren()) {
                            Foods food = itemSnapshot.getValue(Foods.class);
                            if (food != null) {
                                ds.add(food);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Order not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OrderDetailActivity", "Failed to load order data", error.toException());
                Toast.makeText(OrderDetailActivity.this, "Failed to load order data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}