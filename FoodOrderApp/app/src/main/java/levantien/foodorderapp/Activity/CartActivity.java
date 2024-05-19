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
import levantien.foodorderapp.Domain.Order;
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
                        hideLayout();
                    } else {
                        binding.tvEmpty.setVisibility(View.GONE);
                        binding.tvOrderInfo.setVisibility(View.VISIBLE);
                        binding.tvOrderSum.setVisibility(View.VISIBLE);
                        binding.csInfo.setVisibility(View.VISIBLE);
                        binding.csSum.setVisibility(View.VISIBLE);
                        binding.btnCheckout.setVisibility(View.VISIBLE);
                    }

                    // Sau khi tải danh sách giỏ hàng, tính toán và cập nhật giá tiền
                    updateTotalFee(totalFee);
                } else {
                    hideLayout();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Failed to load cart items: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void hideLayout(){
        binding.tvEmpty.setVisibility(View.VISIBLE);
        binding.tvOrderInfo.setVisibility(View.GONE);
        binding.tvOrderSum.setVisibility(View.GONE);
        binding.csInfo.setVisibility(View.GONE);
        binding.csSum.setVisibility(View.GONE);
        binding.btnCheckout.setVisibility(View.GONE);
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

        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các trường nhập
                String name = binding.edtName.getText().toString();
                String phoneNumber = binding.edtPhone.getText().toString();
                String address = binding.edtAdd.getText().toString();
                String subTotal = binding.tvTotalFee.getText().toString();
                String Total = binding.tvTotal.getText().toString();
                String delivery = binding.tvDelivery.getText().toString();
                String tax = binding.tvTax.getText().toString();

                // Kiểm tra xem thông tin có hợp lệ không
                if (name.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                    Toast.makeText(CartActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo đối tượng Order
                Order order = new Order();
                order.setName(name);
                order.setPhoneNumber(phoneNumber);
                order.setAddress(address);
                order.setSubTotal(subTotal);
                order.setToTal(Total);
                order.setDelivery(delivery);
                order.setTax(tax);
                order.setItems(cartItemList);

                // Lưu đơn hàng vào Firebase
                saveOrderToFirebase(order);
            }
        });
    }

    private void saveOrderToFirebase(Order order) {
        // Tạo một nút "orders" trong Firebase
        DatabaseReference userOrdersReference = FirebaseDatabase.getInstance().getReference("users").child(phoneId).child("orders");

        // Push đơn hàng lên Firebase
        String orderId = userOrdersReference.push().getKey();
        userOrdersReference.child(orderId).setValue(order);

        // Xóa giỏ hàng sau khi đặt hàng thành công
        clearCart();
        hideLayout();
        // Hiển thị thông báo hoặc chuyển đến màn hình khác
        Toast.makeText(this, "Đơn hàng đã được đặt thành công", Toast.LENGTH_SHORT).show();
    }

    private void clearCart() {
        // Xóa giỏ hàng trong Firebase
        usersReference.child(phoneId).child("cart").removeValue();

        // Xóa giỏ hàng trong adapter
        cartItemList.clear();
        cartAdapter.notifyDataSetChanged();
    }
}