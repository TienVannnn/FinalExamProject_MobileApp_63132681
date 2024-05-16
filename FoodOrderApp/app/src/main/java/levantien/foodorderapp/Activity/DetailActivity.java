package levantien.foodorderapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import levantien.foodorderapp.Domain.Foods;
import levantien.foodorderapp.Helper.ManagmentCart;
import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;
    private int num = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        managmentCart = new ManagmentCart(this);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Glide.with(this).load(object.getImagePath()).transform(new CenterCrop(), new RoundedCorners(60)).into(binding.picDT);
        binding.tvPrice.setText("$" + object.getPrice());
        binding.tvTitle.setText(object.getTitle());
        binding.description.setText(object.getDescription());
        binding.tvRate.setText(object.getStar() + "Rating");
        binding.ratingBar2.setRating((float) object.getStar());
        binding.tvTotal.setText((num*object.getPrice()) + "$");
        binding.btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num+=1;
                binding.tvNum.setText(num);
                binding.tvTotal.setText("$" + (num * object.getPrice()));
            }
        });

        binding.btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num > 1){
                    num-=1;
                    binding.tvNum.setText(num);
                    binding.tvTotal.setText("$" + (num * object.getPrice()));
                }
            }
        });
        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumberInCart(num);
                managmentCart.insertFood(object);
            }
        });
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}