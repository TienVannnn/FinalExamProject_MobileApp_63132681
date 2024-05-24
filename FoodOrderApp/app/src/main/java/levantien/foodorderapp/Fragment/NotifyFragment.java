package levantien.foodorderapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import levantien.foodorderapp.Adapter.OrderAdapter;
import levantien.foodorderapp.Adapter.SliderAdapter;
import levantien.foodorderapp.Domain.Order;
import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.FragmentHomeBinding;
import levantien.foodorderapp.databinding.FragmentNotifyBinding;

public class NotifyFragment extends Fragment {
    private FragmentNotifyBinding binding;
    private ArrayList<Order> ds;
    private ArrayList<String> orderKeys;
    RecyclerView.Adapter adapter;
    String phoneId;
    boolean isLogin;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Avoid any UI initialization here; move it to onCreateView or later.
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotifyBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ds = new ArrayList<>();
        orderKeys = new ArrayList<>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        phoneId = sharedPreferences.getString("phoneId", "");
        binding.recycleOrder.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new OrderAdapter(ds, orderKeys, getContext());
        binding.recycleOrder.setAdapter(adapter);
        loadOrderKeys();
    }

    private void loadOrderKeys() {
        DatabaseReference userOrdersRef = FirebaseDatabase.getInstance().getReference("users")
                .child(phoneId)
                .child("orders");

        userOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!isLogin){
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.recycleOrder.setVisibility(View.GONE);
                }
                else {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey(); // Lấy key của đơn hàng
                            orderKeys.add(key); // Thêm key vào danh sách
                        }
                        // Sau khi đã lấy danh sách các key, gọi phương thức để load thông tin đơn hàng
                        loadOrders();
                    } else {
                        // Xử lý trường hợp không có đơn hàng nào
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                        binding.recycleOrder.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý trường hợp lỗi
            }
        });
    }

    private void loadOrders() {
        for (String key : orderKeys) {
            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(phoneId) // Thay "phoneId" bằng ID của người dùng
                    .child("orders").child(key);
            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Order order = dataSnapshot.getValue(Order.class); // Lấy thông tin đơn hàng từ dataSnapshot
                        if (order != null) {
                            ds.add(order); // Thêm đơn hàng vào danh sách
                            adapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý trường hợp lỗi
                }
            });
        }
    }

}