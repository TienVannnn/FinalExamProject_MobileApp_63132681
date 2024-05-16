package levantien.foodorderapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {
    ActivityCartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}