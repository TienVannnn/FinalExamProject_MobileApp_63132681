package levantien.foodorderapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import levantien.foodorderapp.Adapter.CartAdapter;
import levantien.foodorderapp.Helper.ChangeNumberItemsListener;
import levantien.foodorderapp.Helper.ManagmentCart;
import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {
    ActivityCartBinding binding;
    private ManagmentCart managmentCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);

        setVariable();
        calculateCart();
        initCartList();
    }

    private void initCartList() {
        if(managmentCart.getListCart().isEmpty()){
            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.recycleCart.setVisibility(View.GONE);
        }
        else {
            binding.tvEmpty.setVisibility(View.GONE);
            binding.recycleCart.setVisibility(View.VISIBLE);
        }
        binding.recycleCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recycleCart.setAdapter(new CartAdapter(managmentCart.getListCart(), managmentCart, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                calculateCart();
            }
        }, this));
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10; // dollar
        double tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

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